package it.polimi.ingsw.PSP32.model;

public class God {
    private String name;
    private String ability;

    public String getAbility() {
        return ability;
    }

    public String getName() {
        return name;
    }

    public God(String name, String ability) {
        this.name = name;
        this.ability =  ability;
    }
}