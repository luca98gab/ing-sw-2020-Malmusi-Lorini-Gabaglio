package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.*;
import it.polimi.ingsw.PSP32.server.ClientHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class PhasesTest {
    Utility utility = Mockito.spy(Utility.class);
    Phases phases = new Phases(utility);
    CheckHasLost checkHasLost = null;
    CheckHasWon checkHasWon = null;
    Building building = null;
    Moving moving = null;
    ClientHandler clientHandler = null;

    Game game0 = null;
    Game game1 = null;
    ArrayList<Player> arraylist0 = null;
    ArrayList<Player> arraylist1 = null;
    Cell cell = null;
    God god = null;
    God ares = null;
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

    @Before
    public void setup() {
        checkHasLost = Mockito.spy(new CheckHasLost(utility));
        checkHasWon = Mockito.spy(new CheckHasWon(utility));
        moving = Mockito.mock(Moving.class);
        building = Mockito.mock(Building.class);
        phases = new Phases(checkHasLost, checkHasWon, moving, building, utility);
        clientHandler = Mockito.mock(ClientHandler.class);

        game0 = new Game(2);
        game1 = new Game(3);

        arraylist0 = new ArrayList<Player>();
        arraylist1 = new ArrayList<Player>();

        cell = new Cell();
        god = new God("god", "ability");
        ares = new God("Ares", "AresAbility");
        athena = new God("Athena", "AthenaAbility");

        player0 = new Player("Mario", "rosso", god);
        player1 = new Player("Marco", "blu", god);
        player2 = new Player("Arturo", "verde", god);
        player0.setRelatedClient(clientHandler);
        player1.setRelatedClient(clientHandler);
        player2.setRelatedClient(clientHandler);

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
    }

    @After
    public void teardown() {
        checkHasLost = null;
        checkHasWon = null;
        moving = null;
        building = null;
        phases = null;
        clientHandler = null;

        game0 = null;
        game1 = null;
        arraylist0 = null;
        arraylist1 = null;
        cell = null;
        god = null;
        ares = null;
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
    }

    @Test
    public void turn_correctI_correctO() throws IOException {
        //test with activePawn null
        phases.turnTest(game0, player0);
        phases.turnTest(game1, player0);

        //setting moving to return pawn0 when asked for activePawn
        Mockito.doReturn(pawn0).when(moving).movePhase(any(Game.class), any(Player.class));

        //test with generic god
        phases.turnTest(game0, player0);
        phases.turnTest(game1, player0);
        verify(moving, times(4)).movePhase(any(Game.class), any(Player.class));
        verify(building, times(2)).buildPhase(any(Game.class), any(Pawn.class));

        //test with true to the second checkHasLost
        Mockito.doReturn(true).when(checkHasLost).checkHasLostForBuild(any(Game.class), any(Pawn.class));
        phases.turnTest(game0, player0);
        phases.turnTest(game1, player0);
        verify(moving, times(6)).movePhase(any(Game.class), any(Player.class));
        verify(building, times(2)).buildPhase(any(Game.class), any(Pawn.class));

        //test with true to the  first checkHasLost
        Mockito.doReturn(true).when(checkHasLost).checkHasLostForMoves(any(Game.class), any(Player.class));
        phases.turnTest(game0, player0);
        phases.turnTest(game1, player0);
        verify(moving, times(6)).movePhase(any(Game.class), any(Player.class));
        verify(building, times(2)).buildPhase(any(Game.class), any(Pawn.class));

        //test with Athena
        player0.setGod(athena);
        phases.turnTest(game0, player0);
        phases.turnTest(game1, player0);

        //test with Ares (set checkAresPower to true)
        Mockito.doReturn(false).when(checkHasLost).checkHasLostForMoves(any(Game.class), any(Player.class));
        Mockito.doReturn(false).when(checkHasLost).checkHasLostForBuild(any(Game.class), any(Pawn.class));
        Mockito.doReturn(true).when(clientHandler).toClientGetObject(eq("wantsToUsePower"), any(Player.class));
        player0.setGod(ares);
        game0.getMap()[4][1].setFloor(1);
        game1.getMap()[4][1].setFloor(1);
        phases.turnTest(game0, player0);
        phases.turnTest(game1, player0);
        verify(building, times(2)).aresPower(any(Game.class), any(Pawn.class));
        verify(moving, times(8)).movePhase(any(Game.class), any(Player.class));
        verify(building, times(4)).buildPhase(any(Game.class), any(Pawn.class));
    }

}