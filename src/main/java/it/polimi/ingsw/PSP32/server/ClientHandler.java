package it.polimi.ingsw.PSP32.server;

import it.polimi.ingsw.PSP32.client.Message;
import it.polimi.ingsw.PSP32.model.God;
import it.polimi.ingsw.PSP32.model.Pawn;
import it.polimi.ingsw.PSP32.model.Player;
import it.polimi.ingsw.PSP32.view.VirtualCli;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


import static it.polimi.ingsw.PSP32.server.Server.*;


public class ClientHandler implements Runnable
{
  private final Socket client;
  private final Boolean first;
  private ObjectOutputStream output;
  private ObjectInputStream input;




  ClientHandler(Socket client, Boolean first) {
    this.client = client;
    this.first = first;
  }




  @Override
  public void run()
  {
    try {
      handleClientConnection();
    } catch (IOException e) {
      System.out.println("client " + client.getInetAddress() + " connection dropped");
    }
  }

//  public int toClientGetInt(String methodName, ArrayList<Object> parameters) throws IOException {
//    Message message = new Message(methodName, parameters);
//    output.reset();
//    output.writeObject(message);
//    int i=0;
//    try {
//      Object incoming = input.readObject();
//      i = ((int) incoming);
//    } catch (ClassNotFoundException | ClassCastException e) {
//      System.out.println("invalid stream from client");
//    }
//    return i;
//  }
//  public int toClientGetInt(String methodName) throws IOException {
//    return toClientGetInt(methodName, null);
//  }
//
//  public Player toClientGetPlayer(String methodName, ArrayList<Object> parameters) throws IOException {
//    Message message = new Message(methodName, parameters);
//    output.reset();
//    output.writeObject(message);
//    Player player = null;
//    try {
//      Object incoming = input.readObject();
//      player = ((Player) incoming);
//    } catch (ClassNotFoundException | ClassCastException e) {
//      System.out.println("invalid stream from client");
//    }
//    return player;
//  }
//  public Player toClientGetPlayer(String methodName) throws IOException {
//    return toClientGetPlayer(methodName, null);
//  }
//
//  public ArrayList toClientGetArrayList(String methodName, ArrayList<Object> parameters) throws IOException {
//    Message message = new Message(methodName, parameters);
//    output.reset();
//    output.writeObject(message);
//    ArrayList arrayList = null;
//    try {
//      Object incoming = input.readObject();
//      arrayList = ((ArrayList) incoming);
//    } catch (ClassNotFoundException | ClassCastException e) {
//      System.out.println("invalid stream from client");
//    }
//    return arrayList;
//  }
//  public ArrayList toClientGetArrayList(String methodName) throws IOException {
//    return toClientGetArrayList(methodName, null);
//  }
//
//  public God[] toClientGetGodArray(String methodName, Object par1, Object par2) throws IOException {
//    ArrayList<Object> parameters = new ArrayList<>();
//    parameters.add(par1);
//    parameters.add(par2);
//    Message message = new Message(methodName, parameters);
//    output.reset();
//    output.writeObject(message);
//    God[] godArray = null;
//    try {
//      Object incoming = input.readObject();
//      godArray = ((God[]) incoming);
//    } catch (ClassNotFoundException | ClassCastException e) {
//      System.out.println("invalid stream from client");
//    }
//    return godArray;
//  }
//  public God[] toClientGetGodArray(String methodName) throws IOException {
//    return toClientGetGodArray(methodName, null, null);
//  }


  public void toClientVoid(String methodName, Object par1, Object par2) throws IOException {
    ArrayList<Object> parameters = new ArrayList<>();
    parameters.add(par1);
    parameters.add(par2);
    Message message = new Message(methodName, parameters, null, null);
    output.reset();
    output.writeObject(message);
  }
  public void toClientVoid(String methodName) throws IOException {
    toClientVoid(methodName, null, null);
  }
  public void toClientVoid(String methodName, Object par) throws IOException {
    toClientVoid(methodName, par, null);
  }
//
//  public int[] toClientGetIntArray(String methodName, Object par1, Object par2) throws IOException {
//    ArrayList<Object> parameters = new ArrayList<>();
//    parameters.add(par1);
//    parameters.add(par2);
//    Message message = new Message(methodName, parameters);
//    output.reset();
//    output.writeObject(message);
//    int[] intArray = null;
//    try {
//      Object incoming = input.readObject();
//      intArray = ((int[]) incoming);
//    } catch (ClassNotFoundException | ClassCastException e) {
//      System.out.println("invalid stream from client");
//    }
//    return intArray;
//  }
//  public int[] toClientGetIntArray(String methodName, Object par) throws IOException {
//    return toClientGetIntArray(methodName, par, null);
//  }
//
//  public Pawn toClientGetPawn(String methodName, Object par1, Object par2) throws IOException {
//    ArrayList<Object> parameters = new ArrayList<>();
//    parameters.add(par1);
//    parameters.add(par2);
//    Message message = new Message(methodName, parameters);
//    output.reset();
//    output.writeObject(message);
//    Pawn pawn = null;
//    try {
//      Object incoming = input.readObject();
//      pawn = ((Pawn) incoming);
//    } catch (ClassNotFoundException | ClassCastException e) {
//      System.out.println("invalid stream from client");
//    }
//    return pawn;
//  }
//  public Pawn toClientGetPawn(String methodName, Object par) throws IOException {
//    return toClientGetPawn(methodName, par, null);
//  }
//  public Pawn toClientGetPawn(String methodName) throws IOException {
//    return toClientGetPawn(methodName, null, null);
//
//  }


  public Object toClientGetObject(String methodName, Object par1, Object par2, Object par3, Object par4) throws IOException {
    ArrayList<Object> parameters = new ArrayList<>();
    parameters.add(par1);
    parameters.add(par2);
    parameters.add(par3);
    parameters.add(par4);
    Message outboundMessage = new Message(methodName, parameters, "Request", null);
    output.reset();
    output.writeObject(outboundMessage);
    Object object= null;
    try {
      Message inboundMessage = (Message) input.readObject();
      if (inboundMessage.getTypeOfMessage().equals("Result")){
        object = inboundMessage.getResult();
      } else {

      }
    } catch (ClassNotFoundException | ClassCastException e) {
      System.out.println("invalid stream from client");
    }
    return object;
  }
  public Object toClientGetObject(String methodName, Object par1, Object par2, Object par3) throws IOException {
    return toClientGetObject(methodName, par1, par2, par3, null);
  }
  public Object toClientGetObject(String methodName, Object par1, Object par2) throws IOException {
    return toClientGetObject(methodName, par1, par2, null);
  }
  public Object toClientGetObject(String methodName, Object par) throws IOException {
    return toClientGetObject(methodName, par, null);
  }
  public Object toClientGetObject(String methodName) throws IOException {
    return toClientGetObject(methodName, null);
  }




  private void handleClientConnection() throws IOException
  {
    System.out.println("Connected to " + client.getInetAddress());

    output = new ObjectOutputStream(client.getOutputStream());
    input = new ObjectInputStream(client.getInputStream());


    if (first){
      Server.playerNum = (int) ((Message) toClientGetObject("getNumOfPlayers")).getResult();

      synchronized(lockNum){
        //set ready flag to true (so isReady returns true)
        lockNum.notifyAll();
      }

    }

    Player player = (Player) ((Message) toClientGetObject("createPlayer")).getResult();
    player.setRelatedClient(this);

    Server.players.add(player);

    synchronized(lockPlayer){
      //set ready flag to true (so isReady returns true)
      lockPlayer.notifyAll();
    }


    while (true){
      //answer to client
    }

  }


}

