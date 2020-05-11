package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.Cell;
import it.polimi.ingsw.PSP32.model.Game;
import it.polimi.ingsw.PSP32.model.Pawn;

import java.io.IOException;

public class CheckHasWon {


    /** Method to check if the current player won
     *
     * @param game : Game
     * @param pawn : Pawn active pawn
     * @param startCell : Cell pawn location before moving
     * @throws IOException
     */
    protected static void checkHasWon(Game game, Pawn pawn, Cell startCell) throws IOException {
        if (game.getMap()[pawn.getX()][pawn.getY()].getFloor() == 3 ||
                (pawn.getPlayer().getGod().getName().equals("Pan") && startCell.getFloor()-game.getMap()[pawn.getX()][pawn.getY()].getFloor()==2)) {
            Utility.toAllClientsVoid(game, "endGameGraphics", pawn.getPlayer());
        }
    }
}
