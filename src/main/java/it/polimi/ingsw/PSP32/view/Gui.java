package it.polimi.ingsw.PSP32.view;

import it.polimi.ingsw.PSP32.client.Client;
import it.polimi.ingsw.PSP32.model.God;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.ArrayList;

public class Gui implements Runnable {

  static JFrame window = new JFrame("Santorini");

  static Font minionPro = new Font("Minion Pro", Font.PLAIN, 25);
  static Font minionProSmall = new Font("Minion Pro", Font.PLAIN, 20);
  static Color darkBrown = new Color(106, 101, 83);
  static Color lightBrown = new Color(240, 230, 211);
  static Color transparent = new Color(240, 230, 211, 0);

  static Boolean small = false;

  static double scale = 1;

  static public void setupWindow() {
    java.net.URL url = ClassLoader.getSystemResource("src/resources/Santorini Images/GameIcon.png");
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setUndecorated(false);
    window.setResizable(false);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    if (dim.width<1200 || dim.height<900) small=true;
    if (!small){
      window.setPreferredSize(new Dimension(1200 , 900));
    } else {
      scale=2/3.0;
      window.setPreferredSize(new Dimension(800 , 600));
      minionPro = new Font("Minion Pro", Font.PLAIN, 18);
      minionProSmall = new Font("Minion Pro", Font.PLAIN, 13);
    }
    window.pack();
    window.setLocationRelativeTo(null);
  }

  @Override
  public void run() {
    setupWindow();

    ArrayList<God> availableGods = new ArrayList<>();
    availableGods.add(new God("Apollo", null));
    availableGods.add(new God("Artemis", null));
    availableGods.add(new God("Pan", null));

    GodPickingScene2 scene = new GodPickingScene2(availableGods);
    scene.show();

  }

  public static void main(String[] args )
  {
    Gui gui = new Gui();
    gui.run();
  }


}
