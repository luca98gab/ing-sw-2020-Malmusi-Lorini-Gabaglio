package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BuildingTest {

    Game game = null;
    God god = null;
    Pawn pawn0 = null;
    Pawn pawn1 = null;
    Player player = null;

    @Before
    public void setup() {

        game = new Game(2);
        god = new God("godName", "ability");
        player = new Player("playerName", "color", god);
        pawn0 = new Pawn (0,0,0, player);
        pawn1 = new Pawn (1,1,1, player);
        player.getPawns()[0] = pawn0;
        player.getPawns()[1] = pawn1;

    }

    @After
    public void teardown() {

    }

    @Test
    public void buildPhase_correctI_correctO() {

    }

}