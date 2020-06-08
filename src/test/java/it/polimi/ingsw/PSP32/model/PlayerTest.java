package it.polimi.ingsw.PSP32.model;

import it.polimi.ingsw.PSP32.server.ClientHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    Player player1 = null;
    Player player2 = null;
    God myGod = null;
    God myGod2 = null;
    ClientHandler clientHandler = null;
    Cell cell = null;

    @Before
    public void setup() {
        myGod = new God("Apollo", "Abilità di Apollo");
        myGod2 = new God("Atena", "Abilità di Atena");
        player1 = new Player("Giorgio", "Blu", myGod);
        player2 = new Player("Giorgio", "Blu", myGod);
        clientHandler = null;
        player1.getPawns()[0] = new Pawn(0, 0, 0, null);
        player1.getPawns()[0].moves(2,4);
    }

    @After
    public void teardown() {
        player1 = null;
        player2 = null;
        myGod = null;
        myGod2 = null;
        clientHandler = null;
        cell = null;
    }

    @Test
    public void getTester_correctI_correctO() {
        assertEquals(player1.getName(), "Giorgio");
        assertEquals(player1.getColor(), "Blu");
        assertEquals(player1.getGod().getName(), "Apollo");
        assertEquals(player1.getPawns()[0].getX(), 2);
    }

    @Test
    public void setGod_correctI_correctO() {
        player1.setGod(myGod2);
        assertEquals(player1.getGod().getName(), "Atena");
    }

    @Test
    public void get_and_set_RelatedClient_correctI_correctO() {
        player1.setRelatedClient(clientHandler);
        assertEquals(player1.getRelatedClient(), null);
    }

    @Test
    public void equals_correctI_correctO() {
        assertEquals(player1.equals(player1), true);
        assertEquals(player1.equals(player2), false);
        assertEquals(player1.equals(cell), false);
    }

}