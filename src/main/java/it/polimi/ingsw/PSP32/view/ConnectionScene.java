package it.polimi.ingsw.PSP32.view;

import it.polimi.ingsw.PSP32.client.ClientGui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionScene extends Gui {

  static JLabel serverConnectionPanel = new JLabel();
  static JLabel serverLabel = new JLabel("Server Address:");
  static JTextField textField = new JTextField();
  static JLabel connInfo = new JLabel("connecting...");
  static JButton playButton = new JButton();



  public void show(){
    window.setContentPane(serverConnectionPanel);
    window.pack();
    window.setVisible(true);
  }


  public ConnectionScene(){

    //JPanel for initial setup

    ImageIcon background = new ImageIcon("src/resources/Santorini Images/SchermataConnessioneServer/Sfondo+Titolo.png");
    Image img = background.getImage();
    Image newImg = img.getScaledInstance( (int) (1200*scale), (int) (900*scale),  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon backgroundResized = new ImageIcon( newImg );

    serverConnectionPanel.setIcon(backgroundResized);
    serverConnectionPanel.setLayout(null);


    serverLabel.setFont(minionPro);
    serverLabel.setForeground(darkBrown);
    serverLabel.setVerticalAlignment(SwingConstants.CENTER);
    serverLabel.setBounds((int) (350*scale), (int) (370*scale), (int) (200*scale), (int) (30*scale));
    serverConnectionPanel.add(serverLabel);

    textField.setFont(minionProSmall);
    textField.setBounds((int) (520*scale), (int) (370*scale), (int) (300*scale), (int) (30*scale));
    textField.setBackground(lightBrown);
    textField.setForeground(darkBrown);
    textField.setBorder(new BevelBorder(BevelBorder.LOWERED));
    serverConnectionPanel.add(textField);
    textField.addActionListener(textFieldListener);

    connInfo.setFont(minionProSmall);
    connInfo.setBounds((int) (500*scale), (int) (480*scale), (int) (200*scale), (int) (30*scale));
    connInfo.setHorizontalAlignment(SwingConstants.CENTER);
    connInfo.setForeground(darkBrown);
    connInfo.setFont(minionProSmall);
    connInfo.setVisible(false);
    serverConnectionPanel.add(connInfo);

    ImageIcon playImage = new ImageIcon("src/resources/Santorini Images/SchermataConnessioneServer/PlayButton.png");
    Image img1 = playImage.getImage();
    Image newImg1 = img1.getScaledInstance( (int) (300*scale), (int) (900/4*scale),  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon playImageResized = new ImageIcon(newImg1);
    playButton.setIcon(playImageResized);
    playButton.setBounds((int) (450*scale), (int) (600*scale), (int) (300*scale), (int) (900/4*scale));
    playButton.setOpaque(false);
    playButton.setContentAreaFilled(false);
    playButton.setBorderPainted(false);
    playButton.setEnabled(false);
    serverConnectionPanel.add(playButton);

  }

  public static void connectionRefused(){
    connInfo.setText("Invalid address");
    textField.setText("");
    playButton.setEnabled(false);
  }

  static ActionListener textFieldListener = e -> {
    String address = textField.getText();
    connInfo.setText("Connecting to "+ address);
    connInfo.setVisible(true);
    ClientGui.ip = address;
    playButton.setEnabled(true);
  };
}
