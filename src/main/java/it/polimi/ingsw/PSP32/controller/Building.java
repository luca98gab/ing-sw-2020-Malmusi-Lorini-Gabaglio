package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.Cell;
import it.polimi.ingsw.PSP32.model.Game;
import it.polimi.ingsw.PSP32.model.Pawn;
import it.polimi.ingsw.PSP32.server.ClientHandler;

import java.io.IOException;

public class Building {

    private final CheckHasWon checkHasWon;
    private final CheckHasLost checkHasLost;
    private final Utility utility;

    Building(CheckHasLost checkHasLost, CheckHasWon checkHasWon, Utility utility){
        this.utility = utility;
        this.checkHasLost = checkHasLost;
        this.checkHasWon = checkHasWon;
    }

    public Building(){
        this.utility = new Utility();
        this.checkHasWon = new CheckHasWon(utility);
        this.checkHasLost = new CheckHasLost(utility);
    }

    /** Method that manages the build phase
     *
     * @param game : Game
     * @param pawn : Pawn active pawn
     * @throws IOException
     */
    protected void buildPhase(Game game, Pawn pawn) throws IOException {

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

        utility.toAllClientsVoid(game, "printBoardColored", game);

        if (god.equals("Demeter")) {
            if (!checkHasLost.checkHasLostForBuild(game, pawn, true, cell)) {
                Cell restriction = cell;
                cell = null;
                if (client.toClientGetObject("waitForBuildCommand", game, pawn, false, true, restriction).equals(false)) {
                    cellCoordinates = (int[]) client.toClientGetObject("getBuildLocationViaArrows", game, pawn, restriction, true);
                    cell = game.getMap()[cellCoordinates[0]][cellCoordinates[1]];
                }
                if (cell != null) {
                    cell.setFloor(cell.getFloor() + 1);
                    if (cell.getFloor() == 4) cell.setHasDome(true);
                    utility.toAllClientsVoid(game, "printBoardColored", game);
                }
            }
        } else if (god.equals("Hestia")){
            if (!checkHasLost.checkHasLostForBuild(game, pawn, false,  null, false)) {
                Cell restriction = cell;
                cell = null;
                if (client.toClientGetObject("waitForBuildCommand", game, pawn, false, true,false).equals(false)) {
                    cellCoordinates = (int[]) client.toClientGetObject("getBuildLocationViaArrows", game, pawn, restriction, false);
                    cell = game.getMap()[cellCoordinates[0]][cellCoordinates[1]];
                }
                if (cell != null) {
                    cell.setFloor(cell.getFloor() + 1);
                    if (cell.getFloor() == 4) cell.setHasDome(true);
                    utility.toAllClientsVoid(game, "printBoardColored", game);
                }
            }
        } else if (god.equals("Hephaestus")) {
            if (cell.getFloor()<3 && (Boolean) client.toClientGetObject("askBuildTwice" , pawn.getPlayer())){
                cell.setFloor(cell.getFloor()+1);
                utility.toAllClientsVoid(game, "printBoardColored", game);
            }
        }


    }


    /**Method that handles the ares power server-side.
     * First of all it selects the correct pawn (the one not used during the turn),
     * then it asks to the client which block he wants to delete, and at the end it updates the game status.
     *
     * @param game: Game
     * @param usedPawn: Pawn, the pawn next to which we will delete a block
     * @throws IOException
     */
    public void aresPower(Game game, Pawn usedPawn) throws IOException {
        Pawn activePawn= null;
        if (usedPawn.getId()==1) activePawn= usedPawn.getPlayer().getPawns()[1];
        else activePawn= usedPawn.getPlayer().getPawns()[0];

        int [] coords = (int []) usedPawn.getPlayer().getRelatedClient().toClientGetObject("aresPower" , game, activePawn);

        game.getMap()[coords[0]][coords[1]].setFloor(game.getMap()[coords[0]] [coords[1]].getFloor()-1);
    }
}
