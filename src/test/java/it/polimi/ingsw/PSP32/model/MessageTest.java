package it.polimi.ingsw.PSP32.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class MessageTest {

    Message message1 = null;
    Message message2 = null;
    ArrayList<Object> arrayList = null;
    Cell result = null;

    @Before
    public void setup() {
        arrayList = new ArrayList<>();
        arrayList.add(0, 1);
        result = new Cell();
        result.setFloor(2);
        message1 = new Message("methodName");
        message2 = new Message("methodName", arrayList, "prova", result);
    }

    @After
    public void teardown() {
        message1=null;
        message2=null;
        arrayList=null;
        result=null;
    }

    @Test
    public void getMethodName_correctI_correctO() {
        assertEquals(message1.getMethodName(), "methodName");
    }

    @Test
    public void getTypeOfMessage_correctI_correctO() {
        assertEquals(message2.getTypeOfMessage(), "prova");
    }

    @Test
    public void getResult_correctI_correctO() {
        assertEquals(((Cell)(message2.getResult())).getFloor(), 2);
    }

    @Test
    public void getParameters_correctI_correctO() {
        assertEquals(((int)message2.getParameters().get(0)), 1);
    }

}