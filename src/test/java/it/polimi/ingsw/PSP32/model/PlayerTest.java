package it.polimi.ingsw.PSP32.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    Player player1 = null;
    God myGod = null;

    @Before
    public void setup() {
        myGod = new God("Apollo", "Abilità di Apollo");
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

}