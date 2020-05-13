package it.polimi.ingsw.PSP32.view;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class SwingLayoutTest {


  public static void main(String[] args) {
    JFrame window = new JFrame("Santorini");


    Font minionPro = new Font("Minion Pro", Font.PLAIN, 25);
    Font minionProSmall = new Font("Minion Pro", Font.PLAIN, 20);
    Color darkBrown = new Color(106, 101, 83);
    Color lightBrown = new Color(240, 230, 211);
    Color transparent = new Color(240, 230, 211, 0);


    //JPanel for initial setup

    JLabel serverConnectionPanel = new JLabel();

    ImageIcon background = new ImageIcon("src/resources/Santorini Images/SchermataConnessioneServer/Sfondo+Titolo.png");
    Image img = background.getImage();
    Image newImg = img.getScaledInstance( 1200, 900,  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon backgroundResized = new ImageIcon( newImg );

    serverConnectionPanel.setIcon(backgroundResized);
    serverConnectionPanel.setLayout(null);

    JLabel serverLabel = new JLabel("Server Address:");
    serverLabel.setFont(minionPro);
    serverLabel.setForeground(darkBrown);
    serverLabel.setVerticalAlignment(SwingConstants.CENTER);
    serverLabel.setBounds(350, 370, 200, 30);
    serverConnectionPanel.add(serverLabel);

    JTextField textField = new JTextField();
    textField.setFont(minionProSmall);
    textField.setBounds(520, 370, 300, 30);
    textField.setBackground(lightBrown);
    textField.setForeground(darkBrown);
    textField.setBorder(new BevelBorder(BevelBorder.LOWERED));
    serverConnectionPanel.add(textField);

    JLabel connInfo = new JLabel("connecting...");
    connInfo.setFont(minionProSmall);
    connInfo.setBounds(500, 480, 200, 30);
    connInfo.setHorizontalAlignment(SwingConstants.CENTER);
    connInfo.setForeground(darkBrown);
    connInfo.setFont(minionProSmall);
    connInfo.setVisible(true);
    serverConnectionPanel.add(connInfo);

    JButton playButton = new JButton();
    ImageIcon playImage = new ImageIcon("src/resources/Santorini Images/SchermataConnessioneServer/PlayButton.png");
    Image img1 = playImage.getImage();
    Image newImg1 = img1.getScaledInstance( 300, 900/4,  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon playImageResized = new ImageIcon(newImg1);
    playButton.setIcon(playImageResized);
    playButton.setBounds(450, 600, 300, 900/4);
    playButton.setOpaque(false);
    playButton.setContentAreaFilled(false);
    playButton.setBorderPainted(false);
    playButton.setEnabled(true);
    serverConnectionPanel.add(playButton);


/**



 //JPanel for the board screen
 JPanel panel = new JPanel();

 panel.setLayout(new BorderLayout());

 JMenu menu = new JMenu("top menu");
 JPanel bottomPanel = new JPanel();
 JLabel b3 = new JLabel("Actions");
 JLabel b4 = new JLabel("Your Player");
 JLabel b5 = new JLabel();

 bottomPanel.setLayout(new FlowLayout());

 JButton lev1 = new JButton("A");
 JButton lev2 = new JButton("B");
 JButton lev3 = new JButton("C");
 JButton dome = new JButton("D");


 bottomPanel.add(lev1);
 bottomPanel.add(lev2);
 bottomPanel.add(lev3);
 bottomPanel.add(dome);


 panel.add(menu, BorderLayout.NORTH);
 panel.add(bottomPanel, BorderLayout.SOUTH);
 panel.add(b3, BorderLayout.WEST);
 panel.add(b4, BorderLayout.EAST);
 panel.add(b5, BorderLayout.CENTER);


 ImageIcon boardImage = new ImageIcon("src/resources/Santorini Images/Board/Cliff_v001.png");
 Image img = boardImage.getImage() ;

 Image newImg = img.getScaledInstance( 600, 600,  java.awt.Image.SCALE_SMOOTH ) ;
 ImageIcon boardImageResized = new ImageIcon( newImg );

 b5.setIcon(boardImageResized);

 b5.setHorizontalAlignment(SwingConstants.CENTER);
 b5.setVerticalAlignment(SwingConstants.CENTER);
 b5.setSize(new Dimension(600, 600));

 */




    window.setContentPane(serverConnectionPanel);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setUndecorated(false);
    window.setResizable(false);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    //window.setPreferredSize(new Dimension(1200 , 900));
    window.pack();
    window.setLocation(dim.width/2-window.getSize().width/2, dim.height/2-window.getSize().height/2);
    window.setVisible(true);

  }
}
