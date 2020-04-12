package it.polimi.ingsw.PSP32.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {

    Game game1 = null;
    Game game2 = null;
    Player[] players = new Player[3];

    @Before
    public void setup() {
        game1 = new Game(2);
        game2 = new Game(3);
        players[0] = new Player(null, null, null);
        players[1] = new Player(null, null, null);
        players[2] = new Player(null, null, null);
    }

    @After
    public void teardown() {
        game1 = null;
        game2 = null;
        players = null;
    }



    @Test
    public void getPlayerNum_correctI_correctO(){
        assertEquals(game1.getPlayerNum(), 2);
        assertEquals(game2.getPlayerNum(), 3);
    }


}