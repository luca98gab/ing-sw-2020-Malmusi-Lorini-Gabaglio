package it.polimi.ingsw.PSP32.view.gui.scenes;

import it.polimi.ingsw.PSP32.client.ServerAdapterGui;
import it.polimi.ingsw.PSP32.model.Player;
import it.polimi.ingsw.PSP32.view.gui.Gui;
import it.polimi.ingsw.PSP32.view.gui.components.generic.Hourglass;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.concurrent.CopyOnWriteArrayList;

import static it.polimi.ingsw.PSP32.client.ServerAdapterGui.lockPlayer;

public class PlayerCreationScene2  extends Gui {

  static JLabel playerCreationPanel = new JLabel();
  static JLabel nameLabel = new JLabel("Nickname:");
  static JTextField textField = new JTextField();
  static JButton startButton = new JButton();

  static JLabel nameInfo = new JLabel();



  static ButtonGroup colorGroup = new ButtonGroup();
  static JRadioButton bluePawn = new JRadioButton();
  static JRadioButton redPawn = new JRadioButton();
  static JRadioButton greenPawn = new JRadioButton();
  static ImageIcon bluePawnIcon = null;
  static ImageIcon redPawnIcon = null;
  static ImageIcon greenPawnIcon = null;
  static ImageIcon bluePawnSelIcon = null;
  static ImageIcon redPawnSelIcon = null;
  static ImageIcon greenPawnSelIcon = null;

  private static CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();

  static Player player;

  static Boolean validName = false;

  public static Player getPlayer() {return player;}


  public void show(){
    window.setContentPane(playerCreationPanel);
    window.setVisible(true);
  }


  public PlayerCreationScene2 (CopyOnWriteArrayList<Player> players){

    PlayerCreationScene2.players = players;

    ImageIcon background = new ImageIcon(getClass().getResource("/Santorini Images/SchermataCreazioneGiocatore/Sfondo.png"));
    Image img = background.getImage();
    Image newImg = img.getScaledInstance( (int) (1200*scale), (int) (900*scale),  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon backgroundResized = new ImageIcon( newImg );

    playerCreationPanel.setIcon(backgroundResized);
    playerCreationPanel.setLayout(null);

    nameLabel.setFont(minionPro);
    nameLabel.setForeground(darkBrown);
    nameLabel.setVerticalAlignment(SwingConstants.CENTER);
    nameLabel.setBounds((int)(390*scale), (int)(180*scale), (int)(120*scale), (int)(30*scale));
    playerCreationPanel.add(nameLabel);

    textField.setFont(minionProSmall);
    textField.setBounds((int)(520*scale), (int)(180*scale), (int)(200*scale), (int)(30*scale));
    textField.setBackground(lightBrown);
    textField.setForeground(darkBrown);
    textField.setBorder(new BevelBorder(BevelBorder.LOWERED));
    playerCreationPanel.add(textField);
    textField.addActionListener(textFieldListener);

    colorGroup.add(redPawn);
    colorGroup.add(bluePawn);
    colorGroup.add(greenPawn);


    ImageIcon bluePawnImage = new ImageIcon(getClass().getResource("/Santorini Images/SchermataCreazioneGiocatore/PedinaBlu.png"));
    Image imgb = bluePawnImage.getImage();
    Image newImgb = imgb.getScaledInstance( (int) (140*scale), (int) (210*scale),  java.awt.Image.SCALE_SMOOTH  ) ;
    bluePawnIcon = new ImageIcon(newImgb);

    bluePawnImage = new ImageIcon(getClass().getResource("/Santorini Images/SchermataCreazioneGiocatore/PedinaBluSelezionato.png"));
    imgb = bluePawnImage.getImage();
    newImgb = imgb.getScaledInstance( (int) (140*scale), (int) (210*scale),  java.awt.Image.SCALE_SMOOTH  ) ;
    bluePawnSelIcon = new ImageIcon(newImgb);

    bluePawn.setIcon(bluePawnIcon);
    bluePawn.setBounds((int) (340*scale), (int) (270*scale), (int) (140*scale), (int) (210*scale));
    bluePawn.setContentAreaFilled(false);
    bluePawn.setBorderPainted(false);
    playerCreationPanel.add(bluePawn);
    bluePawn.addActionListener(bluePawnListener);
    bluePawn.addActionListener(textFieldListener);



    ImageIcon redPawnImage = new ImageIcon(getClass().getResource("/Santorini Images/SchermataCreazioneGiocatore/PedinaRossa.png"));
    Image imgr = redPawnImage.getImage();
    Image newImgr = imgr.getScaledInstance( (int) (140*scale), (int) (210*scale),  java.awt.Image.SCALE_SMOOTH ) ;
    redPawnIcon = new ImageIcon(newImgr);

    redPawnImage = new ImageIcon(getClass().getResource("/Santorini Images/SchermataCreazioneGiocatore/PedinaRossaSelezionato.png"));
    imgr = redPawnImage.getImage();
    newImgr = imgr.getScaledInstance( (int) (140*scale), (int) (210*scale),  java.awt.Image.SCALE_SMOOTH ) ;
    redPawnSelIcon = new ImageIcon(newImgr);

    redPawn.setIcon(redPawnIcon);
    redPawn.setBounds((int) (530*scale), (int) (270*scale), (int) (140*scale), (int) (210*scale));
    redPawn.setContentAreaFilled(false);
    redPawn.setBorderPainted(false);
    playerCreationPanel.add(redPawn);
    redPawn.addActionListener(redPawnListener);
    redPawn.addActionListener(textFieldListener);



    ImageIcon greenPawnImage = new ImageIcon(getClass().getResource("/Santorini Images/SchermataCreazioneGiocatore/PedinaVerde.png"));
    Image imgg = greenPawnImage.getImage();
    Image newImgg = imgg.getScaledInstance( (int) (140*scale), (int) (210*scale),  java.awt.Image.SCALE_SMOOTH  ) ;
    greenPawnIcon = new ImageIcon(newImgg);

    greenPawnImage = new ImageIcon(getClass().getResource("/Santorini Images/SchermataCreazioneGiocatore/PedinaVerdeSelezionato.png"));
    imgg = greenPawnImage.getImage();
    newImgg = imgg.getScaledInstance( (int) (140*scale), (int) (210*scale),  java.awt.Image.SCALE_SMOOTH  ) ;
    greenPawnSelIcon = new ImageIcon(newImgg);

    greenPawn.setIcon(greenPawnIcon);
    greenPawn.setBounds((int) (720*scale), (int) (270*scale), (int) (140*scale), (int) (210*scale));
    greenPawn.setContentAreaFilled(false);
    greenPawn.setBorderPainted(false);
    playerCreationPanel.add(greenPawn);
    greenPawn.addActionListener(greenPawnListener);
    greenPawn.addActionListener(textFieldListener);



    ImageIcon playImage = new ImageIcon(getClass().getResource("/Santorini Images/SchermataCreazioneGiocatore/StartButton.png"));
    Image img1 = playImage.getImage();
    Image newImg1 = img1.getScaledInstance( (int) (300*scale), (int) (900/4*scale),  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon playImageResized = new ImageIcon(newImg1);
    startButton.setIcon(playImageResized);
    startButton.setBounds((int) (450*scale), (int) (580*scale), (int) (300*scale), (int) (900/4*scale));
    startButton.setOpaque(false);
    startButton.setContentAreaFilled(false);
    startButton.setBorderPainted(false);
    startButton.setEnabled(false);
    playerCreationPanel.add(startButton);
    startButton.addActionListener(startButtonListener);

    nameInfo.setFont(minionProSmall);
    nameInfo.setBounds((int) (705*scale), (int) (180*scale), (int) (200*scale), (int) (30*scale));
    nameInfo.setHorizontalAlignment(SwingConstants.CENTER);
    nameInfo.setForeground(darkBrown);
    nameInfo.setFont(minionProSmall);
    nameInfo.setVisible(false);
    playerCreationPanel.add(nameInfo);

    for(int i=0; i<players.size(); i++)
    {
      if (players.get(i).getColor().equals("\u001B[31m"))
        redPawn.setEnabled(false);
      if (players.get(i).getColor().equals("\u001B[34m"))
        bluePawn.setEnabled(false);
      if (players.get(i).getColor().equals("\u001B[32m"))
        greenPawn.setEnabled(false);
    }

  }


  static ActionListener textFieldListener = e -> {
    String str = textField.getText();
    Boolean existing = false;
    if (str.matches("[a-zA-Z]+") || str.equals(" ")){
      for(int i=0; i<players.size(); i++){
        if (textField.getText().toUpperCase().equals(players.get(i).getName().toUpperCase())) existing=true;
      }
      if (!existing){
          validName=true;
          nameInfo.setVisible(false);
          checkCanPlay();
      } else existingName();
    } else {
      textField.setText("name not valid");
      textField.selectAll();
      validName=false;
    }
  };

  static ActionListener redPawnListener = e -> {
    redPawn.setIcon(redPawnSelIcon);
    bluePawn.setIcon(bluePawnIcon);
    greenPawn.setIcon(greenPawnIcon);
    checkCanPlay();
  };

  static ActionListener bluePawnListener = e -> {
    redPawn.setIcon(redPawnIcon);
    bluePawn.setIcon(bluePawnSelIcon);
    greenPawn.setIcon(greenPawnIcon);
    checkCanPlay();
  };

  static ActionListener greenPawnListener = e -> {
    redPawn.setIcon(redPawnIcon);
    bluePawn.setIcon(bluePawnIcon);
    greenPawn.setIcon(greenPawnSelIcon);
    checkCanPlay();
  };

  static ActionListener startButtonListener = e -> {
     if (greenPawn.isSelected()) player = new Player(textField.getText(), "\u001B[32m", null);
     else if (redPawn.isSelected()) player = new Player(textField.getText(), "\u001B[31m", null);
     else if (bluePawn.isSelected()) player = new Player(textField.getText(), "\u001B[34m", null);
     synchronized (lockPlayer) {
       ServerAdapterGui.flagForPlayer.set(1);
       lockPlayer.notifyAll();
     }

    JLabel waitLabel = new JLabel("Waiting for Players...");
    waitLabel.setFont(minionProSmall);
    waitLabel.setForeground(darkBrown);
    waitLabel.setHorizontalAlignment(SwingConstants.CENTER);
    waitLabel.setBounds((int)(500*scale), (int)(755*scale), (int)(200*scale), (int)(30*scale));
    playerCreationPanel.add(waitLabel);
    disableAll();

  };

  private static void disableAll(){
    startButton.setEnabled(false);
    Hourglass hGlass = new Hourglass();
    hGlass.setBounds((int)(1050*scale), (int)(750*scale), (int) (96 * scale), (int) (130 * scale));
    hGlass.setVisible(true);
    playerCreationPanel.add(hGlass);

  }

  private static void checkCanPlay(){
    if (colorGroup.getSelection()!=null &&  validName){
      startButton.setEnabled(true);
    }
  }

  public static void existingName(){
    nameInfo.setText("Invalid name");
    nameInfo.setVisible(true);
    textField.selectAll();
    startButton.setEnabled(false);
  }
}
