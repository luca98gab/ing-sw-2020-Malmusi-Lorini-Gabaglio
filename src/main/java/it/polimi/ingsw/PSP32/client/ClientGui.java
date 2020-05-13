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


public class ClientGui implements Runnable
{

  public static String ip = "0.0.0.0";

  public static void main(String[] args )
  {
    Client client = new Client();
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
    Gui.setupWindow();

    ConnectionScene connectionScene = new ConnectionScene();
    connectionScene.show();

    //missing get ip from connectionScene

    Socket server = null;
    
    do {
      try {
        server = new Socket(ip, Server.SOCKET_PORT);
      } catch (IOException e) {
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


    /**
     * String receivedMessage = null;
     *     do {
     *       receivedMessage = receiveMessage(serverAdapter);
     *       System.out.println(receivedMessage);
     *       String str = scanner.nextLine();
     *       while ("".equals(str));
     *
     *       serverAdapter.requestSend(str);
     *       System.out.println("message sent");
     *     } while (!receivedMessage.equals("esc"));
     *     serverAdapter.stop();
     */




  }


}