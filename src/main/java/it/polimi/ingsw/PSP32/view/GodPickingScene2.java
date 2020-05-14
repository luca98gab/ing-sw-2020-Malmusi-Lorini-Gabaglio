package it.polimi.ingsw.PSP32.view;

import it.polimi.ingsw.PSP32.client.ClientGui;
import it.polimi.ingsw.PSP32.model.God;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static it.polimi.ingsw.PSP32.view.Gui.*;

public class GodPickingScene2 {

  static JLabel godPickingPanel = new JLabel();

  static JButton startButton = new JButton();
  static ArrayList<JButton> cardButtons = new ArrayList<>();

  static ArrayList<ImageIcon> frontIcons = new ArrayList<>();
  static ArrayList<ImageIcon> backIcons = new ArrayList<>();
  static int cardWidth = (int) (170*scale);
  static int cardHeight = (int) (240*scale);
  static ImageIcon emptySpaceIcon;
  static JButton slot = new JButton();


  public void show(){
    window.setContentPane(godPickingPanel);
    window.pack();
    window.setVisible(true);
  }

  public GodPickingScene2(ArrayList<God> availableGods){

    imagesImport(availableGods);

    ImageIcon background = new ImageIcon("src/resources/Santorini Images/SchermataSelezioneGod/Sfondo.png");
    Image img = background.getImage();
    Image newImg = img.getScaledInstance( (int) (1200*scale), (int) (900*scale),  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon backgroundResized = new ImageIcon( newImg );

    godPickingPanel.setIcon(backgroundResized);
    godPickingPanel.setLayout(null);

    for (int i = 0; i < frontIcons.size(); i++){
      cardButtons.add(new JButton());
      JButton card = cardButtons.get(i);
      card.setIcon(frontIcons.get(i));
      if (frontIcons.size()==2){
        card.setBounds((int) ((400 + 200 * i) * scale), (int) (120*scale), cardWidth, cardHeight);
      } else {
        card.setBounds((int) ((300 + 200 * i)*scale), (int) (120*scale), cardWidth, cardHeight);
      }

      card.setOpaque(false);
      card.setContentAreaFilled(false);
      card.setBorderPainted(false);
      godPickingPanel.add(card);
      card.addActionListener(cardButtonListener);
      ImageIcon frontIcon = frontIcons.get(i);
      ImageIcon backIcon = backIcons.get(i);
      card.addMouseListener(new java.awt.event.MouseAdapter() {

        public void mouseEntered(java.awt.event.MouseEvent evt) {
          card.setIcon(backIcon);
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
          card.setIcon(frontIcon);
        }
      });
    }

    ImageIcon image = new ImageIcon("src/resources/Santorini Images/SchermataSelezioneGod/CartaNonSelez.png");
    Image img1 = image.getImage();
    Image newImg1 = img1.getScaledInstance( cardWidth, cardHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    emptySpaceIcon = (new ImageIcon(newImg1));


    slot.setIcon(emptySpaceIcon);
    slot.setBounds((int) (500 * scale), (int) (620 * scale), cardWidth, cardHeight);
    slot.setOpaque(false);
    slot.setContentAreaFilled(false);
    slot.setBorderPainted(false);
    slot.setEnabled(true);
    godPickingPanel.add(slot);
    slot.addActionListener(slotButtonListener);



  }

  static ActionListener cardButtonListener = e -> {
    JButton clickedCard = (JButton) e.getSource();

    int i = cardButtons.indexOf(clickedCard);

    if (i!=-1) {
      ImageIcon frontIcon = frontIcons.get(i);
      slot.setIcon(frontIcon);
    }

  };

  static ActionListener slotButtonListener = e -> {

    slot.setIcon(emptySpaceIcon);

  };


  private void imagesImport(ArrayList<God> availableGods){

    String prefix = "src/resources/Santorini Images/SchermataSelezioneGod/";
    String suffix = ".png";

    for (int i = 0; i < availableGods.size(); i++) {
      imageSetup(prefix + availableGods.get(i).getName() + suffix);
    }

  }

  private void imageSetup(String path){
    ImageIcon image = new ImageIcon(path);
    Image img1 = image.getImage();
    Image newImg1 = img1.getScaledInstance( cardWidth, cardHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    frontIcons.add(new ImageIcon(newImg1));

    path=path.replace(".png", "Turned.png");

    image = new ImageIcon(path);
    img1 = image.getImage();
    newImg1 = img1.getScaledInstance( cardWidth, cardHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    backIcons.add(new ImageIcon(newImg1));
  }



}
