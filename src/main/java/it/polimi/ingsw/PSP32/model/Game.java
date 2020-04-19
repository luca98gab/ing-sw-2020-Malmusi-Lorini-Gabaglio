package it.polimi.ingsw.PSP32.model;

import java.util.ArrayList;

public class Game {
    private Cell[][] map;
    private int playerNum;
    private ArrayList<Player> playerList;
    private Boolean athenaFlag;

    public int getPlayerNum() {
        return playerNum;
    }

    public Boolean getAthenaFlag() {
        return athenaFlag;
    }

    public void setAthenaFlag(Boolean athenaFlag) {
        this.athenaFlag = athenaFlag;
    }

    /**
     * getWhosnext
     * @param p Object containing the player playing right now
     * @return The next player
     */
    public Player getWhosNext(Player p) {
        if (playerList.size() == 2) {
            if (p == playerList.get(0)) return playerList.get(1);
            else return playerList.get(0);
        } else {
            if (p == playerList.get(0)) return playerList.get(1);
            else if (p == playerList.get(1)) return playerList.get(2);
            else return playerList.get(0);
        }
    }

    public Player getPlayer(int n) {
        return playerList.get(n);
    }

    public Cell[][] getMap() {
        return map;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<Player> playerList) {
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
        athenaFlag = false;
    }
}
