package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.Cell;
import it.polimi.ingsw.PSP32.model.Game;
import it.polimi.ingsw.PSP32.model.Pawn;
import it.polimi.ingsw.PSP32.model.Player;

import java.io.IOException;

public class Moving {

    private final CheckHasWon checkHasWon;
    private final CheckHasLost checkHasLost;
    private final Utility utility;

    Moving(CheckHasLost checkHasLost, CheckHasWon checkHasWon, Utility utility){
        this.utility = utility;
        this.checkHasLost = checkHasLost;
        this.checkHasWon = checkHasWon;
    }

    public Moving(){
        this.utility = new Utility();
        this.checkHasWon = new CheckHasWon(utility);
        this.checkHasLost = new CheckHasLost(utility);
    }


    /** Method that manages the move phase, handling the various gods powers
     *
     * @param game
     * @param player : Player current player
     * @return Pawn: return the pawn that just moved
     * @throws IOException
     */
    protected Pawn movePhase(Game game, Player player) throws IOException {

        for (int i=0; i<game.getPlayerList().size(); i++){
            if (!player.equals(game.getPlayer(i)))

                game.getPlayer(i).getRelatedClient().toClientVoid("waitTurnMessage", player.getName(), player.getColor()    );

        }
        String god = player.getGod().getName();
        int[] move = null;
        int activePawnId;
        Pawn activePawn = null;
        Cell startPosition;
        do {

            activePawnId =((Pawn) player.getRelatedClient().toClientGetObject("getActivePawn", game, player)).getId();
            for (int i = 0; i < player.getPawns().length; i++){
                if (player.getPawns()[i].getId()==activePawnId) activePawn = player.getPawns()[i];
            }
            activePawn.getPlayer().setRelatedClient(player.getRelatedClient());
            startPosition = game.getMap()[activePawn.getX()][activePawn.getY()];
            if (god.equals("Prometheus") && ((Boolean) player.getRelatedClient().toClientGetObject("wantsToUsePower", player))){

                int[] cellCoordinates = ((int[]) player.getRelatedClient().toClientGetObject("getBuildLocationViaArrows",game, activePawn, null));
                Cell cell = game.getMap()[cellCoordinates[0]][cellCoordinates[1]];
                cell.setFloor(cell.getFloor()+1);
                if (cell.getFloor() == 4) cell.setHasDome(true);
                utility.toAllClientsVoid(game, "printBoardColored", game);

                Boolean changedFlag;
                if (game.getAthenaFlag().equals(true)) {
                    changedFlag = false;
                } else {
                    changedFlag = true;
                    game.setAthenaFlag(true);
                }

                if(!checkHasLost.checkHasLostForMoves(game, activePawn)) {
                    player.getRelatedClient().toClientGetObject("waitForMoveCommand", game, activePawn, false, false);
                    move = (int[]) player.getRelatedClient().toClientGetObject("getValidMoveViaArrows", game, activePawn, null, false);

                    if (changedFlag.equals(true)) game.setAthenaFlag(false);
                }
                else return null;
            }
            else if ((Boolean) player.getRelatedClient().toClientGetObject("waitForMoveCommand",game, activePawn, true, false)){
                move = (int []) player.getRelatedClient().toClientGetObject("getValidMoveViaArrows", game, activePawn, null, true);
            }
        } while (move==null);

        if (game.getMap()[move[0]][move[1]].getIsFull()!=null){
            Pawn opponentPawn = game.getMap()[move[0]][move[1]].getIsFull();
            if (god.equals("Apollo")){
                switchPawns(game, activePawn, opponentPawn);
            }
            else if (god.equals("Minotaur")){
                pushPawns(game, activePawn, opponentPawn);
            }
        } else movePawnSecure(game, activePawn, move[0], move[1]);

        utility.toAllClientsVoid(game, "printBoardColored", game);

        if (god.equals("Artemis")) {
            if(!checkHasLost.checkHasLostForMoves(game, player, true)) {
                if (player.getRelatedClient().toClientGetObject("waitForMoveCommand", game, activePawn, false, true).equals(true)) {
                        move = (int[]) player.getRelatedClient().toClientGetObject("getValidMoveViaArrows", game, activePawn, startPosition, false);
                }
                if (move != null) {
                    movePawnSecure(game, activePawn, move[0], move[1]);
                    utility.toAllClientsVoid(game, "printBoardColored", game);
                }
            }
        } else if (god.equals("Athena")){
            if (game.getMap()[activePawn.getX()][activePawn.getY()].getFloor()-startPosition.getFloor()==1){
                game.setAthenaFlag(true);
            }
        }

        checkHasWon.checkHasWon(game, activePawn, startPosition);

        return activePawn;
    }

    /** Method to update the state of the board
     *
     * @param game : Game
     * @param pawn : Pawn active pawn
     * @param x : int new x coord. of the pawn
     * @param y : int new y coord. of the pawn
     */
    private void movePawnSecure(Game game, Pawn pawn, int x, int y){
        game.getMap()[pawn.getX()][pawn.getY()].setIsFull(null);
        game.getMap()[x][y].setIsFull(pawn);
        pawn.moves(x, y);
    }

    /** Apollo's oriented method, it switches the position of the 2 given pawns
     *
     * @param game : Game
     * @param pawn1 : Pawn first of the couple
     * @param pawn2 : Pawn second pawn of the couple
     */
    private void switchPawns(Game game, Pawn pawn1, Pawn pawn2){
        int x1 = pawn1.getX();
        int y1 = pawn1.getY();
        int x2 = pawn2.getX();
        int y2 = pawn2.getY();

        game.getMap()[x1][y1].setIsFull(pawn2);
        pawn2.moves(x1, y1);

        game.getMap()[x2][y2].setIsFull(pawn1);
        pawn1.moves(x2, y2);
    }

    /** Minotaur's oriented method, it positions the opponent's pawn where the Minotaur pushes it
     *
     * @param game : Game
     * @param pawn : Pawn pawn that's pushing
     * @param opponentPawn : Pawn that gets pushed
     */
    private void pushPawns(Game game, Pawn pawn, Pawn opponentPawn){

        int x0 = pawn.getX();
        int y0 = pawn.getY();
        int x1 = opponentPawn.getX();
        int y1 = opponentPawn.getY();
        int x2 = opponentPawn.getX()*2 - pawn.getX();
        int y2 = opponentPawn.getY()*2 - pawn.getY();

        game.getMap()[x0][y0].setIsFull(null);
        game.getMap()[x1][y1].setIsFull(pawn);
        pawn.moves(x1, y1);

        game.getMap()[x2][y2].setIsFull(opponentPawn);
        opponentPawn.moves(x2, y2);
    }

}
