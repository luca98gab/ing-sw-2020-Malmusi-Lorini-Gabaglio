package it.polimi.ingsw.PSP32.model;

public class State {
    private Player turn;

    public Player getTurn() {
        return turn;
    }

    public void setNextTurn(Game g) {
        turn = g.getWhosNext(turn);
    }

    public State(Game g) {
        turn = g.getPlayer(0);
    }

}
