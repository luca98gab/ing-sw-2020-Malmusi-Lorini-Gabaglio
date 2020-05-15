package it.polimi.ingsw.PSP32.view;

import it.polimi.ingsw.PSP32.client.ClientGui;
import it.polimi.ingsw.PSP32.client.ServerAdapterGui;
import it.polimi.ingsw.PSP32.model.God;
import it.polimi.ingsw.PSP32.model.Player;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.xml.xpath.XPath;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static it.polimi.ingsw.PSP32.client.ServerAdapterGui.lockNum;
import static it.polimi.ingsw.PSP32.client.ServerAdapterGui.lockPlayer;
import static it.polimi.ingsw.PSP32.view.Gui.*;

public class GameScene {

  static JLabel gamePanel = new JLabel();

  static JButton myCard = new JButton();
  static ImageIcon myCardIconFront;
  static ImageIcon myCardIconBack;
  static JLabel phaseLabel;

  static ArrayList<JButton> cells = new ArrayList<>();


  static int cardWidth = (int) (170*scale);
  static int cardHeight = (int) (240*scale);

  static int cellWidth = (int)(135*scale);
  static int cellHeight = (int)(135*scale);


  static String phase = "Initial Positioning";

  static Player myPlayer;

  static ArrayList<Integer> selectedCells = new ArrayList<>();

  static ImageIcon[] redPawnIcon = new ImageIcon[2];
  static ImageIcon[] bluePawnIcon = new ImageIcon[2];
  static ImageIcon[] greenPawnIcon = new ImageIcon[2];
  static ImageIcon[] myPawnIcon = new ImageIcon[2];


  public void show(){
    window.setContentPane(gamePanel);
    window.setVisible(true);
  }


  public GameScene(Player player){

    initialPosGraphics();

    myPlayer = player;

    ImageIcon background = new ImageIcon("src/resources/Santorini Images/SchermataGioco/Sfondo.png");
    Image img = background.getImage();
    Image newImg = img.getScaledInstance( (int) (1200*scale), (int) (900*scale),  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon backgroundResized = new ImageIcon( newImg );

    gamePanel.setIcon(backgroundResized);
    gamePanel.setLayout(null);

    imagesImport(player.getGod());
    myCard.setIcon(myCardIconFront);
    myCard.setBounds((int)(100*scale), (int)(550*scale), cardWidth, cardHeight);
    myCard.setOpaque(false);
    myCard.setContentAreaFilled(false);
    myCard.setBorderPainted(false);
    gamePanel.add(myCard);
    myCard.addMouseListener(new java.awt.event.MouseAdapter() {

      public void mouseEntered(java.awt.event.MouseEvent evt) {
        myCard.setIcon(myCardIconBack);
      }

      public void mouseExited(java.awt.event.MouseEvent evt) {
        myCard.setIcon(myCardIconFront);
      }
    });


    createCells();
    setupPawnIcons();



  }



  private void imageSetup(String path){
    ImageIcon image = new ImageIcon(path);
    Image img1 = image.getImage();
    Image newImg1 = img1.getScaledInstance( cardWidth, cardHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    myCardIconFront = new ImageIcon(newImg1);

    path=path.replace(".png", "Turned.png");

    image = new ImageIcon(path);
    img1 = image.getImage();
    newImg1 = img1.getScaledInstance( cardWidth, cardHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    myCardIconBack = new ImageIcon(newImg1);
  }

  private void imagesImport(God god){

    String prefix = "src/resources/Santorini Images/SchermataSelezioneGod/";
    String suffix = ".png";

    imageSetup(prefix + god.getName() + suffix);

  }

  private void createCells(){
    for (int i = 0; i < 25; i++){
      JButton cell = new JButton();
      cell.setBounds((int)((437+139*(i%5))*scale), (int)((142+139*(i/5))*scale), cellWidth, cellHeight);
      cell.setOpaque(false);
      cell.setContentAreaFilled(false);
      cell.setBorderPainted(false);
      cell.addActionListener(cellClickedListener);
      gamePanel.add(cell);
      cells.add(cell);
    }
  }

  static ActionListener cellClickedListener = e -> {
    JButton clickedCell = (JButton) e.getSource();

    if (clickedCell!=null){
      switch (phase){
        case "Initial Positioning":
          pawnPositioning(clickedCell);
      }
    }

  };

  private static void pawnPositioning(JButton selectedCell){
    int i = cells.indexOf(selectedCell);

    if (!selectedCells.contains(i) && selectedCells.size()<2){
      selectedCells.add(i);
      selectedCell.setIcon(myPawnIcon[0]);
    } else if (selectedCells.contains(i)){
      selectedCells.remove(((Integer) i));
      selectedCell.setIcon(null);
    }

  }

  private static void setupPawnIcons(){
    String path = "src/resources/Santorini Images/SchermataGioco/SegnapostoRosso.png";
    ImageIcon image = new ImageIcon(path);
    Image img1 = image.getImage();
    Image newImg1 = img1.getScaledInstance( cellWidth, cellHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    redPawnIcon[0] = new ImageIcon(newImg1);

    path=path.replace(".png", "Selected.png");

    image = new ImageIcon(path);
    img1 = image.getImage();
    newImg1 = img1.getScaledInstance( cellWidth, cellHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    redPawnIcon[1] = new ImageIcon(newImg1);

    path = "src/resources/Santorini Images/SchermataGioco/SegnapostoVerde.png";
    image = new ImageIcon(path);
    img1 = image.getImage();
    newImg1 = img1.getScaledInstance( cellWidth, cellHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    greenPawnIcon[0] = new ImageIcon(newImg1);

    path=path.replace(".png", "Selected.png");

    image = new ImageIcon(path);
    img1 = image.getImage();
    newImg1 = img1.getScaledInstance( cellWidth, cellHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    greenPawnIcon[1] = new ImageIcon(newImg1);

    path = "src/resources/Santorini Images/SchermataGioco/SegnapostoBlu.png";
    image = new ImageIcon(path);
    img1 = image.getImage();
    newImg1 = img1.getScaledInstance( cellWidth, cellHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    bluePawnIcon[0] = new ImageIcon(newImg1);

    path=path.replace(".png", "Selected.png");

    image = new ImageIcon(path);
    img1 = image.getImage();
    newImg1 = img1.getScaledInstance( cellWidth, cellHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    bluePawnIcon[1] = new ImageIcon(newImg1);

    switch (myPlayer.getColor()){
      case "\u001B[31m": //red
        myPawnIcon=redPawnIcon;
        break;
      case "\u001B[32m": //green
        myPawnIcon=greenPawnIcon;
        break;
      case "\u001B[34m": //blue
        myPawnIcon=bluePawnIcon;
        break;
    }
  }

  private static void messageReceived(String newPhase, ArrayList<Object> parameters){
    if (newPhase!=null){
      phase = newPhase;
      switch (phase){
        case "Initial Positioning":
          initialPosGraphics();
        case "Move Phase":
          movePhaseGraphics();
        case "Build Phase":
          buildPhaseGraphics();
      }
    }
  }

  private static void initialPosGraphics(){
    phaseLabel = new JLabel("Place your pawns");
    phaseLabel.setFont(lillyBelle);
    phaseLabel.setBounds((int)(60*scale), (int)(150*scale), (int)(250*scale), (int)(100*scale));
    phaseLabel.setForeground(darkBrown);
    phaseLabel.setHorizontalAlignment(SwingConstants.CENTER);
    gamePanel.add(phaseLabel);
  }

  private static void movePhaseGraphics(){
    phaseLabel = new JLabel("Move Phase");
  }

  private static void buildPhaseGraphics(){
    phaseLabel = new JLabel("Build Phase");
  }

}
