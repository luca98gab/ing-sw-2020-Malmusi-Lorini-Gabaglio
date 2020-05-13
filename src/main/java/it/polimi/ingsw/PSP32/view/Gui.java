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

  static Boolean small = false;

  static double scale = 1;

  static public void setupWindow() {
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setUndecorated(false);
    window.setResizable(false);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    if (dim.width<1200 || dim.height<900) small=true;
    if (!small){
      window.setPreferredSize(new Dimension(1200 , 900));
      window.setLocation(dim.width/2-window.getSize().width/2, dim.height/2-window.getSize().height/2);
    } else {
      scale=2/3.0;
      window.setPreferredSize(new Dimension(800 , 600));
      window.setLocation(dim.width/2-window.getSize().width/2, dim.height/2-window.getSize().height/2);
      minionPro = new Font("Minion Pro", Font.PLAIN, 18);
      minionProSmall = new Font("Minion Pro", Font.PLAIN, 13);
    }
  }

  @Override
  public void run() {
    setupWindow();
    GodPickingScene scene = new GodPickingScene();
    scene.show();

  }

  public static void main(String[] args )
  {
    Gui gui = new Gui();
    gui.run();
  }
}
