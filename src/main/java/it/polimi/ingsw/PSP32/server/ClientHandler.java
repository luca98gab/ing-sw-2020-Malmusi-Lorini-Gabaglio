package it.polimi.ingsw.PSP32.server;

import it.polimi.ingsw.PSP32.client.Message;
import it.polimi.ingsw.PSP32.model.Player;
import it.polimi.ingsw.PSP32.view.VirtualCli;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


import static it.polimi.ingsw.PSP32.server.Server.*;


public class ClientHandler implements Runnable
{
  private Socket client;
  private final Boolean first;
  private ObjectOutputStream output;
  private ObjectInputStream input;




  ClientHandler(Socket client, Boolean first) throws IOException {
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

  public int toClientInt(String methodName, ArrayList<Object> parameters) throws IOException {
    Message message = new Message(methodName, parameters);
    output.writeObject(message);
    int i=0;
    try {
      Object incoming = input.readObject();
      i = ((int) incoming);
    } catch (ClassNotFoundException | ClassCastException e) {
      System.out.println("invalid stream from client");
    }
    return i;
  }
  public int toClientInt(String methodName) throws IOException {
    return toClientInt(methodName, null);
  }

  public Player toClientPlayer(String methodName, ArrayList<Object> parameters) throws IOException {
    Message message = new Message(methodName, parameters);
    output.writeObject(message);
    Player player = null;
    try {
      Object incoming = input.readObject();
      player = ((Player) incoming);
    } catch (ClassNotFoundException | ClassCastException e) {
      System.out.println("invalid stream from client");
    }
    return player;
  }
  public Player toClientPlayer(String methodName) throws IOException {
    return toClientPlayer(methodName, null);
  }



  private void handleClientConnection() throws IOException
  {
    System.out.println("Connected to " + client.getInetAddress());

    output = new ObjectOutputStream(client.getOutputStream());
    input = new ObjectInputStream(client.getInputStream());


    if (first){
      Server.playerNum = toClientInt("getNumOfPlayers");

      synchronized(lockNum){
        //set ready flag to true (so isReady returns true)
        lockNum.notifyAll();
      }

    }

    Player player = toClientPlayer("createPlayer");

    Server.players.add(player);

    synchronized(lockPlayer){
      //set ready flag to true (so isReady returns true)
      lockPlayer.notifyAll();
    }
  }


}

