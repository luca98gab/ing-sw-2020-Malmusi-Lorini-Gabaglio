package Model;

public class Game {
    private Cell[][] map;
    private int playerNum;
    private Player[] playingOrder;

    public int getPlayerNum() {
        return playerNum;
    }

    public Player getWhosNext(Player p) {
        if (p == playingOrder[0]) return playingOrder[1];
        else if (p == playingOrder[1]) return playingOrder[2];
        else if (p == playingOrder[2]) return playingOrder[0];
        return null;
    }

    public Player getPlayer(int n) {
        return playingOrder[n];
    }

    public Game(int n){
        map = new Cell[5][5];
        playerNum = n;
        playingOrder = new Player[n];
    }
}
