package it.polimi.ingsw.PSP32.server;

import it.polimi.ingsw.PSP32.model.Game;
import it.polimi.ingsw.PSP32.model.Player;
import it.polimi.ingsw.PSP32.view.LocalCli;
import it.polimi.ingsw.PSP32.controller.Logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server implements Runnable {
  public final static int SOCKET_PORT = 7777;

  private ArrayList<ClientHandler> clients = new ArrayList<>();

  public static ArrayList<Player> players = new ArrayList<>();
  public static int playerNum = 0;
  public static final Object lockNum = new Object();
  public static final Object lockPlayer = new Object();
  public static final Object lock = new Object();



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


    try {
      /* accepts connections; for every connection we accept,
       * create a new Thread executing a ClientHandler */
      Socket client = socket.accept();
      ClientHandler clientHandler = new ClientHandler(client, true);
      clients.add(clientHandler);
      Thread thread = new Thread(clientHandler, "server_" + client.getInetAddress());
      thread.start();
    } catch (IOException e) {
      System.out.println("connection dropped");
    }

    synchronized(lockNum){
      try {
        lockNum.wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }




    for (int i = 1; i < playerNum; ){
      try {
        /* accepts connections; for every connection we accept,
         * create a new Thread executing a ClientHandler */
        Socket client = socket.accept();
        ClientHandler clientHandler = new ClientHandler(client, false);
        clients.add(clientHandler);
        Thread thread = new Thread(clientHandler, "server_" + i + client.getInetAddress());
        i++;
        thread.start();
      } catch (IOException e) {
        System.out.println("connection dropped");
      }
    }

    for (int i = 0; i < playerNum; i++){
      synchronized(lockPlayer){
        try {
          lockPlayer.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

    new Thread(new UnwantedClientHandler(socket)).start();


    Game game = new Game(playerNum);

    game.setPlayerList(new ArrayList<>(players));


    try {
      Logic.godPicking(game.getPlayerList()); //every player picks his card
      for(int i=0; i < game.getPlayerList().size(); i++) {
        game.getPlayerList().get(i).getRelatedClient().toClientVoid("printPlayerInfo", game.getPlayerList(), false);
        LocalCli.printPlayerInfo(game.getPlayerList(), false); //prints every player info
      }
      Logic.firstPawnPositioning(game);
      for (int i = 0; i < game.getPlayerList().size(); i++){
        game.getPlayerList().get(i).getRelatedClient().toClientVoid("printBoardColored", game);
      }

      Logic.startGame(game);

      //LocalCli.printBoardColored(game);
    } catch (IOException e) {
      e.printStackTrace();
    }


    while (true);

  }


  public static void main(String[] args) {
    Server server = new Server();
    server.run();
  }

}



