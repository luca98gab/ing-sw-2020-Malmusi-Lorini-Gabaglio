package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.*;
import it.polimi.ingsw.PSP32.server.ClientHandler;
import it.polimi.ingsw.PSP32.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CheckHasLostTest {

    Utility utility = null;
    CheckHasLost checkHasLost = null;
    ClientHandler clientHandler = null;

    Game game0 = null;
    Game game1 = null;
    ArrayList<Player> arraylist0 = null;
    ArrayList<Player> arraylist1 = null;
    Cell cell = null;
    God god = null;
    Player player0 = null;
    Player player1 = null;
    Player player2 = null;
    Pawn pawn0 = null;
    Pawn pawn1 = null;
    Pawn pawn2 = null;
    Pawn pawn3 = null;
    Pawn pawn4 = null;
    Pawn pawn5 = null;

    @Before
    public void setup() throws IOException {
        utility = Mockito.spy(Utility.class);
        checkHasLost = Mockito.spy(new CheckHasLost(utility));
        clientHandler = Mockito.mock(ClientHandler.class);

        game0 = new Game(2);
        game1 = new Game(3);

        arraylist0 = new ArrayList<Player>();
        arraylist1 = new ArrayList<Player>();

        cell = new Cell();
        god = new God("god", "ability");

        player0 = new Player("Mario", "rosso", god);
        player1 = new Player("Marco", "blu", god);
        player2 = new Player("Arturo", "verde", god);

        pawn0 = new Pawn (0,0,1, player0);
        pawn1 = new Pawn (4,0,2, player0);
        pawn2 = new Pawn (0,4,1, player1);
        pawn3 = new Pawn (4,4,2, player1);
        pawn4 = new Pawn (1,1,1, player2);
        pawn5 = new Pawn (2,2,2, player2);

        player0.getPawns()[0]=pawn0;
        player0.getPawns()[1]=pawn1;

        player1.getPawns()[0]=pawn2;
        player1.getPawns()[1]=pawn3;

        player2.getPawns()[0]=pawn4;
        player2.getPawns()[1]=pawn5;

        arraylist0.add(0, player0);
        arraylist0.add(1, player1);
        arraylist1.add(0, player0);
        arraylist1.add(1, player1);
        arraylist1.add(2, player2);

        game0.setPlayerList(arraylist0);
        game1.setPlayerList(arraylist1);

        player0.setRelatedClient(clientHandler);

        Mockito.doNothing().when(utility).toAllClientsVoid(any(Game.class), anyString(), any(Game.class));
        Mockito.doNothing().when(utility).toAllClientsVoid(any(Game.class), anyString(), any(Player.class));
        Mockito.doNothing().when(clientHandler).toClientVoid(anyString(), any(Player.class));
    }

    @After
    public void teardown() throws IOException {
        utility = null;
        checkHasLost = null;
        clientHandler = null;
        game0 = null;
        game1 = null;
        arraylist0 = null;
        arraylist1 = null;
        cell = null;
        god = null;
        player0 = null;
        player1 = null;
        player2 = null;
        pawn0 = null;
        pawn1 = null;
        pawn2 = null;
        pawn3 = null;
        pawn4 = null;
        pawn5 = null;
    }

    @Test
    public void CheckHasLostForMoves_paramsGamePlayer_correctI_correctO() throws Exception {
        //test with 2 players
        assertEquals(checkHasLost.checkHasLostForMoves(game0, player0), false);
        game0.getMap()[0][1].setHasDome(true);
        game0.getMap()[1][1].setHasDome(true);
        game0.getMap()[1][0].setHasDome(true);
        game0.getMap()[3][1].setHasDome(true);
        game0.getMap()[3][0].setHasDome(true);
        game0.getMap()[4][1].setHasDome(true);
        assertEquals(checkHasLost.checkHasLostForMoves(game0, player0), true);

        //test with three players
        assertEquals(checkHasLost.checkHasLostForMoves(game1, player0), false);
        game1.getMap()[0][1].setHasDome(true);
        game1.getMap()[1][1].setHasDome(true);
        game1.getMap()[1][0].setHasDome(true);
        game1.getMap()[3][1].setHasDome(true);
        game1.getMap()[3][0].setHasDome(true);
        game1.getMap()[4][1].setHasDome(true);
        assertEquals(checkHasLost.checkHasLostForMoves(game1, player0), true);
    }

    @Test
    public void CheckHasLostForMoves_paramsGamePawnArtemisRestriction_correctI_correctO() throws IOException {
        //test with 2 players
        assertEquals(checkHasLost.checkHasLostForMoves(game0, pawn0, false, null), false);
        assertEquals(checkHasLost.checkHasLostForMoves(game0, pawn0, true, null), false);
        game0.getMap()[0][1].setHasDome(true);
        game0.getMap()[1][1].setHasDome(true);
        game0.getMap()[1][0].setHasDome(true);
        game0.getMap()[3][1].setHasDome(true);
        game0.getMap()[3][0].setHasDome(true);
        game0.getMap()[4][1].setHasDome(true);
        assertEquals(checkHasLost.checkHasLostForMoves(game0, pawn0, false, null), true);
        assertEquals(checkHasLost.checkHasLostForMoves(game0, pawn0, true, null), true);

        //test with three players
        assertEquals(checkHasLost.checkHasLostForMoves(game1, pawn0, false, null), false);
        assertEquals(checkHasLost.checkHasLostForMoves(game1, pawn0, true, null), false);
        game1.getMap()[0][1].setHasDome(true);
        game1.getMap()[1][1].setHasDome(true);
        game1.getMap()[1][0].setHasDome(true);
        game1.getMap()[3][1].setHasDome(true);
        game1.getMap()[3][0].setHasDome(true);
        game1.getMap()[4][1].setHasDome(true);
        assertEquals(checkHasLost.checkHasLostForMoves(game1, pawn0, false, null), true);
        assertEquals(checkHasLost.checkHasLostForMoves(game1, pawn0, true, null), true);
    }

    @Test
    public void CheckHasLostForMoves_paramsGamePawn_correctI_correctO() throws IOException {
        //test with 2 players
        assertEquals(checkHasLost.checkHasLostForMoves(game0, pawn0), false);
        game0.getMap()[0][1].setHasDome(true);
        game0.getMap()[1][1].setHasDome(true);
        game0.getMap()[1][0].setHasDome(true);
        game0.getMap()[3][1].setHasDome(true);
        game0.getMap()[3][0].setHasDome(true);
        game0.getMap()[4][1].setHasDome(true);
        assertEquals(checkHasLost.checkHasLostForMoves(game0, pawn0), true);

        //test with three players
        assertEquals(checkHasLost.checkHasLostForMoves(game1, pawn0), false);
        game1.getMap()[0][1].setHasDome(true);
        game1.getMap()[1][1].setHasDome(true);
        game1.getMap()[1][0].setHasDome(true);
        game1.getMap()[3][1].setHasDome(true);
        game1.getMap()[3][0].setHasDome(true);
        game1.getMap()[4][1].setHasDome(true);
        assertEquals(checkHasLost.checkHasLostForMoves(game1, pawn0), true);
    }

    @Test
    public void CheckHasLostForBuild_paramsGamePawnBoolCellBool_correctI_correctO() throws IOException {
        //test with 2 players
        assertEquals(checkHasLost.checkHasLostForBuild(game0, pawn0, false, null, true), false);
        game0.getMap()[1][1].setHasDome(true);
        assertEquals(checkHasLost.checkHasLostForBuild(game0, pawn0, true, null, false), true);
        game0.getMap()[0][1].setHasDome(true);
        game0.getMap()[1][0].setHasDome(true);
        game0.getMap()[3][1].setHasDome(true);
        game0.getMap()[3][0].setHasDome(true);
        game0.getMap()[4][1].setHasDome(true);
        assertEquals(checkHasLost.checkHasLostForBuild(game0, pawn0, false, null, true), true);

        //test with three players
        assertEquals(checkHasLost.checkHasLostForBuild(game1, pawn0, false, null, true), false);
        game1.getMap()[1][1].setHasDome(true);
        assertEquals(checkHasLost.checkHasLostForBuild(game1, pawn0, true, null, false), true);
        game1.getMap()[0][1].setHasDome(true);
        game1.getMap()[1][0].setHasDome(true);
        game1.getMap()[3][1].setHasDome(true);
        game1.getMap()[3][0].setHasDome(true);
        game1.getMap()[4][1].setHasDome(true);
        assertEquals(checkHasLost.checkHasLostForBuild(game1, pawn0, false, null, true), true);
    }

    @Test
    public void CheckHasLostForBuild_paramsGamePawnBoolCell_correctI_correctO() throws IOException {
        //test with 2 players
        assertEquals(checkHasLost.checkHasLostForBuild(game0, pawn0, false, null), false);
        game0.getMap()[1][1].setHasDome(true);
        game0.getMap()[0][1].setHasDome(true);
        game0.getMap()[1][0].setHasDome(true);
        game0.getMap()[3][1].setHasDome(true);
        game0.getMap()[3][0].setHasDome(true);
        game0.getMap()[4][1].setHasDome(true);
        assertEquals(checkHasLost.checkHasLostForBuild(game0, pawn0, false, null), true);

        //test with three players
        assertEquals(checkHasLost.checkHasLostForBuild(game1, pawn0, false, null), false);
        game1.getMap()[1][1].setHasDome(true);
        game1.getMap()[0][1].setHasDome(true);
        game1.getMap()[1][0].setHasDome(true);
        game1.getMap()[3][1].setHasDome(true);
        game1.getMap()[3][0].setHasDome(true);
        game1.getMap()[4][1].setHasDome(true);
        assertEquals(checkHasLost.checkHasLostForBuild(game1, pawn0, false, null), true);
    }

    @Test
    public void CheckHasLostForBuild_paramsGamePawn_correctI_correctO() throws IOException {
        //test with 2 players
        assertEquals(checkHasLost.checkHasLostForBuild(game0, pawn0), false);
        game0.getMap()[1][1].setHasDome(true);
        game0.getMap()[0][1].setHasDome(true);
        game0.getMap()[1][0].setHasDome(true);
        game0.getMap()[3][1].setHasDome(true);
        game0.getMap()[3][0].setHasDome(true);
        game0.getMap()[4][1].setHasDome(true);
        assertEquals(checkHasLost.checkHasLostForBuild(game0, pawn0), true);

        //test with three players
        assertEquals(checkHasLost.checkHasLostForBuild(game1, pawn0), false);
        game1.getMap()[1][1].setHasDome(true);
        game1.getMap()[0][1].setHasDome(true);
        game1.getMap()[1][0].setHasDome(true);
        game1.getMap()[3][1].setHasDome(true);
        game1.getMap()[3][0].setHasDome(true);
        game1.getMap()[4][1].setHasDome(true);
        assertEquals(checkHasLost.checkHasLostForBuild(game1, pawn0), true);
    }

    @Test
    public void toClientVoid_correctI_correctO() throws IOException {
        checkHasLost.toClientVoid(game0, player0, "string");
        verify(checkHasLost, times(1)).toClientVoid(any(Game.class), any(Player.class), anyString());
    }

    @Test
    public void toAllClientsVoid_correctI_correctO() throws IOException {
        checkHasLost.toAllClientsVoid(game0, player0, "string");
        verify(checkHasLost, times(1)).toAllClientsVoid(any(Game.class), any(Player.class), anyString());
    }

    @Test
    public void printBoardColored_correctI_correctO() throws IOException {
        checkHasLost.printBoardColored(game0);
        verify(checkHasLost, times(1)).printBoardColored(any(Game.class));
    }



}