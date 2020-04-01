package it.polimi.ingsw.PSP32.model;

public class Game {
    private Cell[][] map;
    private int playerNum;
    private Player[] playingOrder;

    public int getPlayerNum() {
        return playerNum;
    }

    /**
     * getWhosnext
     * @param p Object containing the player playing right now
     * @return The next player
     */
    public Player getWhosNext(Player p) {
        if (playingOrder.length == 2) {
            if (p == playingOrder[0]) return playingOrder[1];
            else return playingOrder[0];
        } else {
            if (p == playingOrder[0]) return playingOrder[1];
            else if (p == playingOrder[1]) return playingOrder[2];
            else return playingOrder[0];
        }
    }

    public Player getPlayer(int n) {
        return playingOrder[n];
    }

    /**
     *
     * @param p1 First player related object
     * @param p2 Second // //
     * @param p3 Third // // Optional: If there are only 2 players set to 'null'
     */
    public void setPlayingOrder(Player p1, Player p2, Player p3) {
        this.playingOrder[0] = p1;
        this.playingOrder[1] = p2;
        if (p3 != null) this.playingOrder[2] = p3;
    }

    public Game(int n){
        map = new Cell[5][5];
        playerNum = n;
        playingOrder = new Player[n];
    }
}
