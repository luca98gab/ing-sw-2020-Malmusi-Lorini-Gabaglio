package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CheckCanMoveTest {

    Game game = null;
    Pawn pawn0 = null;
    Cell cell = null;
    God god = null;
    God apollo = null;
    God minotaur = null;
    Player player = null;
    Player player2 = null;
    Pawn pawn1 = null;
    Pawn pawn2 = null;

    @Before
    public void setup() {
        game = new Game(2);
        cell = new Cell();
        god = new God("god", "ability");
        apollo = new God("Apollo", "apolloAbility");
        minotaur = new God("Minotaur", "minotaurAbility");
        player = new Player("Mario", "rosso", god);
        player2 = new Player("Marco", "blu", god);
        pawn0 = new Pawn (0,0,0, player);
        pawn1 = new Pawn (4,0,1, player);
        pawn2 = new Pawn (4,4,0, player2);
    }

    @After
    public void teardown() {
        game = null;
        cell = null;
        pawn0 = null;
        god = null;
        apollo = null;
        minotaur = null;
        player = null;
        player2 = null;
        pawn1 = null;
        pawn2 = null;
    }

    @Test
    public void checkCanMoveW_correctI_correctO() {
        //test if pawn can't move in that direction because it's on the edge of that coordinate
        assertEquals(CheckCanMove.checkCanMoveW(game, pawn0, cell),false);

        //tests when not apollo nor minotaur
        pawn0.moves(2,2);
        assertEquals(CheckCanMove.checkCanMoveW(game, pawn0, game.getMap()[pawn0.getX()-1][pawn0.getY()]),false); //test for restriction
        assertEquals(CheckCanMove.checkCanMoveW(game, pawn0, cell),true );//with no obstacles
        game.getMap()[pawn0.getX()-1][pawn0.getY()].setFloor(2);
        assertEquals(CheckCanMove.checkCanMoveW(game, pawn0, cell),false );//when newCell is too high
        game.setAthenaFlag(true);
        game.getMap()[pawn0.getX()-1][pawn0.getY()].setFloor(1);
        assertEquals(CheckCanMove.checkCanMoveW(game, pawn0, cell),false );//when newCell is not too high but with athenaFlag
        game.getMap()[pawn0.getX()-1][pawn0.getY()].setFloor(0);
        game.setAthenaFlag(false);
        game.getMap()[pawn0.getX()-1][pawn0.getY()].setFloor(0);
        game.getMap()[pawn0.getX()-1][pawn0.getY()].setIsFull(pawn2);
        assertEquals(CheckCanMove.checkCanMoveW(game, pawn0, cell),false );//when newCell is full

        //tests when apollo
        player.setGod(apollo);
        assertEquals(CheckCanMove.checkCanMoveW(game, pawn0, cell),true );//when other pawn is not apollo
        game.getMap()[pawn0.getX()-1][pawn0.getY()].setIsFull(pawn1);
        assertEquals(CheckCanMove.checkCanMoveW(game, pawn0, cell),false );//when other pawn is apollo
        game.getMap()[pawn0.getX()-1][pawn0.getY()].setIsFull(null);

        //tests when minotaur
        player2.setGod(minotaur);
        pawn0.moves(0,0);
        pawn2.moves(2,2);
        game.getMap()[pawn2.getX()-1][pawn2.getY()].setIsFull(pawn1);
        assertEquals(CheckCanMove.checkCanMoveW(game, pawn2, cell),true );//when newCell is full and OpponentNewCell is not
        game.getMap()[pawn2.getX()-2][pawn2.getY()].setIsFull(pawn0);
        assertEquals(CheckCanMove.checkCanMoveW(game, pawn2, cell),false );//when newCell is full and OpponentNewCell is too
        game.getMap()[pawn2.getX()-2][pawn2.getY()].setIsFull(null);
        game.getMap()[pawn2.getX()-2][pawn2.getY()].setHasDome(true);
        assertEquals(CheckCanMove.checkCanMoveW(game, pawn2, cell),false );//when OpponentNewCell has dome
        game.getMap()[pawn2.getX()-1][pawn2.getY()].setIsFull(null);
        game.getMap()[pawn2.getX()-1][pawn2.getY()].setHasDome(true);
        assertEquals(CheckCanMove.checkCanMoveW(game, pawn2, cell),false );//when newCell has dome
    }

    @Test
    public void checkCanMoveE_correctI_correctO() {
        //test if pawn can't move in that direction because it's on the edge of that coordinate
        pawn0.moves(4,0);
        assertEquals(CheckCanMove.checkCanMoveE(game, pawn0, cell),false);

        //tests when not apollo nor minotaur
        pawn0.moves(2,2);
        assertEquals(CheckCanMove.checkCanMoveE(game, pawn0, game.getMap()[pawn0.getX()+1][pawn0.getY()]),false); //test for restriction
        assertEquals(CheckCanMove.checkCanMoveE(game, pawn0, cell),true );//with no obstacles
        game.getMap()[pawn0.getX()+1][pawn0.getY()].setFloor(2);
        assertEquals(CheckCanMove.checkCanMoveE(game, pawn0, cell),false );//when newCell is too high
        game.setAthenaFlag(true);
        game.getMap()[pawn0.getX()+1][pawn0.getY()].setFloor(1);
        assertEquals(CheckCanMove.checkCanMoveE(game, pawn0, cell),false );//when newCell is not too high but with athenaFlag
        game.getMap()[pawn0.getX()+1][pawn0.getY()].setFloor(0);
        game.setAthenaFlag(false);
        game.getMap()[pawn0.getX()+1][pawn0.getY()].setFloor(0);
        game.getMap()[pawn0.getX()+1][pawn0.getY()].setIsFull(pawn2);
        assertEquals(CheckCanMove.checkCanMoveE(game, pawn0, cell),false );//when newCell is full

        //tests when apollo
        player.setGod(apollo);
        assertEquals(CheckCanMove.checkCanMoveE(game, pawn0, cell),true );//when other pawn is not apollo
        game.getMap()[pawn0.getX()+1][pawn0.getY()].setIsFull(pawn1);
        assertEquals(CheckCanMove.checkCanMoveE(game, pawn0, cell),false );//when other pawn is apollo
        game.getMap()[pawn0.getX()+1][pawn0.getY()].setIsFull(null);

        //tests when minotaur
        player2.setGod(minotaur);
        pawn0.moves(0,0);
        pawn2.moves(2,2);
        game.getMap()[pawn2.getX()+1][pawn2.getY()].setIsFull(pawn1);
        assertEquals(CheckCanMove.checkCanMoveE(game, pawn2, cell),true );//when newCell is full and OpponentNewCell is not
        game.getMap()[pawn2.getX()+2][pawn2.getY()].setIsFull(pawn0);
        assertEquals(CheckCanMove.checkCanMoveE(game, pawn2, cell),false );//when newCell is full and OpponentNewCell is too
        game.getMap()[pawn2.getX()+2][pawn2.getY()].setIsFull(null);
        game.getMap()[pawn2.getX()+2][pawn2.getY()].setHasDome(true);
        assertEquals(CheckCanMove.checkCanMoveE(game, pawn2, cell),false );//when OpponentNewCell has dome
        game.getMap()[pawn2.getX()+1][pawn2.getY()].setIsFull(null);
        game.getMap()[pawn2.getX()+1][pawn2.getY()].setHasDome(true);
        assertEquals(CheckCanMove.checkCanMoveE(game, pawn2, cell),false );//when newCell has dome
    }

    @Test
    public void checkCanMoveN_correctI_correctO() {
        //test if pawn can't move in that direction because it's on the edge of that coordinate
        assertEquals(CheckCanMove.checkCanMoveN(game, pawn0, cell),false);

        //tests when not apollo nor minotaur
        pawn0.moves(2,2);
        assertEquals(CheckCanMove.checkCanMoveN(game, pawn0, game.getMap()[pawn0.getX()][pawn0.getY()-1]),false); //test for restriction
        assertEquals(CheckCanMove.checkCanMoveN(game, pawn0, cell),true );//with no obstacles
        game.getMap()[pawn0.getX()][pawn0.getY()-1].setFloor(2);
        assertEquals(CheckCanMove.checkCanMoveN(game, pawn0, cell),false );//when newCell is too high
        game.setAthenaFlag(true);
        game.getMap()[pawn0.getX()][pawn0.getY()-1].setFloor(1);
        assertEquals(CheckCanMove.checkCanMoveN(game, pawn0, cell),false );//when newCell is not too high but with athenaFlag
        game.getMap()[pawn0.getX()][pawn0.getY()-1].setFloor(0);
        game.setAthenaFlag(false);
        game.getMap()[pawn0.getX()][pawn0.getY()-1].setFloor(0);
        game.getMap()[pawn0.getX()][pawn0.getY()-1].setIsFull(pawn2);
        assertEquals(CheckCanMove.checkCanMoveN(game, pawn0, cell),false );//when newCell is full

        //tests when apollo
        player.setGod(apollo);
        assertEquals(CheckCanMove.checkCanMoveN(game, pawn0, cell),true );//when other pawn is not apollo
        game.getMap()[pawn0.getX()][pawn0.getY()-1].setIsFull(pawn1);
        assertEquals(CheckCanMove.checkCanMoveN(game, pawn0, cell),false );//when other pawn is apollo
        game.getMap()[pawn0.getX()][pawn0.getY()-1].setIsFull(null);

        //tests when minotaur
        player2.setGod(minotaur);
        pawn0.moves(0,0);
        pawn2.moves(2,2);
        game.getMap()[pawn2.getX()][pawn2.getY()-1].setIsFull(pawn1);
        assertEquals(CheckCanMove.checkCanMoveN(game, pawn2, cell),true );//when newCell is full and OpponentNewCell is not
        game.getMap()[pawn2.getX()][pawn2.getY()-2].setIsFull(pawn0);
        assertEquals(CheckCanMove.checkCanMoveN(game, pawn2, cell),false );//when newCell is full and OpponentNewCell is too
        game.getMap()[pawn2.getX()][pawn2.getY()-2].setIsFull(null);
        game.getMap()[pawn2.getX()][pawn2.getY()-2].setHasDome(true);
        assertEquals(CheckCanMove.checkCanMoveN(game, pawn2, cell),false );//when OpponentNewCell has dome
        game.getMap()[pawn2.getX()][pawn2.getY()-1].setIsFull(null);
        game.getMap()[pawn2.getX()][pawn2.getY()-1].setHasDome(true);
        assertEquals(CheckCanMove.checkCanMoveN(game, pawn2, cell),false );//when newCell has dome
    }

    @Test
    public void checkCanMoveS_correctI_correctO() {
        //test if pawn can't move in that direction because it's on the edge of that coordinate
        pawn0.moves(0,4);
        assertEquals(CheckCanMove.checkCanMoveS(game, pawn0, cell),false);

        //tests when not apollo nor minotaur
        pawn0.moves(2,2);
        assertEquals(CheckCanMove.checkCanMoveS(game, pawn0, game.getMap()[pawn0.getX()][pawn0.getY()+1]),false); //test for restriction
        assertEquals(CheckCanMove.checkCanMoveS(game, pawn0, cell),true );//with no obstacles
        game.getMap()[pawn0.getX()][pawn0.getY()+1].setFloor(2);
        assertEquals(CheckCanMove.checkCanMoveS(game, pawn0, cell),false );//when newCell is too high
        game.setAthenaFlag(true);
        game.getMap()[pawn0.getX()][pawn0.getY()+1].setFloor(1);
        assertEquals(CheckCanMove.checkCanMoveS(game, pawn0, cell),false );//when newCell is not too high but with athenaFlag
        game.getMap()[pawn0.getX()][pawn0.getY()+1].setFloor(0);
        game.setAthenaFlag(false);
        game.getMap()[pawn0.getX()][pawn0.getY()+1].setFloor(0);
        game.getMap()[pawn0.getX()][pawn0.getY()+1].setIsFull(pawn2);
        assertEquals(CheckCanMove.checkCanMoveS(game, pawn0, cell),false );//when newCell is full

        //tests when apollo
        player.setGod(apollo);
        assertEquals(CheckCanMove.checkCanMoveS(game, pawn0, cell),true );//when other pawn is not apollo
        game.getMap()[pawn0.getX()][pawn0.getY()+1].setIsFull(pawn1);
        assertEquals(CheckCanMove.checkCanMoveS(game, pawn0, cell),false );//when other pawn is apollo
        game.getMap()[pawn0.getX()][pawn0.getY()+1].setIsFull(null);

        //tests when minotaur
        player2.setGod(minotaur);
        pawn0.moves(0,0);
        pawn2.moves(2,2);
        game.getMap()[pawn2.getX()][pawn2.getY()+1].setIsFull(pawn1);
        assertEquals(CheckCanMove.checkCanMoveS(game, pawn2, cell),true );//when newCell is full and OpponentNewCell is not
        game.getMap()[pawn2.getX()][pawn2.getY()+2].setIsFull(pawn0);
        assertEquals(CheckCanMove.checkCanMoveS(game, pawn2, cell),false );//when newCell is full and OpponentNewCell is too
        game.getMap()[pawn2.getX()][pawn2.getY()+2].setIsFull(null);
        game.getMap()[pawn2.getX()][pawn2.getY()+2].setHasDome(true);
        assertEquals(CheckCanMove.checkCanMoveS(game, pawn2, cell),false );//when OpponentNewCell has dome
        game.getMap()[pawn2.getX()][pawn2.getY()+1].setIsFull(null);
        game.getMap()[pawn2.getX()][pawn2.getY()+1].setHasDome(true);
        assertEquals(CheckCanMove.checkCanMoveS(game, pawn2, cell),false );//when newCell has dome
    }

    @Test
    public void checkCanMoveNW_correctI_correctO() {
        //test if pawn can't move in that direction because it's on the edge of that coordinate
        assertEquals(CheckCanMove.checkCanMoveNW(game, pawn0, cell),false);

        //tests when not apollo nor minotaur
        pawn0.moves(2,2);
        assertEquals(CheckCanMove.checkCanMoveNW(game, pawn0, game.getMap()[pawn0.getX()-1][pawn0.getY()-1]),false); //test for restriction
        assertEquals(CheckCanMove.checkCanMoveNW(game, pawn0, cell),true );//with no obstacles
        game.getMap()[pawn0.getX()-1][pawn0.getY()-1].setFloor(2);
        assertEquals(CheckCanMove.checkCanMoveNW(game, pawn0, cell),false );//when newCell is too high
        game.setAthenaFlag(true);
        game.getMap()[pawn0.getX()-1][pawn0.getY()-1].setFloor(1);
        assertEquals(CheckCanMove.checkCanMoveNW(game, pawn0, cell),false );//when newCell is not too high but with athenaFlag
        game.getMap()[pawn0.getX()-1][pawn0.getY()-1].setFloor(0);
        game.setAthenaFlag(false);
        game.getMap()[pawn0.getX()-1][pawn0.getY()-1].setFloor(0);
        game.getMap()[pawn0.getX()-1][pawn0.getY()-1].setIsFull(pawn2);
        assertEquals(CheckCanMove.checkCanMoveNW(game, pawn0, cell),false );//when newCell is full

        //tests when apollo
        player.setGod(apollo);
        assertEquals(CheckCanMove.checkCanMoveNW(game, pawn0, cell),true );//when other pawn is not apollo
        game.getMap()[pawn0.getX()-1][pawn0.getY()-1].setIsFull(pawn1);
        assertEquals(CheckCanMove.checkCanMoveNW(game, pawn0, cell),false );//when other pawn is apollo
        game.getMap()[pawn0.getX()-1][pawn0.getY()-1].setIsFull(null);

        //tests when minotaur
        player2.setGod(minotaur);
        pawn0.moves(0,4);
        pawn2.moves(2,2);
        game.getMap()[pawn2.getX()-1][pawn2.getY()-1].setIsFull(pawn1);
        assertEquals(CheckCanMove.checkCanMoveNW(game, pawn2, cell),true );//when newCell is full and OpponentNewCell is not
        game.getMap()[pawn2.getX()-2][pawn2.getY()-2].setIsFull(pawn0);
        assertEquals(CheckCanMove.checkCanMoveNW(game, pawn2, cell),false );//when newCell is full and OpponentNewCell is too
        game.getMap()[pawn2.getX()-2][pawn2.getY()-2].setIsFull(null);
        game.getMap()[pawn2.getX()-2][pawn2.getY()-2].setHasDome(true);
        assertEquals(CheckCanMove.checkCanMoveNW(game, pawn2, cell),false );//when OpponentNewCell has dome
        game.getMap()[pawn2.getX()-1][pawn2.getY()-1].setIsFull(null);
        game.getMap()[pawn2.getX()-1][pawn2.getY()-1].setHasDome(true);
        assertEquals(CheckCanMove.checkCanMoveNW(game, pawn2, cell),false );//when newCell has dome
    }

    @Test
    public void checkCanMoveNE_correctI_correctO() {
        //test if pawn can't move in that direction because it's on the edge of that coordinate
        pawn0.moves(4,0);
        assertEquals(CheckCanMove.checkCanMoveNE(game, pawn0, cell),false);

        //tests when not apollo nor minotaur
        pawn0.moves(2,2);
        assertEquals(CheckCanMove.checkCanMoveNE(game, pawn0, game.getMap()[pawn0.getX()+1][pawn0.getY()-1]),false); //test for restriction
        assertEquals(CheckCanMove.checkCanMoveNE(game, pawn0, cell),true );//with no obstacles
        game.getMap()[pawn0.getX()+1][pawn0.getY()-1].setFloor(2);
        assertEquals(CheckCanMove.checkCanMoveNE(game, pawn0, cell),false );//when newCell is too high
        game.setAthenaFlag(true);
        game.getMap()[pawn0.getX()+1][pawn0.getY()-1].setFloor(1);
        assertEquals(CheckCanMove.checkCanMoveNE(game, pawn0, cell),false );//when newCell is not too high but with athenaFlag
        game.getMap()[pawn0.getX()+1][pawn0.getY()-1].setFloor(0);
        game.setAthenaFlag(false);
        game.getMap()[pawn0.getX()+1][pawn0.getY()-1].setFloor(0);
        game.getMap()[pawn0.getX()+1][pawn0.getY()-1].setIsFull(pawn2);
        assertEquals(CheckCanMove.checkCanMoveNE(game, pawn0, cell),false );//when newCell is full

        //tests when apollo
        player.setGod(apollo);
        assertEquals(CheckCanMove.checkCanMoveNE(game, pawn0, cell),true );//when other pawn is not apollo
        game.getMap()[pawn0.getX()+1][pawn0.getY()-1].setIsFull(pawn1);
        assertEquals(CheckCanMove.checkCanMoveNE(game, pawn0, cell),false );//when other pawn is apollo
        game.getMap()[pawn0.getX()+1][pawn0.getY()-1].setIsFull(null);

        //tests when minotaur
        player2.setGod(minotaur);
        pawn0.moves(0,0);
        pawn2.moves(2,2);
        game.getMap()[pawn2.getX()+1][pawn2.getY()-1].setIsFull(pawn1);
        assertEquals(CheckCanMove.checkCanMoveNE(game, pawn2, cell),true );//when newCell is full and OpponentNewCell is not
        game.getMap()[pawn2.getX()+2][pawn2.getY()-2].setIsFull(pawn0);
        assertEquals(CheckCanMove.checkCanMoveNE(game, pawn2, cell),false );//when newCell is full and OpponentNewCell is too
        game.getMap()[pawn2.getX()+2][pawn2.getY()-2].setIsFull(null);
        game.getMap()[pawn2.getX()+2][pawn2.getY()-2].setHasDome(true);
        assertEquals(CheckCanMove.checkCanMoveNE(game, pawn2, cell),false );//when OpponentNewCell has dome
        game.getMap()[pawn2.getX()+1][pawn2.getY()-1].setIsFull(null);
        game.getMap()[pawn2.getX()+1][pawn2.getY()-1].setHasDome(true);
        assertEquals(CheckCanMove.checkCanMoveNE(game, pawn2, cell),false );//when newCell has dome
    }

    @Test
    public void checkCanMoveSE_correctI_correctO() {
        //test if pawn can't move in that direction because it's on the edge of that coordinate
        pawn0.moves(4,4);
        assertEquals(CheckCanMove.checkCanMoveSE(game, pawn0, cell),false);

        //tests when not apollo nor minotaur
        pawn0.moves(2,2);
        assertEquals(CheckCanMove.checkCanMoveSE(game, pawn0, game.getMap()[pawn0.getX()+1][pawn0.getY()+1]),false); //test for restriction
        assertEquals(CheckCanMove.checkCanMoveSE(game, pawn0, cell),true );//with no obstacles
        game.getMap()[pawn0.getX()+1][pawn0.getY()+1].setFloor(2);
        assertEquals(CheckCanMove.checkCanMoveSE(game, pawn0, cell),false );//when newCell is too high
        game.setAthenaFlag(true);
        game.getMap()[pawn0.getX()+1][pawn0.getY()+1].setFloor(1);
        assertEquals(CheckCanMove.checkCanMoveSE(game, pawn0, cell),false );//when newCell is not too high but with athenaFlag
        game.getMap()[pawn0.getX()+1][pawn0.getY()+1].setFloor(0);
        game.setAthenaFlag(false);
        game.getMap()[pawn0.getX()+1][pawn0.getY()+1].setFloor(0);
        game.getMap()[pawn0.getX()+1][pawn0.getY()+1].setIsFull(pawn2);
        assertEquals(CheckCanMove.checkCanMoveSE(game, pawn0, cell),false );//when newCell is full

        //tests when apollo
        player.setGod(apollo);
        assertEquals(CheckCanMove.checkCanMoveSE(game, pawn0, cell),true );//when other pawn is not apollo
        game.getMap()[pawn0.getX()+1][pawn0.getY()+1].setIsFull(pawn1);
        assertEquals(CheckCanMove.checkCanMoveSE(game, pawn0, cell),false );//when other pawn is apollo
        game.getMap()[pawn0.getX()+1][pawn0.getY()+1].setIsFull(null);

        //tests when minotaur
        player2.setGod(minotaur);
        pawn0.moves(0,0);
        pawn2.moves(2,2);
        game.getMap()[pawn2.getX()+1][pawn2.getY()+1].setIsFull(pawn1);
        assertEquals(CheckCanMove.checkCanMoveSE(game, pawn2, cell),true );//when newCell is full and OpponentNewCell is not
        game.getMap()[pawn2.getX()+2][pawn2.getY()+2].setIsFull(pawn0);
        assertEquals(CheckCanMove.checkCanMoveSE(game, pawn2, cell),false );//when newCell is full and OpponentNewCell is too
        game.getMap()[pawn2.getX()+2][pawn2.getY()+2].setIsFull(null);
        game.getMap()[pawn2.getX()+2][pawn2.getY()+2].setHasDome(true);
        assertEquals(CheckCanMove.checkCanMoveSE(game, pawn2, cell),false );//when OpponentNewCell has dome
        game.getMap()[pawn2.getX()+1][pawn2.getY()+1].setIsFull(null);
        game.getMap()[pawn2.getX()+1][pawn2.getY()+1].setHasDome(true);
        assertEquals(CheckCanMove.checkCanMoveSE(game, pawn2, cell),false );//when newCell has dome
    }

    @Test
    public void checkCanMoveSW_correctI_correctO() {
        //test if pawn can't move in that direction because it's on the edge of that coordinate
        pawn0.moves(0,4);
        assertEquals(CheckCanMove.checkCanMoveSW(game, pawn0, cell),false);

        //tests when not apollo nor minotaur
        pawn0.moves(2,2);
        assertEquals(CheckCanMove.checkCanMoveSW(game, pawn0, game.getMap()[pawn0.getX()-1][pawn0.getY()+1]),false); //test for restriction
        assertEquals(CheckCanMove.checkCanMoveSW(game, pawn0, cell),true );//with no obstacles
        game.getMap()[pawn0.getX()-1][pawn0.getY()+1].setFloor(2);
        assertEquals(CheckCanMove.checkCanMoveSW(game, pawn0, cell),false );//when newCell is too high
        game.setAthenaFlag(true);
        game.getMap()[pawn0.getX()-1][pawn0.getY()+1].setFloor(1);
        assertEquals(CheckCanMove.checkCanMoveSW(game, pawn0, cell),false );//when newCell is not too high but with athenaFlag
        game.getMap()[pawn0.getX()-1][pawn0.getY()+1].setFloor(0);
        game.setAthenaFlag(false);
        game.getMap()[pawn0.getX()-1][pawn0.getY()+1].setFloor(0);
        game.getMap()[pawn0.getX()-1][pawn0.getY()+1].setIsFull(pawn2);
        assertEquals(CheckCanMove.checkCanMoveSW(game, pawn0, cell),false );//when newCell is full

        //tests when apollo
        player.setGod(apollo);
        assertEquals(CheckCanMove.checkCanMoveSW(game, pawn0, cell),true );//when other pawn is not apollo
        game.getMap()[pawn0.getX()-1][pawn0.getY()+1].setIsFull(pawn1);
        assertEquals(CheckCanMove.checkCanMoveSW(game, pawn0, cell),false );//when other pawn is apollo
        game.getMap()[pawn0.getX()-1][pawn0.getY()+1].setIsFull(null);

        //tests when minotaur
        player2.setGod(minotaur);
        pawn0.moves(0,0);
        pawn2.moves(2,2);
        game.getMap()[pawn2.getX()-1][pawn2.getY()+1].setIsFull(pawn1);
        assertEquals(CheckCanMove.checkCanMoveSW(game, pawn2, cell),true );//when newCell is full and OpponentNewCell is not
        game.getMap()[pawn2.getX()-2][pawn2.getY()+2].setIsFull(pawn0);
        assertEquals(CheckCanMove.checkCanMoveSW(game, pawn2, cell),false );//when newCell is full and OpponentNewCell is too
        game.getMap()[pawn2.getX()-2][pawn2.getY()+2].setIsFull(null);
        game.getMap()[pawn2.getX()-2][pawn2.getY()+2].setHasDome(true);
        assertEquals(CheckCanMove.checkCanMoveSW(game, pawn2, cell),false );//when OpponentNewCell has dome
        game.getMap()[pawn2.getX()-1][pawn2.getY()+1].setIsFull(null);
        game.getMap()[pawn2.getX()-1][pawn2.getY()+1].setHasDome(true);
        assertEquals(CheckCanMove.checkCanMoveSW(game, pawn2, cell),false );//when newCell has dome
    }

}