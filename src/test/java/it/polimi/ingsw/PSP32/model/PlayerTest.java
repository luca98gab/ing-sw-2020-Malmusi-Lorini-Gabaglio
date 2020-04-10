package it.polimi.ingsw.PSP32.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    Player player1 = null;
    God myGod = null;
    God myGod2 = null;

    @Before
    public void setup() {
        myGod = new God("Apollo", "Abilità di Apollo");
        myGod2 = new God("Atena", "Abilità di Atena");
        player1 = new Player("Giorgio", "Blu", myGod);
    }

    @After
    public void teardown() {
        player1 = null;
        myGod = null;
    }

    @Test
    public void getTester_correctI_correctO() {
        assertEquals(player1.getName(), "Giorgio");
        assertEquals(player1.getColor(), "Blu");
        assertSame(player1.getGod(), myGod);
        assertEquals(player1.getGod().getName(), "Apollo");
    }

    @Test
    public void setGod_correctI_correctO() {
        player1.setGod(myGod2);
        assertEquals(player1.getGod().getName(), "Atena");
    }

}