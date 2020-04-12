package it.polimi.ingsw.PSP32.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PawnTest {

    Pawn pawn1 = null;

    @Before
    public void setup() {
        pawn1 = new Pawn(0, 1, 0, null);
    }

    @After
    public void teardown() {
        pawn1 = null;
    }

    @Test
    public void getter_and_moves_correctI_correctO() {
        pawn1.moves(2, 3);
        assertEquals(pawn1.getX(), 2);
        assertEquals(pawn1.getY(), 3);
    }


}