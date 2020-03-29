package Model;

public class Pawn {
    private int x;
    private int y;
    private Player player;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moves(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pawn(int x, int y) {
        this.x = x;
        this.y = y;
        player = null;
    }
}
