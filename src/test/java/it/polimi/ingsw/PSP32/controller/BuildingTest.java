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

import static org.junit.Assert.*;

public class BuildingTest {
    Building building = new Building();
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
    God atlas = null;
    God demeter = null;
    God hestia = null;
    God hephaestus = null;
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
    int[] cellCoordinatesDemeter = null;

    @Before
    public void setup() {
        clientHandler = Mockito.mock(ClientHandler.class);
        utility = Mockito.spy(Utility.class);
        checkHasLost = Mockito.spy(new CheckHasLost(utility));
        checkHasWon = Mockito.spy(new CheckHasWon(utility));
        building = new Building(checkHasLost, checkHasWon, utility);

        cellCoordinates = new int[2];
        cellCoordinates[0]=1;
        cellCoordinates[1]=1;
        cellCoordinatesDemeter = new int[2];
        cellCoordinatesDemeter[0]=1;
        cellCoordinatesDemeter[1]=0;

        game0 = new Game(2);
        game1 = new Game(3);

        arraylist0 = new ArrayList<Player>();
        arraylist1 = new ArrayList<Player>();

        cell = new Cell();
        god = new God("god", "ability");
        atlas = new God("Alas", "AtlasAbility");
        demeter = new God("Demeter", "DemeterAbility");
        hestia = new God("Hestia", "HestiaAbility");
        hephaestus = new God("Hephaestus", "HephaestusAbility");

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
        clientHandler = null;
        utility = null;
        checkHasLost = null;
        checkHasWon = null;
        building = null;

        game0 = null;
        game1 = null;
        arraylist0 = null;
        arraylist1 = null;
        cell = null;
        god = null;
        atlas = null;
        demeter = null;
        hestia = null;
        hephaestus = null;
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
        cellCoordinatesDemeter = null;
    }

    @Test
    public void buildPhase_correctI_correctO() throws IOException {
        //value returned when asked where to build
        Mockito.doReturn(cellCoordinates).when(clientHandler).toClientGetObject(eq("getBuildLocationViaArrows"), any(Game.class), any(Pawn.class), eq(null), eq(true));
        //values returned for wantsDome
        Mockito.doReturn(true).when(clientHandler).toClientGetObject(eq("waitForBuildCommand"), any(Game.class), any(Pawn.class), eq(true), eq(false));
        Mockito.doReturn(true).when(clientHandler).toClientGetObject(eq("waitForBuildCommand"), any(Game.class), any(Pawn.class), eq(false), eq(false));
        //value returned for Hephaestus when asked if he wants to build twice
        Mockito.doReturn(true).when(clientHandler).toClientGetObject(eq("askBuildTwice") , any(Player.class));
        //values returned for Demeter second build command
        Mockito.doReturn(false).when(clientHandler).toClientGetObject(eq("waitForBuildCommand"), any(Game.class), any(Pawn.class), eq(false), eq(true), any(Cell.class));
        Mockito.doReturn(cellCoordinatesDemeter).when(clientHandler).toClientGetObject(eq("getBuildLocationViaArrows"), any(Game.class), any(Pawn.class), any(Cell.class), eq(true));
        //values returned for Hestia second build command
        Mockito.doReturn(false).when(clientHandler).toClientGetObject(eq("waitForBuildCommand"), any(Game.class), any(Pawn.class), eq(false), eq(true), eq(false));
        Mockito.doReturn(cellCoordinates).when(clientHandler).toClientGetObject(eq("getBuildLocationViaArrows"), any(Game.class), any(Pawn.class), any(Cell.class), eq(false));
        //mocking utility's toAllClientsVoid
        Mockito.doNothing().when(utility).toAllClientsVoid(any(Game.class), anyString(), any(Game.class));
        //setting checkHasLost to return false when Demeter and Hestia check
        Mockito.doReturn(false).when(checkHasLost).checkHasLostForBuild(any(Game.class), any(Pawn.class), eq(true), any(Cell.class));
        Mockito.doReturn(false).when(checkHasLost).checkHasLostForBuild(any(Game.class), any(Pawn.class), eq(false),  eq(null), eq(false));

        //test without atlas and wantsDome true, 2 players
        building.buildPhase(game0, pawn0);
        assertEquals(game0.getMap()[1][1].getHasDome(), true);
        game0.getMap()[1][1].setHasDome(false);

        //test with atlas and wantsDome true, 2 players
        player0.setGod(atlas);
        building.buildPhase(game0, pawn0);
        assertEquals(game0.getMap()[1][1].getHasDome(), true);
        game0.getMap()[1][1].setHasDome(false);
        player0.setGod(god);

        //test without atlas and wantsDome true, 3 players
        building.buildPhase(game1, pawn0);
        assertEquals(game1.getMap()[1][1].getHasDome(), true);
        game1.getMap()[1][1].setHasDome(false);

        //test with atlas and wantsDome true, 3 players
        player0.setGod(atlas);
        building.buildPhase(game1, pawn0);
        assertEquals(game1.getMap()[1][1].getHasDome(), true);
        game1.getMap()[1][1].setHasDome(false);
        player0.setGod(god);

        //changing values returned for wantsDome to false
        Mockito.doReturn(false).when(clientHandler).toClientGetObject(eq("waitForBuildCommand"), any(Game.class), any(Pawn.class), eq(true), eq(false));
        Mockito.doReturn(false).when(clientHandler).toClientGetObject(eq("waitForBuildCommand"), any(Game.class), any(Pawn.class), eq(false), eq(false));

        //test without atlas and wantsDome false, 2 players
        building.buildPhase(game0, pawn0);
        assertEquals(game0.getMap()[1][1].getFloor(), 1);
        game0.getMap()[1][1].setFloor(0);

        //test with atlas and wantsDome false, 2 players
        player0.setGod(atlas);
        building.buildPhase(game0, pawn0);
        assertEquals(game0.getMap()[1][1].getFloor(), 1);
        game0.getMap()[1][1].setFloor(0);
        player0.setGod(god);

        //test without atlas and wantsDome false, 3 players
        building.buildPhase(game1, pawn0);
        assertEquals(game1.getMap()[1][1].getFloor(), 1);
        game1.getMap()[1][1].setFloor(0);

        //test with atlas and wantsDome false, 3 players
        player0.setGod(atlas);
        building.buildPhase(game1, pawn0);
        assertEquals(game1.getMap()[1][1].getFloor(), 1);
        game1.getMap()[1][1].setFloor(0);
        player0.setGod(god);

        //test with Demeter (2 and 3 players)
        player0.setGod(demeter);
        building.buildPhase(game0, pawn0);
        building.buildPhase(game1, pawn0);
        assertEquals(game0.getMap()[1][0].getFloor(), 1);
        assertEquals(game1.getMap()[1][0].getFloor(), 1);
        game0.getMap()[1][0].setFloor(0);
        game1.getMap()[1][0].setFloor(0);
        game0.getMap()[1][1].setFloor(0);
        game1.getMap()[1][1].setFloor(0);
        //test when Demeter doesn't want to build twice
        Mockito.doReturn(true).when(clientHandler).toClientGetObject(eq("waitForBuildCommand"), any(Game.class), any(Pawn.class), eq(false), eq(true), any(Cell.class));
        building.buildPhase(game0, pawn0);
        building.buildPhase(game1, pawn0);
        assertEquals(game0.getMap()[1][0].getFloor(), 0);
        assertEquals(game1.getMap()[1][0].getFloor(), 0);
        game0.getMap()[1][1].setFloor(0);
        game1.getMap()[1][1].setFloor(0);

        //Test with Hestia (2 and 3 players)
        player0.setGod(hestia);
        building.buildPhase(game0, pawn0);
        building.buildPhase(game1, pawn0);
        assertEquals(game0.getMap()[1][1].getFloor(), 2);
        assertEquals(game1.getMap()[1][1].getFloor(), 2);
        game0.getMap()[1][1].setFloor(0);
        game1.getMap()[1][1].setFloor(0);
        //test when Hestia doesn't want to build twice
        Mockito.doReturn(true).when(clientHandler).toClientGetObject(eq("waitForBuildCommand"), any(Game.class), any(Pawn.class), eq(false), eq(true), eq(false));
        building.buildPhase(game0, pawn0);
        building.buildPhase(game1, pawn0);
        assertEquals(game0.getMap()[1][1].getFloor(), 1);
        assertEquals(game1.getMap()[1][1].getFloor(), 1);
        game0.getMap()[1][1].setFloor(0);
        game1.getMap()[1][1].setFloor(0);

        //test with Hephaestus (2 and 3 players)
        player0.setGod(hephaestus);
        building.buildPhase(game0, pawn0);
        building.buildPhase(game1, pawn0);
        assertEquals(game0.getMap()[1][1].getFloor(), 2);
        assertEquals(game1.getMap()[1][1].getFloor(), 2);
    }

    @Test
    public void aresPower_correctI_correctO() throws IOException {
        //test with 2 players and pawn0
        Mockito.doReturn(cellCoordinates).when(clientHandler).toClientGetObject(eq("aresPower"), any(Game.class), any(Pawn.class));
        game0.getMap()[1][1].setFloor(1);
        building.aresPower(game0, pawn0);
        assertEquals(game0.getMap()[1][1].getFloor(), 0);

        //test with 3 players and pawn1
        Mockito.doReturn(cellCoordinates).when(clientHandler).toClientGetObject(eq("aresPower"), any(Game.class), any(Pawn.class));
        game1.getMap()[1][1].setFloor(1);
        building.aresPower(game1, pawn1);
        assertEquals(game1.getMap()[1][1].getFloor(), 0);
    }

}