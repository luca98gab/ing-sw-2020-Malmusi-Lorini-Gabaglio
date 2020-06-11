package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.Cell;
import it.polimi.ingsw.PSP32.model.Game;
import it.polimi.ingsw.PSP32.model.Pawn;
import it.polimi.ingsw.PSP32.model.Player;

import java.io.IOException;

public class CheckHasWon {

    Utility utility;

    CheckHasWon(Utility utility){
        this.utility = utility;
    }

    /** Method to check if the current player won
     *
     * @param game : Game
     * @param pawn : Pawn active pawn
     * @param startCell : Cell pawn location before moving
     * @throws IOException
     */
    protected void checkHasWon(Game game, Pawn pawn, Cell startCell) throws IOException {
        Boolean heraInGame = false;
        for (int i = 0; i < game.getPlayerList().size(); i++){
            if (game.getPlayerList().get(i).getGod().getName().equals("Hera")){
                heraInGame = true;
            }
        }
        if (!heraInGame || pawn.getPlayer().getGod().getName().equals("Hera")){
            if (game.getMap()[pawn.getX()][pawn.getY()].getFloor() == 3 ||
                    (pawn.getPlayer().getGod().getName().equals("Pan") && startCell.getFloor()-game.getMap()[pawn.getX()][pawn.getY()].getFloor()==2)) {
                endGameGraphics(game, pawn.getPlayer());
            }
        } else {
            if ((game.getMap()[pawn.getX()][pawn.getY()].getFloor() == 3 ||
                    (pawn.getPlayer().getGod().getName().equals("Pan") && startCell.getFloor()-game.getMap()[pawn.getX()][pawn.getY()].getFloor()==2)) && (pawn.getX()<4 && pawn.getY()<4 && pawn.getX()>0 && pawn.getY()>0)) {
                endGameGraphics(game, pawn.getPlayer());
            }
        }

    }

    protected void checkHasWon5Domes(Game game, Player player) throws IOException {
        if (player.getGod().getName().equals("Chronus")){
            int n = 0;
            for (int i = 0; i < 5; i++){
                for (int j = 0; j < 5; j++){
                    if (game.getMap()[i][j].getHasDome().equals(true) && game.getMap()[i][j].getFloor()>2){
                        n++;
                    }
                }
            }
            if (n >= 5) endGameGraphics(game, player);
        }
    }

    void endGameGraphics(Game game, Player player) throws IOException {
        utility.toAllClientsVoid(game, "endGameGraphics", player);
    }
}
