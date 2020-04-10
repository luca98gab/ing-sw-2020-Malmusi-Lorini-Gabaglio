package it.polimi.ingsw.PSP32.model;

public class Player {
    private Pawn pawn1;
    private Pawn pawn2;
    private String color;
    private God power;
    private String name;

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
        pawn1 = new Pawn(0,0);
        pawn2 = new Pawn(0,0);;
        this.color = color;
        this.power = power;
        this.name = name;
    }
}
