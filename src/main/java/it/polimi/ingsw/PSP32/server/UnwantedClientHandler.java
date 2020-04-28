package it.polimi.ingsw.PSP32.server;

import it.polimi.ingsw.PSP32.client.Message;
import it.polimi.ingsw.PSP32.model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;


import static it.polimi.ingsw.PSP32.server.Server.lockNum;
import static it.polimi.ingsw.PSP32.server.Server.lockPlayer;


public class UnwantedClientHandler implements Runnable
{
  private ServerSocket socket;

  UnwantedClientHandler(ServerSocket socket)
  {
    this.socket = socket;
  }


  @Override
  public void run()
  {
    while (true){
      try {
        Socket client = socket.accept();
        handleClientConnection(client);
      } catch (IOException e) {

      }
    }
  }


  private void handleClientConnection(Socket client) throws IOException
  {

    System.out.println("Refusing connection to " + client.getInetAddress());

    ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());

    String str = "Lobby is full\n";
    Message message = new Message(    null, null, "StringInfoToPrint", str);
    output.writeObject(message);

    client.close();
  }


}

