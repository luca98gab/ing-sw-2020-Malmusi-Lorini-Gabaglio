package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.*;
import it.polimi.ingsw.PSP32.server.ClientHandler;

import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("Simplify")

public class Utility {


     /** Method for the communication between clients and server, it prepares the messages that are expected to return nothing
      *
      * @param game : Game
      * @param methodName : String the name of the method that's going to be called client-side
      * @param par1 : Object possible parameter of the function
      * @param par2 : Object possible parameter of the function
      * @throws IOException possible communication errors on the socket
      */
    public static void toAllClientsVoid(Game game, String methodName, Object par1, Object par2) throws IOException {
        for (int i = 0; i < game.getPlayerList().size(); i++){
            game.getPlayerList().get(i).getRelatedClient().toClientVoid(methodName, par1, par2);
        }
    }
    public static void toAllClientsVoid(Game game, String methodName, Object par1) throws IOException {
        toAllClientsVoid(game, methodName, par1, null);
    }
    public static void toAllClientsVoid(Game game, String methodName) throws IOException {
        toAllClientsVoid(game, methodName, null);
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
