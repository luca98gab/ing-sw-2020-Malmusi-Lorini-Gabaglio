package it.polimi.ingsw.PSP32.view;

import it.polimi.ingsw.PSP32.model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

import static it.polimi.ingsw.PSP32.view.Gui.*;

public class PopupWin{



  public PopupWin (JFrame window, Player winner) {

    JDialog popup = new JDialog(window, "dialog Box", true);

    JLabel backgroundLabel = new JLabel();
    JButton b = new JButton();
    JLabel winnerLabel = new JLabel();

    ImageIcon background = new ImageIcon("src/resources/Santorini Images/PopupBackground.png");
    Image img = background.getImage();
    Image newImg = img.getScaledInstance( (int)(750*scale), (int)(285*scale),  java.awt.Image.SCALE_SMOOTH );
    ImageIcon backgroundIcon = new ImageIcon( newImg );

    background = new ImageIcon("src/resources/Santorini Images/escButton.png");
    img = background.getImage();
    newImg = img.getScaledInstance( (int)(40*scale), (int)(40*scale),  java.awt.Image.SCALE_SMOOTH );
    ImageIcon escIcon = new ImageIcon( newImg );


    backgroundLabel.setIcon(backgroundIcon);
    backgroundLabel.setVisible(true);
    backgroundLabel.setOpaque(false);
    backgroundLabel.add(b);
    backgroundLabel.setBounds(0,0, popup.getWidth(), popup.getHeight());
    popup.add(backgroundLabel);

    b.setVisible(true);
    b.setIcon(escIcon);
    b.setBounds((int)(660*scale), (int)(0*scale), escIcon.getIconWidth(), escIcon.getIconHeight());
    b.addActionListener(escListener);

    winnerLabel.setFont(lillyBelle);
    winnerLabel.setForeground(new Color(247, 206, 135));
    winnerLabel.setText(winner.getName().toUpperCase() + " is the winner");
    winnerLabel.setBounds((int)(182*scale), (int)(206*scale), (int)(250*scale), (int)(30*scale));
    winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);

    winnerLabel.setVisible(true);
    backgroundLabel.add(winnerLabel);

    popup.setUndecorated(true); //this has to be before .setBackground !!!!!!!
    popup.setBackground(new Color(0, 0, 0, 0));
    popup.setSize(backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight());
    popup.setLocationRelativeTo(null);
    popup.setVisible(true);

  }

  static ActionListener escListener = e -> {
    window.setVisible(false);
    window.dispose();
    System.exit(0);
  };

}
