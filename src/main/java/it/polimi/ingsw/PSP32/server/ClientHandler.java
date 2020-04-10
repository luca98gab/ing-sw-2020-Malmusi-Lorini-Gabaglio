package it.polimi.ingsw.PSP32.server;

import it.polimi.ingsw.PSP32.model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class ClientHandler implements Runnable
{
  private Socket client;
  private Player player = null;
  private Boolean first;
  private List<assignPlayerObserver> observers = new ArrayList<>();
  private int playernum = 0;

  public void addObserver(assignPlayerObserver observer){
    synchronized (observers){
      observers.add(observer);
    }
  }


  ClientHandler(Socket client, Boolean first)
  {
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


  private void handleClientConnection() throws IOException
  {
    System.out.println("Connected to " + client.getInetAddress());

    ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
    ObjectInputStream input = new ObjectInputStream(client.getInputStream());


    if (first==true){
      String str = "Insert number of players: ";
      output.writeObject(str);

      do {
        try {
          Object incomingString = input.readObject();
          playernum = Integer.parseInt((String) incomingString);
        } catch (ClassNotFoundException | ClassCastException e) {
          System.out.println("invalid stream from client");
        }
      } while (playernum==0);
    }


    String str = "Insert name: ";
    output.writeObject(str);


    String name = null;



    do {
      try {
        Object incomingString = input.readObject();
        name = (String)incomingString;
      } catch (ClassNotFoundException | ClassCastException e) {
        System.out.println("invalid stream from client");
      }
    } while ("".equals(name));

    System.out.println(name);

    str = "Insert color: ";
    output.writeObject(str);

    String color = null;

    do {
      try {
        Object incomingString = input.readObject();
        color = (String)incomingString;
      } catch (ClassNotFoundException | ClassCastException e) {
        System.out.println("invalid stream from client");
      }
    } while ("".equals(color));

    System.out.println(color);

    player = new Player(name, color, null);









    //client.close();
  }
  private void createPlayer(String name, String color){
    player = new Player(name, color, null);
    List<assignPlayerObserver> observers_copy;
    synchronized (observers) {
      observers_copy = new ArrayList<>(observers);
    }
    for (assignPlayerObserver oss1: observers_copy){
      oss1.verifyPlayer(player);
    }
  }

  public Player getPlayer() {
    return player;
  }

  public int getPlayernum() {
    return playernum;
  }


}

