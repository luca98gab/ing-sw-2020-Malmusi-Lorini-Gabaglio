package it.polimi.ingsw.PSP32.server;

import it.polimi.ingsw.PSP32.client.Message;
import it.polimi.ingsw.PSP32.controller.Logic;
import it.polimi.ingsw.PSP32.model.*;
import it.polimi.ingsw.PSP32.view.VirtualCli;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;


import static it.polimi.ingsw.PSP32.server.Server.*;


public class ClientHandler implements Runnable
{
  private final Socket client;
  private final Boolean first;
  private ObjectOutputStream output;
  private ObjectInputStream input;
  public static volatile int i=0;




  ClientHandler(Socket client, Boolean first) {
    this.client = client;
    this.first = first;
  }




  @Override
  public void run()
  {
    try {
      handleClientConnection();
    } catch (IOException | InterruptedException e) {
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
      while (inboundMessage.getTypeOfMessage().equals("Request")){
        Boolean valid = null;
        switch (inboundMessage.getMethodName()){
          case "checkCanMoveSW":
            valid = Logic.checkCanMoveSW((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanMoveS":
            valid = Logic.checkCanMoveS((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanMoveSE":
            valid = Logic.checkCanMoveSE((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanMoveW":
            valid = Logic.checkCanMoveW((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanMoveE":
            valid = Logic.checkCanMoveE((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanMoveNW":
            valid = Logic.checkCanMoveNW((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanMoveN":
            valid = Logic.checkCanMoveN((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanMoveNE":
            valid = Logic.checkCanMoveNE((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanBuildSW":
            valid = Logic.checkCanBuildSW((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanBuildS":
            valid = Logic.checkCanBuildS((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanBuildSE":
            valid = Logic.checkCanBuildSE((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanBuildW":
            valid = Logic.checkCanBuildW((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanBuildE":
            valid = Logic.checkCanBuildE((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanBuildNW":
            valid = Logic.checkCanBuildNW((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanBuildN":
            valid = Logic.checkCanBuildN((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanBuildNE":
            valid = Logic.checkCanBuildNE((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
          break;
        }
        Message outboundMessageInner = null;
        if (valid!=null){
          outboundMessageInner = new Message(null, null, "Result", valid);
        }

        output.reset();
        output.writeObject(outboundMessageInner);
        inboundMessage = (Message) input.readObject();
      }
      if (inboundMessage.getTypeOfMessage().equals("Result")){
        object = inboundMessage.getResult();
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


  public synchronized void playerCreation(CopyOnWriteArrayList<Player> playersList) throws IOException {
    Player player = (Player) toClientGetObject("createPlayer", playersList, playerNum);
    player.setRelatedClient(this);
    playersList.add(player);
  }

  private void handleClientConnection() throws IOException, InterruptedException {
    System.out.println("Connected to " + client.getInetAddress());

    output = new ObjectOutputStream(client.getOutputStream());
    input = new ObjectInputStream(client.getInputStream());
    
    if (first){
      Server.playerNum = (int) toClientGetObject("getNumOfPlayers");

      synchronized(lockNum){
        //set ready flag to true (so isReady returns true)
        lockNum.notifyAll();
      }

    }


    synchronized ( lock){
      playerCreation(players);
      i++;
    }


    if (i == playerNum) {

      synchronized (lockPlayer) {
        lockPlayer.notifyAll();
      }
    }


    while (true){
      //answer to client
    }

  }


}

