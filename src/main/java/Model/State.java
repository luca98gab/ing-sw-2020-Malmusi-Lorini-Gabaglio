package Model;

public class State {
    private Player turn;

    public Player getTurn() {
        return turn;
    }

    public void nextTurn(Game g) {
        turn = g.getWhosNext(turn);
    }

    public State(Game g) {
        turn = g.getPlayer(0);
    }

}
