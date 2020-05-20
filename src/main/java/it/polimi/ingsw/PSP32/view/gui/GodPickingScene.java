package it.polimi.ingsw.PSP32.view.gui;

import it.polimi.ingsw.PSP32.client.ServerAdapterGui;
import it.polimi.ingsw.PSP32.model.God;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static it.polimi.ingsw.PSP32.client.ServerAdapterGui.lockGods;
import static it.polimi.ingsw.PSP32.view.gui.Gui.*;

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
  static JButton playButton = new JButton();

  static JLabel waitLabel = new JLabel("Other players are choosing their card...");



  static int playerNum ;

  static ArrayList<God> gods = new ArrayList<>();
  static God[] allGods = new God[9];



  static int selectedNum = 0;


  public void show(){
    window.setContentPane(godPickingPanel);
    window.pack();
    window.setVisible(true);
  }

  public GodPickingScene(int playerNum, God[] allGods){


    this.playerNum = playerNum;
    this.allGods = allGods;

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

    ImageIcon playImage = new ImageIcon("src/resources/Santorini Images/SchermataConnessioneServer/PlayButton.png");
    Image img2 = playImage.getImage();
    Image newImg2 = img2.getScaledInstance( (int) (300*scale), (int) (900/4*scale),  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon playImageResized = new ImageIcon(newImg2);
    playButton.setIcon(playImageResized);
    playButton.setBounds((int) (865*scale), (int) (620*scale), (int) (300*scale), (int) (900/4*scale));
    playButton.setOpaque(false);
    playButton.setContentAreaFilled(false);
    playButton.setBorderPainted(false);
    playButton.setEnabled(false);
    godPickingPanel.add(playButton);
    playButton.addActionListener(playButtonLister);


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
        if (selectedNum==playerNum) playButton.setEnabled(true);
      }
    }
  };

  static ActionListener slotButtonListener = e -> {
    JButton clickedCard = (JButton) e.getSource();

    int i = selectedCards.indexOf(clickedCard);

    if (i!=-1) {
      selectedCards.get(i).setIcon(emptySpaceIcon);
      selectedNum--;
      playButton.setEnabled(false);
    }
  };

  static ActionListener playButtonLister = e ->{

    waitLabel.setBounds((int) (400*scale), (int) (855*scale), (int) (400*scale), (int) (20*scale));
    waitLabel.setFont(minionProXSmall);
    waitLabel.setHorizontalAlignment(SwingConstants.CENTER);
    waitLabel.setForeground(lightBrown);
    godPickingPanel.add(waitLabel);
    playButton.setEnabled(false);
    waitLabel.setVisible(true);
    Hourglass hGlass = new Hourglass();
    hGlass.setBounds((int)(50*scale), (int)(750*scale), (int) (96 * scale), (int) (130 * scale));
    hGlass.setVisible(true);
    godPickingPanel.add(hGlass);
    godPickingPanel.revalidate();




      for (int i=0; i<playerNum; i++){
            if (selectedCards.get(i).getIcon().equals(cardButtons.get(0).getIcon())) gods.add(allGods[0]);
            else if (selectedCards.get(i).getIcon().equals(cardButtons.get(1).getIcon())) gods.add(allGods[1]);
            else if (selectedCards.get(i).getIcon().equals(cardButtons.get(2).getIcon())) gods.add(allGods[2]);
            else if (selectedCards.get(i).getIcon().equals(cardButtons.get(3).getIcon())) gods.add(allGods[3]);
            else if (selectedCards.get(i).getIcon().equals(cardButtons.get(4).getIcon())) gods.add(allGods[4]);
            else if (selectedCards.get(i).getIcon().equals(cardButtons.get(5).getIcon())) gods.add(allGods[5]);
            else if (selectedCards.get(i).getIcon().equals(cardButtons.get(6).getIcon())) gods.add(allGods[6]);
            else if (selectedCards.get(i).getIcon().equals(cardButtons.get(7).getIcon())) gods.add(allGods[7]);
            else if (selectedCards.get(i).getIcon().equals(cardButtons.get(8).getIcon())) gods.add(allGods[8]);
          }
    synchronized (lockGods) {
      ServerAdapterGui.flagForGods.set(1);
      lockGods.notifyAll();
    }



  };

  public static God[] getSelectedGods(){
    return gods.toArray(new God[0]); }

  private void imagesImport(){

    imageSetup("src/resources/Santorini Images/SchermataSelezioneGod/Apollo.png");

    imageSetup("src/resources/Santorini Images/SchermataSelezioneGod/Artemis.png");

    imageSetup("src/resources/Santorini Images/SchermataSelezioneGod/Athena.png");

    imageSetup("src/resources/Santorini Images/SchermataSelezioneGod/Atlas.png");

    imageSetup("src/resources/Santorini Images/SchermataSelezioneGod/Demeter.png");

    imageSetup("src/resources/Santorini Images/SchermataSelezioneGod/Hephaestus.png");

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
