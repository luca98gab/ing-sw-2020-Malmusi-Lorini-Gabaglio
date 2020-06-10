package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CheckCanBuildTest {

    Game game = null;
    Pawn pawn0 = null;
    Pawn pawn1 = null;
    Cell cell = null;
    Player player = null;
    God god = null;
    God zeus = null;

    @Before
    public void setup() {
        game = new Game(2);
        cell = new Cell();
        god = new God("test","testability");
        zeus = new God("Zeus","zeusability");
        player = new Player("player", "blu", god);
        pawn0 = new Pawn (0,0,1, player);
        pawn1 = new Pawn (0,0,2, player);
    }

    @After
    public void teardown() {
        game = null;
        pawn0 = null;
        pawn1 = null;
        cell = null;
        player = null;
        god = null;
        zeus = null;
    }

    @Test
    public void checkCanBuildW_correctI_correctO() {
        //test if pawn can't build in that direction because it's on the edge of that coordinate
        assertEquals(CheckCanBuild.checkCanBuildW(game, pawn0, cell, true),false);

        //test for when newcell is on border
        pawn0.moves(1,0);
        assertEquals(CheckCanBuild.checkCanBuildW(game, pawn0, cell, false),false);
        assertEquals(CheckCanBuild.checkCanBuildW(game, pawn0, cell, true),true);
        assertEquals(CheckCanBuild.checkCanBuildW(game, pawn0, game.getMap()[0][0], true),false);
        game.getMap()[0][0].setIsFull(pawn1);
        assertEquals(CheckCanBuild.checkCanBuildW(game, pawn0, cell, true),false);//test newcell full
        game.getMap()[0][0].setIsFull(null);
        game.getMap()[0][0].setHasDome(true);
        assertEquals(CheckCanBuild.checkCanBuildW(game, pawn0, cell, true),false);//test newcell hasdome

        //test for when newcell is not on border
        pawn0.moves(2,2);
        assertEquals(CheckCanBuild.checkCanBuildW(game, pawn0, cell, false),true);
        assertEquals(CheckCanBuild.checkCanBuildW(game, pawn0, game.getMap()[1][2], false),false);
        game.getMap()[1][2].setIsFull(pawn1);
        assertEquals(CheckCanBuild.checkCanBuildW(game, pawn0, cell, false),false);//test newcell full
        game.getMap()[1][2].setIsFull(null);
        game.getMap()[1][2].setHasDome(true);
        assertEquals(CheckCanBuild.checkCanBuildW(game, pawn0, cell, false),false);//test newcell hasdome
    }

    @Test
    public void checkCanBuildE_correctI_correctO() {
        //test if pawn can't build in that direction because it's on the edge of that coordinate
        pawn0.moves(4,4);
        assertEquals(CheckCanBuild.checkCanBuildE(game, pawn0, cell, true),false);

        //test for when newcell is on border
        pawn0.moves(3,0);
        assertEquals(CheckCanBuild.checkCanBuildE(game, pawn0, cell, false),false);
        assertEquals(CheckCanBuild.checkCanBuildE(game, pawn0, cell, true),true);
        assertEquals(CheckCanBuild.checkCanBuildE(game, pawn0, game.getMap()[4][0], true),false);
        game.getMap()[4][0].setIsFull(pawn1);
        assertEquals(CheckCanBuild.checkCanBuildE(game, pawn0, cell, true),false);//test newcell full
        game.getMap()[4][0].setIsFull(null);
        game.getMap()[4][0].setHasDome(true);
        assertEquals(CheckCanBuild.checkCanBuildE(game, pawn0, cell, true),false);//test newcell hasdome

        //test for when newcell is not on border
        pawn0.moves(2,2);
        assertEquals(CheckCanBuild.checkCanBuildE(game, pawn0, cell, false),true);
        assertEquals(CheckCanBuild.checkCanBuildE(game, pawn0, game.getMap()[3][2], false),false);
        game.getMap()[3][2].setIsFull(pawn1);
        assertEquals(CheckCanBuild.checkCanBuildE(game, pawn0, cell, false),false);//test newcell full
        game.getMap()[3][2].setIsFull(null);
        game.getMap()[3][2].setHasDome(true);
        assertEquals(CheckCanBuild.checkCanBuildE(game, pawn0, cell, false),false);//test newcell hasdome
    }

    @Test
    public void checkCanBuildN_correctI_correctO() {
        //test if pawn can't build in that direction because it's on the edge of that coordinate
        assertEquals(CheckCanBuild.checkCanBuildN(game, pawn0, cell, true),false);

        //test for when newcell is on border
        pawn0.moves(0,1);
        assertEquals(CheckCanBuild.checkCanBuildN(game, pawn0, cell, false),false);
        assertEquals(CheckCanBuild.checkCanBuildN(game, pawn0, cell, true),true);
        assertEquals(CheckCanBuild.checkCanBuildN(game, pawn0, game.getMap()[0][0], true),false);
        game.getMap()[0][0].setIsFull(pawn1);
        assertEquals(CheckCanBuild.checkCanBuildN(game, pawn0, cell, true),false);//test newcell full
        game.getMap()[0][0].setIsFull(null);
        game.getMap()[0][0].setHasDome(true);
        assertEquals(CheckCanBuild.checkCanBuildN(game, pawn0, cell, true),false);//test newcell hasdome

        //test for when newcell is not on border
        pawn0.moves(2,2);
        assertEquals(CheckCanBuild.checkCanBuildN(game, pawn0, cell, false),true);
        assertEquals(CheckCanBuild.checkCanBuildN(game, pawn0, game.getMap()[2][1], false),false);
        game.getMap()[2][1].setIsFull(pawn1);
        assertEquals(CheckCanBuild.checkCanBuildN(game, pawn0, cell, false),false);//test newcell full
        game.getMap()[2][1].setIsFull(null);
        game.getMap()[2][1].setHasDome(true);
        assertEquals(CheckCanBuild.checkCanBuildN(game, pawn0, cell, false),false);//test newcell hasdome
    }

    @Test
    public void checkCanBuildS_correctI_correctO() {
        //test if pawn can't build in that direction because it's on the edge of that coordinate
        pawn0.moves(4,4);
        assertEquals(CheckCanBuild.checkCanBuildS(game, pawn0, cell, true),false);

        //test for when newcell is on border
        pawn0.moves(0,3);
        assertEquals(CheckCanBuild.checkCanBuildS(game, pawn0, cell, false),false);
        assertEquals(CheckCanBuild.checkCanBuildS(game, pawn0, cell, true),true);
        assertEquals(CheckCanBuild.checkCanBuildS(game, pawn0, game.getMap()[0][4], true),false);
        game.getMap()[0][4].setIsFull(pawn1);
        assertEquals(CheckCanBuild.checkCanBuildS(game, pawn0, cell, true),false);//test newcell full
        game.getMap()[0][4].setIsFull(null);
        game.getMap()[0][4].setHasDome(true);
        assertEquals(CheckCanBuild.checkCanBuildS(game, pawn0, cell, true),false);//test newcell hasdome

        //test for when newcell is not on border
        pawn0.moves(2,2);
        assertEquals(CheckCanBuild.checkCanBuildS(game, pawn0, cell, false),true);
        assertEquals(CheckCanBuild.checkCanBuildS(game, pawn0, game.getMap()[2][3], false),false);
        game.getMap()[2][3].setIsFull(pawn1);
        assertEquals(CheckCanBuild.checkCanBuildS(game, pawn0, cell, false),false);//test newcell full
        game.getMap()[2][3].setIsFull(null);
        game.getMap()[2][3].setHasDome(true);
        assertEquals(CheckCanBuild.checkCanBuildS(game, pawn0, cell, false),false);//test newcell hasdome
    }

    @Test
    public void checkCanBuildNW_correctI_correctO() {
        //test if pawn can't build in that direction because it's on the edge of that coordinate
        assertEquals(CheckCanBuild.checkCanBuildNW(game, pawn0, cell, true),false);

        //test for when newcell is on border
        pawn0.moves(1,1);
        assertEquals(CheckCanBuild.checkCanBuildNW(game, pawn0, cell, false),false);
        assertEquals(CheckCanBuild.checkCanBuildNW(game, pawn0, cell, true),true);
        assertEquals(CheckCanBuild.checkCanBuildNW(game, pawn0, game.getMap()[0][0], true),false);
        game.getMap()[0][0].setIsFull(pawn1);
        assertEquals(CheckCanBuild.checkCanBuildNW(game, pawn0, cell, true),false);//test newcell full
        game.getMap()[0][0].setIsFull(null);
        game.getMap()[0][0].setHasDome(true);
        assertEquals(CheckCanBuild.checkCanBuildNW(game, pawn0, cell, true),false);//test newcell hasdome

        //test for when newcell is not on border
        pawn0.moves(2,2);
        assertEquals(CheckCanBuild.checkCanBuildNW(game, pawn0, cell, false),true);
        assertEquals(CheckCanBuild.checkCanBuildNW(game, pawn0, game.getMap()[1][1], false),false);
        game.getMap()[1][1].setIsFull(pawn1);
        assertEquals(CheckCanBuild.checkCanBuildNW(game, pawn0, cell, false),false);//test newcell full
        game.getMap()[1][1].setIsFull(null);
        game.getMap()[1][1].setHasDome(true);
        assertEquals(CheckCanBuild.checkCanBuildNW(game, pawn0, cell, false),false);//test newcell hasdome
    }

    @Test
    public void checkCanBuildNE_correctI_correctO() {
        //test if pawn can't build in that direction because it's on the edge of that coordinate
        pawn0.moves(4,0);
        assertEquals(CheckCanBuild.checkCanBuildNE(game, pawn0, cell, true),false);

        //test for when newcell is on border
        pawn0.moves(3,1);
        assertEquals(CheckCanBuild.checkCanBuildNE(game, pawn0, cell, false),false);
        assertEquals(CheckCanBuild.checkCanBuildNE(game, pawn0, cell, true),true);
        assertEquals(CheckCanBuild.checkCanBuildNE(game, pawn0, game.getMap()[4][0], true),false);
        game.getMap()[4][0].setIsFull(pawn1);
        assertEquals(CheckCanBuild.checkCanBuildNE(game, pawn0, cell, true),false);//test newcell full
        game.getMap()[4][0].setIsFull(null);
        game.getMap()[4][0].setHasDome(true);
        assertEquals(CheckCanBuild.checkCanBuildNE(game, pawn0, cell, true),false);//test newcell hasdome

        //test for when newcell is not on border
        pawn0.moves(2,2);
        assertEquals(CheckCanBuild.checkCanBuildNE(game, pawn0, cell, false),true);
        assertEquals(CheckCanBuild.checkCanBuildNE(game, pawn0, game.getMap()[3][1], false),false);
        game.getMap()[3][1].setIsFull(pawn1);
        assertEquals(CheckCanBuild.checkCanBuildNE(game, pawn0, cell, false),false);//test newcell full
        game.getMap()[3][1].setIsFull(null);
        game.getMap()[3][1].setHasDome(true);
        assertEquals(CheckCanBuild.checkCanBuildNE(game, pawn0, cell, false),false);//test newcell hasdome
    }

    @Test
    public void checkCanBuildSE_correctI_correctO() {
        //test if pawn can't build in that direction because it's on the edge of that coordinate
        pawn0.moves(4,4);
        assertEquals(CheckCanBuild.checkCanBuildSE(game, pawn0, cell, true),false);

        //test for when newcell is on border
        pawn0.moves(3,3);
        assertEquals(CheckCanBuild.checkCanBuildSE(game, pawn0, cell, false),false);
        assertEquals(CheckCanBuild.checkCanBuildSE(game, pawn0, cell, true),true);
        assertEquals(CheckCanBuild.checkCanBuildSE(game, pawn0, game.getMap()[4][4], true),false);
        game.getMap()[4][4].setIsFull(pawn1);
        assertEquals(CheckCanBuild.checkCanBuildSE(game, pawn0, cell, true),false);//test newcell full
        game.getMap()[4][4].setIsFull(null);
        game.getMap()[4][4].setHasDome(true);
        assertEquals(CheckCanBuild.checkCanBuildSE(game, pawn0, cell, true),false);//test newcell hasdome

        //test for when newcell is not on border
        pawn0.moves(2,2);
        assertEquals(CheckCanBuild.checkCanBuildSE(game, pawn0, cell, false),true);
        assertEquals(CheckCanBuild.checkCanBuildSE(game, pawn0, game.getMap()[3][3], false),false);
        game.getMap()[3][3].setIsFull(pawn1);
        assertEquals(CheckCanBuild.checkCanBuildSE(game, pawn0, cell, false),false);//test newcell full
        game.getMap()[3][3].setIsFull(null);
        game.getMap()[3][3].setHasDome(true);
        assertEquals(CheckCanBuild.checkCanBuildSE(game, pawn0, cell, false),false);//test newcell hasdome
    }

    @Test
    public void checkCanBuildSW_correctI_correctO() {
        //test if pawn can't build in that direction because it's on the edge of that coordinate
        pawn0.moves(0,4);
        assertEquals(CheckCanBuild.checkCanBuildSW(game, pawn0, cell, true),false);

        //test for when newcell is on border
        pawn0.moves(1,3);
        assertEquals(CheckCanBuild.checkCanBuildSW(game, pawn0, cell, false),false);
        assertEquals(CheckCanBuild.checkCanBuildSW(game, pawn0, cell, true),true);
        assertEquals(CheckCanBuild.checkCanBuildSW(game, pawn0, game.getMap()[0][4], true),false);
        game.getMap()[0][4].setIsFull(pawn1);
        assertEquals(CheckCanBuild.checkCanBuildSW(game, pawn0, cell, true),false);//test newcell full
        game.getMap()[0][4].setIsFull(null);
        game.getMap()[0][4].setHasDome(true);
        assertEquals(CheckCanBuild.checkCanBuildSW(game, pawn0, cell, true),false);//test newcell hasdome

        //test for when newcell is not on border
        pawn0.moves(2,2);
        assertEquals(CheckCanBuild.checkCanBuildSW(game, pawn0, cell, false),true);
        assertEquals(CheckCanBuild.checkCanBuildSW(game, pawn0, game.getMap()[1][3], false),false);
        game.getMap()[1][3].setIsFull(pawn1);
        assertEquals(CheckCanBuild.checkCanBuildSW(game, pawn0, cell, false),false);//test newcell full
        game.getMap()[1][3].setIsFull(null);
        game.getMap()[1][3].setHasDome(true);
        assertEquals(CheckCanBuild.checkCanBuildSW(game, pawn0, cell, false),false);//test newcell hasdome
    }

    @Test
    public void checkCanBuildBelow_correctI_correctO() {
        assertEquals(CheckCanBuild.checkCanBuildBelow(game, pawn0, cell),false);
        player.setGod(zeus);
        assertEquals(CheckCanBuild.checkCanBuildBelow(game, pawn0, cell),true);
        game.getMap()[0][0].setFloor(3);
        assertEquals(CheckCanBuild.checkCanBuildBelow(game, pawn0, cell),false);
    }

    @Test
    public void checkAresPower_correctI_correctO() {
        pawn1.moves(4,4);
        player.getPawns()[0] = pawn0;
        player.getPawns()[1] = pawn1;
        assertEquals(CheckCanBuild.checkAresPower(game, pawn0),false);//with no buildings around pawn
        game.getMap()[3][3].setFloor(2);
        assertEquals(CheckCanBuild.checkAresPower(game, pawn0),true);//with one building around pawn
        game.getMap()[3][3].setIsFull(pawn0);
        assertEquals(CheckCanBuild.checkAresPower(game, pawn0),false);//with one building but occupied by another pawn
        game.getMap()[3][3].setIsFull(null);
        game.getMap()[3][3].setFloor(3);
        game.getMap()[4][3].setFloor(3);
        game.getMap()[3][4].setFloor(3);
        game.getMap()[3][3].setHasDome(true);
        game.getMap()[4][3].setHasDome(true);
        game.getMap()[3][4].setHasDome(true);
        assertEquals(CheckCanBuild.checkAresPower(game, pawn0),false);//with only domes around pawn

        //reset some parameters
        game.getMap()[3][3].setHasDome(false);
        game.getMap()[4][3].setHasDome(false);
        game.getMap()[3][4].setHasDome(false);
        game.getMap()[3][3].setFloor(0);
        game.getMap()[4][3].setFloor(0);
        game.getMap()[3][4].setFloor(0);

        //same as before but with inverted pawns
        assertEquals(CheckCanBuild.checkAresPower(game, pawn1),false);//with no buildings around pawn
        game.getMap()[1][1].setFloor(2);
        assertEquals(CheckCanBuild.checkAresPower(game, pawn1),true);//with one building around pawn
        game.getMap()[1][1].setIsFull(pawn1);
        assertEquals(CheckCanBuild.checkAresPower(game, pawn1),false);//with one building but occupied by another pawn
        game.getMap()[1][1].setIsFull(null);
        game.getMap()[1][1].setFloor(3);
        game.getMap()[0][1].setFloor(3);
        game.getMap()[1][0].setFloor(3);
        game.getMap()[1][1].setHasDome(true);
        game.getMap()[0][1].setHasDome(true);
        game.getMap()[1][0].setHasDome(true);
        assertEquals(CheckCanBuild.checkAresPower(game, pawn1),false);//with only domes around pawn
    }

}