package it.polimi.ingsw.PSP32.model;

import java.io.Serializable;

public class Pawn implements Serializable {
    private int x;
    private int y;
    private int id;
    private Player player;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     *
     * @param x new x coordinate of the pawn
     * @param y new y coordinate of the pawn
     */
    public void moves(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pawn(int x, int y, int id, Player player) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.player = player;
    }
}
