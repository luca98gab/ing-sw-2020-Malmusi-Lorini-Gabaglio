package it.polimi.ingsw.PSP32.server;

import it.polimi.ingsw.PSP32.model.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;


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
    while (Server.continueUnwantedClientHandler){
      try {
        Socket client = socket.accept();
        handleClientConnection(client);
      } catch (IOException e) {

      }
    }
    synchronized (Server.lockUnwanted){
      Server.flagForEnded.set(1);
      Server.lockUnwanted.notifyAll();
    }
  }

  /** Method to handle the exceeding clients
   *
   * @param client : Socket the socket between the server and the unwanted client
   * @throws IOException possible connection errors
   */
  private void handleClientConnection(Socket client) throws IOException {

    System.out.println("Refusing connection to " + client.getInetAddress());

    ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());

    String str = "Lobby is full\n";
    Message message = new Message(    null, null, "StringInfoToPrint", str);
    output.reset();
    output.writeObject(message);

    client.close();
  }


}

