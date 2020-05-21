package it.polimi.ingsw.PSP32.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import it.polimi.ingsw.PSP32.exceptions.LobbyIsFullException;
import it.polimi.ingsw.PSP32.model.*;
import it.polimi.ingsw.PSP32.view.cli.VirtualCli;
import it.polimi.ingsw.PSP32.view.gui.*;

@SuppressWarnings({"unchecked", "unused"})

public class ServerAdapterGui
{
  private Socket server;
  private static ObjectOutputStream outputStm;
  private static ObjectInputStream inputStm;
  private ExecutorService executionQueue = Executors.newSingleThreadExecutor();
  public static final Object lockPlayer = new Object();
  public static AtomicInteger flagForPlayer = new AtomicInteger(0);
  public static final Object lockNum = new Object();
  public static AtomicInteger flagForNum = new AtomicInteger(0);
  public static final Object lockGods = new Object();
  public static AtomicInteger flagForGods = new AtomicInteger(0);
  public static final Object lockOwnGod = new Object();
  public static AtomicInteger flagForOwnGod = new AtomicInteger(0);
  static boolean firstTime= true;
  public static final Object lockFirstPositioning = new Object();
  public static AtomicInteger flagForFirstPositioning = new AtomicInteger(0);
  public static final Object lockActivePawn = new Object();
  public static AtomicInteger flagForActivePawn = new AtomicInteger(0);
  public static final Object lockBuilding = new Object();
  public static AtomicInteger flagForBuilding = new AtomicInteger(0);
  public static final Object lockMove2 = new Object();
  public static AtomicInteger flagForMove2 = new AtomicInteger(0);




  public ServerAdapterGui(Socket server) throws IOException
  {
    this.server = server;
    outputStm = new ObjectOutputStream(server.getOutputStream());
    inputStm = new ObjectInputStream(server.getInputStream());
  }


  public void stop()
  {
    executionQueue.execute(() -> {
      try {
        server.close();
      } catch (IOException e) {e.printStackTrace(); }
    });
    executionQueue.shutdown();
  }


  public Future<String> requestConversion(String input)
  {
    return executionQueue.submit(() -> {
      outputStm.writeObject(input);
      return (String)inputStm.readObject();
    });
  }

  public void requestSend(String input)
  {
    executionQueue.submit(() -> {
      try {
        outputStm.writeObject(input);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  public Future<String> requestRead()
  {
    return executionQueue.submit(() -> (String)inputStm.readObject());
  }

  /** Method to answer to the server
   *
   * @return Boolean (False= keep answering to the server, True= close the client )
   * @throws ExecutionException
   * @throws InterruptedException
   * @throws IOException
   * @throws LobbyIsFullException
   */
  public Boolean answerToServer() throws ExecutionException, InterruptedException {

    Object incomingObject = executionQueue.submit(() -> inputStm.readObject()).get();
    Message message = (Message)incomingObject;

    if (message.getTypeOfMessage()!=null && message.getTypeOfMessage().equals("StringInfoToPrint") && message.getResult().equals("Lobby is full\n")) GameScene.messageReceived("Lobby Is Full", message.getParameters());


    switch (message.getMethodName()){
      case "getNumOfPlayers":
        PlayerCreationScene playerCreationScene = new PlayerCreationScene();
        playerCreationScene.show();
        synchronized (lockNum) {
          while (flagForNum.get() == 0) {
            try {
              lockNum.wait();
            } catch (InterruptedException e) {}
          }
        }
        int n = PlayerCreationScene.getPlayerNum();
        sendResultMessage(n);
        break;

      case "createPlayer":
        if( ((CopyOnWriteArrayList<Player>) message.getParameters().get(0)).size() != 0  ) {
          PlayerCreationScene2 playerCreationScene2 = new PlayerCreationScene2(((CopyOnWriteArrayList<Player>) message.getParameters().get(0)));
          playerCreationScene2.show();

          synchronized (lockPlayer) {
            while (flagForPlayer.get() == 0) {
              try {
                lockPlayer.wait();
              } catch (InterruptedException e) {}
            }
          }
          Player p = PlayerCreationScene2.getPlayer();
          sendResultMessage(p);
          break;
        }
        else sendResultMessage(PlayerCreationScene.getPlayer());
        break;

      case "gameGodsPicking":
       GodPickingScene godSelectionScene = new GodPickingScene (((ArrayList<Player>) message.getParameters().get(0)).size(), ((God[]) message.getParameters().get(1)));
       godSelectionScene.show();

        synchronized (lockGods) {
          while (flagForGods.get() == 0) {
            try {
              lockGods.wait();
            } catch (InterruptedException e) {}
          }
        }
        God[] g = GodPickingScene.getSelectedGods();
        sendResultMessage(g);
        break;

      case "ownGodSelection":
        GodPickingScene2 ownGodSelection = new GodPickingScene2 (((ArrayList<God>) message.getParameters().get(1)));
        ownGodSelection.show();

        synchronized (lockOwnGod) {
          while (flagForOwnGod.get() == 0) {
            try {
              lockOwnGod.wait();
            } catch (InterruptedException e) {}
          }
        }
        God[] god =  {GodPickingScene2.getGod()};
        sendResultMessage(god);
        Player player = (Player) message.getParameters().get(0);
        player.setGod(god[0]);
        GameScene gameScene = new GameScene(player);
        gameScene.show();
        break;
      case "getPawnInitialPosition":

        if (firstTime) {
          Player player1 = (Player) message.getParameters().get(1);
          if (((Game) message.getParameters().get(0)).getPlayerList().get(0).equals(player1)){
            GameScene gameScene1 = new GameScene(player1);
            gameScene1.show();
          }
          GameScene.messageReceived("Refresh Screen", message.getParameters());
          GameScene.messageReceived("Initial Positioning", message.getParameters());
        }

        synchronized (lockFirstPositioning) {
          while (flagForFirstPositioning.get() == 0) {
            try {
              lockFirstPositioning.wait();
            } catch (InterruptedException e) {}
          }
        }
       sendResultMessage(GameScene.getCoords(firstTime));
        firstTime=false;

        break;
      case "printBoardColored":
        GameScene.messageReceived("Refresh Screen", message.getParameters());
        break;
      case "getActivePawn":
        if(((Player) message.getParameters().get(1)).getGod().getName().equals("Prometheus"))
          if(GameScene.messageReceived("Power", message.getParameters())){
              GameScene.messageReceived("BuildFirst", message.getParameters());
            synchronized (lockActivePawn) {
              while (flagForActivePawn.get() == 0) {
                try {
                  lockActivePawn.wait();
                } catch (InterruptedException e) {}
              }
            }
            flagForActivePawn.set(0);

              sendResultMessage(GameScene.getActivePawn());
              break;
          }
        GameScene.messageReceived("Move Phase", message.getParameters());

        synchronized (lockActivePawn) {
          while (flagForActivePawn.get() == 0) {
            try {
              lockActivePawn.wait();
            } catch (InterruptedException e) {}
          }
        }
        flagForActivePawn.set(0);
        Pawn pawn = GameScene.getActivePawn();
        sendResultMessage(pawn);
        break;
      case "wantsToUsePower":
        Boolean bool1=GameScene.getWantsToUsePower();
        sendResultMessage(bool1);
        break;
      case "waitForBuildCommand":
        Boolean power1;
        if ((Boolean) message.getParameters().get(2)){
          power1 = GameScene.messageReceived("Power", message.getParameters());
          GameScene.messageReceived("Build Phase", message.getParameters());
          synchronized (lockBuilding) {
            while (flagForBuilding.get() == 0) {
              try {
                lockBuilding.wait();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
            flagForBuilding.set(0);
          }
          sendResultMessage(power1);
          break;
        }
        else if ((Boolean) message.getParameters().get(3)){
          power1 = GameScene.messageReceived("Power", message.getParameters());
          if (power1){
          GameScene.messageReceived("Build Phase 2", message.getParameters());
          synchronized (lockBuilding) {
            while (flagForBuilding.get() == 0) {
              try {
                lockBuilding.wait();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
            flagForBuilding.set(0);
          }
            GameScene.messageReceived("Wait", message.getParameters()) ;
            sendResultMessage(!power1);
          break;
          }
          else {
            GameScene.messageReceived("Wait", message.getParameters()) ;
          sendResultMessage(!power1);
            break;
          }
        }

        else {
          GameScene.messageReceived("Build Phase", message.getParameters());
          synchronized (lockBuilding) {
            while (flagForBuilding.get() == 0) {
              try {
                lockBuilding.wait();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
            flagForBuilding.set(0);
          }
          sendResultMessage(false);
        }
        break;
      case "getBuildLocationViaArrows":
        sendResultMessage(GameScene.getBuildLocation());
        break;
      case "waitForMoveCommand":
        if((Boolean) message.getParameters().get(3))
        {
          Boolean power =GameScene.messageReceived("Power", message.getParameters());
          sendResultMessage( power);
          break;

        }
        sendResultMessage(true);
        break;
      case "getValidMoveViaArrows":
        if (!(Boolean) message.getParameters().get(3)){
        GameScene.messageReceived("Move Phase 2", message.getParameters());
        synchronized (lockMove2) {
          while (flagForMove2.get() == 0) {
            try {
              lockMove2.wait();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
          flagForMove2.set(0);
        int x[] = {GameScene.getActivePawn().getX(), GameScene.getActivePawn().getY()};
          sendResultMessage(x);
          break;
        }
        else if (((Pawn) message.getParameters().get(1)).getPlayer().getGod().getName().equals("Prometheus") && GameScene.getWantsToUsePower()){
          GameScene.messageReceived("Move Phase 2", message.getParameters());
          synchronized (lockMove2) {
            while (flagForMove2.get() == 0) {
              try {
                lockMove2.wait();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          }
          flagForMove2.set(0);
          sendResultMessage(new int[]{GameScene.getActivePawn().getX(), GameScene.getActivePawn().getY()});
        }
        else sendResultMessage(new int[]{GameScene.getActivePawn().getX(), GameScene.getActivePawn().getY()});
        break;
      case "askBuildTwice":

        sendResultMessage(GameScene.messageReceived("Power", message.getParameters()));
        GameScene.messageReceived("Wait", message.getParameters()) ;
        break;
      case "endGameGraphics":
        GameScene.messageReceived("Endgame", message.getParameters());
        return true;
      case "removedPlayerGraphics":
        VirtualCli.removedPlayerGraphics((Player) message.getParameters().get(0));
        break;
      case "Disconnection":
        GameScene.messageReceived("Disconnection", null);
        return true;

    }
    return false;
  }

  public void requestSendObject(Object object)
  {
    executionQueue.submit(() -> {
      try {
        outputStm.reset();
        outputStm.writeObject(object);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  /** Method to send to the server the answer to a request
   *
   * @param object : Object the result
   */
  public void sendResultMessage(Object object) {
    executionQueue.submit(() -> {
      try {
        outputStm.reset();
        Message message = new Message(null, null, "Result", object);
        outputStm.writeObject(message);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  /** Method to ask to the server something (Overload)
   *
   * @param methodName : String the name of the method to call
   * @param par1 : Object possible parameter for the method to call
   * @param par2
   * @param par3
   * @param par4
   * @return :
   * @throws IOException
   */
  public static Object toServerGetObject(String methodName, Object par1, Object par2, Object par3, Object par4) throws IOException {
    ArrayList<Object> parameters = new ArrayList<>();
    parameters.add(par1);
    parameters.add(par2);
    parameters.add(par3);
    parameters.add(par4);
    Message outboundMessage = new Message(methodName, parameters, "Request", null);
    outputStm.reset();
    outputStm.writeObject(outboundMessage);
    Object object = null;
    try {
      Message inboundMessage = (Message) inputStm.readObject();
      if (inboundMessage.getTypeOfMessage()!=null && inboundMessage.getTypeOfMessage().equals("Result")){
        object = inboundMessage.getResult();
      }
    } catch (ClassNotFoundException | ClassCastException | NullPointerException e) {
      e.printStackTrace();
      System.out.println("invalid stream from server");
    }
    return object;
  }
  public static Object toServerGetObject(String methodName, Object par1, Object par2, Object par3) throws IOException {
    return toServerGetObject(methodName, par1, par2, par3, null);
  }
  public static Object toServerGetObject(String methodName, Object par1, Object par2) throws IOException {
    return toServerGetObject(methodName, par1, par2, null, null);
  }
  public static Object toServerGetObject(String methodName, Object par) throws IOException {
    return toServerGetObject(methodName, par, null, null, null);
  }
}



