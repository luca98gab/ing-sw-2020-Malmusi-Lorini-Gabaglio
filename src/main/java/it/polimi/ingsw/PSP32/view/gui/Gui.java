package it.polimi.ingsw.PSP32.view.gui;

import it.polimi.ingsw.PSP32.model.God;
import it.polimi.ingsw.PSP32.model.Player;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Gui implements Runnable {

  static JFrame window = new JFrame("Santorini");

  static Font minionPro = new Font("a", Font.PLAIN, 25);
  static Font minionProSmall = new Font("a", Font.PLAIN, 20);
  static Font minionProXSmall = new Font("a", Font.PLAIN, 20);
  static Font lillyBelle = new Font("LillyBelle", Font.PLAIN, 25);
  static Color darkBrown = new Color(106, 101, 83);
  static Color lightBrown = new Color(240, 230, 211);
  static Color transparent = new Color(240, 230, 211, 0);


  static double scale = 1;

  static public void setupWindow() throws IOException, FontFormatException {
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setUndecorated(false);
    window.setResizable(false);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    addFont();

    //scale = dim.height/1200.0;
    window.getContentPane().setPreferredSize(new Dimension((int)(1200*scale) , (int)(900*scale)));
    minionPro = new Font("a", Font.PLAIN, (int)(23*scale));
    minionProSmall = new Font("a", Font.PLAIN, (int)(18*scale));
    minionProXSmall = new Font("a", Font.PLAIN, (int)(14*scale));
    lillyBelle = new Font("LillyBelle", Font.PLAIN, (int)(25*scale));


    window.pack();
    window.setLocationRelativeTo(null);
  }

  @Override
  public void run() {
    try {
      setupWindow();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (FontFormatException e) {
      e.printStackTrace();
    }

    ArrayList<Player> players = new ArrayList<>();
    players.add( new Player("Gio", "\u001B[31m", new God("Apollo", null)));

    GameScene scene = new GameScene(players.get(0));
    scene.show();


  }

  public static void main(String[] args )
  {
    Gui gui = new Gui();
    gui.run();
  }


  private static void addFont() throws IOException, FontFormatException {

    Font f = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/Santorini Images/LillyBelle.ttf"));

    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    ge.registerFont(f);

  }


}
