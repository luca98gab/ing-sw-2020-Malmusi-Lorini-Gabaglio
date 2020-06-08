package it.polimi.ingsw.PSP32.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameTest {

    Game game1 = null;
    Game game2 = null;
    ArrayList<Player> twoPlayers = null;
    ArrayList<Player> threePlayers = null;

    @Before
    public void setup() {
        game1 = new Game(2);
        game2 = new Game(3);
        twoPlayers = new ArrayList<Player>(2);
        threePlayers = new ArrayList<Player>(3);
        Player player1 = new Player("arturo", null, null);
        Player player2 = new Player("gianni", null, null);
        Player player3 = new Player("piero", null, null);
        twoPlayers.add(0, player1);
        twoPlayers.add(1, player2);
        threePlayers.add(0, player1);
        threePlayers.add(1, player2);
        threePlayers.add(2, player3);
        game1.getMap()[1][1].setFloor(2);

    }

    @After
    public void teardown() {
        game1 = null;
        game2 = null;
        twoPlayers = null;
        threePlayers = null;
    }

    @Test
    public void getPlayerNum_correctI_correctO(){
        assertEquals(game1.getPlayerNum(), 2);
        assertEquals(game2.getPlayerNum(), 3);
    }

    @Test
    public void getAthenaFlag_correctI_correctO(){
        assertEquals(game1.getAthenaFlag(), false);
        game1.setAthenaFlag(true);
        assertEquals(game1.getAthenaFlag(), true);
    }

    @Test
    public void getWhosNext_correctI_correctO(){
        game1.setPlayerList(twoPlayers);
        assertEquals(game1.getWhosNext(twoPlayers.get(0)).getName(), "gianni");
        assertEquals(game1.getWhosNext(twoPlayers.get(1)).getName(), "arturo");
        game2.setPlayerList(threePlayers);
        assertEquals(game2.getWhosNext(threePlayers.get(0)).getName(), "gianni");
        assertEquals(game2.getWhosNext(threePlayers.get(1)).getName(), "piero");
        assertEquals(game2.getWhosNext(threePlayers.get(2)).getName(), "arturo");
    }

    @Test
    public void getPlayerList_correctI_correctO(){
        game1.setPlayerList(twoPlayers);
        game2.setPlayerList(threePlayers);
        assertEquals(game1.getPlayerList().get(1).getName(), "gianni");
        assertEquals(game2.getPlayerList().get(1).getName(), "gianni");
    }

    @Test
    public void getPlayer_correctI_correctO(){
        game1.setPlayerList(twoPlayers);
        game2.setPlayerList(threePlayers);
        assertEquals(game1.getPlayer(1).getName(), "gianni");
        assertEquals(game2.getPlayer(2).getName(), "piero");
    }

    @Test
    public void getMap_correctI_correctO(){
        assertEquals(game1.getMap()[1][1].getFloor(), 2);
    }




}