package it.polimi.ingsw.PSP32.server;

import it.polimi.ingsw.PSP32.model.Game;
import it.polimi.ingsw.PSP32.model.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.WatchEvent;
import java.util.concurrent.TimeUnit;


public class Server implements Runnable, assignPlayerObserver
{
  public final static int SOCKET_PORT = 7777;

  Player p1 = null;
  Player p2 = null;
  Player p3 = null;

  @Override
  public void run() {
    ServerSocket socket;
    try {
      socket = new ServerSocket(SOCKET_PORT);
    } catch (IOException e) {
      System.out.println("cannot open server socket");
      System.exit(1);
      return;
    }

    ClientHandler clientHandler1 = null;
    ClientHandler clientHandler2 = null;
    ClientHandler clientHandler3 = null;

    try {
      /* accepts connections; for every connection we accept,
       * create a new Thread executing a ClientHandler */
      Socket client = socket.accept();
      clientHandler1 = new ClientHandler(client, true);
      Thread thread1 = new Thread(clientHandler1, "server_" + client.getInetAddress());
      thread1.start();
    } catch (IOException e) {
      System.out.println("connection dropped");
    }

    do {
      p1=clientHandler1.getPlayer();
    } while (p1==null);
    Game game1 = new Game(clientHandler1.getPlayernum());

    try {
      /* accepts connections; for every connection we accept,
       * create a new Thread executing a ClientHandler */
      Socket client = socket.accept();
      clientHandler2 = new ClientHandler(client, false);
      Thread thread2 = new Thread(clientHandler2, "server_" + client.getInetAddress());
      thread2.start();
    } catch (IOException e) {
      System.out.println("connection dropped");
    }

    if (game1.getPlayerNum()==3){
      try {
        /* accepts connections; for every connection we accept,
         * create a new Thread executing a ClientHandler */
        Socket client = socket.accept();
        clientHandler3 = new ClientHandler(client, false);
        Thread thread3 = new Thread(clientHandler3, "server_" + client.getInetAddress());
        thread3.start();
      } catch (IOException e) {
        System.out.println("connection dropped");
      }

      do {
        p3=clientHandler3.getPlayer();

      } while (p3==null);
    }

    do {
      p2=clientHandler2.getPlayer();

    } while (p2==null);


    System.out.println("Giocatori: ");
    System.out.println(p1.getName());
    System.out.println(p2.getName());
    if (game1.getPlayerNum()==3) System.out.println(p3.getName());


  }


  public static void main(String[] args)
  {
    Server server = new Server();
    server.run();
  }


  @Override
  public synchronized void verifyPlayer(Player newPlayer) {

  }
}

