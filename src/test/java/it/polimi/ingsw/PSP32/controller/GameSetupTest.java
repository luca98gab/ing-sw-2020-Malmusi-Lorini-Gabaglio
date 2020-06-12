package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.*;
import it.polimi.ingsw.PSP32.server.ClientHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

public class GameSetupTest {

    ClientHandler clientHandler = null;
    ClientHandler clientHandler2 = null;

    God[] gods = null;
    God[] chosenGods0 = null;
    God[] chosenGods1 = null;
    int[] coordinates = null;
    God apollo = null;
    God artemis = null;
    God athena = null;
    God atlas = null;
    God demeter = null;
    God hephaestus = null;
    God minotaur = null;
    God pan = null;
    God prometheus = null;
    God hera = null;
    God chronus = null;
    God hestia = null;
    God zeus = null;
    God ares = null;
    ArgumentCaptor<God[]> captor = ArgumentCaptor.forClass(God[].class);
    Game game0 = null;
    Game game1 = null;
    ArrayList<Player> arraylist0 = null;
    ArrayList<Player> arraylist1 = null;
    Cell cell = null;
    God god = null;
    Player player0 = null;
    Player player1 = null;
    Player player2 = null;

    @Before
    public void setup() {
        clientHandler = Mockito.mock(ClientHandler.class);
        clientHandler2 = Mockito.mock(ClientHandler.class);
        gods = new God[14];
        coordinates = new int[2];
        coordinates[0] = 0;
        coordinates[1] = 0;
        chosenGods1 = new God[3];
        chosenGods0 = new God[2];
        apollo = new God("Apollo", "God of Music - Your Move: Your Worker may move into an opponent's square, and trade places with him.");
        artemis = new God("Artemis", "Goddess of the Hunt - Your Move: Your Worker may move one additional time, but not back to its initial space.");
        athena = new God("Athena", "Goddess of Wisdom - Opponent's Turn: If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn.");
        atlas = new God("Atlas", "Titan Shouldering the Heavens - Your Build: Your Worker may build a dome at any level.");
        demeter = new God("Demeter", "Goddess of the Harvest - Your Build: Your Worker may build one additional time, but not on the same space.");
        hephaestus = new God("Hephaestus", "God of Blacksmiths - Your Build: Your Worker may build one additional time, but only on the same space and not a dome .");
        minotaur = new God("Minotaur", "Bull-headed Monster - Your Move: Your Worker may move into an opponent Worker's space, if their Worker can be forced one space straight backwards to an unoccupied space at any level.");
        pan = new God("Pan", "God of the Wild - Win Condition: You also win if your Worker moves down two or more levels.");
        prometheus = new God("Prometheus", "Titan Benefactor of Mankind - Your Turn: If your Worker does not move up, it may build both before and after moving.");
        hera = new God("Hera", "Goddess of Marriage - Opponent's Turn: An opponent cannot win by moving into a perimeter space.");
        chronus = new God("Chronus", "God of Time - Win Condition: You also win when there are at least five Complete Towers on the board.");
        hestia = new God("Hestia", "Goddess of Hearth and Home - Your Build: Your Worker may build one additional time, but this cannot be on a perimeter space.");
        zeus = new God("Zeus", "God of the Sky - Your Build: Your Worker may build a block under itself.");
        ares = new God("Ares", "God of War - End of Your Turn: You may remove an unoccupied block (not dome) neighboring your unmoved Worker.");
        gods[0] = apollo;
        gods[1] = artemis;
        gods[2] = athena;
        gods[3] = atlas;
        gods[4] = demeter;
        gods[5] = hephaestus;
        gods[6] = minotaur;
        gods[7] = pan;
        gods[8] = prometheus;
        gods[9] = hera;
        gods[10] = chronus;
        gods[11] = hestia;
        gods[12] = zeus;
        gods[13] = ares;
        chosenGods0[0] = apollo;
        chosenGods0[1] = artemis;
        chosenGods1[0] = apollo;
        chosenGods1[1] = artemis;
        chosenGods1[2] = athena;

        game0 = new Game(2);
        game1 = new Game(3);

        arraylist0 = new ArrayList<Player>();
        arraylist1 = new ArrayList<Player>();

        cell = new Cell();
        god = new God("god", "ability");

        player0 = new Player("Mario", "rosso", god);
        player1 = new Player("Marco", "blu", god);
        player2 = new Player("Arturo", "verde", god);

        arraylist0.add(0, player0);
        arraylist0.add(1, player1);
        arraylist1.add(0, player0);
        arraylist1.add(1, player1);
        arraylist1.add(2, player2);

        game0.setPlayerList(arraylist0);
        game1.setPlayerList(arraylist1);

        player0.setRelatedClient(clientHandler);
        player1.setRelatedClient(clientHandler);
        player2.setRelatedClient(clientHandler2);
    }

    @After
    public void teardown() {
        gods = null;
        apollo = null;
        artemis = null;
        athena = null;
        atlas = null;
        demeter = null;
        hephaestus = null;
        minotaur = null;
        pan = null;
        prometheus =null;
        hera = null;
        chronus = null;
        hestia = null;
        zeus = null;
        ares = null;
        chosenGods1 = null;
        chosenGods0 = null;
        game0 = null;
        game1 = null;
        arraylist0 = null;
        arraylist1 = null;
        cell = null;
        god = null;
        player0 = null;
        player1 = null;
        player2 = null;
    }

    @Test
    public void godPicking_threePlayers_correctI_correctO() throws IOException {
        Mockito.doReturn(chosenGods1).when(clientHandler).toClientGetObject(anyString(), anyList(), captor.capture());
        Mockito.doNothing().when(clientHandler).toClientVoid(anyString(), anyString(), anyString());
        Mockito.doReturn(chosenGods1).when(clientHandler).toClientGetObject(anyString(), any(Player.class), anyList());
        Mockito.doReturn(chosenGods1).when(clientHandler2).toClientGetObject(anyString(), anyList(), captor.capture());
        Mockito.doNothing().when(clientHandler2).toClientVoid(anyString(), anyString(), anyString());
        Mockito.doReturn(chosenGods1).when(clientHandler2).toClientGetObject(anyString(), any(Player.class), anyList());
        GameSetup.godPicking(arraylist1);
        assertEquals(captor.getValue(), gods);
        verify(clientHandler, times(1)).toClientGetObject(anyString(), anyList(), any(God[].class));
        verify(clientHandler, times(4)).toClientVoid(anyString(), anyString(), anyString());
        verify(clientHandler, times(1)).toClientGetObject(anyString(), any(Player.class), anyList());
        verify(clientHandler2, times(2)).toClientVoid(anyString(), anyString(), anyString());
        verify(clientHandler2, times(1)).toClientGetObject(anyString(), any(Player.class), anyList());
        verify(clientHandler2, times(0)).toClientGetObject(anyString(), anyList(), any(God[].class));
    }

    @Test
    public void godPicking_twoPlayers_correctI_correctO() throws IOException {
        Mockito.doReturn(chosenGods0).when(clientHandler).toClientGetObject(anyString(), anyList(), captor.capture());
        Mockito.doNothing().when(clientHandler).toClientVoid(anyString(), anyString(), anyString());
        Mockito.doReturn(chosenGods0).when(clientHandler).toClientGetObject(anyString(), any(Player.class), anyList());
        GameSetup.godPicking(arraylist0);
        assertEquals(captor.getValue(), gods);
        verify(clientHandler, times(1)).toClientGetObject(anyString(), anyList(), any(God[].class));
        verify(clientHandler, times(2)).toClientVoid(anyString(), anyString(), anyString());
        verify(clientHandler, times(1)).toClientGetObject(anyString(), any(Player.class), anyList());
    }

    @Test
    public void firstPawnPositioning_correctI_correctO() throws IOException {
        player2.setRelatedClient(clientHandler);
        Mockito.doNothing().when(clientHandler).toClientVoid(anyString(), any(Player.class));
        Mockito.doNothing().when(clientHandler).toClientVoid(anyString(), eq(null), eq(null));
        Mockito.doNothing().when(clientHandler).toClientVoid(anyString(), any(Game.class), eq(null));
        Mockito.doReturn(coordinates).when(clientHandler).toClientGetObject(anyString(), any(Game.class), any(Player.class));
        //three players
        GameSetup.firstPawnPositioning(game1);
        assertEquals(game1.getPlayerList().get(0).getPawns()[0].getX(), 0);
        assertEquals(game1.getPlayerList().get(0).getPawns()[0].getY(), 0);
        assertEquals(game1.getPlayerList().get(0).getPawns()[1].getX(), 0);
        assertEquals(game1.getPlayerList().get(0).getPawns()[1].getY(), 0);
        assertEquals(game1.getPlayerList().get(1).getPawns()[0].getX(), 0);
        assertEquals(game1.getPlayerList().get(1).getPawns()[0].getY(), 0);
        assertEquals(game1.getPlayerList().get(1).getPawns()[1].getX(), 0);
        assertEquals(game1.getPlayerList().get(1).getPawns()[1].getY(), 0);
        assertEquals(game1.getPlayerList().get(2).getPawns()[0].getX(), 0);
        assertEquals(game1.getPlayerList().get(2).getPawns()[0].getY(), 0);
        assertEquals(game1.getPlayerList().get(2).getPawns()[1].getX(), 0);
        assertEquals(game1.getPlayerList().get(2).getPawns()[1].getY(), 0);
        assertNotEquals(game1.getMap()[0][0].getIsFull(), null);
    }

}