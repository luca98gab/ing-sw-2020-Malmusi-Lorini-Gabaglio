package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.*;
import it.polimi.ingsw.PSP32.server.ClientHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class MovingTest {
    Moving moving = new Moving();
    CheckHasLost checkHasLost = null;
    CheckHasWon checkHasWon = null;
    ClientHandler clientHandler = null;
    Utility utility = null;

    Game game0 = null;
    Game game1 = null;
    ArrayList<Player> arraylist0 = null;
    ArrayList<Player> arraylist1 = null;
    Cell cell = null;
    God god = null;
    God prometheus = null;
    God apollo = null;
    God minotaur = null;
    God artemis = null;
    God athena = null;
    Player player0 = null;
    Player player1 = null;
    Player player2 = null;
    Pawn pawn0 = null;
    Pawn pawn1 = null;
    Pawn pawn2 = null;
    Pawn pawn3 = null;
    Pawn pawn4 = null;
    Pawn pawn5 = null;
    int[] cellCoordinates = null;
    int[] cellCoordinates1 = null;

    @Before
    public void setup() {
        clientHandler = Mockito.mock(ClientHandler.class);
        utility = Mockito.spy(Utility.class);
        checkHasLost = Mockito.spy(new CheckHasLost(utility));
        checkHasWon = Mockito.spy(new CheckHasWon(utility));
        moving = new Moving(checkHasLost, checkHasWon, utility);

        cellCoordinates = new int[2];
        cellCoordinates[0]=1;
        cellCoordinates[1]=1;
        cellCoordinates1 = new int[2];
        cellCoordinates1[0]=2;
        cellCoordinates1[1]=2;

        game0 = new Game(2);
        game1 = new Game(3);

        arraylist0 = new ArrayList<Player>();
        arraylist1 = new ArrayList<Player>();

        cell = new Cell();
        god = new God("god", "ability");
        prometheus = new God("Prometheus", "PrometheusAbility");
        apollo = new God("Apollo", "ApolloAbility");
        minotaur = new God("Minotaur", "MinotaurAbility");
        artemis = new God("Artemis", "ArtemisAbility");
        athena = new God("Athena", "AthenaAbility");

        player0 = new Player("Mario", "rosso", god);
        player1 = new Player("Marco", "blu", god);
        player2 = new Player("Arturo", "verde", god);
        player0.setRelatedClient(clientHandler);
        player1.setRelatedClient(clientHandler);
        player2.setRelatedClient(clientHandler);

        pawn0 = new Pawn(0, 0, 1, player0);
        pawn1 = new Pawn(4, 0, 2, player0);
        pawn2 = new Pawn(0, 4, 1, player1);
        pawn3 = new Pawn(4, 4, 2, player1);
        pawn4 = new Pawn(1, 1, 1, player2);
        pawn5 = new Pawn(2, 2, 2, player2);

        player0.getPawns()[0] = pawn0;
        player0.getPawns()[1] = pawn1;

        player1.getPawns()[0] = pawn2;
        player1.getPawns()[1] = pawn3;

        player2.getPawns()[0] = pawn4;
        player2.getPawns()[1] = pawn5;

        arraylist0.add(0, player0);
        arraylist0.add(1, player1);
        arraylist1.add(0, player0);
        arraylist1.add(1, player1);
        arraylist1.add(2, player2);

        game0.setPlayerList(arraylist0);
        game1.setPlayerList(arraylist1);
    }

    @After
    public void teardown() {
        clientHandler = null;
        utility = null;
        checkHasLost = null;
        checkHasWon = null;
        moving = null;

        game0 = null;
        game1 = null;
        arraylist0 = null;
        arraylist1 = null;
        cell = null;
        god = null;
        prometheus = null;
        apollo = null;
        minotaur = null;
        artemis = null;
        athena = null;
        player0 = null;
        player1 = null;
        player2 = null;
        pawn0 = null;
        pawn1 = null;
        pawn2 = null;
        pawn3 = null;
        pawn4 = null;
        pawn5 = null;
        cellCoordinates = null;
        cellCoordinates1 = null;
    }

    @Test
    public void movePhase_noSpecialGods_correctI_correctO() throws IOException {
        //Mockito setup
        Mockito.doReturn(pawn0).when(clientHandler).toClientGetObject("getActivePawn", game0, player0);
        Mockito.doReturn(true).when(clientHandler).toClientGetObject("waitForMoveCommand", game0, pawn0, true, false);
        Mockito.doReturn(cellCoordinates).when(clientHandler).toClientGetObject("getValidMoveViaArrows", game0, pawn0, null, true);
        Mockito.doReturn(pawn0).when(clientHandler).toClientGetObject("getActivePawn", game1, player0);
        Mockito.doReturn(true).when(clientHandler).toClientGetObject("waitForMoveCommand", game1, pawn0, true, false);
        Mockito.doReturn(cellCoordinates).when(clientHandler).toClientGetObject("getValidMoveViaArrows", game1, pawn0, null, true);

        //test with 2 players
        assertEquals(moving.movePhase(game0, player0), pawn0);
        assertEquals(pawn0.getX(), 1);
        assertEquals(pawn0.getY(), 1);
        assertEquals(game0.getMap()[1][1].getIsFull(), pawn0);

        //reset pawn0 position
        pawn0.moves(0,0);

        //test with 3 players
        assertEquals(moving.movePhase(game1, player0), pawn0);
        assertEquals(pawn0.getX(), 1);
        assertEquals(pawn0.getY(), 1);
        assertEquals(game1.getMap()[1][1].getIsFull(), pawn0);
    }

    @Test
    public void movePhase_Prometheus_correctI_correctO() throws IOException {


    }

    @Test
    public void movePhase_Artemis_correctI_correctO() throws IOException {
        int a = pawn0.getX();
        int b = pawn0.getY();
        player0.setGod(artemis);

        //setup 2 players
        Mockito.doReturn(pawn0).when(clientHandler).toClientGetObject("getActivePawn", game0, player0);
        Mockito.doReturn(true).when(clientHandler).toClientGetObject("waitForMoveCommand", game0, pawn0, true, false);
        Mockito.doReturn(cellCoordinates).when(clientHandler).toClientGetObject("getValidMoveViaArrows", game0, pawn0, null, true);
        cell = game0.getMap()[a][b];
        Mockito.doReturn(true).when(clientHandler).toClientGetObject("waitForMoveCommand", game0, pawn0, false, true);
        Mockito.doReturn(cellCoordinates1).when(clientHandler).toClientGetObject("getValidMoveViaArrows", game0, pawn0, cell, false);
        //test 2 players
        assertEquals(moving.movePhase(game0, player0), pawn0);
        assertEquals(pawn0.getX(), 2);
        assertEquals(pawn0.getY(), 2);
        assertEquals(game0.getMap()[2][2].getIsFull(), pawn0);
        assertNull(game0.getMap()[1][1].getIsFull());

        //reset pawn0
        pawn0.moves(0, 0);

        //setup 3 player
        Mockito.doReturn(pawn0).when(clientHandler).toClientGetObject("getActivePawn", game1, player0);
        Mockito.doReturn(true).when(clientHandler).toClientGetObject("waitForMoveCommand", game1, pawn0, true, false);
        Mockito.doReturn(cellCoordinates).when(clientHandler).toClientGetObject("getValidMoveViaArrows", game1, pawn0, null, true);
        cell = game1.getMap()[a][b];
        Mockito.doReturn(true).when(clientHandler).toClientGetObject("waitForMoveCommand", game1, pawn0, false, true);
        Mockito.doReturn(cellCoordinates1).when(clientHandler).toClientGetObject("getValidMoveViaArrows", game1, pawn0, cell, false);
        //test 3 players
        assertEquals(moving.movePhase(game1, player0), pawn0);
        assertEquals(pawn0.getX(), 2);
        assertEquals(pawn0.getY(), 2);
        assertEquals(game1.getMap()[2][2].getIsFull(), pawn0);
        assertNull(game1.getMap()[1][1].getIsFull());
    }

    @Test
    public void movePhase_Athena_correctI_correctO() throws IOException {
        //setup
        player0.setGod(athena);
        game0.getMap()[1][1].setFloor(1);
        game1.getMap()[1][1].setFloor(1);

        //Mockito setup
        Mockito.doReturn(pawn0).when(clientHandler).toClientGetObject("getActivePawn", game0, player0);
        Mockito.doReturn(true).when(clientHandler).toClientGetObject("waitForMoveCommand", game0, pawn0, true, false);
        Mockito.doReturn(cellCoordinates).when(clientHandler).toClientGetObject("getValidMoveViaArrows", game0, pawn0, null, true);
        Mockito.doReturn(pawn0).when(clientHandler).toClientGetObject("getActivePawn", game1, player0);
        Mockito.doReturn(true).when(clientHandler).toClientGetObject("waitForMoveCommand", game1, pawn0, true, false);
        Mockito.doReturn(cellCoordinates).when(clientHandler).toClientGetObject("getValidMoveViaArrows", game1, pawn0, null, true);

        //test 2 players
        assertEquals(moving.movePhase(game0, player0), pawn0);
        assertEquals(pawn0.getX(), 1);
        assertEquals(pawn0.getY(), 1);
        assertEquals(game0.getMap()[1][1].getIsFull(), pawn0);
        assertEquals(game0.getAthenaFlag(), true);

        //reset pawn0
        pawn0.moves(0, 0);

        //test 3 players
        assertEquals(moving.movePhase(game1, player0), pawn0);
        assertEquals(pawn0.getX(), 1);
        assertEquals(pawn0.getY(), 1);
        assertEquals(game1.getMap()[1][1].getIsFull(), pawn0);
        assertEquals(game1.getAthenaFlag(), true);
    }

    @Test
    public void movePhase_Apollo_correctI_correctO() throws IOException {
        //setup
        player0.setGod(apollo);
        pawn2.moves(1,1);
        game0.getMap()[1][1].setIsFull(pawn2);
        game0.getMap()[0][0].setIsFull(pawn0);
        game1.getMap()[1][1].setIsFull(pawn2);
        game1.getMap()[0][0].setIsFull(pawn0);

        //Mockito setup
        Mockito.doReturn(pawn0).when(clientHandler).toClientGetObject("getActivePawn", game0, player0);
        Mockito.doReturn(true).when(clientHandler).toClientGetObject("waitForMoveCommand", game0, pawn0, true, false);
        Mockito.doReturn(cellCoordinates).when(clientHandler).toClientGetObject("getValidMoveViaArrows", game0, pawn0, null, true);
        Mockito.doReturn(pawn0).when(clientHandler).toClientGetObject("getActivePawn", game1, player0);
        Mockito.doReturn(true).when(clientHandler).toClientGetObject("waitForMoveCommand", game1, pawn0, true, false);
        Mockito.doReturn(cellCoordinates).when(clientHandler).toClientGetObject("getValidMoveViaArrows", game1, pawn0, null, true);

        //test 2 players
        assertEquals(moving.movePhase(game0, player0), pawn0);
        assertEquals(pawn0.getX(), 1);
        assertEquals(pawn0.getY(), 1);
        assertEquals(game0.getMap()[1][1].getIsFull(), pawn0);
        assertEquals(pawn2.getX(), 0);
        assertEquals(pawn2.getY(), 0);
        assertEquals(game0.getMap()[0][0].getIsFull(), pawn2);

        //reset pawn0 and pawn2
        pawn0.moves(0, 0);
        pawn2.moves(1,1);

        //test 3 players
        assertEquals(moving.movePhase(game1, player0), pawn0);
        assertEquals(pawn0.getX(), 1);
        assertEquals(pawn0.getY(), 1);
        assertEquals(game1.getMap()[1][1].getIsFull(), pawn0);
        assertEquals(pawn2.getX(), 0);
        assertEquals(pawn2.getY(), 0);
        assertEquals(game1.getMap()[0][0].getIsFull(), pawn2);
    }

    @Test
    public void movePhase_Minotaur_correctI_correctO() throws IOException {
        //setup
        player0.setGod(minotaur);
        pawn2.moves(1,1);
        game0.getMap()[1][1].setIsFull(pawn2);
        game0.getMap()[0][0].setIsFull(pawn0);
        game1.getMap()[1][1].setIsFull(pawn2);
        game1.getMap()[0][0].setIsFull(pawn0);

        //Mockito setup
        Mockito.doReturn(pawn0).when(clientHandler).toClientGetObject("getActivePawn", game0, player0);
        Mockito.doReturn(true).when(clientHandler).toClientGetObject("waitForMoveCommand", game0, pawn0, true, false);
        Mockito.doReturn(cellCoordinates).when(clientHandler).toClientGetObject("getValidMoveViaArrows", game0, pawn0, null, true);
        Mockito.doReturn(pawn0).when(clientHandler).toClientGetObject("getActivePawn", game1, player0);
        Mockito.doReturn(true).when(clientHandler).toClientGetObject("waitForMoveCommand", game1, pawn0, true, false);
        Mockito.doReturn(cellCoordinates).when(clientHandler).toClientGetObject("getValidMoveViaArrows", game1, pawn0, null, true);

        //test 2 players
        assertEquals(moving.movePhase(game0, player0), pawn0);
        assertEquals(pawn0.getX(), 1);
        assertEquals(pawn0.getY(), 1);
        assertEquals(game0.getMap()[1][1].getIsFull(), pawn0);
        assertEquals(pawn2.getX(), 2);
        assertEquals(pawn2.getY(), 2);
        assertEquals(game0.getMap()[2][2].getIsFull(), pawn2);

        //reset pawn0 and pawn2
        pawn0.moves(0, 0);
        pawn2.moves(1,1);

        //test 3 players
        assertEquals(moving.movePhase(game1, player0), pawn0);
        assertEquals(pawn0.getX(), 1);
        assertEquals(pawn0.getY(), 1);
        assertEquals(game1.getMap()[1][1].getIsFull(), pawn0);
        assertEquals(pawn2.getX(), 2);
        assertEquals(pawn2.getY(), 2);
        assertEquals(game1.getMap()[2][2].getIsFull(), pawn2);
    }
}
