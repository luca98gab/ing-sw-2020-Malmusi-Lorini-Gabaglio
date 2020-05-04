package it.polimi.ingsw.PSP32.client;

import it.polimi.ingsw.PSP32.exceptions.LobbyIsFullException;
import it.polimi.ingsw.PSP32.server.Server;


import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class Client implements Runnable
{
  private Boolean exit=false;



  public static String ip = "0.0.0.0";

  public static void main(String[] args )
  {
    Client client = new Client();
    client.run();
    System.exit(0);
  }

  public void setExit (Boolean bool){
    exit=bool;
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

    Scanner scanner = new Scanner(System.in);

    /**
     * System.out.println("IP address of server?");
     * String ip = scanner.nextLine();
     */



    Socket server;
    try {
      server = new Socket(ip, Server.SOCKET_PORT);
    } catch (IOException e) {
      System.out.println("server unreachable");
      return;
    }
    System.out.println("Connected");

    ServerAdapter serverAdapter;
    try {
      serverAdapter = new ServerAdapter(server);
    } catch (IOException e) {
      System.out.println("There is already a game in progress, try later");
      return;
    }



    while (!exit) {
      try {
        exit = serverAdapter.answerToServer();
      } catch (ExecutionException | InterruptedException | IOException | NullPointerException | LobbyIsFullException  e) {exit = true; }
    }
    }






}


