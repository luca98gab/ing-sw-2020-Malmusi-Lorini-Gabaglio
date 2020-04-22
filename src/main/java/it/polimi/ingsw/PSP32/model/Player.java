package it.polimi.ingsw.PSP32.model;

import it.polimi.ingsw.PSP32.server.ClientHandler;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Player implements Serializable {
    private Pawn[] pawns;
    private String color;
    private God power;
    private String name;
    private transient ClientHandler relatedClient;

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

    public ClientHandler getRelatedClient() {
        return relatedClient;
    }

    public void setRelatedClient(ClientHandler relatedClient) {
        this.relatedClient = relatedClient;
    }

    public Player(String name, String color, God power){
        pawns = new Pawn[2];
        this.color = color;
        this.power = power;
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return Arrays.equals(pawns, player.pawns) &&
                Objects.equals(color, player.color) &&
                Objects.equals(power, player.power) &&
                Objects.equals(name, player.name) &&
                Objects.equals(relatedClient, player.relatedClient);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(color, power, name, relatedClient);
        result = 31 * result + Arrays.hashCode(pawns);
        return result;
    }
}
