package it.polimi.ingsw.PSP32.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PawnTest {

    Pawn pawn1 = null;
    Player player = null;
    God god = null;

    @Before
    public void setup() {
        god = new God("Nome", "Abilita");
        player = new Player("Mario", "Rosso", god);
        pawn1 = new Pawn(0, 1, 0, player);
    }

    @After
    public void teardown() {
        pawn1 = null;
        player = null;
        god = null;
    }

    @Test
    public void getterXY_and_moves_correctI_correctO() {
        pawn1.moves(2, 3);
        assertEquals(pawn1.getX(), 2);
        assertEquals(pawn1.getY(), 3);
    }

    @Test
    public void getId_correctI_correctO() {
        assertEquals(pawn1.getId(), 0);
    }

    @Test
    public void getPlayer_correctI_correctO() {
        assertEquals(pawn1.getPlayer().getName(), "Mario");
    }


}