package it.polimi.ingsw.PSP32.view;

import it.polimi.ingsw.PSP32.client.Client;
import it.polimi.ingsw.PSP32.model.God;
import it.polimi.ingsw.PSP32.model.Player;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Gui implements Runnable {

  static JFrame window = new JFrame("Santorini");

  static Font minionPro = new Font("Minion Pro", Font.PLAIN, 25);
  static Font minionProSmall = new Font("Minion Pro", Font.PLAIN, 20);
  static Font lillyBelle = new Font("LillyBelle", Font.PLAIN, 25);
  static Color darkBrown = new Color(106, 101, 83);
  static Color lightBrown = new Color(240, 230, 211);
  static Color transparent = new Color(240, 230, 211, 0);


  static double scale = 1;

  static public void setupWindow()  {
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setUndecorated(false);
    window.setResizable(false);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    scale = 900.0/dim.height;
    window.getContentPane().setPreferredSize(new Dimension((int)(1200*scale) , (int)(900*scale)));
    minionPro = new Font("Minion Pro", Font.PLAIN, (int)(25*scale));
    minionProSmall = new Font("Minion Pro", Font.PLAIN, (int)(20*scale));
    lillyBelle = new Font("LillyBelle", Font.PLAIN, (int)(25*scale));



    //
    //if (dim.width<1200 || dim.height<900) small=true;
    //if (!small){
    // window.setPreferredSize(new Dimension(1200 , 900));
    //} else {
    //  scale=2/3.0;
    //  window.setPreferredSize(new Dimension(800 , 600));
    //  minionPro = new Font("Minion Pro", Font.PLAIN, 18);
    //  minionProSmall = new Font("Minion Pro", Font.PLAIN, 13);
    //}
    window.pack();
    window.setLocationRelativeTo(null);
  }

  @Override
  public void run() {
    setupWindow();

    Player player = new Player("Gio", "\u001B[31m", new God("Apollo", null));

    GameScene scene = new GameScene(player);
    scene.show();

  }

  public static void main(String[] args )
  {
    Gui gui = new Gui();
    gui.run();
  }


}
