package it.polimi.ingsw.PSP32.client;

import it.polimi.ingsw.PSP32.server.Server;
import it.polimi.ingsw.PSP32.view.gui.scenes.ConnectionScene;
import it.polimi.ingsw.PSP32.view.gui.Gui;


import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;


public class ClientGui implements Runnable
{
  public static final Object lockAddress = new Object();
  public static String ip;
  public static AtomicInteger flagForAddr = new AtomicInteger(0);

  public static void main(String[] args )
  {
    ClientGui client = new ClientGui();
    client.run();
  }

  @Override
  public void run()
  {
    try {
      Gui.setupWindow();
    } catch (IOException | FontFormatException e) {
      e.printStackTrace();
    }

    ConnectionScene connectionScene = new ConnectionScene();
    connectionScene.show();

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
      Thread thread = new Thread(new ClientHeartbeat(serverAdapter));
      thread.start();
    } catch (IOException e) {
      System.out.println("could not contact server");
      return;
    }

    while (true){
      try {
        serverAdapter.answerToServer();
      } catch (ExecutionException | InterruptedException  | NullPointerException  e) {e.printStackTrace();
        return;
      }
    }
  }
}