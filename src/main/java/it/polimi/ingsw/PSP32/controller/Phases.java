package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.Game;
import it.polimi.ingsw.PSP32.model.Pawn;
import it.polimi.ingsw.PSP32.model.Player;

import java.io.IOException;

public class Phases {

    private final CheckHasLost checkHasLost;
    private final CheckHasWon checkHasWon;
    private final Moving moving;
    private final Building building;
    private final Utility utility;

    public Phases(Utility utility){
        this.utility = utility;
        CheckHasLost checkHasLost = new CheckHasLost(utility);
        CheckHasWon checkHasWon = new CheckHasWon(utility);
        this.checkHasLost = checkHasLost;
        this.checkHasWon = checkHasWon;
        this.moving = new Moving(checkHasLost, checkHasWon, utility);
        this.building = new Building(checkHasLost, checkHasWon, utility);
    }

    public Phases(CheckHasLost checkHasLost, CheckHasWon checkHasWon, Moving moving, Building building, Utility utility){
        this.utility = utility;
        this.checkHasWon = checkHasWon;
        this.checkHasLost = checkHasLost;
        this.moving = moving;
        this.building = building;
    }

    /** Method that starts the game and manages the sequence of events
     *
     * @param game : Game
     * @throws IOException
     */
    public void startGame(Game game) throws IOException {

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
    private Boolean turn(Game game, Player player) throws IOException {
        Pawn activePawn=null;
        if (player.getGod().getName().equals("Athena")) {
            game.setAthenaFlag(false);
        }

        player.getRelatedClient().toClientVoid("printTurnInfo", player);

        checkHasWon.checkHasWon5Domes(game, player);

        if (checkHasLost.checkHasLostForMoves(game, player).equals(false)){

            activePawn = moving.movePhase(game, player);
            if (activePawn==null) {
                return true;
            }

            if(!checkHasLost.checkHasLostForBuild(game, activePawn)){
                building.buildPhase(game, activePawn);
                checkHasWon.checkHasWon5Domes(game, player);
            }
            else return true;
        } else return true;

        if (player.getGod().getName().equals("Ares") && CheckCanBuild.checkAresPower(game, activePawn))
        {
            if ((Boolean)player.getRelatedClient().toClientGetObject("wantsToUsePower",player)){
                building.aresPower(game, activePawn);
            }
            utility.toAllClientsVoid(game, "printBoardColored", game);
        }

        return false;
    }

    Boolean turnTest(Game game, Player player) throws IOException {
        return turn(game, player);
    }
}
