package it.polimi.ingsw.PSP32.server;

import it.polimi.ingsw.PSP32.model.Message;
import it.polimi.ingsw.PSP32.controller.CheckCanBuild;
import it.polimi.ingsw.PSP32.controller.CheckCanMove;
import it.polimi.ingsw.PSP32.controller.Utility;
import it.polimi.ingsw.PSP32.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import static it.polimi.ingsw.PSP32.server.Server.*;


public class ClientHandler implements Runnable
{
  private  ObjectOutputStream output;
  private  ObjectInputStream input;
  private final Socket client;
  private final Boolean first;
  private final Utility utility;
  private ExecutorService executionQueue = Executors.newSingleThreadExecutor();

  public ClientHandler(Socket client, Boolean first, Utility utility) {
    this.utility = utility;
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
      exit = true;
      synchronized (lockNum) {
        lockNum.notifyAll();
      }
      synchronized (lockPlayer) {
        lockPlayer.notifyAll();
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
  public Object toClientGetObject(String methodName, Object par1, Object par2, Object par3, Object par4, Object par5) throws IOException {
    ArrayList<Object> parameters = new ArrayList<>();
    parameters.add(0, par1);
    parameters.add(1, par2);
    parameters.add(2, par3);
    parameters.add(3, par4);
    parameters.add(4, par5);
    Message outboundMessage = new Message(methodName, parameters, "Request", null);
      output.reset();
    output.writeObject(outboundMessage);
    Object object= null;
    try {
      Message inboundMessage = (Message) input.readObject();
      while(inboundMessage.getTypeOfMessage().equals("Heartbeat")){inboundMessage = (Message) input.readObject();}
      while (inboundMessage.getTypeOfMessage().equals("Request")){
        Boolean valid = null;
        switch (inboundMessage.getMethodName()){
          case "checkCanMoveSW":
            valid = CheckCanMove.checkCanMoveSW((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanMoveS":
            valid = CheckCanMove.checkCanMoveS((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanMoveSE":
            valid = CheckCanMove.checkCanMoveSE((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanMoveW":
            valid = CheckCanMove.checkCanMoveW((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanMoveE":
            valid = CheckCanMove.checkCanMoveE((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanMoveNW":
            valid = CheckCanMove.checkCanMoveNW((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanMoveN":
            valid = CheckCanMove.checkCanMoveN((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanMoveNE":
            valid = CheckCanMove.checkCanMoveNE((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
          case "checkCanBuildSW":
            valid = CheckCanBuild.checkCanBuildSW((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2), (Boolean) inboundMessage.getParameters().get(3));
            break;
          case "checkCanBuildS":
            valid = CheckCanBuild.checkCanBuildS((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2), (Boolean) inboundMessage.getParameters().get(3));
            break;
          case "checkCanBuildSE":
            valid = CheckCanBuild.checkCanBuildSE((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2), (Boolean) inboundMessage.getParameters().get(3));
            break;
          case "checkCanBuildW":
            valid = CheckCanBuild.checkCanBuildW((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2), (Boolean) inboundMessage.getParameters().get(3));
            break;
          case "checkCanBuildE":
            valid = CheckCanBuild.checkCanBuildE((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2), (Boolean) inboundMessage.getParameters().get(3));
            break;
          case "checkCanBuildNW":
            valid = CheckCanBuild.checkCanBuildNW((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2), (Boolean) inboundMessage.getParameters().get(3));
            break;
          case "checkCanBuildN":
            valid = CheckCanBuild.checkCanBuildN((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2), (Boolean) inboundMessage.getParameters().get(3));
            break;
          case "checkCanBuildNE":
            valid = CheckCanBuild.checkCanBuildNE((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2), (Boolean) inboundMessage.getParameters().get(3));
          break;
          case "checkCanBuildBelow":
            valid = CheckCanBuild.checkCanBuildBelow((Game)inboundMessage.getParameters().get(0), (Pawn)inboundMessage.getParameters().get(1), (Cell)inboundMessage.getParameters().get(2));
            break;
        }
        Message outboundMessageInner = null;
        if (valid!=null){
          outboundMessageInner = new Message(null, null, "Result", valid);
        }

        output.reset();
        output.writeObject(outboundMessageInner);
        inboundMessage = (Message) input.readObject();
        while(inboundMessage.getTypeOfMessage().equals("Heartbeat")){inboundMessage = (Message) input.readObject();}

      }
      if (inboundMessage.getTypeOfMessage().equals("Result")){
        object = inboundMessage.getResult();
      }
    } catch (ClassNotFoundException | ClassCastException e) {
      e.printStackTrace();
      System.out.println("invalid stream from client");
    }
    return object;
  }
  public Object toClientGetObject(String methodName, Object par1, Object par2, Object par3, Object par4) throws IOException{
    return toClientGetObject(methodName, par1, par2, par3, par4, null);
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
      }catch (SocketTimeoutException s) {
        synchronized (lockNum) {
          Server.flagForSync.set(1);
          Server.flagForTimeout.set(1);
          lockNum.notifyAll();
          client.close();
          this.stop();
        }
      }
      catch (IOException e){
        exit=true;
        synchronized(lockNum){
          Server.flagForSync.set(1);
          lockNum.notifyAll();
        }

      }
    try{
      playerCreation(players);
      }
    catch (IOException e) {
      utility.notifyClosingGame(clients);
      exit = true;
      synchronized (lockPlayer) {
        Server.flagForSync.set(1);
        lockPlayer.notifyAll();
      }
    }

      synchronized(lockNum){
        Server.flagForSync.set(1);
        lockNum.notifyAll();
      }

    } else{

      try {
        playerCreation(players);
      }catch (SocketTimeoutException s) {
        synchronized (lockPlayer) {
          Server.flagForSync.set(1);
          Server.flagForTimeout.set(1);
          lockPlayer.notifyAll();
          client.close();
          this.stop();
        }
      }
      catch (IOException e){
        utility.notifyClosingGame(clients);
        exit=true;
        synchronized(lockPlayer){
          Server.flagForSync.set(1);
          lockPlayer.notifyAll();
        }

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

