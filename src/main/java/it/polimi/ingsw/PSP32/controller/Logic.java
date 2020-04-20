package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.*;
import it.polimi.ingsw.PSP32.view.LocalCli;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Logic {

    //game sequence related methods

    public static void startGame(){
        Game game = gameSetup();

        do {
            for (int i = 0; i < game.getPlayerList().size(); i++){
                turn(game, game.getPlayerList().get(i));
            }
        } while (true);
    }

    private static void turn(Game game, Player player){

        if (player.getGod().getName().equals("Athena")) game.setAthenaFlag(false);

        LocalCli.printTurnInfo(player);

        if (checkHasLost(game, player).equals(false)){

            Pawn activePawn = movePhase(game, player);

            buildPhase(game, activePawn);
        }

    }



    //startup methods

    private static Game gameSetup(){

        Game game = new Game(LocalCli.getNumOfPlayers());

        game.setPlayerList(createPlayerList(game.getPlayerNum())); //creates a list containing the players in the game

        godPicking(game.getPlayerList()); //every player picks his card

        LocalCli.printPlayerInfo(game.getPlayerList(), false); //prints every player info

        firstPawnPositioning(game); //places pawns on the board for every player

        LocalCli.printBoardColored(game);

        return game;
    }

    /** Method to create and fill the list of players when starting the game.
     * Calls the createPlayer() method to create the object related to every single player in the game.
     *
     * @param playerNum Number of players in the game
     * @return Player[playerNum] type: Array containing the players
     */
    private static ArrayList<Player> createPlayerList(int playerNum){
        ArrayList<Player> playersList = new ArrayList<>();

        for (int i = 0; i < playerNum; i++) {
            playersList.add(LocalCli.createPlayer(i));
        }
        return playersList;
    }

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
    private static void godPicking(ArrayList<Player> playersList){

        God[] allGodsList = allGods();

        God[] gameGods = LocalCli.gameGodsPicking(playersList, allGodsList);
        ArrayList<God> remainingGods = new ArrayList<God>(Arrays.asList(gameGods));

        for (int j = 1; j < playersList.size(); j++){
            God selection = LocalCli.ownGodSelection(playersList.get(j), remainingGods);
            playersList.get(j).setGod(selection);
            remainingGods.remove(selection);
        }

        playersList.get(0).setGod(remainingGods.get(0));

        LocalCli.player1GodAssignment(playersList.get(0), remainingGods.get(0));

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
            FileReader f = new FileReader("src/Gods.txt");
            BufferedReader b = new BufferedReader(f);
            int godsNum = Integer.parseInt(b.readLine());
            String string = "";
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

    private static void firstPawnPositioning(Game game){
        for (int i = 0; i < game.getPlayerList().size(); i++){
            LocalCli.printTurnInfo(game.getPlayerList().get(i));

            for (int j = 0; j < 2; j++) {
                int[] coordinates = LocalCli.getPawnInitialPosition(game);
                int x = coordinates[0];
                int y = coordinates[1];
                game.getPlayerList().get(i).getPawns()[j] = new Pawn(x, y, j+1, game.getPlayerList().get(i));
                game.getMap()[x][y].setIsFull(game.getPlayerList().get(i).getPawns()[j]);
            }
        }
    }



    //move phase methods

    private static Pawn movePhase(Game game, Player player){

        String god = player.getGod().getName();
        int[] move = null;
        Pawn activePawn = null;
        Cell startPosition;
        do {
            activePawn = LocalCli.getActivePawn(game, player);
            startPosition = game.getMap()[activePawn.getX()][activePawn.getY()];
            if (god.equals("Prometheus") && LocalCli.wantsToUsePower(player)){

                Cell cell = LocalCli.getBuildLocationViaArrows(game, activePawn, null);
                cell.setFloor(cell.getFloor()+1);
                if (cell.getFloor() == 4) cell.setHasDome(true);

                LocalCli.printBoardColored(game);

                Boolean changedFlag;
                if (game.getAthenaFlag().equals(true)) {
                    changedFlag = false;
                } else {
                    changedFlag = true;
                    game.setAthenaFlag(true);
                }

                LocalCli.waitForMoveCommand(game, activePawn, false, false);
                move = LocalCli.getValidMoveViaArrows(game, activePawn, null, false);

                if (changedFlag.equals(true)) game.setAthenaFlag(false);

            } else if (LocalCli.waitForMoveCommand(game, activePawn, true, false).equals(true)){
                move = LocalCli.getValidMoveViaArrows(game, activePawn, null, true);
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

        LocalCli.printBoardColored(game);

        if (god.equals("Artemis")) {
            if (LocalCli.waitForMoveCommand(game, activePawn, false, true).equals(true)){
                move = LocalCli.getValidMoveViaArrows(game, activePawn, startPosition, true);
            }
            if (move!=null) {
                movePawnSecure(game, activePawn, move[0], move[1]);
                LocalCli.printBoardColored(game);
            }
        } else if (god.equals("Athena")){
            if (game.getMap()[activePawn.getX()][activePawn.getY()].getFloor()-startPosition.getFloor()<1){
                game.setAthenaFlag(true);
            }
        }

        checkHasWon(game, activePawn, startPosition);

        return activePawn;
    }

    private static void movePawnSecure(Game game, Pawn pawn, int x, int y){
        game.getMap()[pawn.getX()][pawn.getY()].setIsFull(null);
        game.getMap()[x][y].setIsFull(pawn);
        pawn.moves(x, y);
    }

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

    private static void buildPhase(Game game, Pawn pawn){
        String god = pawn.getPlayer().getGod().getName();

        Boolean wantsDome;
        if (god.equals("Atlas")) wantsDome= LocalCli.waitForBuildCommand(game, pawn, true, false);
        else wantsDome= LocalCli.waitForBuildCommand(game, pawn, false, false);

        Cell cell = LocalCli.getBuildLocationViaArrows(game, pawn, null);

        if (wantsDome){
            cell.setHasDome(true);
        } else {
            cell.setFloor(cell.getFloor()+1);
            if (cell.getFloor() == 4) cell.setHasDome(true);
        }

        LocalCli.printBoardColored(game);

        if (god.equals("Demeter")) {
            Cell restriction = cell;
            if (LocalCli.waitForBuildCommand(game, pawn, false, true).equals(false)){
                cell = LocalCli.getBuildLocationViaArrows(game, pawn, restriction);
            }
            if (cell!=null) {
                cell.setFloor(cell.getFloor()+1);
                if (cell.getFloor() == 4) cell.setHasDome(true);
                LocalCli.printBoardColored(game);
            }
        } else if (god.equals("Hephaestus")) {
            if (cell.getFloor()<3 && LocalCli.askBuildTwice(pawn.getPlayer())){
                cell.setFloor(cell.getFloor()+1);
                LocalCli.printBoardColored(game);
            }
        }


    }




    //methods to check things

    private static void checkHasWon(Game game, Pawn pawn, Cell startCell){
        if (game.getMap()[pawn.getX()][pawn.getY()].getFloor() == 3 ||
                (pawn.getPlayer().getGod().getName().equals("Pan") && startCell.getFloor()-game.getMap()[pawn.getX()][pawn.getY()].getFloor()==2)) {
            LocalCli.endGameGraphics(pawn.getPlayer());
            while (true);
        }
    }

    private static Boolean checkHasLost(Game game, Player player){
        int movablePawns = 0;
        for (int j = 0; j < player.getPawns().length; j++){
            if (checkCanMoveE(game, player.getPawns()[j], null) || checkCanMoveW(game, player.getPawns()[j], null) ||
                    checkCanMoveN(game, player.getPawns()[j], null) || checkCanMoveS(game, player.getPawns()[j], null) ||
                    checkCanMoveSE(game, player.getPawns()[j], null) || checkCanMoveNE(game, player.getPawns()[j], null) ||
                    checkCanMoveSW(game, player.getPawns()[j], null) || checkCanMoveNW(game, player.getPawns()[j], null)){
                movablePawns++;
            }
        }
        if (movablePawns == 0){
            if (game.getPlayerList().size()==3){
                System.out.println("YOU LOST - Player removed from the game");
                game.getPlayerList().remove(player);
                return true;
            } else {
                game.getPlayerList().remove(player);
                LocalCli.endGameGraphics(game.getPlayerList().get(0));
                while (true);
            }

        }
        return false;
    }


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
}
