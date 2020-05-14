package it.polimi.ingsw.PSP32.view;

import it.polimi.ingsw.PSP32.client.ClientGui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static it.polimi.ingsw.PSP32.view.Gui.*;

public class GodPickingScene {

  static JLabel godPickingPanel = new JLabel();

  static JButton startButton = new JButton();
  static ArrayList<JButton> cardButtons = new ArrayList<>();

  static ArrayList<ImageIcon> frontIcons = new ArrayList<>();
  static ArrayList<ImageIcon> backIcons = new ArrayList<>();
  static int cardWidth = (int) (170*scale);
  static int cardHeight = (int) (240*scale);
  static ImageIcon emptySpaceIcon;
  static ArrayList<JButton> selectedCards = new ArrayList<>();

  static int playerNum = 3;

  static int selectedNum = 0;


  public void show(){
    window.setContentPane(godPickingPanel);
    window.pack();
    window.setVisible(true);
  }

  public GodPickingScene(){

    imagesImport();

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
      if (i<5){
        card.setBounds((int) ((100+200*i)*scale), (int) (120*scale), cardWidth, cardHeight);
      } else {
        card.setBounds((int) ((200+200*(i-5))*scale), (int) (350*scale), cardWidth, cardHeight);
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

    for (int i = 0; i < playerNum; i++){
      selectedCards.add(new JButton());
      JButton slot = selectedCards.get(i);
      slot.setIcon(emptySpaceIcon);
      if (playerNum==2) {
        slot.setBounds((int) ((400 + 200 * i) * scale), (int) (620 * scale), cardWidth, cardHeight);
      } else slot.setBounds((int) ((300 + 200 * i) * scale), (int) (620 * scale), cardWidth, cardHeight);
      slot.setOpaque(false);
      slot.setContentAreaFilled(false);
      slot.setBorderPainted(false);
      slot.setEnabled(true);
      godPickingPanel.add(slot);
      slot.addActionListener(slotButtonListener);
    }


  }

  static ActionListener cardButtonListener = e -> {
    JButton clickedCard = (JButton) e.getSource();

    int i = cardButtons.indexOf(clickedCard);

    if (i!=-1) {
      ImageIcon frontIcon = frontIcons.get(i);

      Boolean alreadySelected = false;
      int firstFreePos=0;

      for (int j = playerNum-1; j >= 0; j--){
        if (selectedCards.get(j).getIcon().equals(frontIcon)) alreadySelected=true;
        if (selectedCards.get(j).getIcon().equals(emptySpaceIcon)) firstFreePos = j;
      }

      if (selectedNum < playerNum && !alreadySelected) {

        selectedCards.get(firstFreePos).setIcon(frontIcon);
        selectedNum++;
      }
    }
  };

  static ActionListener slotButtonListener = e -> {
    JButton clickedCard = (JButton) e.getSource();

    int i = selectedCards.indexOf(clickedCard);

    if (i!=-1) {
      selectedCards.get(i).setIcon(emptySpaceIcon);
      selectedNum--;
    }
  };


  private void imagesImport(){

    imageSetup("src/resources/Santorini Images/SchermataSelezioneGod/Apollo.png");

    imageSetup("src/resources/Santorini Images/SchermataSelezioneGod/Artemis.png");

    imageSetup("src/resources/Santorini Images/SchermataSelezioneGod/Athena.png");

    imageSetup("src/resources/Santorini Images/SchermataSelezioneGod/Atlas.png");

    imageSetup("src/resources/Santorini Images/SchermataSelezioneGod/Demeter.png");

    imageSetup("src/resources/Santorini Images/SchermataSelezioneGod/Hepheastus.png");

    imageSetup("src/resources/Santorini Images/SchermataSelezioneGod/Minotaur.png");

    imageSetup("src/resources/Santorini Images/SchermataSelezioneGod/Pan.png");

    imageSetup("src/resources/Santorini Images/SchermataSelezioneGod/Prometheus.png");
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
