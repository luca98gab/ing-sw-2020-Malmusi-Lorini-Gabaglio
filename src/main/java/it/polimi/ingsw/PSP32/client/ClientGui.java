package it.polimi.ingsw.PSP32.client;

import it.polimi.ingsw.PSP32.exceptions.LobbyIsFullException;
import it.polimi.ingsw.PSP32.server.Server;
import it.polimi.ingsw.PSP32.view.ConnectionScene;
import it.polimi.ingsw.PSP32.view.Gui;
import it.polimi.ingsw.PSP32.view.PlayerCreationScene;


import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;


public class ClientGui implements Runnable
{
  public static final Object lockAddress = new Object();
  public static String ip;
  public static AtomicInteger flagForAddr = new AtomicInteger();


  public static void main(String[] args )
  {
    ClientGui client = new ClientGui();
    client.run();
  }

  public String receiveMessage(ServerAdapter serverAdapter1) {
    Future<String> stringFuture = serverAdapter1.requestRead();
    String response = null;
    while (response == null){
      try {
        response = stringFuture.get(5, TimeUnit.SECONDS);
      } catch (InterruptedException | TimeoutException e) {
      } catch (ExecutionException e) {
        return "server not reachable";
      }
    }
    return response;
  }


  @Override
  public void run()
  {
    flagForAddr.set(0);

    Gui.setupWindow();
    ConnectionScene connectionScene = new ConnectionScene();
    connectionScene.show();

    //missing get ip from connectionScene
    Socket server = null;

    do {
      synchronized (lockAddress) {
        while (flagForAddr.get() == 0) {
          try {
            lockAddress.wait();
          } catch (InterruptedException e) {}
        }
      }
      try {
        server = new Socket(ip, Server.SOCKET_PORT);
      } catch (IOException e) {
        flagForAddr.set(0);
        ConnectionScene.connectionRefused();
      }
    } while (server==null);

    ServerAdapterGui serverAdapter;
    try {
      serverAdapter = new ServerAdapterGui(server);
    } catch (IOException e) {
      System.out.println("could not contact server");
      return;
    }

    while (true){
      try {
        serverAdapter.answerToServer();

      } catch (ExecutionException | InterruptedException | IOException | NullPointerException | LobbyIsFullException e) {
        return;
      }
    }

  }


}