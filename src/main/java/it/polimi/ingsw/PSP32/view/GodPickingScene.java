package it.polimi.ingsw.PSP32.view;

import it.polimi.ingsw.PSP32.client.ClientGui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionListener;

import static it.polimi.ingsw.PSP32.view.Gui.*;

public class GodPickingScene {

  static JLabel godPickingPanel = new JLabel();

  static JButton startButton = new JButton();
  static JButton g1 = new JButton();

  static ImageIcon g1Icon = null;
  static ImageIcon g2Icon = null;
  static ImageIcon g3Icon = null;
  static ImageIcon g4Icon = null;
  static ImageIcon g5Icon = null;
  static ImageIcon g6Icon = null;
  static ImageIcon g7Icon = null;
  static ImageIcon g8Icon = null;
  static ImageIcon g9Icon = null;
  static ImageIcon g1bIcon = null;
  static ImageIcon g2bIcon = null;
  static ImageIcon g3bIcon = null;
  static ImageIcon g4bIcon = null;
  static ImageIcon g5bIcon = null;
  static ImageIcon g6bIcon = null;
  static ImageIcon g7bIcon = null;
  static ImageIcon g8bIcon = null;
  static ImageIcon g9bIcon = null;



  public void show(){
    window.setContentPane(godPickingPanel);
    window.pack();
    window.setVisible(true);
  }

  private void imagesSetup(){
    ImageIcon image = new ImageIcon("src/resources/Santorini Images/SchermataSelezioneGod/Apollo.png");
    Image img1 = image.getImage();
    Image newImg1 = img1.getScaledInstance( (int) (160*scale), (int) (225*scale),  java.awt.Image.SCALE_SMOOTH ) ;
    g1Icon = new ImageIcon(newImg1);

    image = new ImageIcon("src/resources/Santorini Images/SchermataSelezioneGod/ApolloTurned.png");
    img1 = image.getImage();
    newImg1 = img1.getScaledInstance( (int) (160*scale), (int) (225*scale),  java.awt.Image.SCALE_SMOOTH ) ;
    g1bIcon = new ImageIcon(newImg1);
  }

  public GodPickingScene(){

    imagesSetup();

    ImageIcon background = new ImageIcon("src/resources/Santorini Images/SchermataSelezioneGod/Sfondo.png");
    Image img = background.getImage();
    Image newImg = img.getScaledInstance( (int) (1200*scale), (int) (900*scale),  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon backgroundResized = new ImageIcon( newImg );

    godPickingPanel.setIcon(backgroundResized);
    godPickingPanel.setLayout(null);

    g1.setIcon(g1Icon);
    g1.setBounds((int) (100*scale), (int) (130*scale), (int) (160*scale), (int) (225*scale));
    g1.setOpaque(false);
    g1.setContentAreaFilled(false);
    g1.setBorderPainted(false);
    godPickingPanel.add(g1);
    g1.addActionListener(g1buttonListener);



    ImageIcon playImage = new ImageIcon("src/resources/Santorini Images/SchermataCreazioneGiocatore/StartButton.png");
    Image img1 = playImage.getImage();
    Image newImg1 = img1.getScaledInstance( (int) (300*scale), (int) (900/4*scale),  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon playImageResized = new ImageIcon(newImg1);
    startButton.setIcon(playImageResized);
    startButton.setBounds((int) (450*scale), (int) (580*scale), (int) (300*scale), (int) (900/4*scale));
    startButton.setOpaque(false);
    startButton.setContentAreaFilled(false);
    startButton.setBorderPainted(false);
    startButton.setEnabled(false);
    godPickingPanel.add(startButton);
    startButton.addActionListener(startButtonListener);

  }


  static ActionListener startButtonListener = e -> {

  };

  static ActionListener g1buttonListener = e -> {
    if (g1.getIcon()==g1bIcon) g1.setIcon(g1Icon);
    else g1.setIcon(g1bIcon);
  };

}
