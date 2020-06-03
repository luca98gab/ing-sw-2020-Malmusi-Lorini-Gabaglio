/**
 *  CLI for Santorini.
 *
 *  Every output is formatted using numbers starting from 1 (not from 0) if not prompted otherwise.
 *  If a user has to select the first element in a list he will do so by writing 1. The same rule applies to the board cells.
 */

package it.polimi.ingsw.PSP32.view.cli;

import it.polimi.ingsw.PSP32.client.ServerAdapter;
import it.polimi.ingsw.PSP32.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;


public class VirtualCli implements Runnable {

    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static void main( String[] args ) {
        VirtualCli localCli = new VirtualCli();
        localCli.run();
    }

    private static void out(String string){
        System.out.print(string);
    }
    private static void out(int i){
        out(Integer.toString(i));
    }
    private static void outln(String string){
        out(string + "\n");
    }
    private static void outln(){
        outln("");
    }
    private static void outln(int i){
        outln(Integer.toString(i));
    }


    //methods to get input

    /** Method to ask the number of players for the game
     *
     * @return int # of players
     */
    public static int getNumOfPlayers(){

        out("\n\nSANTORINI by Gio-Poco-Davim" + "\n\n" + "New game." + "\n\n" + "Insert number of players: ");

        return checkForValidIntInput(2, 3, null);
    }

    /** Method that creates the Player object asking the parameters to the user.
     *
     * !!! Does not check for duplicated names or colors
     *
     * @return Player type: object of the newly created player
     */
    public static Player createPlayer(CopyOnWriteArrayList<Player> playerList, int num ) throws IOException {
        String str;
        Boolean boolName=true;


        do {
            out("Insert name: ");
            str = checkForValidStringInput(false, true, 1, 20, null);
            boolName = checkExistingName(playerList, str);
            if (!boolName) out("The name is already taken \n\n");
        }while(!boolName);

        Boolean boolColor;
        String color;

        do {
            out("Choose a color  " + BLUE + "B (Blue)" + RED + "  R (Red)" + GREEN + "  G (Green)" + RESET + ": ");
            color = checkForValidStringInput(false, false, 1, 10, null);
            if (color.matches("\\b(?i)(?:red|rosso|r)\\b")) {
                color = RED;
            } else if (color.matches("\\b(?i)(?:green|verde|g)\\b")) {
                color = GREEN;
            } else if (color.matches("\\b(?i)(?:blue|blu|b)\\b")) {
                color = BLUE;
            } else {
                out("Color not recognized \n\n");
                color = "NO";
            }
            if (color.equals("NO"))
                boolColor = false;
            else {

                boolColor = checkExistingColor(playerList, color);
                if (!boolColor) out("The color is already taken \n\n");
            }
        }while(!boolColor);

        if (playerList.size()<num-1)
            out("\nWaiting for other players to join... \n");




        Player player = new Player(str, color, null);
        outln();

        return player;
    }

    /** Method used to select the 2/3 gods the players are going to play with, 'player 1' picks the gods from allGodsList
     *
     * @param playersList: ArrayList used to get the 'player 1' and the number of players
     * @param allGodsList: God[] list of gods
     * @return God[]
     */
    public static God[] gameGodsPicking(ArrayList<Player> playersList, God[] allGodsList) {

        out(RESET + playersList.get(0).getColor() + "PLAYER 1: ");
        outln(playersList.get(0).getName().toUpperCase());

        outln("\nSelect " + playersList.size() + " cards from the following ones:\n");
        for (int i = 0; i < allGodsList.length; i++) {
            outln(i+1 + " " + allGodsList[i].getName() + " --> " + allGodsList[i].getAbility());
        }

        God [] gameGods = new God[playersList.size()];
        outln();

        for (int i = 0; i < playersList.size(); i++){
            out("Select God " + (i+1) + ": ");
            Boolean valid=true;
            do {
                gameGods[i] = allGodsList[checkForValidIntInput(1, allGodsList.length, null)-1];
                for (int j = 0; j < i; j++){
                    if (gameGods[j].equals(gameGods[i])){
                        valid = false;
                        out("God already in use. Select God " + (i+1) + ": ");
                    } else valid = true;
                }
            } while (!valid);
        }

        return gameGods;
    }

    /** Method for the god selection of each player
     *
     * @param player: player that's selecting the god
     * @param gameGods: ArrayList<God> list of gods selected for this game
     * @return God: selected god
     */
    public static God ownGodSelection(Player player, ArrayList<God> gameGods){
        out(RESET + player.getColor() + "\n\nPLAYER: ");
        outln(player.getName().toUpperCase());
        outln("\nAvailable Gods:\n");
        for (int i = 0; i < gameGods.size(); i++){
            outln((i+1) + ": " + gameGods.get(i).getName() + " --> " + gameGods.get(i).getAbility());
        }
        out("\nSelect your god: ");
        int choiceIndex = checkForValidIntInput(1, gameGods.size(), null)-1;

        God choice = gameGods.get(choiceIndex);

        return choice;
    }

    /** Method for the initial pawn positioning
     *
     * @param game: Game
     * @return int[] coordinates of the pawn
     */
    public static int[] getPawnInitialPosition(Game game){
        out("Select a cell for your pawn.\nX = ");
        int x = checkForValidIntInput(1, 5, null)-1;
        out("Y = ");
        int y = checkForValidIntInput(1, 5, null)-1;
        while (game.getMap()[x][y].getIsFull()!=null){
            out("\nSelected cell is already occupied: " + game.getMap()[x][y].getIsFull().getPlayer().getName() +
                    " " + game.getMap()[x][y].getIsFull().getId() + ". Select another cell for your pawn.\nX = ");
            x = checkForValidIntInput(1, 5, null)-1;
            out("Y = ");
            y = checkForValidIntInput(1, 5, null)-1;
        }
        int[] position = new int[2];
        position[0] = x;
        position[1] = y;

        return position;
    }

    /** Method to ask to the player which pawn he wants to use
     *
     * @param game: Game
     * @param player: player current player
     * @return Pawn selected pawn
     */
    public static Pawn getActivePawn(Game game, Player player){
        for (int j = 0; j < player.getPawns().length; j++){
            outln("Pawn " + (j+1) + ": [" + (player.getPawns()[j].getX()+1)
                    + "," + (player.getPawns()[j].getY()+1) + "]");
        }

        out("\nChoose pawn: ");
        return player.getPawns()[checkForValidIntInput(1, 2, null)-1];
    }

    /** Method to ask to the the player where he wants to move, than calls a check method to validate the request
     *
     * @param game: Game
     * @param pawn: Pawn selected pawn
     * @param restriction: Cell possible restricted cells
     * @param canChangePawn: Boolean possibility to change the pawn (depends on the current phase)
     * @return int [] resultant coordinates of the pawn after the move
     */
    public static int[] getValidMoveViaArrows(Game game, Pawn pawn, Cell restriction, Boolean canChangePawn) throws IOException {

        int x=0, y=0;
        Boolean valid = false;
        Boolean esc = false;
        do {
            out(pawn.getPlayer().getColor() + "\nWhere to move?\nUse Number Keypad or QWEASDZXC keys: ");
            switch (checkForValidStringInput(true, false , 1, 1, "INVALID MOVE")){
                case "1":
                case "z":
                    x = pawn.getX()-1;
                    y = pawn.getY()+1;
                    valid = (Boolean) ServerAdapter.toServerGetObject("checkCanMoveSW", game, pawn, restriction);
                    break;
                case "2":
                case "x":
                    x = pawn.getX();
                    y = pawn.getY()+1;
                    valid = (Boolean) ServerAdapter.toServerGetObject("checkCanMoveS", game, pawn, restriction);
                    break;
                case "3":
                case "c":
                    x = pawn.getX()+1;
                    y = pawn.getY()+1;
                    valid = (Boolean) ServerAdapter.toServerGetObject("checkCanMoveSE", game, pawn, restriction);
                    break;
                case "4":
                case "a":
                    x = pawn.getX()-1;
                    y = pawn.getY();
                    valid = (Boolean) ServerAdapter.toServerGetObject("checkCanMoveW", game, pawn, restriction);
                    break;
                case "6":
                case "d":
                    x = pawn.getX()+1;
                    y = pawn.getY();
                    valid = (Boolean) ServerAdapter.toServerGetObject("checkCanMoveE", game, pawn, restriction);
                    break;
                case "7":
                case "q":
                    x = pawn.getX()-1;
                    y = pawn.getY()-1;
                    valid = (Boolean) ServerAdapter.toServerGetObject("checkCanMoveNW", game, pawn, restriction);
                    break;
                case "8":
                case "w":
                    x = pawn.getX();
                    y = pawn.getY()-1;
                    valid = (Boolean) ServerAdapter.toServerGetObject("checkCanMoveN", game, pawn, restriction);
                    break;
                case "9":
                case "e":
                    x = pawn.getX()+1;
                    y = pawn.getY()-1;
                    valid = (Boolean) ServerAdapter.toServerGetObject("checkCanMoveNE", game, pawn, restriction);
                    break;
            }
            if (!valid) outln("INVALID MOVE");
            if (!valid && canChangePawn){
                out("\nDo you still want to move this pawn? [Y/N] : ");
                esc = !checkForValidYNInput(null);
            }
        } while (!valid && !esc);

        int[] move = new int[2];
        move[0]=x;
        move[1]=y;

        if (esc) return null;
        else return move;
    }

    /** Method to ask to the the player where he wants to build, than calls a check method to validate the request
     *
     * @param game: Game
     * @param pawn: Pawn selected pawn
     * @param restriction: Cell possible restricted cells
     * @return Cell selected cell to build on
     */
    public static int[] getBuildLocationViaArrows(Game game, Pawn pawn, Cell restriction, Boolean edgeCellsAllowed) throws IOException {
        int x=0, y=0;
        Boolean valid = false;
        do {
            out(pawn.getPlayer().getColor() + "\nWhere to build?\nUse Number Keypad or QWEASDZXC keys: ");
            switch (checkForValidStringInput(true, false , 1, 1, "INVALID LOCATION")){
                case "1":
                case "z":
                    x = pawn.getX()-1;
                    y = pawn.getY()+1;
                    valid = (Boolean) ServerAdapter.toServerGetObject("checkCanBuildSW", game, pawn, restriction, edgeCellsAllowed);
                    break;
                case "2":
                case "x":
                    x = pawn.getX();
                    y = pawn.getY()+1;
                    valid = (Boolean) ServerAdapter.toServerGetObject("checkCanBuildS", game, pawn, restriction, edgeCellsAllowed);
                    break;
                case "3":
                case "c":
                    x = pawn.getX()+1;
                    y = pawn.getY()+1;
                    valid = (Boolean) ServerAdapter.toServerGetObject("checkCanBuildSE", game, pawn, restriction, edgeCellsAllowed);
                    break;
                case "4":
                case "a":
                    x = pawn.getX()-1;
                    y = pawn.getY();
                    valid = (Boolean) ServerAdapter.toServerGetObject("checkCanBuildW", game, pawn, restriction, edgeCellsAllowed);
                    break;
                case "6":
                case "d":
                    x = pawn.getX()+1;
                    y = pawn.getY();
                    valid = (Boolean) ServerAdapter.toServerGetObject("checkCanBuildE", game, pawn, restriction, edgeCellsAllowed);
                    break;
                case "7":
                case "q":
                    x = pawn.getX()-1;
                    y = pawn.getY()-1;
                    valid = (Boolean) ServerAdapter.toServerGetObject("checkCanBuildNW", game, pawn, restriction, edgeCellsAllowed);
                    break;
                case "8":
                case "w":
                    x = pawn.getX();
                    y = pawn.getY()-1;
                    valid = (Boolean) ServerAdapter.toServerGetObject("checkCanBuildN", game, pawn, restriction, edgeCellsAllowed);
                    break;
                case "9":
                case "e":
                    x = pawn.getX()+1;
                    y = pawn.getY()-1;
                    valid = (Boolean) ServerAdapter.toServerGetObject("checkCanBuildNE", game, pawn, restriction, edgeCellsAllowed);
                    break;
                case "5":
                case "s":
                    x=pawn.getX();
                    y=pawn.getY();
                    valid = (Boolean) ServerAdapter.toServerGetObject("checkCanBuildBelow", game, pawn, restriction);
                    break;
            }
            if (!valid) outln("INVALID LOCATION");
        } while (!valid);

        return new int[]{x, y};
    }

    /** Method to ask if the player wants to build again (Hephaestus's power)
     *
     * @param player: Player current player that selected Hephaestus as his god
     * @return Boolean (true= the player wants to build again , false= the player doesn't want to build again)
     */
    public static Boolean askBuildTwice(Player player){
        out(player.getColor() + "\nDo you want to build again on the same cell? [Y/N]: ");
        return checkForValidYNInput(null);
    }

    /** Method used to ask to the player if he wants to use his god's power
     *
     * @param player: Player current player
     * @return Boolean (true= the player wants to use his power , false= the player doesn't want to use his power)
     */
    public static Boolean wantsToUsePower(Player player){
        out(player.getColor() + "\nDo you want to use your power? [Y/N]: ");
        return checkForValidYNInput(null);
    }

    public static int[] aresPower(Game game, Pawn pawn){
        int x=0;
        int y=0;
        Boolean valid=false;
        do {
            out(pawn.getPlayer().getColor() + "\nWhere do you want to remove the floor?\nUse Number Keypad or QWEASDZXC keys: ");
            switch (checkForValidStringInput(true, false , 1, 1, "INVALID LOCATION")){
                case "1":
                case "z":
                    x = pawn.getX()-1;
                    y = pawn.getY()+1;

                    if (0<x && x<4 && 0<y && y<4 && game.getMap()[x][y].getFloor()>0 && !game.getMap()[x][y].getHasDome() && game.getMap()[x][y].getIsFull()==null) valid=true;
                    break;
                case "2":
                case "x":
                    x = pawn.getX();
                    y = pawn.getY()+1;
                    if (0<x && x<4 && 0<y && y<4 && game.getMap()[x][y].getFloor()>0 && !game.getMap()[x][y].getHasDome() && game.getMap()[x][y].getIsFull()==null) valid=true;
                    break;
                case "3":
                case "c":
                    x = pawn.getX()+1;
                    y = pawn.getY()+1;
                    if (0<x && x<4 && 0<y && y<4 && game.getMap()[x][y].getFloor()>0 && !game.getMap()[x][y].getHasDome() && game.getMap()[x][y].getIsFull()==null) valid=true;
                    break;
                case "4":
                case "a":
                    x = pawn.getX()-1;
                    y = pawn.getY();
                    if (0<x && x<4 && 0<y && y<4 && game.getMap()[x][y].getFloor()>0 && !game.getMap()[x][y].getHasDome() && game.getMap()[x][y].getIsFull()==null) valid=true;
                    break;
                case "6":
                case "d":
                    x = pawn.getX()+1;
                    y = pawn.getY();
                    if (0<x && x<4 && 0<y && y<4 && game.getMap()[x][y].getFloor()>0 && !game.getMap()[x][y].getHasDome() && game.getMap()[x][y].getIsFull()==null) valid=true;
                    break;
                case "7":
                case "q":
                    x = pawn.getX()-1;
                    y = pawn.getY()-1;
                    if (0<x && x<4 && 0<y && y<4 && game.getMap()[x][y].getFloor()>0 && !game.getMap()[x][y].getHasDome() && game.getMap()[x][y].getIsFull()==null) valid=true;
                    break;
                case "8":
                case "w":
                    x = pawn.getX();
                    y = pawn.getY()-1;
                    if (0<x && x<4 && 0<y && y<4 && game.getMap()[x][y].getFloor()>0 && !game.getMap()[x][y].getHasDome() && game.getMap()[x][y].getIsFull()==null) valid=true;
                    break;
                case "9":
                case "e":
                    x = pawn.getX()+1;
                    y = pawn.getY()-1;
                    if (0<x && x<4 && 0<y && y<4 && game.getMap()[x][y].getFloor()>0 && !game.getMap()[x][y].getHasDome() && game.getMap()[x][y].getIsFull()==null) valid=true;
                    break;
                case "5":
                case "s":
                    x=pawn.getX();
                    y=pawn.getY();
                    if (0<x && x<4 && 0<y && y<4 && game.getMap()[x][y].getFloor()>0 && !game.getMap()[x][y].getHasDome() && game.getMap()[x][y].getIsFull()==null) valid=true;
                    break;
            }
            if (!valid) outln("INVALID LOCATION");
        } while (!valid);


        return (new int[]{x, y});
    }



    //methods to check input

    /** Method to get from the user a valid input:
     * Integer value expected, between min and max.
     *
     * @param min : min acceptable int value in input
     * @param max : max acceptable int value in input
     * @return int : the valid value
     */
    private static int checkForValidIntInput(int min, int max, String customErrorMessage){
        Scanner scanner = new Scanner(System.in);
        String str;
        if (customErrorMessage == null) customErrorMessage = "\nInsert a new one: ";

        str = scanner.nextLine();

        while (!str.matches("^[0-9]+$") || Integer.parseInt(str) < min || Integer.parseInt(str) > max){
            out("Input not valid (Integer number requested - min " + min + " - max " + max + ")." +
                    customErrorMessage);
            str = scanner.nextLine();
        }

        return Integer.parseInt(str);
    }

    /** Method to get from the user a valid input:
     *  String value that is coherent with the parameters.
     *
     * @param canContainNumbers boolean for digits
     * @param canContainSymbols boolean for symbols
     * @param minLength min length of the word
     * @param maxLength max length of the word
     * @return  String : valid value from input
     */
    protected static String checkForValidStringInput(Boolean canContainNumbers, Boolean canContainSymbols, int minLength, int maxLength, String customErrorMessage){
        String str;
        Scanner scanner = new Scanner(System.in);
        str = scanner.nextLine();
        if (customErrorMessage == null) customErrorMessage = "\nInsert a new one: ";

        if (canContainNumbers.equals(false) && canContainSymbols.equals(false)){
            while ((str == null) || !str.matches("[a-zA-Z]+") || str.equals(" ") || (str.length() < minLength) || (str.length() > maxLength)){
                out("Input not valid (No Numbers - No Symbols - min " + minLength + " characters - max " + maxLength + " characters)." +
                        customErrorMessage);
                str = scanner.nextLine();
            }
        } else if (canContainNumbers.equals(true) && canContainSymbols.equals(false)){
            while (str == null || str.matches("[!@#$%&*()_+=|<>?{}\\[\\]~-]") || str.equals(" ") || str.length() < minLength || str.length() > maxLength){
                out("Input not valid (No Symbols - min " + minLength + " characters - max " + maxLength + " characters)." +
                        customErrorMessage);
                str = scanner.nextLine();
            }
        } else if (canContainNumbers.equals(true) && canContainSymbols.equals(true)){
            while (str == null || str.equals(" ") || str.length() < minLength || str.length() > maxLength){
                out("Input not valid (min " + minLength + " characters - max " + maxLength + " characters)." +
                        "\nInsert a new one: ");
                str = scanner.nextLine();
            }
        } else if (canContainNumbers.equals(false) && canContainSymbols.equals(true)){
            while (str == null || str.matches(".*\\d.*") || str.equals(" ") || str.length() < minLength || str.length() > maxLength){
                out("Input not valid (No Numbers - min " + minLength + " characters - max " + maxLength + " characters)." +
                        customErrorMessage);
                str = scanner.nextLine();
            }
        }

        return str;
    }

    /** Method to get from the user a valid input:
     *  Yes or No [Y/N]
     *
     * @param customErrorMessage: String
     * @return Boolean (true = Yes, false= No)
     */
    private static Boolean checkForValidYNInput(String customErrorMessage){
        String str;
        Scanner scanner = new Scanner(System.in);
        str = scanner.nextLine();
        if (customErrorMessage == null) customErrorMessage = "\nInsert a new one: ";

        while ((str == null) || (!str.matches("\\b(?i)(?:yes|si|y)\\b") && !str.matches("\\b(?i)(?:no|n)\\b"))){
            out("Input not valid" + customErrorMessage);
            str = scanner.nextLine();
        }
        if (str.matches("\\b(?i)(?:yes|si|y)\\b")) return true;
        else return false;
    }

    /** Method to check if the name selected by the client is already taken
     *
     * @param playerList : CopyOnWriteArrayList
     * @param name : String the name that the client wants
     * @return Boolean (False: the name is already taken, True: the name is ok)
     */
    public static Boolean checkExistingName(CopyOnWriteArrayList<Player> playerList, String name) {
        name = name.toUpperCase();
        for (int i = 0; i < playerList.size(); i++) {
            if (name.equals(playerList.get(i).getName().toUpperCase()))
                return false;
        }
        return true;
    }

    /** Method to check if the color selected by the client is already taken
     *
     * @param playerList : CopyOnWriteArrayList
     * @param color : String the color that the client wants
     * @return Boolean (False: the name is already taken, True: the name is ok)
     */
    public static Boolean checkExistingColor(CopyOnWriteArrayList<Player> playerList, String color) {
        for (int i = 0; i < playerList.size(); i++) {
            if (color.equals(playerList.get(i).getColor()))
                return false;
        }
        return true;
    }







    // output methods

    /** Method to print the god that's been assigned to player1 after the others chose
     *
     * @param player: Player player1
     * @param god: God the remaining god
     */
    public static void player1GodAssignment(Player player, God god){
        out(RESET + player.getColor() + "\n\nPLAYER 1: ");
        outln(player.getName().toUpperCase());
        outln("\nYour God is:");
        outln(god.getName() + " --> " + god.getAbility());
    }

    /** Method to print the name of the current player
     *
     * @param player: Player current player
     */
    public static void printTurnInfo(Player player){
        out(RESET + player.getColor() + "\n\nPLAYER: ");
        outln(player.getName().toUpperCase());
    }

    /** Prints info related to the specified cell
     *
     * @param game current game
     * @param x cell x
     * @param y cell y
     */
    private static void printCellInfo(Game game, int x, int y){
        outln("\n\nCell [" + (x+1) + "," + (y+1) + "]:");
        if (game.getMap()[x][y].getIsFull()!=null){
            outln("-Occupied by " + game.getMap()[x][y].getIsFull().getPlayer().getName() +
                    " " + game.getMap()[x][y].getIsFull().getId());
        } else outln("-Empty");
        outln("Floor: " + game.getMap()[x][y].getFloor());
        outln("Dome: " + game.getMap()[x][y].getHasDome());
    }

    /** Method to print the board. Floors and players
     *
     * @param game Game : current game
     */
    public static void printBoardColored(Game game){
        outln(RESET + "\n+   -  -  -  -  -   +\n");
        for (int i = 0; i < 5; i++){
            out(RESET + "|   ");
            for (int j = 0; j < 5; j++){
                if (game.getMap()[j][i].getHasDome().equals(false)){
                    if (game.getMap()[j][i].getIsFull()==null){
                        out(RESET + game.getMap()[j][i].getFloor() + "  ");
                    } else {
                        out(game.getMap()[j][i].getIsFull().getPlayer().getColor() + game.getMap()[j][i].getFloor());
                        if (game.getMap()[j][i].getIsFull().getId() == 1){
                            out("' ");
                        } else out("\" ");
                    }

                } else out(RESET + "@  ");
            }
            outln(RESET + " |\n");
        }
        outln(RESET + "+   -  -  -  -  -   +");
    }

    /** Method to print the end game graphics
     *
     * @param player: Player winner
     */
    public static void endGameGraphics(Player player){
        String[] colors = {BLACK, RED, GREEN, BLUE, YELLOW, PURPLE, CYAN, WHITE, BLACK, RED, GREEN, BLUE,
                YELLOW, PURPLE, CYAN, WHITE, BLACK, RED, GREEN, BLUE, YELLOW, PURPLE, CYAN, WHITE, BLACK,
                RED, GREEN, BLUE, YELLOW, PURPLE, CYAN, WHITE, BLACK, RED, GREEN, BLUE};
        int num = player.getName().length();
        outln(player.getColor());
        for (int i = 0; i < num+11; i++){
            out(colors[i] + "#");
        }
        outln();
        for (int i = 0; i < num+11; i++){
            out(colors[i+4] + "#");
        }
        outln();
        outln("## " + player.getColor() + player.getName().toUpperCase() + " WINS" + RESET + " ##");
        for (int i = 0; i < num+11; i++){
            out(colors[i+2] + "#");
        }
        outln();
        for (int i = 0; i < num+11; i++){
            out(colors[i+6] + "#");
        }

        System.out.println("\nThe game is over, your client is closing\n");
    }

    /** Method to show to a player that he has been removed after he lost
     *
     * @param player : Player the player that lost
     */
    public static void removedPlayerGraphics(Player player){
        System.out.println(player.getName().toUpperCase() + " LOST - Player removed from the game");
    }

    /** Method that prints the info related to the players in the game (Name, Color, God Name, God Power, Pawns)
     *
     * @param players : type Player[] array containing the players in the game
     * @param printPawns : type Boolean - true if you want to print the positions of the pawns
     */
    public static void printPlayerInfo(ArrayList<Player> players, Boolean printPawns){
        for (int i = 0; i < players.size(); i++){
            outln(RESET + players.get(i).getColor() + "\n\nPLAYER " + (i+1) + " INFO:\n" +
                    "\nName: " + players.get(i).getName());
            if (players.get(i).getGod()!=null){
                outln("God: " + players.get(i).getGod().getName() + ": " + players.get(i).getGod().getAbility());
            }
            if (printPawns.equals(true)){
                for (int j = 0; j < players.get(i).getPawns().length; j++){
                    Pawn pawn = players.get(i).getPawns()[j];
                    outln("Pawn " + (j+1) + ": " +  (pawn.getX()+1) + "," +  (pawn.getY()+1));
                }
            }
        }
    }



    /** Method that waits the player's input in the build phase
     *
     * @param game: Game
     * @param pawn: Pawn active pawn
     * @param canBuildDome: Boolean true= if the player can build a special dome
     * @param allowEsc: Boolean true= if this wait is a non mandatory build
     * @return Boolean
     */
    public static Boolean waitForBuildCommand(Game game, Pawn pawn, Boolean canBuildDome, Boolean allowEsc){
        String text1 = "";
        String text2 = "";
        if (canBuildDome) text1 = "- Build Dome (d) ";
        if (allowEsc) text2 = "- Esc (e) ";
        String cmd;
        do {
            outln(pawn.getPlayer().getColor() + "\nAvailable commands: Build (b) " + text1 + text2 + "- MyPlayerInfo (i) - PrintBoard (p)");
            out("Command: ");
            cmd = checkForValidStringInput(false, false, 1, 3, null);
            switch (cmd) {
                case "b":
                    return false;
                case "i":
                    ArrayList<Player> myPlayer = new ArrayList<Player>();
                    myPlayer.add( pawn.getPlayer());
                    printPlayerInfo(myPlayer, true);
                    break;
                case "e":
                case "d":
                    if (canBuildDome || allowEsc) return true;
                    else outln("NOT VALID");
                    break;
                case "p":
                    printBoardColored(game);
                    break;
                default:
                    outln("NOT VALID");
                    break;
            }
        } while (true);
    }

    /** Method that waits the player's input in the move phase
     *
     * @param game: Game
     * @param pawn: Pawn active pawn
     * @param allowSwitch: Boolean true= if the player can switch his pawn with another pawn (Apollo's power)
     * @param allowEsc: Boolean true= if this wait is a non mandatory move
     * @return Boolean
     */
    public static Boolean waitForMoveCommand(Game game, Pawn pawn, Boolean allowSwitch, Boolean allowEsc){
        String text1 = "";
        String text2 = "";
        if (allowSwitch) text1 = "- Change Pawn (c) ";
        if (allowEsc) text2 = "- Esc (e) ";
        String cmd;
        do {
            outln(pawn.getPlayer().getColor() + "\nAvailable commands: Move (m) " + text1 + text2 + "- PlayerInfo (i) - PrintBoard (p)");
            out("Command: ");
            cmd = checkForValidStringInput(false, false, 1, 3, null);
            switch (cmd) {
                case "m":
                    return true;
                case "i":
                    ArrayList<Player> myPlayer = new ArrayList<Player>();
                    myPlayer.add(pawn.getPlayer());
                    printPlayerInfo(myPlayer, true);
                    break;

                case "p":
                    printBoardColored(game);
                    break;
                case "c":
                case "e":
                    if (allowSwitch || allowEsc){
                        return false;
                    } else {
                        outln("NOT VALID");
                        break;
                    }
                default:
                    outln("NOT VALID");
                    break;
            }
        } while (true);

    }






    @Override
    public void run() {

        Game game = new Game(2);
        game.getMap()[0][0].setHasDome(true);
        printBoardColored(game);

    }

}

