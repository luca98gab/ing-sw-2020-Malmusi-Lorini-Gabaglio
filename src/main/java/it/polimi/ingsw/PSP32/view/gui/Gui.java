package it.polimi.ingsw.PSP32.view.gui;

import it.polimi.ingsw.PSP32.controller.GameSetup;
import it.polimi.ingsw.PSP32.model.God;
import it.polimi.ingsw.PSP32.model.Player;
import it.polimi.ingsw.PSP32.view.gui.scenes.GameScene;
import it.polimi.ingsw.PSP32.view.gui.scenes.GodPickingScene;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


public class Gui implements Runnable {

  public static JFrame window = new JFrame("Santorini");

  public static Font minionPro;
  public static Font minionProSmall;
  public static Font minionProXSmall;
  public static Font lillyBelle;
  public static Color darkBrown = new Color(106, 101, 83);
  public static Color lightBrown = new Color(240, 230, 211);
  public static Color transparent = new Color(240, 230, 211, 0);


  public static double scale = 1;


  static public void setupWindow() throws IOException, FontFormatException {
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setUndecorated(false);
    window.setResizable(false);
    window.setIconImage((new ImageIcon(Gui.class.getResource("/Santorini Images/GameIcon.png")).getImage()));
    Taskbar taskbar=Taskbar.getTaskbar();
    taskbar.setIconImage((new ImageIcon(Gui.class.getResource("/Santorini Images/GameIcon.png")).getImage()));

    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    addFont();

    scale = dim.height/1200.0;
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



    GodPickingScene scene = new GodPickingScene(2, null);
    scene.show();


  }

  public static void main(String[] args )
  {
    Gui gui = new Gui();
    gui.run();
  }


  private static void addFont(){

    Font f;
    try {
      InputStream is = Gui.class.getResourceAsStream("/Santorini Images/LillyBelle.ttf");
      f = Font.createFont(Font.TRUETYPE_FONT, is);
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(f);
    } catch (FontFormatException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }


}
