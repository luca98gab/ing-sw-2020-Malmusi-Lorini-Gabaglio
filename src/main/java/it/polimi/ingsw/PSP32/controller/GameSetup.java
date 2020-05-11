package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.Game;
import it.polimi.ingsw.PSP32.model.God;
import it.polimi.ingsw.PSP32.model.Pawn;
import it.polimi.ingsw.PSP32.model.Player;
import it.polimi.ingsw.PSP32.server.ClientHandler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GameSetup {



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
}
