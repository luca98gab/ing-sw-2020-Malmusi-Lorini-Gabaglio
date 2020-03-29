package Model;

public class Cell {
    private int floor;
    private Pawn isFull;
    private Boolean hasDome;

    public int getFloor() {
        return floor;
    }

    public Pawn fullState() {
        return isFull;
    }

    public Boolean getHasDome() {
        return hasDome;
    }

    public void modifyFloor(int floor) {
        this.floor = floor;
    }

    public void setIsFull(Pawn isFull) {
        this.isFull = isFull;
    }

    public Cell() {
        floor = 0;
        isFull = null;
        hasDome = false;
    }
}
