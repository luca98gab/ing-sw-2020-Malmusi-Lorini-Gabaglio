package it.polimi.ingsw.PSP32.view.gui.components.gameSceneComponents;

import it.polimi.ingsw.PSP32.model.God;
import it.polimi.ingsw.PSP32.model.Player;
import it.polimi.ingsw.PSP32.view.gui.Gui;
import it.polimi.ingsw.PSP32.view.gui.components.generic.Toast;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static it.polimi.ingsw.PSP32.view.gui.scenes.GameScene.*;

public class PlayersPopup{

  int colorIconW = (int)(80*scale);
  int colorIconH = (int)(80*scale);

  static JDialog popup = new JDialog(window, "Players", false);

  public PlayersPopup () {
    if (game!=null) {

      JLabel backgroundLabel = new JLabel();
      backgroundLabel.setSize((int) (game.getPlayerList().size() * 200 * scale), (int) (370 * scale));

      ArrayList<JPanel> playerPanels = new ArrayList<>();

      for (int i = 0; i < game.getPlayerList().size(); i++) {

        JPanel playerPanel = new JPanel();
        playerPanels.add(playerPanel);

        Player player = game.getPlayerList().get(i);
        JLabel nameLabel = new JLabel(player.getName());
        nameLabel.setFont(minionProXSmall);
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

        playerPanel.setVisible(true);
        playerPanel.setOpaque(false);
      }


      for (int i = 0; i < playerPanels.size(); i++) {
        backgroundLabel.add(playerPanels.get(i));
        playerPanels.get(i).setBounds((int) (200 * i * scale), 0, (int) (200 * scale), (int) (390 * scale));

      }

      ImageIcon image = new ImageIcon(getClass().getResource("/Santorini Images/Sfondo.png"));
      Image img1 = image.getImage();
      Image newImg1 = img1.getScaledInstance(backgroundLabel.getWidth(), backgroundLabel.getHeight(), java.awt.Image.SCALE_SMOOTH);
      backgroundLabel.setIcon(new ImageIcon(newImg1));
      backgroundLabel.setOpaque(true);

      popup.add(backgroundLabel);

      popup.setSize(backgroundLabel.getSize());
      popup.setLocationRelativeTo(window);
      popup.setVisible(true);
      popup.setResizable(false);
      popup.setIconImage((new ImageIcon(Gui.class.getResource("/Santorini Images/GameIcon.png")).getImage()));

    } else new Toast("Not Possible Yet", (JLabel) (window.getContentPane()), 2000);

  }


  private ImageIcon[] imageSetup(String path){
    ImageIcon[] cardIcons = new ImageIcon[2];

    ImageIcon image = new ImageIcon(getClass().getResource(path));
    Image img1 = image.getImage();
    Image newImg1 = img1.getScaledInstance( cardWidth, cardHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    cardIcons[0] = new ImageIcon(newImg1);

    path=path.replace(".png", "Turned.png");

    image = new ImageIcon(getClass().getResource(path));
    img1 = image.getImage();
    newImg1 = img1.getScaledInstance( cardWidth, cardHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    cardIcons[1] = new ImageIcon(newImg1);

    return cardIcons;
  }

  private ImageIcon[] imagesImport(God god){

    String prefix = "/Santorini Images/SchermataSelezioneGod/";
    String suffix = ".png";

    return imageSetup(prefix + god.getName() + suffix);

  }

}
