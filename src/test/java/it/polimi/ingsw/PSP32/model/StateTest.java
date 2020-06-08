package it.polimi.ingsw.PSP32.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class StateTest {

    State state = null;
    ArrayList<Player> players;
    Game game1 = null;
    Player player1 = null;
    Player player2 = null;
    Player player3 = null;

    @Before
    public void setup() {
        players = new ArrayList<>();
        player1 = new Player("Giorgio", "Blu", null);
        player2 = new Player("Davide", "Rosso", null);
        player3 = new Player("Luca", "Verde", null);
        players.add(0, player1);
        players.add(1, player2);
        players.add(2, player3);

        game1 = new Game(3);
        game1.setPlayerList(players);
        state = new State(game1);
    }

    @After
    public void teardown() {
        players = null;
        state = null;
        game1 = null;
        player1 = null;
        player2 = null;
        player3 = null;
    }

    @Test
    public void getTurn_setTurn_correctI_correctO() {
        state.setNextTurn(game1);
        assertEquals(state.getTurn().getName(), "Davide");
        state.setNextTurn(game1);
        assertEquals(state.getTurn().getName(), "Luca");
        state.setNextTurn(game1);
        assertEquals(state.getTurn().getName(), "Giorgio");
    }
}