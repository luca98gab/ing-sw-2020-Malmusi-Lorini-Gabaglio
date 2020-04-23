package it.polimi.ingsw.PSP32.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.*;
import it.polimi.ingsw.PSP32.model.*;
import it.polimi.ingsw.PSP32.view.*;

public class ServerAdapter
{
  private Socket server;
  private ObjectOutputStream outputStm;
  private ObjectInputStream inputStm;
  private ExecutorService executionQueue = Executors.newSingleThreadExecutor();


  public ServerAdapter(Socket server) throws IOException
  {
    this.server = server;
    this.outputStm = new ObjectOutputStream(server.getOutputStream());
    this.inputStm = new ObjectInputStream(server.getInputStream());
  }


  public void stop()
  {
    executionQueue.execute(() -> {
      try {
        server.close();
      } catch (IOException e) { }
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

  public void answerToServer() throws ExecutionException, InterruptedException {

    Object incomingObject = executionQueue.submit(() -> inputStm.readObject()).get();
    Message message = (Message)incomingObject;
    switch (message.getMethodName()){
      case "getNumOfPlayers":
        int n = VirtualCli.getNumOfPlayers();
        sendResultMessage(n);
        break;
      case "createPlayer":
        Player p = VirtualCli.createPlayer();
        sendResultMessage(p);
        break;
      case "gameGodsPicking":
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
        Cell cell=VirtualCli.getBuildLocationViaArrows((Game) message.getParameters().get(0), (Pawn) message.getParameters().get(1),(Cell) message.getParameters().get(2) );
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
      case "checkHasWon":
        VirtualCli.endGameGraphics((Player) message.getParameters().get(0));
        break;
    }
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

  public void sendResultMessage(Object object) {
    executionQueue.submit(() -> {
      try {
        outputStm.reset();
        Message message = new Message(null, null, "Result", object);
        outputStm.writeObject(object);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

}



