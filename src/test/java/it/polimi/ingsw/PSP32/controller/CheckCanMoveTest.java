package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.Game;
import it.polimi.ingsw.PSP32.model.Player;
import it.polimi.ingsw.PSP32.model.State;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class CheckCanMoveTest {

    State state = null;
    ArrayList<Player> players;
    Game game1 = null;

    @Before
    public void setup() {
        players.set(0, new Player("Giorgio", "Blu", null));
        players.set(1, new Player("Davide", "Rosso", null));
        players.set(2, new Player("Luca", "Verde", null));

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