package it.polimi.ingsw.PSP32.model;

public class Game {
    private Cell[][] map;
    private int playerNum;
    private Player[] playerList;

    public int getPlayerNum() {
        return playerNum;
    }

    /**
     * getWhosnext
     * @param p Object containing the player playing right now
     * @return The next player
     */
    public Player getWhosNext(Player p) {
        if (playerList.length == 2) {
            if (p == playerList[0]) return playerList[1];
            else return playerList[0];
        } else {
            if (p == playerList[0]) return playerList[1];
            else if (p == playerList[1]) return playerList[2];
            else return playerList[0];
        }
    }

    public Player getPlayer(int n) {
        return playerList[n];
    }

    public Cell[][] getMap() {
        return map;
    }

    public Player[] getPlayerList() {
        return playerList;
    }

    public void setPlayerList(Player[] playerList) {
        this.playerList = playerList;
    }

    public Game(int n){
        map = new Cell[5][5];
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++){
                map[i][j]=new Cell();
            }
        }
        playerNum = n;
        playerList = null;
    }
}
