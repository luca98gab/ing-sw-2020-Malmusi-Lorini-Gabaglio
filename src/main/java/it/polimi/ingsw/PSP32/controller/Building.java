package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.Cell;
import it.polimi.ingsw.PSP32.model.Game;
import it.polimi.ingsw.PSP32.model.Pawn;
import it.polimi.ingsw.PSP32.server.ClientHandler;

import java.io.IOException;

public class Building {


    /** Method that manages the build phase
     *
     * @param game : Game
     * @param pawn : Pawn active pawn
     * @throws IOException
     */

    protected static void buildPhase(Game game, Pawn pawn) throws IOException {

        String god = pawn.getPlayer().getGod().getName();

        ClientHandler client = pawn.getPlayer().getRelatedClient();
        Boolean wantsDome;
        if (god.equals("Atlas")) wantsDome = (Boolean) client.toClientGetObject("waitForBuildCommand",game, pawn, true, false);
        else wantsDome = (Boolean) client.toClientGetObject("waitForBuildCommand",game, pawn, false, false);

        int[] cellCoordinates = (int[]) client.toClientGetObject("getBuildLocationViaArrows", game, pawn, null, true);

        Cell cell = game.getMap()[cellCoordinates[0]][cellCoordinates[1]];

        if (wantsDome){
            cell.setHasDome(true);
        } else {
            cell.setFloor(cell.getFloor()+1);
            if (cell.getFloor() == 4) cell.setHasDome(true);
        }

        Utility.toAllClientsVoid(game, "printBoardColored", game);

        if (god.equals("Demeter")) {
            if (!CheckHasLost.checkHasLostForBuild(game, pawn, true, cell)) {
                Cell restriction = cell;
                cell = null;
                if (client.toClientGetObject("waitForBuildCommand", game, pawn, false, true, restriction).equals(false)) {
                    cellCoordinates = (int[]) client.toClientGetObject("getBuildLocationViaArrows", game, pawn, restriction, true);
                    cell = game.getMap()[cellCoordinates[0]][cellCoordinates[1]];
                }
                if (cell != null) {
                    cell.setFloor(cell.getFloor() + 1);
                    if (cell.getFloor() == 4) cell.setHasDome(true);
                    Utility.toAllClientsVoid(game, "printBoardColored", game);
                }
            }
        } else if (god.equals("Hestia")){
            if (!CheckHasLost.checkHasLostForBuild(game, pawn, false,  null, false)) {
                Cell restriction = cell;
                cell = null;
                if (client.toClientGetObject("waitForBuildCommand", game, pawn, false, true).equals(false)) {
                    cellCoordinates = (int[]) client.toClientGetObject("getBuildLocationViaArrows", game, pawn, restriction, false);
                    cell = game.getMap()[cellCoordinates[0]][cellCoordinates[1]];
                }
                if (cell != null) {
                    cell.setFloor(cell.getFloor() + 1);
                    if (cell.getFloor() == 4) cell.setHasDome(true);
                    Utility.toAllClientsVoid(game, "printBoardColored", game);
                }
            }
        } else if (god.equals("Hephaestus")) {
            if (cell.getFloor()<3 && (Boolean) client.toClientGetObject("askBuildTwice" , pawn.getPlayer())){
                cell.setFloor(cell.getFloor()+1);
                Utility.toAllClientsVoid(game, "printBoardColored", game);
            }
        }


    }
}
