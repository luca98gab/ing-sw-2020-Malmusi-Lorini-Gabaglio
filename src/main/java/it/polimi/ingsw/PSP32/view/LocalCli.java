/**
 *  CLI for Santorini.
 *
 *  Every output is formatted using numbers starting from 1 (not from 0) if not prompted otherwise.
 *  If a user has to select the first element in a list he will do so by writing 1. The same rule applies to the board cells.
 */

package it.polimi.ingsw.PSP32.view;

import it.polimi.ingsw.PSP32.controller.Logic;
import it.polimi.ingsw.PSP32.model.*;

import java.util.Scanner;
import java.util.ArrayList; // import the ArrayList class


public class LocalCli implements Runnable {

    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static void main() {
        LocalCli localCli = new LocalCli();
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
    public static Player createPlayer(int i){

        out("\nPLAYER ");
        outln(i+1);

        out("Insert name: ");
        String str = checkForValidStringInput(false, true, 1, 20, null);

        out("Insert color: ");
        String color = checkForValidStringInput(false, false, 1, 10, null);

        if (color.matches("\\b(?i)(?:red|rosso|r)\\b")){
            color = RED;
        } else if (color.matches("\\b(?i)(?:green|verde|g)\\b")){
            color = GREEN;
        } else if (color.matches("\\b(?i)(?:yellow|giallo|y)\\b")){
            color = YELLOW;
        } else if (color.matches("\\b(?i)(?:blue|blu|b)\\b")){
            color = BLUE;
        } else if (color.matches("\\b(?i)(?:purple|viola|p)\\b")){
            color = PURPLE;
        } else if (color.matches("\\b(?i)(?:cyan|ciano|c|azzurro)\\b")){
            color = CYAN;
        } else if (color.matches("\\b(?i)(?:black|nero)\\b")){
            color = BLACK;
        } else if (color.matches("\\b(?i)(?:white|bianco|w)\\b")){
            color = WHITE;
        }



        Player player = new Player(str, color, null);
        outln();

        return player;
    }

    public static God[] gameGodsPicking(ArrayList<Player> playersList, God[] allGodsList){


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

    public static Pawn getActivePawn(Game game, Player player){
        for (int j = 0; j < player.getPawns().length; j++){
            outln("Pawn " + (j+1) + ": [" + (player.getPawns()[j].getX()+1)
                    + "," + (player.getPawns()[j].getY()+1) + "]");
        }

        out("\nChoose pawn: ");
        return player.getPawns()[checkForValidIntInput(1, 2, null)-1];
    }

    public static int[] getValidMoveViaArrows(Game game, Pawn pawn, Cell restriction, Boolean canChangePawn){

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
                    valid = Logic.checkCanMoveSW(game, pawn, restriction);
                    break;
                case "2":
                case "x":
                    x = pawn.getX();
                    y = pawn.getY()+1;
                    valid = Logic.checkCanMoveS(game, pawn, restriction);
                    break;
                case "3":
                case "c":
                    x = pawn.getX()+1;
                    y = pawn.getY()+1;
                    valid = Logic.checkCanMoveSE(game, pawn, restriction);
                    break;
                case "4":
                case "a":
                    x = pawn.getX()-1;
                    y = pawn.getY();
                    valid = Logic.checkCanMoveW(game, pawn, restriction);
                    break;
                case "6":
                case "d":
                    x = pawn.getX()+1;
                    y = pawn.getY();
                    valid = Logic.checkCanMoveE(game, pawn, restriction);
                    break;
                case "7":
                case "q":
                    x = pawn.getX()-1;
                    y = pawn.getY()-1;
                    valid = Logic.checkCanMoveNW(game, pawn, restriction);
                    break;
                case "8":
                case "w":
                    x = pawn.getX();
                    y = pawn.getY()-1;
                    valid = Logic.checkCanMoveN(game, pawn, restriction);
                    break;
                case "9":
                case "e":
                    x = pawn.getX()+1;
                    y = pawn.getY()-1;
                    valid = Logic.checkCanMoveNE(game, pawn, restriction);
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

    public static Cell getBuildLocationViaArrows(Game game, Pawn pawn, Cell restriction){
        int x=0, y=0;
        Boolean valid = false;
        do {
            out(pawn.getPlayer().getColor() + "\nWhere to build?\nUse Number Keypad or QWEASDZXC keys: ");
            switch (checkForValidStringInput(true, false , 1, 1, "INVALID LOCATION")){
                case "1":
                case "z":
                    x = pawn.getX()-1;
                    y = pawn.getY()+1;
                    valid = Logic.checkCanBuildSW(game, pawn, restriction);
                    break;
                case "2":
                case "x":
                    x = pawn.getX();
                    y = pawn.getY()+1;
                    valid = Logic.checkCanBuildS(game, pawn, restriction);
                    break;
                case "3":
                case "c":
                    x = pawn.getX()+1;
                    y = pawn.getY()+1;
                    valid = Logic.checkCanBuildSE(game, pawn, restriction);
                    break;
                case "4":
                case "a":
                    x = pawn.getX()-1;
                    y = pawn.getY();
                    valid = Logic.checkCanBuildW(game, pawn, restriction);
                    break;
                case "6":
                case "d":
                    x = pawn.getX()+1;
                    y = pawn.getY();
                    valid = Logic.checkCanBuildE(game, pawn, restriction);
                    break;
                case "7":
                case "q":
                    x = pawn.getX()-1;
                    y = pawn.getY()-1;
                    valid = Logic.checkCanBuildNW(game, pawn, restriction);
                    break;
                case "8":
                case "w":
                    x = pawn.getX();
                    y = pawn.getY()-1;
                    valid = Logic.checkCanBuildN(game, pawn, restriction);
                    break;
                case "9":
                case "e":
                    x = pawn.getX()+1;
                    y = pawn.getY()-1;
                    valid = Logic.checkCanBuildNE(game, pawn, restriction);
                    break;
            }
            if (!valid) outln("INVALID LOCATION");
        } while (!valid);

        return game.getMap()[x][y];
    }

    public static Boolean askBuildTwice(Player player){
        out(player.getColor() + "\nDo you want to build again on the same cell? [Y/N]: ");
        return checkForValidYNInput(null);
    }

    public static Boolean wantsToUsePower(Player player){
        out(player.getColor() + "\nDo you want to use your power? [Y/N]: ");
        return checkForValidYNInput(null);
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
    private static String checkForValidStringInput(Boolean canContainNumbers, Boolean canContainSymbols, int minLength,
                                                   int maxLength, String customErrorMessage){
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






    // output methods

    public static void player1GodAssignment(Player player, God god){
        out(RESET + player.getColor() + "\n\nPLAYER 1: ");
        outln(player.getName().toUpperCase());
        outln("\nYour God is:");
        outln(god.getName() + " --> " + god.getAbility());
    }

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
        outln(RESET + "\n+   1  2  3  4  5   +\n");
        for (int i = 0; i < 5; i++){
            out(RESET + (i+1) + "   ");
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
                    if (canBuildDome) return true;
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

        //Logic.startGame();

    }

}

