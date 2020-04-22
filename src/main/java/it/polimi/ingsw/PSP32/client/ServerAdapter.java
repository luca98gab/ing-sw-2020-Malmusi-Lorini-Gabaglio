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
        requestSendObject(n);
        break;
      case "createPlayer":
        Player p = VirtualCli.createPlayer();
        requestSendObject(p);
        break;
      case "gameGodsPicking":
        God[] g = VirtualCli.gameGodsPicking(((ArrayList<Player>) message.getParameters().get(0)), ((God[]) message.getParameters().get(1)));
        requestSendObject(g);
        break;
      case "ownGodSelection":
        God[] god = {VirtualCli.ownGodSelection(((Player) message.getParameters().get(0)), ((ArrayList<God>) message.getParameters().get(1)))};
        requestSendObject(god);
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
        requestSendObject(position);
        break;
      case "printBoardColored":
        VirtualCli.printBoardColored((Game) message.getParameters().get(0));
    }
    message = null;

  }

  public void requestSendObject(Object object)
  {
    executionQueue.submit(() -> {
      try {
        outputStm.writeObject(object);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

}



