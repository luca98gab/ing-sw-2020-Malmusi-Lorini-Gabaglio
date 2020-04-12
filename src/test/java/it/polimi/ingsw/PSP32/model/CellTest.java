package it.polimi.ingsw.PSP32.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {

    Cell cell1 = null;
    Pawn pawn1 = null;

    @Before
    public void setup() {
        cell1 = new Cell();
        pawn1 = new Pawn(5, 0, 0, null);
    }

    @After
    public void teardown() {
        cell1 = null;
        pawn1 = null;
    }

    @Test
    public void set_getFloor_correctI_correctO() {
        cell1.setFloor(4);
        assertEquals(cell1.getFloor(), 4);
    }

    @Test
    public void set_getIsFull_correctI_correctO() {
        cell1.setIsFull(pawn1);
        assertEquals(cell1.getIsFull().getX(), 5);
        cell1.setIsFull(null);
        assertEquals(cell1.getIsFull(), null);
    }

    @Test
    public void set_getHasDome_correctI_correctO() {
        cell1.setHasDome(true);
        assertEquals(cell1.getHasDome(), true);
    }

}