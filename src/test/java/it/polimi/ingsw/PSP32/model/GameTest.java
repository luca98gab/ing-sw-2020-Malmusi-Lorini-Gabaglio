package it.polimi.ingsw.PSP32.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {

    Game game1 = null;
    Game game2 = null;
    Player player1 = null;
    Player player2 = null;
    Player player3 = null;

    @Before
    public void setup() {
        game1 = new Game(2);
        game2 = new Game(3);
        player1 = new Player(null, null, null);
        player2 = new Player(null, null, null);
        player3 = new Player(null, null, null);
    }

    @After
    public void teardown() {
        game1 = null;
        game2 = null;
        player1 = null;
        player2 = null;
        player3 = null;
    }

    @Test
    public void setPlayingOrder_getWhosNext_correctI_correctO(){
        game1.setPlayingOrder(player1, player2, null);
        assertEquals(game1.getWhosNext(player1), player2);
        assertEquals(game1.getWhosNext(player2), player1);
        game2.setPlayingOrder(player1, player2, player3);
        assertEquals(game2.getWhosNext(player1), player2);
        assertEquals(game2.getWhosNext(player2), player3);
        assertEquals(game2.getWhosNext(player3), player1);
    }

    @Test
    public void getPlayer_setPlayingOrder_correctI_correctO(){
        game1.setPlayingOrder(player1, player2, null);
        assertEquals(game1.getPlayer(0), player1);
    }

    @Test
    public void getPlayerNum_correctI_correctO(){
        assertEquals(game1.getPlayerNum(), 2);
        assertEquals(game2.getPlayerNum(), 3);
    }


}