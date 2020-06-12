package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class CheckHasWonTest {

    Utility utility = Mockito.mock(Utility.class);
    CheckHasWon checkHasWon = Mockito.spy(new CheckHasWon(utility));

    Game game0 = null;
    Game game1 = null;
    ArrayList<Player> arraylist0 = null;
    ArrayList<Player> arraylist1 = null;
    Cell cell = null;
    God god = null;
    God chronus = null;
    God pan = null;
    God hera = null;
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
        Mockito.doNothing().when(utility).toAllClientsVoid(any(Game.class), anyString(), any(Player.class));
        game0 = new Game(2);
        game1 = new Game(3);

        arraylist0 = new ArrayList<Player>();
        arraylist1 = new ArrayList<Player>();

        cell = new Cell();
        god = new God("god", "ability");
        chronus = new God("Chronus", "ChronusAbility");
        pan = new God("Pan", "PanAbility");
        hera = new God("Hera", "HeraAbility");

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
    }

    @After
    public void teardown() {
        game0 = null;
        game1 = null;
        arraylist0 = null;
        arraylist1 = null;
        cell = null;
        god = null;
        chronus = null;
        pan = null;
        hera= null;
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
    public void checkHasWon5Domes_correctI_correctO() throws IOException {
        for(int i=0; i<5; i++){
            game0.getMap()[0][i].setFloor(3);
            game0.getMap()[0][i].setHasDome(true);
        }
        player0.setGod(chronus);
        checkHasWon.checkHasWon5Domes(game0, player0);
        verify(checkHasWon, times(1)).endGameGraphics(any(Game.class), any(Player.class));
    }

    @Test
    public void checkHasWon_correctI_correctO() throws IOException {
        pawn2.moves(2,3);
        game1.getMap()[2][2].setFloor(2);
        //case with hera in game and hera player
        player1.setGod(pan);
        player2.setGod(hera);
        game1.getMap()[1][1].setFloor(3);
        checkHasWon.checkHasWon(game1, pawn4, game1.getMap()[0][0]);
        verify(checkHasWon, times(1)).endGameGraphics(any(Game.class), any(Player.class));
        //case with hera in game and not hera player and not on border
        pawn0.moves(1,1);
        checkHasWon.checkHasWon(game1, pawn0, game1.getMap()[0][0]);
        verify(checkHasWon, times(2)).endGameGraphics(any(Game.class), any(Player.class));
        //cases with pan
        checkHasWon.checkHasWon(game1, pawn2, game1.getMap()[2][2]);
        verify(checkHasWon, times(3)).endGameGraphics(any(Game.class), any(Player.class));
        player2.setGod(god);
        checkHasWon.checkHasWon(game1, pawn2, game1.getMap()[2][2]);
        verify(checkHasWon, times(4)).endGameGraphics(any(Game.class), any(Player.class));
    }

    @Test
    public void endGameGraphics_correctI_correctO() throws IOException {
        checkHasWon.endGameGraphics(game0, player0);
        verify(checkHasWon, times(1)).endGameGraphics(any(Game.class), any(Player.class));
    }

}