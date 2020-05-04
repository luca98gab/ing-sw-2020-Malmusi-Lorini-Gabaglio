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
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;


import static it.polimi.ingsw.PSP32.server.Server.*;


public class ClientHandler implements Runnable
{
  private  ObjectOutputStream output;
  private  ObjectInputStream input;
  private final Socket client;
  private final Boolean first;
  private ExecutorService executionQueue = Executors.newSingleThreadExecutor();

  ClientHandler(Socket client, Boolean first) {
    this.client = client;
    this.first = first;
  }


  public void stop()
  {
    executionQueue.execute(() -> {
      try {
        client.close();
      } catch (IOException e) { }
    });
    executionQueue.shutdown();
  }


  public static Socket getClient(ClientHandler clientHandler)
  {
   return clientHandler.client;
  }


  @Override
  public void run()
  {
    try {
      handleClientConnection();
    } catch (IOException e) {
      synchronized (lockNum) {
        exit = true;
        lockNum.notifyAll();
      }


    }
  }

  /** Method to ask something to the client expecting nothing in return (Overload)
   *
   * @param methodName : String name of the method to call client-side
   * @param par1: Object possible parameter for the method
   * @param par2
   * @throws IOException
   */
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

  /** Method to ask something to the client expecting a Object in return (Overload)
   *
   * @param methodName : String name of the method to call client-side
   * @param par1: Object possible parameter for the method
   * @param par2
   * @param par3
   * @param par4
   * @throws IOException
   */
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

  /** Method to create a player instance on the server
   *
   * @param playersList : CopyOnWriteArrayList list of already existing players
   * @throws IOException
   */
  public synchronized void playerCreation(CopyOnWriteArrayList<Player> playersList) throws IOException {
    Player player = (Player) toClientGetObject("createPlayer", playersList, playerNum);
    player.setRelatedClient(this);
    playersList.add(player);
  }

  private void handleClientConnection() throws IOException {
    System.out.println("Connected to " + client.getInetAddress());

    output = new ObjectOutputStream(client.getOutputStream());
    input = new ObjectInputStream(client.getInputStream());
    
    if (first){
      try{
        Server.playerNum = (int) toClientGetObject("getNumOfPlayers");
      }
      catch (IOException e){
        throw new IOException();
      }

      playerCreation(players);

      synchronized(lockNum){
        //set ready flag to true (so isReady returns true)
        Server.flagForSync.set(1);
        lockNum.notifyAll();
      }

    } else{

      try {
        playerCreation(players);
      }
      catch (IOException e){
        Logic.notifyClosingGame(clients);
        synchronized(lockPlayer){
          Server.flagForSync.set(1);
          lockPlayer.notifyAll();
        }
        throw new IOException();


      }

      synchronized(lockPlayer){
        //set ready flag to true (so isReady returns true)
        Server.flagForSync.set(1);
        lockPlayer.notifyAll();
      }
    }


    while (true){
      //answer to client
    }

  }


}

