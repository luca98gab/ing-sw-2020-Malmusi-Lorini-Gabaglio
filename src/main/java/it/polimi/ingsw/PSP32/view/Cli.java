/**
 *  CLI for Santorini.
 *
 *  Every output is formatted using numbers starting from 1 (not from 0) if not prompted otherwise.
 *  If a user has to select the first element in a list he will do so by writing 1. The same rule applies to the board cells.
 */

package it.polimi.ingsw.PSP32.view;

import it.polimi.ingsw.PSP32.client.Client;
import it.polimi.ingsw.PSP32.model.Game;
import it.polimi.ingsw.PSP32.model.God;
import it.polimi.ingsw.PSP32.model.Pawn;
import it.polimi.ingsw.PSP32.model.Player;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.io.IOException;



public class Cli implements Runnable {

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
            System.out.println("\n\nPLAYER " + (i+1) + " INFO:\n" +
                    "\nName: " + players[i].getName() +
                    "\nColor: " + players[i].getColor() +
                    "\nGod: " + players[i].getGod().getName() +
                    ": " + players[i].getGod().getAbility());
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

        System.out.print("PLAYER 1: ");
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
            System.out.print("\n\nPLAYER " + (j+2) + ": ");
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

        System.out.print("\n\nPLAYER 1: ");
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
    public static int checkForValidIntInput(int min, int max){
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
    public static String checkForValidStringInput(Boolean canContainNumbers, Boolean canContainSymbols, int minLength, int maxLength){
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
    public static void firstPawnPositioning(Game game){
        for (int i = 0; i < game.getPlayerList().length; i++){
            System.out.print("\n\nPLAYER " + (i+1) + ": ");
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


    @Override
    public void run() {

        System.out.print("\n\nSANTORINI by Gio-Poco-Davim" + "\n\n" + "New game." + "\n\n" + "Insert number of players: ");

        int playerNum = 0;
        playerNum = checkForValidIntInput(2, 3); //gets the number of players from the first one

        Game game = new Game(playerNum);

        game.setPlayerList(createPlayerList(playerNum)); //creates a list containing the players in the game

        godPicking(game.getPlayerList()); //every player picks his card

        //printPlayerInfo(game.getPlayerList(), false); //prints every player info

        firstPawnPositioning(game); //places pawns on the board for every player

        printPlayerInfo(game.getPlayerList(), true); //prints every player info

    }



}

