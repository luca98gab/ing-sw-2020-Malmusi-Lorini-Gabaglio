package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.Game;
import it.polimi.ingsw.PSP32.model.Pawn;
import it.polimi.ingsw.PSP32.model.Player;

import java.io.IOException;

public class Phases {




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
     * @return Boolean (True = the game can advance with the next turn)
     * @throws IOException
     */
    private static Boolean turn(Game game, Player player) throws IOException {
        if (player.getGod().getName().equals("Athena")) game.setAthenaFlag(false);

        player.getRelatedClient().toClientVoid("printTurnInfo", player);

        CheckHasWon.checkHasWon5Domes(game, player);

        if (CheckHasLost.checkHasLostForMoves(game, player).equals(false)){

            Pawn activePawn = Moving.movePhase(game, player);
            if (activePawn==null) return true;

            if(!CheckHasLost.checkHasLostForBuild(game, activePawn)){
                Building.buildPhase(game, activePawn);
                CheckHasWon.checkHasWon5Domes(game, player);
            }
            else return true;
        } else return true;
        return false;

    }
}
