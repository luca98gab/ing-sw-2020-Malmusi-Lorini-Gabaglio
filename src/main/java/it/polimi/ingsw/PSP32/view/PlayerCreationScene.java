package it.polimi.ingsw.PSP32.view;

import it.polimi.ingsw.PSP32.client.ClientGui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionListener;

import static it.polimi.ingsw.PSP32.view.Gui.*;

public class PlayerCreationScene {

  static JLabel playerCreationPanel = new JLabel();
  static JLabel nameLabel = new JLabel("Nickname:");
  static JTextField textField = new JTextField();
  static JButton startButton = new JButton();

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

  static ButtonGroup numGroup = new ButtonGroup();
  static JRadioButton twoPlayers = new JRadioButton();
  static JRadioButton threePlayers = new JRadioButton();
  static ImageIcon twoPlayersIcon = null;
  static ImageIcon threePlayersIcon = null;
  static ImageIcon twoPlayersSelIcon = null;
  static ImageIcon threePlayersSelIcon = null;

  static int playerNum = 0;
  static String name = null;


  public void show(){
    window.setContentPane(playerCreationPanel);
    window.pack();
    window.setVisible(true);
  }

  public static int getPlayerNum() {
    return playerNum;
  }

  public PlayerCreationScene(){

    ImageIcon background = new ImageIcon("src/resources/Santorini Images/SchermataCreazioneGiocatore/Sfondo.png");
    Image img = background.getImage();
    Image newImg = img.getScaledInstance( 1200, 900,  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon backgroundResized = new ImageIcon( newImg );

    playerCreationPanel.setIcon(backgroundResized);
    playerCreationPanel.setLayout(null);

    nameLabel.setFont(minionPro);
    nameLabel.setForeground(darkBrown);
    nameLabel.setVerticalAlignment(SwingConstants.CENTER);
    nameLabel.setBounds(350, 180, 200, 30);
    playerCreationPanel.add(nameLabel);

    textField.setFont(minionProSmall);
    textField.setBounds(520, 180, 300, 30);
    textField.setBackground(lightBrown);
    textField.setForeground(darkBrown);
    textField.setBorder(new BevelBorder(BevelBorder.LOWERED));
    playerCreationPanel.add(textField);
    textField.addActionListener(textFieldListener);

    colorGroup.add(redPawn);
    colorGroup.add(bluePawn);
    colorGroup.add(greenPawn);


    ImageIcon bluePawnImage = new ImageIcon("src/resources/Santorini Images/SchermataCreazioneGiocatore/PedinaBlu.png");
    Image imgb = bluePawnImage.getImage();
    Image newImgb = imgb.getScaledInstance( 140, 210,  java.awt.Image.SCALE_SMOOTH  ) ;
    bluePawnIcon = new ImageIcon(newImgb);

    bluePawnImage = new ImageIcon("src/resources/Santorini Images/SchermataCreazioneGiocatore/PedinaBluSelezionato.png");
    imgb = bluePawnImage.getImage();
    newImgb = imgb.getScaledInstance( 140, 210,  java.awt.Image.SCALE_SMOOTH  ) ;
    bluePawnSelIcon = new ImageIcon(newImgb);

    bluePawn.setIcon(bluePawnIcon);
    bluePawn.setBounds(340, 260, 140, 210);
    bluePawn.setContentAreaFilled(false);
    bluePawn.setBorderPainted(false);
    playerCreationPanel.add(bluePawn);
    bluePawn.addActionListener(bluePawnListener);



    ImageIcon redPawnImage = new ImageIcon("src/resources/Santorini Images/SchermataCreazioneGiocatore/PedinaRossa.png");
    Image imgr = redPawnImage.getImage();
    Image newImgr = imgr.getScaledInstance( 140, 210,  java.awt.Image.SCALE_SMOOTH ) ;
    redPawnIcon = new ImageIcon(newImgr);

    redPawnImage = new ImageIcon("src/resources/Santorini Images/SchermataCreazioneGiocatore/PedinaRossaSelezionato.png");
    imgr = redPawnImage.getImage();
    newImgr = imgr.getScaledInstance( 140, 210,  java.awt.Image.SCALE_SMOOTH ) ;
    redPawnSelIcon = new ImageIcon(newImgr);

    redPawn.setIcon(redPawnIcon);
    redPawn.setBounds(530, 260, 140, 210);
    redPawn.setContentAreaFilled(false);
    redPawn.setBorderPainted(false);
    playerCreationPanel.add(redPawn);
    redPawn.addActionListener(redPawnListener);



    ImageIcon greenPawnImage = new ImageIcon("src/resources/Santorini Images/SchermataCreazioneGiocatore/PedinaVerde.png");
    Image imgg = greenPawnImage.getImage();
    Image newImgg = imgg.getScaledInstance( 140, 210,  java.awt.Image.SCALE_SMOOTH  ) ;
    greenPawnIcon = new ImageIcon(newImgg);

    greenPawnImage = new ImageIcon("src/resources/Santorini Images/SchermataCreazioneGiocatore/PedinaVerdeSelezionato.png");
    imgg = greenPawnImage.getImage();
    newImgg = imgg.getScaledInstance( 140, 210,  java.awt.Image.SCALE_SMOOTH  ) ;
    greenPawnSelIcon = new ImageIcon(newImgg);

    greenPawn.setIcon(greenPawnIcon);
    greenPawn.setBounds(720, 260, 140, 210);
    greenPawn.setContentAreaFilled(false);
    greenPawn.setBorderPainted(false);
    playerCreationPanel.add(greenPawn);
    greenPawn.addActionListener(greenPawnListener);




    numGroup.add(twoPlayers);
    numGroup.add(threePlayers);

    ImageIcon twoPawnImage = new ImageIcon("src/resources/Santorini Images/SchermataCreazioneGiocatore/2Players.png");
    img = twoPawnImage.getImage();
    newImg = img.getScaledInstance( 240, 180,  java.awt.Image.SCALE_SMOOTH  ) ;
    twoPlayersIcon = new ImageIcon(newImg);

    twoPawnImage = new ImageIcon("src/resources/Santorini Images/SchermataCreazioneGiocatore/2PlayersSelezionato.png");
    img = twoPawnImage.getImage();
    newImg = img.getScaledInstance( 240, 180,  java.awt.Image.SCALE_SMOOTH  ) ;
    twoPlayersSelIcon = new ImageIcon(newImg);

    twoPlayers.setIcon(twoPlayersIcon);
    twoPlayers.setBounds(350, 500, 240, 180);
    twoPlayers.setContentAreaFilled(false);
    twoPlayers.setBorderPainted(false);
    playerCreationPanel.add(twoPlayers);
    twoPlayers.addActionListener(twoPawnListener);



    ImageIcon threePawnImage = new ImageIcon("src/resources/Santorini Images/SchermataCreazioneGiocatore/3Players.png");
    img = threePawnImage.getImage();
    newImg = img.getScaledInstance( 240, 180,  java.awt.Image.SCALE_SMOOTH  ) ;
    threePlayersIcon = new ImageIcon(newImg);

    threePawnImage = new ImageIcon("src/resources/Santorini Images/SchermataCreazioneGiocatore/3PlayersSelezionato.png");
    img = threePawnImage.getImage();
    newImg = img.getScaledInstance( 240, 180,  java.awt.Image.SCALE_SMOOTH  ) ;
    threePlayersSelIcon = new ImageIcon(newImg);

    threePlayers.setIcon(threePlayersIcon);
    threePlayers.setBounds(600, 500, 240, 180);
    threePlayers.setContentAreaFilled(false);
    threePlayers.setBorderPainted(false);
    playerCreationPanel.add(threePlayers);
    threePlayers.addActionListener(threePawnListener);





    ImageIcon playImage = new ImageIcon("src/resources/Santorini Images/SchermataCreazioneGiocatore/StartButton.png");
    Image img1 = playImage.getImage();
    Image newImg1 = img1.getScaledInstance( 300, 900/4,  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon playImageResized = new ImageIcon(newImg1);
    startButton.setIcon(playImageResized);
    startButton.setBounds(450, 650, 300, 900/4);
    startButton.setOpaque(false);
    startButton.setContentAreaFilled(false);
    startButton.setBorderPainted(false);
    startButton.setEnabled(false);
    playerCreationPanel.add(startButton);
    startButton.addActionListener(startButtonListener);

  }


  static ActionListener textFieldListener = e -> {
    checkCanPlay();
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

  static ActionListener twoPawnListener = e -> {
    twoPlayers.setIcon(twoPlayersSelIcon);
    threePlayers.setIcon(threePlayersIcon);
    checkCanPlay();
  };

  static ActionListener threePawnListener = e -> {
    twoPlayers.setIcon(twoPlayersIcon);
    threePlayers.setIcon(threePlayersSelIcon);
    checkCanPlay();
  };

  static ActionListener startButtonListener = e -> {
    if (numGroup.getSelection().equals(twoPlayers)) playerNum=2;
    else playerNum=3;

    name=textField.getName();

  };

  private static void checkCanPlay(){
    if (colorGroup.getSelection()!=null && numGroup.getSelection()!=null && textField.getText()!=null && !textField.getText().equals("")){
      startButton.setEnabled(true);
    }
  }
}
