package it.polimi.ingsw.PSP32.view;

import it.polimi.ingsw.PSP32.client.Client;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class Gui implements Runnable{

  static JFrame window = new JFrame("Santorini");

  static Font minionPro = new Font("Minion Pro", Font.PLAIN, 25);
  static Font minionProSmall = new Font("Minion Pro", Font.PLAIN, 20);
  static Color darkBrown = new Color(106, 101, 83);
  static Color lightBrown = new Color(240, 230, 211);
  static Color transparent = new Color(240, 230, 211, 0);

  static public void setupWindow() {
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setUndecorated(false);
    window.setResizable(false);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    window.setPreferredSize(new Dimension(1200 , 900));
    window.setLocation(dim.width/2-window.getSize().width/2, dim.height/2-window.getSize().height/2);

  }

  @Override
  public void run() {
    setupWindow();
    PlayerCreationScene playerCreationScene = new PlayerCreationScene();
    playerCreationScene.show();

  }

  public static void main(String[] args )
  {
    Gui gui = new Gui();
    gui.run();
  }
}
