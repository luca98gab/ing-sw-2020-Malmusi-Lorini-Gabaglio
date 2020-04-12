package it.polimi.ingsw.PSP32.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StateTest {

    State state = null;
    Player[] players = new Player[3];
    Game game1 = null;

    @Before
    public void setup() {
        players[0] = new Player("Giorgio", "Blu", null);
        players[1] = new Player("Davide", "Rosso", null);
        players[2] = new Player("Luca", "Verde", null);

        game1 = new Game(3);
        game1.setPlayerList(players);
        state = new State(game1);
    }

    @After
    public void teardown() {
        players = null;
        state = null;
        game1 = null;
    }

    @Test
    public void get_setTurn_correctI_correctO() {
        state.setNextTurn(game1);
        assertEquals(state.getTurn().getName(), "Davide");
        state.setNextTurn(game1);
        assertEquals(state.getTurn().getName(), "Luca");
        state.setNextTurn(game1);
        assertEquals(state.getTurn().getName(), "Giorgio");
    }

}