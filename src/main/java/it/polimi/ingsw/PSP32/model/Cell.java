package it.polimi.ingsw.PSP32.model;

public class Cell {
    private int floor;
    private Pawn pawnHere;
    private Boolean hasDome;

    public int getFloor() {
        return floor;
    }

    public Boolean getHasDome() {
        return hasDome;
    }

    public Pawn getIsFull() {
        return pawnHere;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setHasDome(Boolean hasDome) {
        this.hasDome = hasDome;
    }

    public void setIsFull(Pawn isFull) {
        this.pawnHere = isFull;
    }

    public Cell() {
        floor = 0;
        pawnHere = null;
        hasDome = false;
    }
}
