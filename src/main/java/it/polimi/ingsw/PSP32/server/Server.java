package it.polimi.ingsw.PSP32.server;

import it.polimi.ingsw.PSP32.controller.Phases;
import it.polimi.ingsw.PSP32.controller.GameSetup;
import it.polimi.ingsw.PSP32.model.Game;
import it.polimi.ingsw.PSP32.model.Player;
import it.polimi.ingsw.PSP32.controller.Utility;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class Server implements Runnable {
  public final static int SOCKET_PORT = 7778;

  public static volatile ArrayList<ClientHandler> clients = new ArrayList<>();

  public static volatile CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();
  public static int playerNum = 0;
  public static final Object lockNum = new Object();
  public static final Object lockPlayer = new Object();
  public static AtomicInteger flagForSync = new AtomicInteger();
  public static volatile Boolean exit;

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
        ServerSocket socket;

        try {
            socket = new ServerSocket(SOCKET_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("cannot open server socket");
            System.exit(1);
            return;
        }

        flagForSync.set(0);

        exit = false;
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

        synchronized (lockNum) {
            while (flagForSync.get() == 0) {
                try {
                    lockNum.wait();
                } catch (InterruptedException  e) {
                    e.printStackTrace();
                }
            }
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
                Socket client = socket.accept();
                ClientHandler clientHandler = new ClientHandler(client, false);
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
        }
        if(exit) {
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
            Utility.toAllClientsVoid(game, "printBoardColored", game);

        } catch (IOException e) {
            Utility.notifyClosingGame(clients);
            StopClients(socket, players.size());
            return;
        }

        try {
            Phases.startGame(game);
        } catch (IOException e) {
            System.out.println("Someone left, the game is being restarted");
            Utility.notifyClosingGame(clients);
        }

        StopClients(socket, players.size());


  }


  public static void main(String[] args) {


      Server server = new Server();
      do {
          server.run();
          players.clear();
          playerNum=0;
          clients.clear();
          exit=false;
      }while (true);
     // System.exit(0);





  }



}



