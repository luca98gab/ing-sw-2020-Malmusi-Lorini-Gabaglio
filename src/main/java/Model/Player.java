package Model;

public class Player {
    private Pawn pawn1;
    private Pawn pawn2;
    private String color;
    private God power;
    private String name;

    public String getColor() {
        return color;
    }

    public God getGod(){
        return power;
    }

    public String getName() {
        return name;
    }
/*
    public Player(String color, String name, God power){
        pawn1 = new Pawn();
        pawn2 = new Pawn();

    }
 */
    public Player(){
        pawn1 = null;
        pawn2 = null;
        color = "";
        power = null;
        name = "";
    }
}
