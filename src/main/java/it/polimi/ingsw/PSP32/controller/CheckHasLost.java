package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.Cell;
import it.polimi.ingsw.PSP32.model.Game;
import it.polimi.ingsw.PSP32.model.Pawn;
import it.polimi.ingsw.PSP32.model.Player;

import java.io.IOException;

public class CheckHasLost {


    /** Method to check if the player can move anywhere (Overload)
     *
     * @param game : Game
     * @param player : Pawn active pawn
     * @return Boolean(True= no valid build locations, False= there are valid build locations)
     * @throws IOException
     */
    protected static Boolean checkHasLostForMoves(Game game, Player player) throws IOException {
        int movablePawns = 0;
        for (int j = 0; j < player.getPawns().length; j++){
            if (CheckCanMove.checkCanMoveE(game, player.getPawns()[j], null) || CheckCanMove.checkCanMoveW(game, player.getPawns()[j], null) ||
                    CheckCanMove.checkCanMoveN(game, player.getPawns()[j], null) || CheckCanMove.checkCanMoveS(game, player.getPawns()[j], null) ||
                    CheckCanMove.checkCanMoveSE(game, player.getPawns()[j], null) || CheckCanMove.checkCanMoveNE(game, player.getPawns()[j], null) ||
                    CheckCanMove.checkCanMoveSW(game, player.getPawns()[j], null) || CheckCanMove.checkCanMoveNW(game, player.getPawns()[j], null)){
                movablePawns++;
            }
        }
        if (movablePawns == 0){
            if (game.getPlayerList().size()==3){
                Utility.toAllClientsVoid(game, "removedPlayerGraphics", player);
                game.getPlayerList().remove(player);
                return true;
            } else {
                Utility.toAllClientsVoid(game,"endGameGraphics" , game.getWhosNext(player));
                while (true);
            }
        }
        return false;
    }
    protected static Boolean checkHasLostForMoves(Game game, Pawn pawn, Boolean artemis,  Cell restriction) throws IOException {
        int movablePawns = 0;
        if (CheckCanMove.checkCanMoveE(game, pawn, restriction) || CheckCanMove.checkCanMoveW(game, pawn, restriction) ||
                CheckCanMove.checkCanMoveN(game, pawn, restriction) || CheckCanMove.checkCanMoveS(game, pawn, restriction) ||
                CheckCanMove.checkCanMoveSE(game, pawn, restriction) || CheckCanMove.checkCanMoveNE(game, pawn, restriction) ||
                CheckCanMove.checkCanMoveSW(game, pawn, restriction) || CheckCanMove.checkCanMoveNW(game, pawn, restriction)){
            movablePawns++;
        }
        if(artemis && movablePawns==0){
            return true;
        }
        if (artemis){
            return false;
        }

        if (movablePawns == 0){
            if (game.getPlayerList().size()==3){
                Utility.toAllClientsVoid(game, "removedPlayerGraphics", pawn.getPlayer());
                game.getPlayerList().remove(pawn.getPlayer());
                return true;
            } else {
                Utility.toAllClientsVoid(game,"endGameGraphics" , game.getWhosNext(pawn.getPlayer()));
                while (true);
            }
        }
        return false;
    }
    protected static Boolean checkHasLostForMoves(Game game, Pawn pawn) throws IOException {
       return checkHasLostForMoves(game, pawn, false,  null);
    }

    /** Method to check if the player can build anywhere (Overload)
     *
     * @param game : Game
     * @param pawn : Pawn active pawn
     * @param demeterPower : Boolean flag to know if this is the first build of demeter (i.e. the player can't use his power)
     * @param cell : Cell current cell
     * @return Boolean(True= no valid build locations, False= there are valid build locations)
     * @throws IOException
     */
    protected static Boolean checkHasLostForBuild(Game game, Pawn pawn, Boolean demeterPower, Cell cell, Boolean edgeCellsAllowed) throws IOException {
        Player player = pawn.getPlayer();


        if (CheckCanBuild.checkCanBuildE(game, pawn, cell, edgeCellsAllowed) || CheckCanBuild.checkCanBuildW(game, pawn, cell, edgeCellsAllowed) ||
                CheckCanBuild.checkCanBuildN(game, pawn, cell, edgeCellsAllowed) || CheckCanBuild.checkCanBuildS(game, pawn, cell, edgeCellsAllowed) ||
                CheckCanBuild.checkCanBuildSE(game, pawn, cell, edgeCellsAllowed) || CheckCanBuild.checkCanBuildNE(game, pawn, cell, edgeCellsAllowed) ||
                CheckCanBuild.checkCanBuildSW(game, pawn, cell, edgeCellsAllowed) || CheckCanBuild.checkCanBuildNW(game, pawn, cell, edgeCellsAllowed)){
            return false;
        }
        else {
            if(demeterPower) {
                if (game.getPlayerList().size() == 3) {
                    Utility.toAllClientsVoid(game, "removedPlayerGraphics", player);
                    game.getPlayerList().remove(player);
                    return true;
                } else {
                    game.getPlayerList().remove(player);
                    player.getRelatedClient().toClientVoid("endGameGraphics", game.getPlayerList().get(0));
                    while (true) ;
                }
            }
            else return true;
        }



    }
    protected static Boolean checkHasLostForBuild(Game game, Pawn pawn, Boolean demeter, Cell cell) throws IOException {
        return checkHasLostForBuild(game, pawn, false, null, true);
    }
    protected static Boolean checkHasLostForBuild(Game game, Pawn pawn) throws IOException {
        return checkHasLostForBuild(game, pawn, false, null);
    }
}
