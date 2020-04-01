package it.polimi.ingsw.PSP32.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StateTest {

    State state = null;
    Player player1 = null;
    Player player2 = null;
    Player player3 = null;
    Game game1 = null;

    @Before
    public void setup() {
        player1 = new Player("Giorgio", "Blu", null);
        player2 = new Player("Davide", "Rosso", null);
        player3 = new Player("Luca", "Verde", null);
        game1 = new Game(3);
        game1.setPlayingOrder(player1, player2, player3);
        state = new State(game1);
    }

    @After
    public void teardown() {
        player1 = null;
        player2 = null;
        player3 = null;
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