package it.polimi.ingsw.PSP32.client;

public class ClientMain {
  public static void main(String[] args) {
    if (args.length > 0 && args[0].equalsIgnoreCase("CLI"))
      new Client().run();
    else
      new ClientGui().run();
  }
}