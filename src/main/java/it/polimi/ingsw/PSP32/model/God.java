package it.polimi.ingsw.PSP32.model;

import java.io.Serializable;
import java.util.Objects;

public class God implements Serializable {
    private final String name;
    private final String ability;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof God)) return false;
        God god = (God) o;
        return Objects.equals(name, god.name) &&
                Objects.equals(ability, god.ability);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ability);
    }
}