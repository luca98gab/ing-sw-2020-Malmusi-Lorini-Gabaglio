package it.polimi.ingsw.PSP32.server;

import it.polimi.ingsw.PSP32.controller.Phases;
import it.polimi.ingsw.PSP32.controller.GameSetup;
import it.polimi.ingsw.PSP32.model.Game;
import it.polimi.ingsw.PSP32.model.Player;
import it.polimi.ingsw.PSP32.controller.Utility;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class Server implements Runnable {

  private static JFrame serverWindow = new JFrame("SantoriniServer");

  public final static int SOCKET_PORT = 7778;

  public static volatile ArrayList<ClientHandler> clients = new ArrayList<>();

  public static volatile CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();
  public static int playerNum = 0;
  public static final Object lockNum = new Object();
  public static final Object lockPlayer = new Object();
  public static AtomicInteger flagForSync = new AtomicInteger();
  public static volatile Boolean exit;
  public static AtomicInteger flagForTimeout = new AtomicInteger();
  private int timeout=20000;

  private final Utility utility = new Utility();
  private final Phases phases = new Phases(utility);

    /** Method to stop the clients after someone won or someone left the match
     *
     * @param socket : Socket
     * @param i : number of clients
     */
    private void StopClients(ServerSocket socket, int i) {

        for (int k = 0; k < i; k++) {
            assert players.get(k) != null;
            players.get(k).getRelatedClient().stop();
        }
        try {
           socket.close();
         } catch (IOException e) {
             e.printStackTrace();
            }
    }


  @Override
  public void run() {

    serverWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    serverWindow.setResizable(false);
    serverWindow.setIconImage((new ImageIcon(getClass().getResource("/Santorini Images/GameIconServer.jpeg")).getImage()));
    Taskbar taskbar=Taskbar.getTaskbar();
    if(taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) taskbar.setIconImage(serverWindow.getIconImage());
    serverWindow.pack();
    serverWindow.setVisible(true);
      ServerSocket socket=null;
      try{

      try {
          socket = new ServerSocket(SOCKET_PORT);
      } catch (IOException e) {
          e.printStackTrace();
          System.out.println("cannot open server socket");
          System.exit(1);
          return;
      }

      flagForSync.set(0);
      flagForTimeout.set(0);

      exit = false;
      Socket client = new Socket();
      try {
          /* accepts connections; for every connection we accept,
           * create a new Thread executing a ClientHandler */
          client = socket.accept();
          ClientHandler clientHandler = new ClientHandler(client, true, utility);
          clients.add(clientHandler);
          Thread thread = new Thread(clientHandler, "server_" + client.getInetAddress());
          thread.start();

      } catch (IOException e) {
          System.out.println("connection dropped");
      }
      client.setSoTimeout(timeout);

      synchronized (lockNum) {
          while (flagForSync.get() == 0) {
              try {
                  lockNum.wait();
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
      }
      if(flagForTimeout.get()==1)
      {
          throw new SocketTimeoutException();
      }

      if (exit) {
          try {
              socket.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
          return;
      }

      flagForSync.set(0);


      for (int i = 1; i < playerNum; ) {
          try {
              /* accepts connections; for every connection we accept,
               * create a new Thread executing a ClientHandler */
              client = socket.accept();
              ClientHandler clientHandler = new ClientHandler(client, false, utility);
              clients.add(clientHandler);
              Thread thread = new Thread(clientHandler, "server_" + i + client.getInetAddress());
              i++;
              thread.start();

          } catch (IOException e) {
              try {
                  socket.close();
                  return;
              } catch (IOException ex) {
                  ex.printStackTrace();
              }
              return;
          }
          client.setSoTimeout(timeout);

          synchronized (lockPlayer) {
              while (flagForSync.get() == 0) {
                  try {
                      lockPlayer.wait();
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
          }
          flagForSync.set(0);
          if(flagForTimeout.get()==1)
          {
              throw new SocketTimeoutException();
          }
      }
      if (exit) {
          try {
              socket.close();
              return;
          } catch (IOException e) {
              e.printStackTrace();
          }
      }


      new Thread(new UnwantedClientHandler(socket)).start();


      Game game = new Game(playerNum);


      game.setPlayerList(new ArrayList<>(players));


      try {
          GameSetup.godPicking(game.getPlayerList()); //every player picks his card
          for (int i = 0; i < game.getPlayerList().size(); i++) {
              game.getPlayerList().get(i).getRelatedClient().toClientVoid("printPlayerInfo", game.getPlayerList(), false);
          }
          GameSetup.firstPawnPositioning(game);
          utility.toAllClientsVoid(game, "printBoardColored", game);

      } catch (IOException e) {
          utility.notifyClosingGame(clients);
          StopClients(socket, players.size());
          return;
      }

      try {
          phases.startGame(game);
      } catch (SocketTimeoutException s) {
          System.out.println("Someone left, the game is being restarted");
          utility.notifyClosingGame(clients);
          try {
              socket.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
          return;
      } catch (IOException e) {
          System.out.println("Someone left, the game is being restarted");
          utility.notifyClosingGame(clients);
          try {
              socket.close();
          } catch (IOException t) {
              e.printStackTrace();
          }
          return;
      }

      //StopClients(socket, players.size());
  }catch(SocketException | SocketTimeoutException s)
    {
        System.out.println("Someone disconnected, the game is being restarted");
        utility.notifyClosingGame(clients);
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

  }


  public static void main(String[] args) {


      Server server = new Server();
      do {
          server.run();
          System.out.println("new session");
          flagForTimeout.set(0);
          flagForSync.set(0);
          players.clear();
          playerNum=0;
          clients.clear();
          exit=false;
      }while (true);
     // System.exit(0);
  }



}



