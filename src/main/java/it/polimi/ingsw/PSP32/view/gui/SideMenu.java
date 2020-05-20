package it.polimi.ingsw.PSP32.view.gui;

import it.polimi.ingsw.PSP32.model.*;

import javax.swing.*;

import java.awt.*;

import static it.polimi.ingsw.PSP32.view.gui.GameScene.*;
import static it.polimi.ingsw.PSP32.view.gui.Gui.*;

public class SideMenu extends JPanel {

  int colorIconW = (int)(80*scale);
  int colorIconH = (int)(80*scale);

  int cardIconW = (int)(80*scale);
  int cardIconH = (int)(80*scale);



  public SideMenu() {

    if (game!=null) {

      for (int i = 0; i < game.getPlayerList().size(); i++) {
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new FlowLayout());
        playerPanel.setSize(250, 250);

        Player player = game.getPlayerList().get(i);
        JLabel nameLabel = new JLabel(player.getName());
        JLabel colorLabel = new JLabel();
        JLabel cardLabel = new JLabel();


        ImageIcon thisPawnIcon = null;

        switch (player.getColor()) {
          case "\u001B[31m":
            thisPawnIcon = redPawnIcon[0];
            break;
          case "\u001B[32m":
            thisPawnIcon = greenPawnIcon[0];
            break;
          case "\u001B[34m":
            thisPawnIcon = bluePawnIcon[0];
            break;
        }

        Image img = thisPawnIcon.getImage().getScaledInstance(colorIconW, colorIconH, java.awt.Image.SCALE_SMOOTH);
        ImageIcon myColorIcon = new ImageIcon(img);

        colorLabel.setIcon(myColorIcon);


        ImageIcon[] cardIcons = imagesImport(player.getGod());
        cardLabel.setIcon(cardIcons[0]);
        cardLabel.setBounds((int) (100 * scale), (int) (550 * scale), cardWidth, cardHeight);
        cardLabel.setOpaque(false);
        cardLabel.addMouseListener(new java.awt.event.MouseAdapter() {

          public void mouseEntered(java.awt.event.MouseEvent evt) {
            cardLabel.setIcon(cardIcons[1]);
          }

          public void mouseExited(java.awt.event.MouseEvent evt) {
            cardLabel.setIcon(cardIcons[0]);
          }
        });

        playerPanel.add(nameLabel);
        playerPanel.add(colorLabel);
        playerPanel.add(cardLabel);

        add(playerPanel);

      }
    } else {
      JLabel label = new JLabel("no Players yet");
      this.add(label);
    }
    setOpaque(true);
    setBounds((int) ((1200-250) * scale), (int) (0 * scale), (int) (250 * scale), (int) (500 * scale));
    setBackground(new Color(198, 190, 174));

    setVisible(true);
    layeredPane.add(this, 100);
  }


  private ImageIcon[] imageSetup(String path){
    ImageIcon[] cardIcons = new ImageIcon[2];

    ImageIcon image = new ImageIcon(path);
    Image img1 = image.getImage();
    Image newImg1 = img1.getScaledInstance( cardWidth, cardHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    cardIcons[0] = new ImageIcon(newImg1);

    path=path.replace(".png", "Turned.png");

    image = new ImageIcon(path);
    img1 = image.getImage();
    newImg1 = img1.getScaledInstance( cardWidth, cardHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    cardIcons[1] = new ImageIcon(newImg1);

    return cardIcons;
  }

  private ImageIcon[] imagesImport(God god){

    String prefix = "src/resources/Santorini Images/SchermataSelezioneGod/";
    String suffix = ".png";

    return imageSetup(prefix + god.getName() + suffix);

  }
}
