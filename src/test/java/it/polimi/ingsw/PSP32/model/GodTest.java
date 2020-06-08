package it.polimi.ingsw.PSP32.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GodTest {

    God myGod1 = null;
    God myGod2 = null;
    Cell cell = null;

    @Before
    public void setup() {
        myGod1 = new God("Apollo", "Abilità di Apollo");
        myGod2 = new God("Apollo", "Abilità di Apollo");
        cell = new Cell();
    }

    @After
    public void teardown() {
        myGod1 = null;
        myGod2 = null;
    }

    @Test
    public void getName_correctI_correctO() {
        assertEquals(myGod1.getName(), "Apollo");
    }

    @Test
    public void getAbility_correctI_correctO() {
        assertEquals(myGod1.getAbility(), "Abilità di Apollo");
    }

    @Test
    public void equals_correctI_correctO() {
        assertEquals(myGod1.equals(myGod1), true);
        assertEquals(myGod1.equals(myGod2), true);
        assertEquals(myGod1.equals(cell), false);
    }

}