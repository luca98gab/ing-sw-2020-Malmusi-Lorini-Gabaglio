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
import it.polimi.ingsw.PSP32.view.*;
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
  public Boolean answerToServer() throws ExecutionException, InterruptedException, IOException, LobbyIsFullException {

    Object incomingObject = executionQueue.submit(() -> inputStm.readObject()).get();
    Message message = (Message)incomingObject;

 //   if (message.getTypeOfMessage()!=null && message.getTypeOfMessage().equals("StringInfoToPrint")) System.out.println(message.getResult());
  //  if (message.getTypeOfMessage()!=null && message.getTypeOfMessage().equals("StringInfoToPrint") && message.getResult().equals("Lobby is full\n")) throw new LobbyIsFullException(null);


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
        }
        else sendResultMessage(PlayerCreationScene.getPlayer());
        break;
      case "gameGodsPicking":

      //  GodSelectionScene godSelectionScene = new GodSelectionScene (((ArrayList<Player>) message.getParameters().get(0)).size());
       // godSelectionScene.show();


        God[] g = VirtualCli.gameGodsPicking(((ArrayList<Player>) message.getParameters().get(0)), ((God[]) message.getParameters().get(1)));
        sendResultMessage(g);
        break;
      case "ownGodSelection":
        God[] god = {VirtualCli.ownGodSelection(((Player) message.getParameters().get(0)), ((ArrayList<God>) message.getParameters().get(1)))};
        sendResultMessage(god);
        break;
      case "player1GodAssignment":
        VirtualCli.player1GodAssignment(((Player) message.getParameters().get(0)), ((God) message.getParameters().get(1)));
        break;
      case "printPlayerInfo":
        VirtualCli.printPlayerInfo(((ArrayList<Player>) message.getParameters().get(0)), ((Boolean) message.getParameters().get(1)));
        break;
      case "printTurnInfo":
        VirtualCli.printTurnInfo((Player) message.getParameters().get(0));
        break;
      case "getPawnInitialPosition":
        int[] position = VirtualCli.getPawnInitialPosition((Game) message.getParameters().get(0));
        sendResultMessage(position);
        break;
      case "printBoardColored":
        VirtualCli.printBoardColored((Game) message.getParameters().get(0));
        break;
      case "getActivePawn":
        Pawn pawn=VirtualCli.getActivePawn((Game) message.getParameters().get(0), (Player) message.getParameters().get(1) );
        sendResultMessage(pawn);
        break;
      case "wantsToUsePower":
        Boolean bool1=VirtualCli.wantsToUsePower((Player) message.getParameters().get(0));
        sendResultMessage(bool1);
        break;
      case "waitForBuildCommand":
        Boolean bool2=VirtualCli.waitForBuildCommand((Game) message.getParameters().get(0), (Pawn) message.getParameters().get(1),(Boolean) message.getParameters().get(2),(Boolean) message.getParameters().get(3) );
        sendResultMessage(bool2);
        break;
      case "getBuildLocationViaArrows":
        int[] cell=VirtualCli.getBuildLocationViaArrows((Game) message.getParameters().get(0), (Pawn) message.getParameters().get(1),(Cell) message.getParameters().get(2) );
        sendResultMessage(cell);
        break;
      case "waitForMoveCommand":
        Boolean bool3=VirtualCli.waitForMoveCommand((Game) message.getParameters().get(0), (Pawn) message.getParameters().get(1),(Boolean) message.getParameters().get(2),(Boolean) message.getParameters().get(3) );
        sendResultMessage(bool3);
        break;
      case "getValidMoveViaArrows":
        int[] move=VirtualCli.getValidMoveViaArrows((Game) message.getParameters().get(0), (Pawn) message.getParameters().get(1), (Cell) message.getParameters().get(2), (Boolean) message.getParameters().get(3));
        sendResultMessage(move);
        break;
      case "askBuildTwice":
        Boolean bool4=VirtualCli.askBuildTwice((Player) message.getParameters().get(0));
        sendResultMessage(bool4);
        break;
      case "endGameGraphics":
        VirtualCli.endGameGraphics((Player) message.getParameters().get(0));
        return true;
      case "removedPlayerGraphics":
        VirtualCli.removedPlayerGraphics((Player) message.getParameters().get(0));
        break;
      case "waitTurnMessage":
        System.out.println("\n"+ message.getParameters().get(1) +message.getParameters().get(0)+ "\u001b[0m "+ "is playing his turn...");
        break;
      case "waitGodsPicking":
        System.out.println(""+ message.getParameters().get(1) + message.getParameters().get(0)+ "\u001b[0m "+ "is selecting the gods...");
        break;
      case "waitOwnGodSelection":
        System.out.println("\n"+ message.getParameters().get(1) + message.getParameters().get(0)+ "\u001b[0m "+ "is selecting his own god...");
        break;
      case "Disconnection":
        System.out.println("\n"+   "\u001b[31m" +"   ## WARNING ## \n"+  "\u001b[0m" );
        System.out.println("Someone left the match, the game is being shutted down");
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
      if (inboundMessage.getTypeOfMessage().equals("Result")){
        object = inboundMessage.getResult();
      }
    } catch (ClassNotFoundException | ClassCastException e) {
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



