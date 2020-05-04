package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.*;
import it.polimi.ingsw.PSP32.server.ClientHandler;
import it.polimi.ingsw.PSP32.view.VirtualCli;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
 @SuppressWarnings("Simplify")
public class Logic{

    //game sequence related methods

     /** Method that starts the game and manages the sequence of events
      *
      * @param game : Game
      * @throws IOException
      */
    public static void startGame(Game game) throws IOException {

        do {
            for (int i = 0; i < game.getPlayerList().size();){
                if (turn(game, game.getPlayerList().get(i)).equals(false)){
                    i++;
                }
            }
        } while (true);
    }

     /** Method that manages the turn
      *
      * @param game : game
      * @param player : Player current player
      * @return Boolean (True= the game can advance with the next turn)
      * @throws IOException
      */
    private static Boolean turn(Game game, Player player) throws IOException {
        if (player.getGod().getName().equals("Athena")) game.setAthenaFlag(false);

        player.getRelatedClient().toClientVoid("printTurnInfo", player);

        if (checkHasLostForMoves(game, player).equals(false)){

            Pawn activePawn = movePhase(game, player);
            if (activePawn==null) return true;

            if(!checkHasLostForBuild(game, activePawn)){
                buildPhase(game, activePawn);
            }
            else return true;
        } else return true;
        return false;

    }



    //startup methods

    /** Method to create and fill the list of players when starting the game.
     * Calls the createPlayer() method to create the object related to every single player in the game.
     *
     * @param playerNum Number of players in the game
     * @return Player[playerNum] type: Array containing the players
     */
    /*  private static ArrayList<Player> createPlayerList(int playerNum){
        ArrayList<Player> playersList = new ArrayList<>();

        for (int i = 0; i < playerNum; i++) {
            playersList.add(VirtualCli.createPlayer(i));
        }
        return playersList;
    }
*/


    /** Method for the initial picking of the gods in the game:
     *  -Imports Gods from file
     *  -Selection of the 2/3 cards in the game
     *  -God picking by every player
     *  -Adds God information to the related Player object
     *
     *
     *  !!! It is possible for different players to use the same God
     *
     * @param playersList : type Player[] array containing the players in the game
     */
    public static void godPicking(ArrayList<Player> playersList) throws IOException {

        God[] allGodsList = allGods();

        for (int i=1; i<playersList.size(); i++)
            playersList.get(i).getRelatedClient().toClientVoid("waitGodsPicking", playersList.get(0).getName(), playersList.get(0).getColor());
        God[] gameGods = (God []) playersList.get(0).getRelatedClient().toClientGetObject("gameGodsPicking", playersList, allGodsList);
        ArrayList<God> remainingGods = new ArrayList<>(Arrays.asList(gameGods));

        for (int j = 1; j < playersList.size(); j++){
            for(int z = 0; z< playersList.size(); z++) {
                if (z != j)
                    playersList.get(z).getRelatedClient().toClientVoid("waitOwnGodSelection", playersList.get(j).getName(), playersList.get(j).getColor());
            }

            God selection = ((God [])playersList.get(j).getRelatedClient().toClientGetObject("ownGodSelection", playersList.get(j), remainingGods))[0];

        for (int i = 0; i < remainingGods.size(); i++){
                if (remainingGods.get(i).equals(selection)){
                    remainingGods.remove(remainingGods.get(i));
                }
            }
            playersList.get(j).setGod(selection);


        }

        playersList.get(0).setGod(remainingGods.get(0));

        playersList.get(0).getRelatedClient().toClientVoid("player1GodAssignment", playersList.get(0), remainingGods.get(0));
        //LocalCli.player1GodAssignment(playersList.get(0), remainingGods.get(0));

    }

    /** Method to read from the Gods.txt file located in /src and to store the data in God objects.
     * The file has to be filled as following:
     *
     * Line 0: Int      //number of gods written in it
     * Line 1:
     * Line 2: String   //name of the god
     * Line 3: String   //description
     * -----Repeat from Line 1------
     *
     * Every God occupies 3 lines in the file: empty/name/description
     *
     * @return God[] : Array containing all the gods that can be used in the game
     */
    private static God[] allGods(){

        try {
            FileReader f = new FileReader("src/resources/Santorini Images/Gods.txt");
            BufferedReader b = new BufferedReader(f);
            int godsNum = Integer.parseInt(b.readLine());
            String string;
            God[] allGods = new God[godsNum];
            for (int i = 0; i < godsNum; i++){
                String name;
                String ability;

                string = b.readLine();
                if (string == null) break;
                string = b.readLine();
                if (string == null) break;
                name = string;
                string = b.readLine();
                if (string == null) break;
                ability = string;

                allGods[i] = new God(name, ability);

            }
            System.out.println("Gods imported");
            return allGods;

        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND");
        } catch (IOException e) {
            System.out.println("ERROR IN IO");
        }
        return null;
    }

     /** Method to manage the initial positioning of the pawns
      *
      * @param game : Game current game
      * @throws IOException
      */
    public static void firstPawnPositioning(Game game) throws IOException {
        for (int i = 0; i < game.getPlayerList().size(); i++){

            ClientHandler client = game.getPlayerList().get(i).getRelatedClient();

            client.toClientVoid("printTurnInfo", game.getPlayerList().get(i));

            game.getPlayerList().get(i).getRelatedClient().toClientVoid("printBoardColored", game);

            for (int j = 0; j < 2; j++) {
                int[] coordinates = (int []) client.toClientGetObject("getPawnInitialPosition", game);
                int x = coordinates[0];
                int y = coordinates[1];
                game.getPlayerList().get(i).getPawns()[j] = new Pawn(x, y, j+1, game.getPlayerList().get(i));
                game.getMap()[x][y].setIsFull(game.getPlayerList().get(i).getPawns()[j]);
            }
        }
    }



    //move phase methods

     /** Method that manages the move phase, handling the various gods powers
      *
      * @param game
      * @param player : Player current player
      * @return Pawn: return the pawn that just moved
      * @throws IOException
      */
    private static Pawn movePhase(Game game, Player player) throws IOException {

        for (int i=0; i<game.getPlayerList().size(); i++){
            if (!player.equals(game.getPlayer(i)))

            game.getPlayer(i).getRelatedClient().toClientVoid("waitTurnMessage", player.getName(), player.getColor()    );

        }
        String god = player.getGod().getName();
        int[] move = null;
        int activePawnId;
        Pawn activePawn = null;
        Cell startPosition;
        do {
            activePawnId = ((Pawn) player.getRelatedClient().toClientGetObject("getActivePawn", game, player)).getId();
            for (int i = 0; i < player.getPawns().length; i++){
                if (player.getPawns()[i].getId()==activePawnId) activePawn = player.getPawns()[i];
            }
            activePawn.getPlayer().setRelatedClient(player.getRelatedClient());
            startPosition = game.getMap()[activePawn.getX()][activePawn.getY()];
            if (god.equals("Prometheus") && ((Boolean) player.getRelatedClient().toClientGetObject("wantsToUsePower", player))){

                int[] cellCoordinates = ((int[]) player.getRelatedClient().toClientGetObject("getBuildLocationViaArrows",game, activePawn, null));
                Cell cell = game.getMap()[cellCoordinates[0]][cellCoordinates[1]];
                cell.setFloor(cell.getFloor()+1);
                if (cell.getFloor() == 4) cell.setHasDome(true);
                toAllClientsVoid(game, "printBoardColored", game);

                Boolean changedFlag;
                if (game.getAthenaFlag().equals(true)) {
                    changedFlag = false;
                } else {
                    changedFlag = true;
                    game.setAthenaFlag(true);
                }

                if(!checkHasLostForMoves(game, activePawn)) {
                    player.getRelatedClient().toClientGetObject("waitForMoveCommand", game, activePawn, false, false);
                    move = (int[]) player.getRelatedClient().toClientGetObject("getValidMoveViaArrows", game, activePawn, null, false);

                    if (changedFlag.equals(true)) game.setAthenaFlag(false);
                }
                else return null;
            } else if ((Boolean) player.getRelatedClient().toClientGetObject("waitForMoveCommand",game, activePawn, true, false)){
                move = (int []) player.getRelatedClient().toClientGetObject("getValidMoveViaArrows", game, activePawn, null, true);
            }
        } while (move==null);

        if (game.getMap()[move[0]][move[1]].getIsFull()!=null){
            Pawn opponentPawn = game.getMap()[move[0]][move[1]].getIsFull();
            if (god.equals("Apollo")){
                switchPawns(game, activePawn, opponentPawn);
            } else if (god.equals("Minotaur")){
                pushPawns(game, activePawn, opponentPawn);
                Cell startCellOpponent = game.getMap()[opponentPawn.getX()][opponentPawn.getY()];
                checkHasWon(game, opponentPawn, startCellOpponent);
            }
        } else movePawnSecure(game, activePawn, move[0], move[1]);

        toAllClientsVoid(game, "printBoardColored", game);

        if (god.equals("Artemis")) {
            if(!checkHasLostForMoves(game, player, true)) {
                if (player.getRelatedClient().toClientGetObject("waitForMoveCommand", game, activePawn, false, true).equals(true)) {
                    move = (int[]) player.getRelatedClient().toClientGetObject("getValidMoveViaArrows", game, activePawn, startPosition, true);
                }
                if (move != null) {
                    movePawnSecure(game, activePawn, move[0], move[1]);
                    toAllClientsVoid(game, "printBoardColored", game);
                }
            }
        } else if (god.equals("Athena")){
            if (game.getMap()[activePawn.getX()][activePawn.getY()].getFloor()-startPosition.getFloor()==1){
                game.setAthenaFlag(true);
            }
        }

        checkHasWon(game, activePawn, startPosition);

        return activePawn;
    }

     /** Method to update the state of the board
      *
      * @param game : Game
      * @param pawn : Pawn active pawn
      * @param x : int new x coord. of the pawn
      * @param y : int new y coord. of the pawn
      */
    private static void movePawnSecure(Game game, Pawn pawn, int x, int y){
        game.getMap()[pawn.getX()][pawn.getY()].setIsFull(null);
        game.getMap()[x][y].setIsFull(pawn);
        pawn.moves(x, y);
    }

     /** Apollo's oriented method, it switches the position of the 2 given pawns
      *
      * @param game : Game
      * @param pawn1 : Pawn first of the couple
      * @param pawn2 : Pawn second pawn of the couple
      */
    private static void switchPawns(Game game, Pawn pawn1, Pawn pawn2){
        int x1 = pawn1.getX();
        int y1 = pawn1.getY();
        int x2 = pawn2.getX();
        int y2 = pawn2.getY();

        game.getMap()[x1][y1].setIsFull(pawn2);
        pawn2.moves(x1, y1);

        game.getMap()[x2][y2].setIsFull(pawn1);
        pawn1.moves(x2, y2);
    }

     /** Minotaur's oriented method, it positions the opponent's pawn where the Minotaur pushes it
      *
      * @param game : Game
      * @param pawn : Pawn pawn that's pushing
      * @param opponentPawn : Pawn that gets pushed
      */
    private static void pushPawns(Game game, Pawn pawn, Pawn opponentPawn){

        int x0 = pawn.getX();
        int y0 = pawn.getY();
        int x1 = opponentPawn.getX();
        int y1 = opponentPawn.getY();
        int x2 = opponentPawn.getX()*2 - pawn.getX();
        int y2 = opponentPawn.getY()*2 - pawn.getY();

        game.getMap()[x0][y0].setIsFull(null);
        game.getMap()[x1][y1].setIsFull(pawn);
        pawn.moves(x1, y1);

        game.getMap()[x2][y2].setIsFull(opponentPawn);
        opponentPawn.moves(x2, y2);
    }




    //build phase methods

     /** Method that manages the build phase
      *
      * @param game : Game
      * @param pawn : Pawn active pawn
      * @throws IOException
      */
    private static void buildPhase(Game game, Pawn pawn) throws IOException {

        String god = pawn.getPlayer().getGod().getName();

        ClientHandler client = pawn.getPlayer().getRelatedClient();
        Boolean wantsDome;
        if (god.equals("Atlas")) wantsDome = (Boolean) client.toClientGetObject("waitForBuildCommand",game, pawn, true, false);
        else wantsDome = (Boolean) client.toClientGetObject("waitForBuildCommand",game, pawn, false, false);

        int[] cellCoordinates = (int[]) client.toClientGetObject("getBuildLocationViaArrows", game, pawn, null);

        Cell cell = game.getMap()[cellCoordinates[0]][cellCoordinates[1]];

        if (wantsDome){
            cell.setHasDome(true);
        } else {
            cell.setFloor(cell.getFloor()+1);
            if (cell.getFloor() == 4) cell.setHasDome(true);
        }

        toAllClientsVoid(game, "printBoardColored", game);

        if (god.equals("Demeter")) {
            if(!checkHasLostForBuild(game, pawn, true, cell)) {
                Cell restriction = cell;
                if (client.toClientGetObject("waitForBuildCommand", game, pawn, false, true).equals(false)) {
                    cellCoordinates = (int[]) client.toClientGetObject("getBuildLocationViaArrows", game, pawn, restriction);
                    cell = game.getMap()[cellCoordinates[0]][cellCoordinates[1]];
                }
                if (cell != null) {
                    cell.setFloor(cell.getFloor() + 1);
                    if (cell.getFloor() == 4) cell.setHasDome(true);
                    toAllClientsVoid(game, "printBoardColored", game);
                }
            }
        } else if (god.equals("Hephaestus")) {
            if (cell.getFloor()<3 && (Boolean) client.toClientGetObject("askBuildTwice" , pawn.getPlayer())){
                cell.setFloor(cell.getFloor()+1);
                toAllClientsVoid(game, "printBoardColored", game);
            }
        }


    }



    //comm methods

     /** Method for the communication between clients and server, it prepares the messages that are expected to return nothing
      *
      * @param game : Game
      * @param methodName : String the name of the method that's going to be called client-side
      * @param par1 : Object possible parameter of the function
      * @param par2 : Object possible parameter of the function
      * @throws IOException possible communication errors on the socket
      */
    private static void toAllClientsVoid(Game game, String methodName, Object par1, Object par2) throws IOException {
        for (int i = 0; i < game.getPlayerList().size(); i++){
            game.getPlayerList().get(i).getRelatedClient().toClientVoid(methodName, par1, par2);
        }
    }
    public static void toAllClientsVoid(Game game, String methodName, Object par1) throws IOException {
        toAllClientsVoid(game, methodName, par1, null);
    }
    private static void toAllClientsVoid(Game game, String methodName) throws IOException {
        toAllClientsVoid(game, methodName, null);
    }




    //methods to check things

     /** Method to check if the current player won
      *
      * @param game : Game
      * @param pawn : Pawn active pawn
      * @param startCell : Cell pawn location before moving
      * @throws IOException
      */
    private static void checkHasWon(Game game, Pawn pawn, Cell startCell) throws IOException {
        if (game.getMap()[pawn.getX()][pawn.getY()].getFloor() == 3 ||
                (pawn.getPlayer().getGod().getName().equals("Pan") && startCell.getFloor()-game.getMap()[pawn.getX()][pawn.getY()].getFloor()==2)) {
            toAllClientsVoid(game, "endGameGraphics", pawn.getPlayer());
            //pawn.getPlayer().getRelatedClient().toClientVoid("endGameGraphics", pawn.getPlayer());
        }
    }

    private static Boolean checkHasLostForMoves(Game game, Pawn pawn) throws IOException {
         int movablePawns = 0;
         if (checkCanMoveE(game, pawn, null) || checkCanMoveW(game, pawn, null) ||
                     checkCanMoveN(game, pawn, null) || checkCanMoveS(game, pawn, null) ||
                     checkCanMoveSE(game, pawn, null) || checkCanMoveNE(game, pawn, null) ||
                     checkCanMoveSW(game, pawn, null) || checkCanMoveNW(game, pawn, null)){
                 movablePawns++;
             }
         if (movablePawns == 0){
             if (game.getPlayerList().size()==3){
                 toAllClientsVoid(game, "removedPlayerGraphics", pawn.getPlayer());
                 game.getPlayerList().remove(pawn.getPlayer());
                 return true;
             } else {
                 game.getPlayerList().remove(pawn.getPlayer());
                 pawn.getPlayer().getRelatedClient().toClientVoid("endGameGraphics" ,game.getPlayerList().get(0));
                 while (true);
             }
         }
         return false;
     }
    private static Boolean checkHasLostForMoves(Game game, Player player, Boolean artemis) throws IOException {
        int movablePawns = 0;
        for (int j = 0; j < player.getPawns().length; j++){
            if (checkCanMoveE(game, player.getPawns()[j], null) || checkCanMoveW(game, player.getPawns()[j], null) ||
                    checkCanMoveN(game, player.getPawns()[j], null) || checkCanMoveS(game, player.getPawns()[j], null) ||
                    checkCanMoveSE(game, player.getPawns()[j], null) || checkCanMoveNE(game, player.getPawns()[j], null) ||
                    checkCanMoveSW(game, player.getPawns()[j], null) || checkCanMoveNW(game, player.getPawns()[j], null)){
                movablePawns++;
            }
        }
        if (movablePawns == 0 && !artemis){
            if (game.getPlayerList().size()==3){
                toAllClientsVoid(game, "removedPlayerGraphics", player);
                game.getPlayerList().remove(player);
                return true;
            } else {
                game.getPlayerList().remove(player);
                player.getRelatedClient().toClientVoid("endGameGraphics" ,game.getPlayerList().get(0));
                while (true);
            }
        }
        else {
            if(movablePawns==0 && artemis) return true;
        }
        return false;
    }
    private static Boolean checkHasLostForMoves(Game game, Player player) throws IOException {
        return checkHasLostForMoves(game, player, false);
    }


    /** Method to check if the player can build anywhere (Overload)
      *
      * @param game : Game
      * @param pawn : Pawn active pawn
      * @param demeter : Boolean flag to know if this is the first build of demeter (i.e. the player can't use his power)
      * @param cell : Cell current cell
      * @return Boolean(True= no valid build locations, False= there are valid build locations)
      * @throws IOException
      */
    private static Boolean checkHasLostForBuild(Game game, Pawn pawn, Boolean demeter, Cell cell) throws IOException {
         Player player = pawn.getPlayer();

         if (checkCanBuildE(game, pawn, cell) || checkCanBuildW(game, pawn, cell) ||
                     checkCanBuildN(game, pawn, cell) || checkCanBuildS(game, pawn, cell) ||
                     checkCanBuildSE(game, pawn, cell) || checkCanBuildNE(game, pawn, cell) ||
                     checkCanBuildSW(game, pawn, cell) || checkCanBuildNW(game, pawn, cell)){
             return false;
         }
         else {
             if(demeter) {
                 if (game.getPlayerList().size() == 3) {
                     toAllClientsVoid(game, "removedPlayerGraphics", player);
                     game.getPlayerList().remove(player);
                     return true;
                 } else {
                     game.getPlayerList().remove(player);
                     player.getRelatedClient().toClientVoid("endGameGraphics", game.getPlayerList().get(0));
                     while (true) ;
                 }
             }
             else return true;
         }
    }
    private static Boolean checkHasLostForBuild(Game game, Pawn pawn) throws IOException {
         return checkHasLostForBuild(game, pawn, false, null);
     }


     /** A series of methods to check for valid move locations
      *
      * @param game : Game
      * @param pawn : Pawn active pawn
      * @param restriction : Cell possible restricted cells
      * @return Boolean (True= you can move there, False= you can't move there)
      */
     public static Boolean checkCanMoveW(Game game, Pawn pawn, Cell restriction){
        String god = pawn.getPlayer().getGod().getName();
        int climbableFloor = 1;
        if (game.getAthenaFlag()==true){
            climbableFloor = 0;
        }
        if (pawn.getX()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()-1][pawn.getY()];
            if (!newCell.equals(restriction) && (newCell.getIsFull() == null || god.equals("Apollo")) && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && newCell.getHasDome().equals(false)){
                return true;
            } else if (god.equals("Minotaur") && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && !newCell.getHasDome() && pawn.getX()-1>0){
                Cell opponentNewCell = game.getMap()[pawn.getX()-2][pawn.getY()];
                if (!opponentNewCell.getHasDome() && opponentNewCell.getIsFull()==null){
                    return true;
                }
            }
        }
        return false;
    }
    public static Boolean checkCanMoveE(Game game, Pawn pawn, Cell restriction){
        String god = pawn.getPlayer().getGod().getName();
        int climbableFloor = 1;
        if (game.getAthenaFlag()==true){
            climbableFloor = 0;
        }
        if (pawn.getX()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()+1][pawn.getY()];
            if (!newCell.equals(restriction) && (newCell.getIsFull() == null || god.equals("Apollo")) && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && newCell.getHasDome().equals(false)){
                return true;
            } else if (god.equals("Minotaur") && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && !newCell.getHasDome() && pawn.getX()+1<4){
                Cell opponentNewCell = game.getMap()[pawn.getX()+2][pawn.getY()];
                if (!opponentNewCell.getHasDome() && opponentNewCell.getIsFull()==null){
                    return true;
                }
            }
        }
        return false;
    }
    public static Boolean checkCanMoveN(Game game, Pawn pawn, Cell restriction){
        String god = pawn.getPlayer().getGod().getName();
        int climbableFloor = 1;
        if (game.getAthenaFlag()==true){
            climbableFloor = 0;
        }
        if (pawn.getY()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()][pawn.getY()-1];
            if (!newCell.equals(restriction) && (newCell.getIsFull() == null || god.equals("Apollo")) && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && newCell.getHasDome().equals(false)){
                return true;
            } else if (god.equals("Minotaur") && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && !newCell.getHasDome() && pawn.getY()-1>0){
                Cell opponentNewCell = game.getMap()[pawn.getX()][pawn.getY()-2];
                if (!opponentNewCell.getHasDome() && opponentNewCell.getIsFull()==null){
                    return true;
                }
            }
        }
        return false;
    }
    public static Boolean checkCanMoveS(Game game, Pawn pawn, Cell restriction){
        String god = pawn.getPlayer().getGod().getName();
        int climbableFloor = 1;
        if (game.getAthenaFlag()==true){
            climbableFloor = 0;
        }
        if (pawn.getY()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()][pawn.getY()+1];
            if (!newCell.equals(restriction) && (newCell.getIsFull() == null || god.equals("Apollo")) && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && newCell.getHasDome().equals(false)){
                return true;
            } else if (god.equals("Minotaur") && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && !newCell.getHasDome() && pawn.getY()+1<4){
                Cell opponentNewCell = game.getMap()[pawn.getX()][pawn.getY()+2];
                if (!opponentNewCell.getHasDome() && opponentNewCell.getIsFull()==null){
                    return true;
                }
            }
        }
        return false;
    }
    public static Boolean checkCanMoveNW(Game game, Pawn pawn, Cell restriction){
        String god = pawn.getPlayer().getGod().getName();
        int climbableFloor = 1;
        if (game.getAthenaFlag()){
            climbableFloor = 0;
        }
        if (pawn.getX()>0 && pawn.getY()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()-1][pawn.getY()-1];
            if (!newCell.equals(restriction) && (newCell.getIsFull() == null || god.equals("Apollo")) && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && newCell.getHasDome().equals(false)){
                return true;
            } else if (god.equals("Minotaur") && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && !newCell.getHasDome() && pawn.getX()-1>0 && pawn.getY()-1>0){
                Cell opponentNewCell = game.getMap()[pawn.getX()-2][pawn.getY()-2];
                if (!opponentNewCell.getHasDome() && opponentNewCell.getIsFull()==null){
                    return true;
                }
            }
        }
        return false;
    }
    public static Boolean checkCanMoveNE(Game game, Pawn pawn, Cell restriction){
        String god = pawn.getPlayer().getGod().getName();
        int climbableFloor = 1;
        if (game.getAthenaFlag()==true){
            climbableFloor = 0;
        }
        if (pawn.getX()<4 && pawn.getY()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()+1][pawn.getY()-1];
            if (!newCell.equals(restriction) && (newCell.getIsFull() == null || god.equals("Apollo")) && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && newCell.getHasDome().equals(false)){
                return true;
            } else if (god.equals("Minotaur") && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && !newCell.getHasDome() && pawn.getX()+1<4 && pawn.getY()-1>0){
                Cell opponentNewCell = game.getMap()[pawn.getX()+2][pawn.getY()-2];
                if (!opponentNewCell.getHasDome() && opponentNewCell.getIsFull()==null){
                    return true;
                }
            }
        }
        return false;
    }
    public static Boolean checkCanMoveSE(Game game, Pawn pawn, Cell restriction){
        String god = pawn.getPlayer().getGod().getName();
        int climbableFloor = 1;
        if (game.getAthenaFlag()==true){
            climbableFloor = 0;
        }
        if (pawn.getX()<4 && pawn.getY()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()+1][pawn.getY()+1];
            if (!newCell.equals(restriction) && (newCell.getIsFull() == null || god.equals("Apollo")) && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && newCell.getHasDome().equals(false)){
                return true;
            } else if (god.equals("Minotaur") && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && !newCell.getHasDome() && pawn.getX()+1<4 && pawn.getY()+1<4){
                Cell opponentNewCell = game.getMap()[pawn.getX()+2][pawn.getY()+2];
                if (!opponentNewCell.getHasDome() && opponentNewCell.getIsFull()==null){
                    return true;
                }
            }
        }
        return false;
    }
    public static Boolean checkCanMoveSW(Game game, Pawn pawn, Cell restriction){
        String god = pawn.getPlayer().getGod().getName();
        int climbableFloor = 1;
        if (game.getAthenaFlag()==true){
            climbableFloor = 0;
        }
        if (pawn.getX()>0 && pawn.getY()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()-1][pawn.getY()+1];
            if (!newCell.equals(restriction) && (newCell.getIsFull() == null || god.equals("Apollo")) && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && newCell.getHasDome().equals(false)){
                return true;
            } else if (god.equals("Minotaur") && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && !newCell.getHasDome() && pawn.getX()-1>0 && pawn.getY()+1<4){
                Cell opponentNewCell = game.getMap()[pawn.getX()-2][pawn.getY()+2];
                if (!opponentNewCell.getHasDome() && opponentNewCell.getIsFull()==null){
                    return true;
                }
            }
        }
        return false;
    }


     /** A series of methods to check for valid build locations
      *
      * @param game : Game
      * @param pawn : Pawn active pawn
      * @param restriction : Cell possible restricted cells
      * @return Boolean (True= you can build there, False= you can't build there)
      */
    public static Boolean checkCanBuildW(Game game, Pawn pawn, Cell restriction){
        if (pawn.getX()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()-1][pawn.getY()];
            if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildE(Game game, Pawn pawn, Cell restriction){
        if (pawn.getX()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()+1][pawn.getY()];
            if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildN(Game game, Pawn pawn, Cell restriction){
        if (pawn.getY()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()][pawn.getY()-1];
            if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildS(Game game, Pawn pawn, Cell restriction){
        if (pawn.getY()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()][pawn.getY()+1];
            if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildNW(Game game, Pawn pawn, Cell restriction){
        if (pawn.getX()>0 && pawn.getY()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()-1][pawn.getY()-1];
            if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildNE(Game game, Pawn pawn, Cell restriction){
        if (pawn.getX()<4 && pawn.getY()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()+1][pawn.getY()-1];
            if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildSE(Game game, Pawn pawn, Cell restriction){
        if (pawn.getX()<4 && pawn.getY()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()+1][pawn.getY()+1];
            if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildSW(Game game, Pawn pawn, Cell restriction){
        if (pawn.getX()>0 && pawn.getY()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()-1][pawn.getY()+1];
            if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }

     /** Method to notify all clients that the game is closing
      *
      * @param clients: ArrayList clienthandlers of each client
      */
    public static void notifyClosingGame(ArrayList<ClientHandler> clients) {
        for (int i = 0; i < clients.size(); i++) {
                try {
                    clients.get(i).toClientVoid("Disconnection");
                } catch (IOException e) { }
            }
    }

}
