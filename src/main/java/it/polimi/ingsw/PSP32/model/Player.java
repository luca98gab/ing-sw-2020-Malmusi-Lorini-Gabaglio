package it.polimi.ingsw.PSP32.model;

public class Player {
    private Pawn[] pawns;
    private String color;
    private God power;
    private String name;

    public Pawn[] getPawns() {
        return pawns;
    }

    public String getColor() {
        return color;
    }

    public void setGod(God power){
        this.power = power;
    }

    public God getGod(){
        return power;
    }

    public String getName() {
        return name;
    }

    public Player(String name, String color, God power){
        pawns = new Pawn[2];
        this.color = color;
        this.power = power;
        this.name = name;
    }
}
