
package it.polimi.ingsw.PSP32.view.gui.components.gameSceneComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import it.polimi.ingsw.PSP32.view.gui.scenes.GameScene;

import static it.polimi.ingsw.PSP32.view.gui.Gui.*;

public class AboutPage {

  static JDialog popup = new JDialog(window, "dialog Box", true);

  public AboutPage () {

    JLayeredPane subBackgroundLabel = new JLayeredPane();
    JLabel backgroundLabel = new JLabel();
    JLabel iconLabel = new JLabel();
    JButton b = new JButton();

    ImageIcon background = new ImageIcon(getClass().getResource("/Santorini Images/GameIcon.png"));
    Image img = background.getImage();
    Image newImg = img.getScaledInstance( (int)(250*scale), (int)(250*scale),  java.awt.Image.SCALE_SMOOTH );
    ImageIcon backgroundIcon = new ImageIcon( newImg );

    background = new ImageIcon(getClass().getResource("/Santorini Images/escButton.png"));
    img = background.getImage();
    newImg = img.getScaledInstance( (int)(40*scale), (int)(40*scale),  java.awt.Image.SCALE_SMOOTH );
    ImageIcon escIcon = new ImageIcon( newImg );


    backgroundLabel.setText("<html><br/><br/>Developed by:<br/><br/>-Giorgio Lorini<br/>-Davide Malmusi<br/>-Luca Gabaglio<br/>" +
            "<br/>as a project for PoliMi.<br/>Based on SANTORINI by Roxley<html/>");
    backgroundLabel.setFont(minionProXSmall);
    backgroundLabel.setForeground(Color.white);
    backgroundLabel.setVerticalAlignment(SwingConstants.TOP);
    backgroundLabel.setHorizontalAlignment(SwingConstants.CENTER);
    backgroundLabel.setVisible(true);
    backgroundLabel.setOpaque(true);
    backgroundLabel.setBackground(new Color(40, 100, 150));
    subBackgroundLabel.add(b);
    backgroundLabel.setBounds(0,0, (int)(400*scale), (int)(400*scale));
    subBackgroundLabel.add(backgroundLabel, 2);

    iconLabel.setIcon(backgroundIcon);
    iconLabel.setVisible(true);
    iconLabel.setOpaque(false);
    iconLabel.setBounds((int)(75*scale),(int)(200*scale), backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight());
    subBackgroundLabel.add(iconLabel, 1);


    subBackgroundLabel.setVisible(true);
    subBackgroundLabel.setOpaque(false);
    subBackgroundLabel.setBounds(0,0, popup.getWidth(), (int)(600*scale));
    popup.add(subBackgroundLabel);


    b.setVisible(true);
    b.setIcon(escIcon);
    b.setBounds((int)(360*scale), (int)(0*scale), escIcon.getIconWidth(), escIcon.getIconHeight());
    b.setOpaque(false);
    b.setContentAreaFilled(false);
    b.setBorderPainted(false);
    b.addActionListener(escListener);


    popup.setUndecorated(true); //this has to be before .setBackground !!!!!!!
    popup.setBackground(new Color(40, 100, 150, 0));
    popup.setSize((int)(400*scale), (int)(500*scale));
    popup.setLocationRelativeTo(window);
    popup.setVisible(true);

  }

  static ActionListener escListener = e -> {
    popup.setVisible(false);
    popup.dispose();
  };

}
