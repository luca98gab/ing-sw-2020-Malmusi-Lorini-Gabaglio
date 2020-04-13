/**
 *  CLI for Santorini.
 *
 *  Every output is formatted using numbers starting from 1 (not from 0) if not prompted otherwise.
 *  If a user has to select the first element in a list he will do so by writing 1. The same rule applies to the board cells.
 */

package it.polimi.ingsw.PSP32.view;

import it.polimi.ingsw.PSP32.client.Client;
import it.polimi.ingsw.PSP32.model.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class Cli implements Runnable {

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
        Cli cli = new Cli();
        cli.run();
    }

    /** Method to create and fill the list of players when starting the game.
     * Calls the createPlayer() method to create the object related to every single player in the game.
     *
     * @param playerNum Number of players in the game
     * @return Player[playerNum] type: Array containing the players
     */
    private static Player[] createPlayerList(int playerNum){
        Player[] playersList = new Player[playerNum];

        for (int i = 0; i < playerNum; i++) {
            System.out.print("\nPLAYER ");
            System.out.println(i+1);
            playersList[i] = createPlayer();
        }
        return playersList;
    }

    /** Method that creates the Player object asking the parameters to the user.
     *
     * !!! Does not check for duplicated names or colors
     *
     * @return Player type: object of the newly created player
     */
    private static Player createPlayer(){

        Scanner scanner = new Scanner(System.in);

        System.out.print("Insert name: ");
        String str = checkForValidStringInput(false, true, 1, 20);

        System.out.print("Insert color: ");
        String color = checkForValidStringInput(false, false, 1, 10);

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
        System.out.println();

        return player;
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

    /** Method that prints the info related to the players in the game (Name, Color, God Name, God Power, Pawns)
     *
     * @param players : type Player[] array containing the players in the game
     * @param printPawns : type Boolean - true if you want to print the positions of the pawns
     */
    private static void printPlayerInfo(Player[] players, Boolean printPawns){
        for (int i = 0; i < players.length; i++){
            System.out.println(RESET + players[i].getColor() + "\n\nPLAYER " + (i+1) + " INFO:\n" +
                    "\nName: " + players[i].getName());
            if (players[i].getGod()!=null){
                System.out.println("God: " + players[i].getGod().getName() + ": " + players[i].getGod().getAbility());
            }
            if (printPawns.equals(true)){
                for (int j = 0; j < players[i].getPawns().length; j++){
                    System.out.println("Pawn " + (j+1) + ": " +  (players[i].getPawns()[j].getX()+1) + "," +  (players[i].getPawns()[j].getY()+1));
                }
            }
        }
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
    private static void godPicking(Player[] playersList){

        God[] allGodsList = allGods();

        System.out.print(RESET + playersList[0].getColor() + "PLAYER 1: ");
        System.out.println(playersList[0].getName().toUpperCase());

        System.out.println("\nSelect " + playersList.length + " cards from the following ones:\n");
        for (int i = 0; i < allGodsList.length; i++) {
            System.out.println(i+1 + " " + allGodsList[i].getName() + " --> " + allGodsList[i].getAbility());
        }

        int[] gods = new int[playersList.length];
        System.out.println();

        for (int i = 0; i < playersList.length; i++){
            System.out.print("Select God " + (i+1) + ": ");
            gods[i] = checkForValidIntInput(1, allGodsList.length)-1;
        }

        for (int j = 0; j < playersList.length-1; j++){
            System.out.print(RESET + playersList[j+1].getColor() + "\n\nPLAYER " + (j+2) + ": ");
            System.out.println(playersList[j+1].getName().toUpperCase());
            System.out.println("\nAvailable Gods:\n");
            for (int i = 0; i < playersList.length-j; i++){
                System.out.println((i+1) + ": " + allGodsList[gods[i]].getName() + " --> " + allGodsList[gods[i]].getAbility());
            }
            System.out.print("\nSelect your god: ");
            int used = checkForValidIntInput(1, playersList.length-j)-1;
            playersList[j+1].setGod(allGodsList[gods[used]]);

            for (int i = used; i < playersList.length-1-j; i++){
                gods[i]=gods[i+1];
            }
        }

        System.out.print(RESET + playersList[0].getColor() + "\n\nPLAYER 1: ");
        System.out.println(playersList[0].getName().toUpperCase());
        System.out.println("\nYour God is:");
        System.out.println(allGodsList[gods[0]].getName() + " --> " + allGodsList[gods[0]].getAbility());
        playersList[0].setGod(allGodsList[gods[0]]);

    }

    /** Method to get from the user a valid input:
     * Integer value expected, between min and max.
     *
     * @param min : min acceptable int value in input
     * @param max : max acceptable int value in input
     * @return int : the valid value
     */
    private static int checkForValidIntInput(int min, int max){
        Scanner scanner = new Scanner(System.in);
        String str;

        str = scanner.nextLine();

        while (!str.matches(".*\\d.*") || Integer.parseInt(str) < min || Integer.parseInt(str) > max){
            System.out.print("Input not valid (Integer number requested - min " + min + " - max " + max + ")." +
                    "\nInsert a new one: ");
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
    private static String checkForValidStringInput(Boolean canContainNumbers, Boolean canContainSymbols, int minLength, int maxLength){
        String str;
        Scanner scanner = new Scanner(System.in);
        str = scanner.nextLine();

        if (canContainNumbers.equals(false) && canContainSymbols.equals(false)){
            while ((str == null) || !str.matches("[a-zA-Z]+") || str.equals(" ") || (str.length() < minLength) || (str.length() > maxLength)){
                System.out.print("Input not valid (No Numbers - No Symbols - min " + minLength + " characters - max " + maxLength + " characters)." +
                        "\nInsert a new one: ");
                str = scanner.nextLine();
            }
        } else if (canContainNumbers.equals(true) && canContainSymbols.equals(false)){
            while (str == null || str.matches("[!@#$%&*()_+=|<>?{}\\[\\]~-]") || str.equals(" ") || str.length() < minLength || str.length() > maxLength){
                System.out.print("Input not valid (No Symbols - min " + minLength + " characters - max " + maxLength + " characters)." +
                        "\nInsert a new one: ");
                str = scanner.nextLine();
            }
        } else if (canContainNumbers.equals(true) && canContainSymbols.equals(true)){
            while (str == null || str.equals(" ") || str.length() < minLength || str.length() > maxLength){
                System.out.print("Input not valid (min " + minLength + " characters - max " + maxLength + " characters)." +
                        "\nInsert a new one: ");
                str = scanner.nextLine();
            }
        } else if (canContainNumbers.equals(false) && canContainSymbols.equals(true)){
            while (str == null || str.matches(".*\\d.*") || str.equals(" ") || str.length() < minLength || str.length() > maxLength){
                System.out.print("Input not valid (No Numbers - min " + minLength + " characters - max " + maxLength + " characters)." +
                        "\nInsert a new one: ");
                str = scanner.nextLine();
            }
        }

        return str;
    }

    /** Prompts every player to position their pawns on the game board.
     *
     * @param game : current Game object
     */
    private static void firstPawnPositioning(Game game){
        for (int i = 0; i < game.getPlayerList().length; i++){
            System.out.print(RESET + game.getPlayerList()[i].getColor() + "\n\nPLAYER " + (i+1) + ": ");
            System.out.println(game.getPlayerList()[i].getName().toUpperCase());

            for (int j = 0; j < 2; j++) {
                System.out.print("Select a cell for your pawn " + (j+1) + ".\nX = ");
                int x = checkForValidIntInput(1, 5)-1;
                System.out.print("Y = ");
                int y = checkForValidIntInput(1, 5)-1;
                while (game.getMap()[x][y].getIsFull()!=null){
                    System.out.print("\nSelected cell is already occupied: " + game.getMap()[x][y].getIsFull().getPlayer().getName() +
                            " " + game.getMap()[x][y].getIsFull().getId() + ". Select another cell for your pawn " + (j+1) + ".\nX = ");
                    x = checkForValidIntInput(1, 5)-1;
                    System.out.print("Y = ");
                    y = checkForValidIntInput(1, 5)-1;
                }
                game.getPlayerList()[i].getPawns()[j] = new Pawn(x, y, j+1, game.getPlayerList()[i]);
                game.getMap()[x][y].setIsFull(game.getPlayerList()[i].getPawns()[j]);
            }
        }
    }

    /** Prints info related to the specified cell
     *
     * @param game current game
     * @param x cell x
     * @param y cell y
     */
    private static void printCellInfo(Game game, int x, int y){
        System.out.println("\n\nCell [" + (x+1) + "," + (y+1) + "]:");
        if (game.getMap()[x][y].getIsFull()!=null){
            System.out.println("-Occupied by " + game.getMap()[x][y].getIsFull().getPlayer().getName() +
                    " " + game.getMap()[x][y].getIsFull().getId());
        } else System.out.println("-Empty");
        System.out.println("Floor: " + game.getMap()[x][y].getFloor());
        System.out.println("Dome: " + game.getMap()[x][y].getHasDome());
    }

    /** Method to print the board. Floors and players
     *
     * @param game Game : current game
     */
    private static void printBoardColored(Game game){
        System.out.println(RESET + "\n+   1  2  3  4  5   +\n");
        for (int i = 0; i < 5; i++){
            System.out.print(RESET + (i+1) + "   ");
            for (int j = 0; j < 5; j++){
                if (game.getMap()[j][i].getHasDome().equals(false)){
                    if (game.getMap()[j][i].getIsFull()==null){
                        System.out.print(RESET + game.getMap()[j][i].getFloor() + "  ");
                    } else {
                        System.out.print(game.getMap()[j][i].getIsFull().getPlayer().getColor() + game.getMap()[j][i].getFloor());
                        if (game.getMap()[j][i].getIsFull().getId() == 1){
                            System.out.print("' ");
                        } else System.out.print("\" ");
                    }

                } else System.out.print(RESET + "@  ");
            }
            System.out.println(RESET + " |\n");
        }
        System.out.println(RESET + "+   -  -  -  -  -   +");
    }

    /** Method to print the board. Floors or/and players
     *
     * @param game Game : current game
     * @param printFloors Boolean : true to print floors
     * @param printPlayers Boolean : true to print players
     */
    private static void printBoard(Game game, Boolean printFloors, Boolean printPlayers){

        if (printFloors.equals(true)) {
            System.out.println(RESET + "+   1  2  3  4  5   +\n");
            for (int i = 0; i < 5; i++){
                System.out.print((i+1) + "   ");
                for (int j = 0; j < 5; j++){
                    if (game.getMap()[j][i].getHasDome().equals(false)){
                        System.out.print(game.getMap()[j][i].getFloor() + "  ");

                    } else System.out.print("@  ");
                }
                System.out.println(" |\n");
            }
            System.out.println("+   -  -  -  -  -   +\n");
        }

        if (printPlayers.equals(true)) {
            System.out.println(RESET + "+   1  2  3  4  5   +\n");
            for (int i = 0; i < 5; i++){
                System.out.print((i+1) + "   ");
                for (int j = 0; j < 5; j++){
                    if (game.getMap()[j][i].getIsFull()==null){
                        System.out.print(RESET + "#  ");
                    } else System.out.print(game.getMap()[j][i].getIsFull().getPlayer().getColor() +
                            game.getMap()[j][i].getIsFull().getPlayer().getName().charAt(0) + "  ");
                }
                System.out.println(RESET + " |\n");
            }
            System.out.println("+   -  -  -  -  -   +\n");
        }
    }

    private static Boolean getValidMove(Game game, Pawn pawn){
        System.out.print("\nWhere to move?\nX: ");
        int x = checkForValidIntInput(Math.max(1, pawn.getX()), Math.min(5, pawn.getX()+2))-1;
        System.out.print("Y: ");
        int y = checkForValidIntInput(Math.max(1, pawn.getY()), Math.min(5, pawn.getY()+2))-1;
        Boolean valid = false;
        int esc = 1;
        if (x == pawn.getX() && y == pawn.getY()){
            valid = false;
            System.out.println("INVALID MOVE: You are already there!");
        } else if (game.getMap()[x][y].getIsFull()!=null){
            valid = false;
            System.out.println("INVALID MOVE: Selected cell is occupied by " + game.getMap()[x][y].getIsFull().getPlayer().getName());
        } else if (game.getMap()[x][y].getFloor() > 1 + game.getMap()[pawn.getX()][pawn.getY()].getFloor()){
            valid = false;
            System.out.println("INVALID MOVE: You cannot climb 2 floors in 1 move!");
        } else if (game.getMap()[x][y].getHasDome().equals(true)){
            valid = false;
            System.out.println("INVALID MOVE: Selected cell has a dome on it!");
        } else valid = true;
        while (!valid && esc == 1){
            System.out.print("\nDo you still want to move this pawn? [1 = Yes, 2 = No] : ");
            esc = checkForValidIntInput(1, 2);
            if (esc == 2) break;
            System.out.print("\nWhere to move?\nX: ");
            x = checkForValidIntInput(Math.max(1, pawn.getX()) , Math.min(5, pawn.getX()+2))-1;
            System.out.print("Y: ");
            x = checkForValidIntInput(Math.max(1, pawn.getY()) , Math.min(5, pawn.getY()+2))-1;
            if (x==pawn.getX() && y==pawn.getY()){
                valid = false;
                System.out.println("INVALID MOVE: You are already there!");
            } else if (game.getMap()[x][y].getIsFull()!=null){
                valid = false;
                System.out.println("INVALID MOVE: Selected cell is occupied by " + game.getMap()[x][y].getIsFull().getPlayer().getName());
            } else if (game.getMap()[x][y].getFloor() > 1 + game.getMap()[pawn.getX()][pawn.getY()].getFloor()){
                valid = false;
                System.out.println("INVALID MOVE: You cannot climb 2 floors in 1 move!");
            } else if (game.getMap()[x][y].getHasDome().equals(true)){
                valid = false;
                System.out.println("INVALID MOVE: Selected cell has a dome on it!");
            } else valid = true;
        }
        if (esc == 1){
            movePawnSecure(game, pawn, x, y);
            return true;
        } else return false;
    }

    private static Boolean getValidMoveViaArrows(Game game, Pawn pawn){
        System.out.print("\nWhere to move?\nUse Number Keypad or QWEASDZXC keys: ");
        int x=0, y=0;
        switch (checkForValidStringInput(true, false , 1, 1)){
            case "1":
            case "z":
                x = pawn.getX()-1;
                y = pawn.getY()+1;
                break;
            case "2":
            case "x":
                x = pawn.getX();
                y = pawn.getY()+1;
                break;
            case "3":
            case "c":
                x = pawn.getX()+1;
                y = pawn.getY()+1;
                break;
            case "4":
            case "a":
                x = pawn.getX()-1;
                y = pawn.getY();
                break;
            case "6":
            case "d":
                x = pawn.getX()+1;
                y = pawn.getY();
                break;
            case "7":
            case "q":
                x = pawn.getX()-1;
                y = pawn.getY()-1;
                break;
            case "8":
            case "w":
                x = pawn.getX();
                y = pawn.getY()-1;
                break;
            case "9":
            case "e":
                x = pawn.getX()+1;
                y = pawn.getY()-1;
                break;
        }
        Boolean valid;
        int esc = 1;
        if (x < 0 || y < 0 || x > 4 || y > 4){
            valid = false;
            System.out.println("INVALID MOVE: Out of board!");
        } else if (x == pawn.getX() && y == pawn.getY()){
            valid = false;
            System.out.println("INVALID MOVE: You are already there!");
        } else if (game.getMap()[x][y].getIsFull()!=null){
            valid = false;
            System.out.println("INVALID MOVE: Selected cell is occupied by " + game.getMap()[x][y].getIsFull().getPlayer().getName());
        } else if (game.getMap()[x][y].getFloor() > 1 + game.getMap()[pawn.getX()][pawn.getY()].getFloor()){
            valid = false;
            System.out.println("INVALID MOVE: You cannot climb 2 floors in 1 move!");
        } else if (game.getMap()[x][y].getHasDome().equals(true)){
            valid = false;
            System.out.println("INVALID MOVE: Selected cell has a dome on it!");
        } else valid = true;
        while (!valid && esc == 1){
            System.out.print("\nDo you still want to move this pawn? [1 = Yes, 2 = No] : ");
            esc = checkForValidIntInput(1, 2);
            if (esc == 2) break;
            System.out.print("\nWhere to move?\nUse Number Keypad or QWEASDZXC keys: ");
            switch (checkForValidStringInput(true, false , 1, 1)){
                case "1":
                case "z":
                    x = pawn.getX()-1;
                    y = pawn.getY()+1;
                    break;
                case "2":
                case "x":
                    x = pawn.getX();
                    y = pawn.getY()+1;
                    break;
                case "3":
                case "c":
                    x = pawn.getX()+1;
                    y = pawn.getY()+1;
                    break;
                case "4":
                case "a":
                    x = pawn.getX()-1;
                    y = pawn.getY();
                    break;
                case "6":
                case "d":
                    x = pawn.getX()+1;
                    y = pawn.getY();
                    break;
                case "7":
                case "q":
                    x = pawn.getX()-1;
                    y = pawn.getY()-1;
                    break;
                case "8":
                case "w":
                    x = pawn.getX();
                    y = pawn.getY()-1;
                    break;
                case "9":
                case "e":
                    x = pawn.getX()+1;
                    y = pawn.getY()-1;
                    break;
            }
            if (x < 0 || y < 0 || x > 4 || y > 4){
                valid = false;
                System.out.println("INVALID MOVE: Out of board!");
            } else if (x==pawn.getX() && y==pawn.getY()){
                valid = false;
                System.out.println("INVALID MOVE: You are already there!");
            } else if (game.getMap()[x][y].getIsFull()!=null){
                valid = false;
                System.out.println("INVALID MOVE: Selected cell is occupied by " + game.getMap()[x][y].getIsFull().getPlayer().getName());
            } else if (game.getMap()[x][y].getFloor() > 1 + game.getMap()[pawn.getX()][pawn.getY()].getFloor()){
                valid = false;
                System.out.println("INVALID MOVE: You cannot climb 2 floors in 1 move!");
            } else if (game.getMap()[x][y].getHasDome().equals(true)){
                valid = false;
                System.out.println("INVALID MOVE: Selected cell has a dome on it!");
            } else valid = true;
        }
        if (esc == 1){
            movePawnSecure(game, pawn, x, y);
            return true;
        } else return false;
    }

    private static void movePawnSecure(Game game, Pawn pawn, int x, int y){
        game.getMap()[pawn.getX()][pawn.getY()].setIsFull(null);
        game.getMap()[x][y].setIsFull(pawn);
        pawn.moves(x, y);
        return;
    }

    private static Pawn userWantsToMove(Game game, Player player){
        for (int j = 0; j < player.getPawns().length; j++){
            System.out.println("Pawn " + (j+1) + ": [" + (player.getPawns()[j].getX()+1)
                    + "," + (player.getPawns()[j].getY()+1) + "]");
        }
        Boolean moved = false;
        int choice;
        do {
            System.out.print("\nChoose pawn: ");
            choice = checkForValidIntInput(1, 2)-1;
            //moved = getValidMove(game, player.getPawns()[choice]);
            moved = getValidMoveViaArrows(game, player.getPawns()[choice]);
        } while (moved.equals(false));
        return player.getPawns()[choice];
    }

    private static void userWantsToBuild(Game game, Pawn pawn){
        System.out.print(pawn.getPlayer().getColor() + "\nWhere to build?\nX: ");
        int x = checkForValidIntInput(Math.max(1, pawn.getX()) , Math.min(5, pawn.getX()+2))-1;
        System.out.print("Y: ");
        int y = checkForValidIntInput(Math.max(1, pawn.getY()) , Math.min(5, pawn.getY()+2))-1;
        Boolean valid = false;
        if (x == pawn.getX() && y == pawn.getY()){
            valid = false;
            System.out.println("INVALID BUILD LOCATION: You are there!");
        } else if (game.getMap()[x][y].getIsFull()!=null){
            valid = false;
            System.out.println("INVALID BUILD LOCATION: Selected cell is occupied by " + game.getMap()[x][y].getIsFull().getPlayer().getName());
        } else if (game.getMap()[x][y].getHasDome().equals(true)){
            valid = false;
            System.out.println("INVALID BUILD LOCATION: Selected cell has a dome on it!");
        } else valid = true;
        while (!valid){
            System.out.print("\nWhere to build?\nX: ");
            x = checkForValidIntInput(Math.max(1, pawn.getX()) , Math.min(5, pawn.getX()+2))-1;
            System.out.print("Y: ");
            y = checkForValidIntInput(Math.max(1, pawn.getY()) , Math.min(5, pawn.getY()+2))-1;
            valid = false;
            if (x == pawn.getX() && y == pawn.getY()){
                valid = false;
                System.out.println("INVALID BUILD LOCATION: You are there!");
            } else if (game.getMap()[x][y].getIsFull()!=null){
                valid = false;
                System.out.println("INVALID BUILD LOCATION: Selected cell is occupied by " + game.getMap()[x][y].getIsFull().getPlayer().getName());
            } else if (game.getMap()[x][y].getHasDome().equals(true)){
                valid = false;
                System.out.println("INVALID BUILD LOCATION: Selected cell has a dome on it!");
            } else valid = true;
        }


        game.getMap()[x][y].setFloor(game.getMap()[x][y].getFloor()+1);
        if (game.getMap()[x][y].getFloor() == 4) game.getMap()[x][y].setHasDome(true);
        return;
    }

    private static void userWantsToBuildViaArrows(Game game, Pawn pawn){
        System.out.print(pawn.getPlayer().getColor() + "\nWhere to build?\nUse Number Keypad or QWEASDZXC keys: ");
        int x=0, y=0;
        switch (checkForValidStringInput(true, false , 1, 1)){
            case "1":
            case "z":
                x = pawn.getX()-1;
                y = pawn.getY()+1;
                break;
            case "2":
            case "x":
                x = pawn.getX();
                y = pawn.getY()+1;
                break;
            case "3":
            case "c":
                x = pawn.getX()+1;
                y = pawn.getY()+1;
                break;
            case "4":
            case "a":
                x = pawn.getX()-1;
                y = pawn.getY();
                break;
            case "6":
            case "d":
                x = pawn.getX()+1;
                y = pawn.getY();
                break;
            case "7":
            case "q":
                x = pawn.getX()-1;
                y = pawn.getY()-1;
                break;
            case "8":
            case "w":
                x = pawn.getX();
                y = pawn.getY()-1;
                break;
            case "9":
            case "e":
                x = pawn.getX()+1;
                y = pawn.getY()-1;
                break;
        }
        Boolean valid = false;
        if (x == pawn.getX() && y == pawn.getY()){
            valid = false;
            System.out.println("INVALID BUILD LOCATION: You are there!");
        } else if (game.getMap()[x][y].getIsFull()!=null){
            valid = false;
            System.out.println("INVALID BUILD LOCATION: Selected cell is occupied by " + game.getMap()[x][y].getIsFull().getPlayer().getName());
        } else if (game.getMap()[x][y].getHasDome().equals(true)){
            valid = false;
            System.out.println("INVALID BUILD LOCATION: Selected cell has a dome on it!");
        } else valid = true;
        while (!valid){
            System.out.print("\nWhere to build?\nUse Number Keypad or QWEASDZXC keys: ");
            switch (checkForValidStringInput(true, false , 1, 1)){
                case "1":
                case "z":
                    x = pawn.getX()-1;
                    y = pawn.getY()+1;
                    break;
                case "2":
                case "x":
                    x = pawn.getX();
                    y = pawn.getY()+1;
                    break;
                case "3":
                case "c":
                    x = pawn.getX()+1;
                    y = pawn.getY()+1;
                    break;
                case "4":
                case "a":
                    x = pawn.getX()-1;
                    y = pawn.getY();
                    break;
                case "6":
                case "d":
                    x = pawn.getX()+1;
                    y = pawn.getY();
                    break;
                case "7":
                case "q":
                    x = pawn.getX()-1;
                    y = pawn.getY()-1;
                    break;
                case "8":
                case "w":
                    x = pawn.getX();
                    y = pawn.getY()-1;
                    break;
                case "9":
                case "e":
                    x = pawn.getX()+1;
                    y = pawn.getY()-1;
                    break;
            }
            if (x == pawn.getX() && y == pawn.getY()){
                valid = false;
                System.out.println("INVALID BUILD LOCATION: You are there!");
            } else if (game.getMap()[x][y].getIsFull()!=null){
                valid = false;
                System.out.println("INVALID BUILD LOCATION: Selected cell is occupied by " + game.getMap()[x][y].getIsFull().getPlayer().getName());
            } else if (game.getMap()[x][y].getHasDome().equals(true)){
                valid = false;
                System.out.println("INVALID BUILD LOCATION: Selected cell has a dome on it!");
            } else valid = true;
        }


        game.getMap()[x][y].setFloor(game.getMap()[x][y].getFloor()+1);
        if (game.getMap()[x][y].getFloor() == 4) game.getMap()[x][y].setHasDome(true);
        return;
    }

    private static void standardTurn(Game game, int i){

        System.out.print(game.getPlayerList()[i].getColor() + "\n\nTURN OF PLAYER " + (i+1) + ": ");
        System.out.println(game.getPlayerList()[i].getName().toUpperCase());
        String cmd;
        Pawn movedPawn = null;

        checkHasLost(game, game.getPlayerList()[i]);

        do {
            System.out.println("\nAvailable commands: Move (m) - PlayerInfo (i) - PrintBoard (p)");
            System.out.print("Command: ");
            cmd = checkForValidStringInput(false, false, 1, 3);
            switch (cmd) {
                case "m":
                    movedPawn = userWantsToMove(game, game.getPlayerList()[i]);
                    printBoardColored(game);
                    break;
                case "i":
                    Player[] myPlayer = new Player[1];
                    myPlayer[0] = game.getPlayerList()[i];
                    printPlayerInfo(myPlayer, true);
                    break;

                case "p":
                    printBoardColored(game);
                    break;
            }
        } while (!cmd.equals("m"));

        checkHasWon(game, movedPawn);

        do {
            System.out.println(game.getPlayerList()[i].getColor() + "\nAvailable commands: Build (b) - MyPlayerInfo (i) - PrintBoard (p)");
            System.out.print("Command: ");
            cmd = checkForValidStringInput(false, false, 1, 3);
            switch (cmd) {
                case "b":
                    userWantsToBuildViaArrows(game, movedPawn);
                    printBoardColored(game);
                    break;
                case "i":
                    Player[] myPlayer = new Player[1];
                    myPlayer[0] = game.getPlayerList()[i];
                    printPlayerInfo(myPlayer, true);
                    break;

                case "p":
                    printBoardColored(game);
                    break;
            }
        } while (!cmd.equals("b"));
    }

    private static void checkHasLost(Game game, Player player){
        int movablePawns = 0;
        for (int j = 0; j < player.getPawns().length; j++){
            if (checkCanMoveE(game, player.getPawns()[j]) || checkCanMoveW(game, player.getPawns()[j]) ||
                    checkCanMoveN(game, player.getPawns()[j]) || checkCanMoveS(game, player.getPawns()[j]) ||
                    checkCanMoveSE(game, player.getPawns()[j]) || checkCanMoveNE(game, player.getPawns()[j]) ||
                    checkCanMoveSW(game, player.getPawns()[j]) || checkCanMoveNW(game, player.getPawns()[j])){
                movablePawns++;
            }
        }
        if (movablePawns == 0){
            System.out.println("YOU LOST");
            while (true);
        }
    }

    private static void checkHasWon(Game game, Pawn pawn){
        if (game.getMap()[pawn.getX()][pawn.getY()].getFloor() == 3) {
            System.out.println("YOU WON");
            endGame(pawn.getPlayer());
        }
    }

    private static void endGame(Player player){
        int num = player.getName().length();
        System.out.println(player.getColor());
        for (int i = 0; i < num+10; i++){
            System.out.print("#");
        }
        for (int i = 0; i < num+10; i++){
            System.out.print("#");
        }
        System.out.println("## " + player.getName().toUpperCase() + " WON ##");
        for (int i = 0; i < num+10; i++){
            System.out.print("#");
        }
        for (int i = 0; i < num+10; i++){
            System.out.print("#");
        }

        while (true);

    }

    private static Boolean checkCanMoveW(Game game, Pawn pawn){
        if (pawn.getX()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()-1][pawn.getY()];
            if (newCell.getIsFull() == null && newCell.getFloor() < currentCell.getFloor()+2 && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }

    private static Boolean checkCanMoveE(Game game, Pawn pawn){
        if (pawn.getX()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()+1][pawn.getY()];
            if (newCell.getIsFull() == null && newCell.getFloor() < currentCell.getFloor()+2 && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }

    private static Boolean checkCanMoveN(Game game, Pawn pawn){
        if (pawn.getY()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()][pawn.getY()-1];
            if (newCell.getIsFull() == null && newCell.getFloor() < currentCell.getFloor()+2 && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }

    private static Boolean checkCanMoveS(Game game, Pawn pawn){
        if (pawn.getY()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()][pawn.getY()+1];
            if (newCell.getIsFull() == null && newCell.getFloor() < currentCell.getFloor()+2 && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }

    private static Boolean checkCanMoveNW(Game game, Pawn pawn){
        if (pawn.getX()>0 && pawn.getY()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()-1][pawn.getY()-1];
            if (newCell.getIsFull() == null && newCell.getFloor() < currentCell.getFloor()+2 && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }

    private static Boolean checkCanMoveNE(Game game, Pawn pawn){
        if (pawn.getX()<4 && pawn.getY()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()+1][pawn.getY()-1];
            if (newCell.getIsFull() == null && newCell.getFloor() < currentCell.getFloor()+2 && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }

    private static Boolean checkCanMoveSE(Game game, Pawn pawn){
        if (pawn.getX()<4 && pawn.getY()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()+1][pawn.getY()+1];
            if (newCell.getIsFull() == null && newCell.getFloor() < currentCell.getFloor()+2 && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }

    private static Boolean checkCanMoveSW(Game game, Pawn pawn){
        if (pawn.getX()>0 && pawn.getY()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()-1][pawn.getY()+1];
            if (newCell.getIsFull() == null && newCell.getFloor() < currentCell.getFloor()+2 && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {

        System.out.print("\n\nSANTORINI by Gio-Poco-Davim" + "\n\n" + "New game." + "\n\n" + "Insert number of players: ");

        int playerNum = 0;
        playerNum = checkForValidIntInput(2, 3); //gets the number of players from the first one

        Game game = new Game(playerNum);


        game.setPlayerList(createPlayerList(playerNum)); //creates a list containing the players in the game

        //godPicking(game.getPlayerList()); //every player picks his card

        printPlayerInfo(game.getPlayerList(), false); //prints every player info

        firstPawnPositioning(game); //places pawns on the board for every player

        //printPlayerInfo(game.getPlayerList(), true); //prints every player info

        printBoardColored(game);


        do {
            for (int i = 0; i < game.getPlayerNum(); i++){
                standardTurn(game, i);
            }
        } while (true);


    }

}

