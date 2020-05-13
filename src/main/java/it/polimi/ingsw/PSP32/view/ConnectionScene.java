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
    Image newImg = img.getScaledInstance( 1200, 900,  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon backgroundResized = new ImageIcon( newImg );

    serverConnectionPanel.setIcon(backgroundResized);
    serverConnectionPanel.setLayout(null);


    serverLabel.setFont(minionPro);
    serverLabel.setForeground(darkBrown);
    serverLabel.setVerticalAlignment(SwingConstants.CENTER);
    serverLabel.setBounds(350, 370, 200, 30);
    serverConnectionPanel.add(serverLabel);

    textField.setFont(minionProSmall);
    textField.setBounds(520, 370, 300, 30);
    textField.setBackground(lightBrown);
    textField.setForeground(darkBrown);
    textField.setBorder(new BevelBorder(BevelBorder.LOWERED));
    serverConnectionPanel.add(textField);
    textField.addActionListener(textFieldListener);

    connInfo.setFont(minionProSmall);
    connInfo.setBounds(500, 480, 200, 30);
    connInfo.setHorizontalAlignment(SwingConstants.CENTER);
    connInfo.setForeground(darkBrown);
    connInfo.setFont(minionProSmall);
    connInfo.setVisible(false);
    serverConnectionPanel.add(connInfo);

    ImageIcon playImage = new ImageIcon("src/resources/Santorini Images/SchermataConnessioneServer/PlayButton.png");
    Image img1 = playImage.getImage();
    Image newImg1 = img1.getScaledInstance( 300, 900/4,  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon playImageResized = new ImageIcon(newImg1);
    playButton.setIcon(playImageResized);
    playButton.setBounds(450, 600, 300, 900/4);
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
