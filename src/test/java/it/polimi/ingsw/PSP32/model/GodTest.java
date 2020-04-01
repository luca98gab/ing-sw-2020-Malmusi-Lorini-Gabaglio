package it.polimi.ingsw.PSP32.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GodTest {

    God myGod = null;

    @Before
    public void setup() {
        myGod = new God("Apollo", "Abilità di Apollo");
    }

    @After
    public void teardown() {
        myGod = null;
    }

    @Test
    public void getName_correctI_correctO() {
        assertEquals(myGod.getName(), "Apollo");
    }

    @Test
    public void getAbility_correctI_correctO() {
        assertEquals(myGod.getAbility(), "Abilità di Apollo");
    }

}