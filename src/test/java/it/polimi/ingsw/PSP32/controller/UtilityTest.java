package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.client.Client;
import it.polimi.ingsw.PSP32.model.*;
import it.polimi.ingsw.PSP32.server.ClientHandler;
import jdk.jshell.execution.Util;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class UtilityTest {

    Utility utility;
    ClientHandler clientHandler = Mockito.mock(ClientHandler.class);
    Game game0 = null;
    Game game1 = null;
    ArrayList<Player> arraylist0 = null;
    ArrayList<Player> arraylist1 = null;
    ArrayList<ClientHandler> arrayListClients = null;
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
    Object forTest = null;

    @Before
    public void setup() throws IOException {
        utility = new Utility();
        forTest = new Object();

        game0 = new Game(2);
        game1 = new Game(3);

        arraylist0 = new ArrayList<Player>();
        arraylist1 = new ArrayList<Player>();
        arrayListClients = new ArrayList<ClientHandler>();
        arrayListClients.add(0, clientHandler);
        arrayListClients.add(1, clientHandler);
        arrayListClients.add(2, clientHandler);

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
        player0.setRelatedClient(clientHandler);

        player1.getPawns()[0]=pawn2;
        player1.getPawns()[1]=pawn3;
        player1.setRelatedClient(clientHandler);

        player2.getPawns()[0]=pawn4;
        player2.getPawns()[1]=pawn5;
        player2.setRelatedClient(clientHandler);

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
        utility = null;
        game0 = null;
        game1 = null;
        arraylist0 = null;
        arraylist1 = null;
        arrayListClients = null;
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
        forTest = null;

    }

    @Test
    public void toAllClientsVoid_paramsGameStringObjectObject_correctI_correctO() throws IOException {
        //test with two players
        utility.toAllClientsVoid(game0, "test", forTest, forTest);
        verify(clientHandler, times(2)).toClientVoid(anyString(), any(Object.class), any(Object.class));

        //test with three players
        utility.toAllClientsVoid(game1, "test", forTest, forTest);
        verify(clientHandler, times(5)).toClientVoid(anyString(), any(Object.class), any(Object.class));
    }

    @Test
    public void toAllClientsVoid_paramsGameStringObject_correctI_correctO() throws IOException {
        //test with two players
        utility.toAllClientsVoid(game0, "test", forTest);
        verify(clientHandler, times(2)).toClientVoid(anyString(), any(Object.class), eq(null));

        //test with three players
        utility.toAllClientsVoid(game1, "test", forTest);
        verify(clientHandler, times(5)).toClientVoid(anyString(), any(Object.class), eq(null));
    }

    @Test
    public void toAllClientsVoid_paramsGameString_correctI_correctO() throws IOException {
        //test with two players
        utility.toAllClientsVoid(game0, "test");
        verify(clientHandler, times(2)).toClientVoid(anyString(), eq(null), eq(null));

        //test with three players
        utility.toAllClientsVoid(game1, "test");
        verify(clientHandler, times(5)).toClientVoid(anyString(), eq(null), eq(null));
    }

    @Test
    public void notifyClosingGame_correctI_correctO() throws IOException {
        utility.notifyClosingGame(arrayListClients);
        verify(clientHandler, times(3)).toClientVoid(anyString());
    }

}