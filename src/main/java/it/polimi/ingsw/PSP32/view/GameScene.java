package it.polimi.ingsw.PSP32.view;

import it.polimi.ingsw.PSP32.client.ClientGui;
import it.polimi.ingsw.PSP32.client.ServerAdapter;
import it.polimi.ingsw.PSP32.client.ServerAdapterGui;
import it.polimi.ingsw.PSP32.model.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.xml.xpath.XPath;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import static it.polimi.ingsw.PSP32.client.ServerAdapterGui.*;
import static it.polimi.ingsw.PSP32.view.Gui.*;
import static javax.swing.SwingConstants.CENTER;

public class GameScene {

  static JLabel gamePanel = new JLabel();

  static JButton myCard = new JButton();
  static ImageIcon myCardIconFront;
  static ImageIcon myCardIconBack;
  static JLabel phaseLabel = new JLabel();
  static JLabel phaseInfo = new JLabel();
  static JButton phaseButton = new JButton();

  static int [] position1= {-1,-1};
  static int [] position2= {-1,-1};

  static int [] buildLocation ={-1,-1};

  static Pawn activePawn;

  static Game game;

  static ArrayList<JButton> cells = new ArrayList<>();


  static int cardWidth = (int) (170*scale);
  static int cardHeight = (int) (240*scale);

  static int cellWidth = (int)(135*scale);
  static int cellHeight = (int)(135*scale);

  static String phase;

  static Player myPlayer;

  static ArrayList<Integer> selectedCells = new ArrayList<>();

  static ImageIcon[] redPawnIcon = new ImageIcon[2];
  static ImageIcon[] bluePawnIcon = new ImageIcon[2];
  static ImageIcon[] greenPawnIcon = new ImageIcon[2];
  static ImageIcon[] myPawnIcon = new ImageIcon[2];

  static ImageIcon[] building = new ImageIcon[4];



  public void show(){
    window.setContentPane(gamePanel);
    window.setVisible(true);
  }


  public GameScene(Player player){

    myPlayer = player;

    sceneSetup();

    setupPhaseZone();


    waitGraphics();



  }

 static ActionListener phaseButtonListener = e ->{
    switch (phase){
     case ("Initial Positioning"):
       position1[0]= selectedCells.get(0)%5;
       position1[1]= selectedCells.get(0)/5;
       position2[0]= selectedCells.get(1)%5;
       position2[1]= selectedCells.get(1)/5;



       synchronized(lockFirstPositioning){
         ServerAdapterGui.flagForFirstPositioning.set(1);
         lockFirstPositioning.notifyAll();
       }

       waitGraphics();

       break;

   }

  };

  private static void sceneSetup(){
    ImageIcon background = new ImageIcon("src/resources/Santorini Images/SchermataGioco/Sfondo.png");
    Image img = background.getImage();
    Image newImg = img.getScaledInstance( (int) (1200*scale), (int) (900*scale),  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon backgroundResized = new ImageIcon( newImg );

    gamePanel.setIcon(backgroundResized);
    gamePanel.setLayout(null);

    imagesImport(myPlayer.getGod());
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
    setupBuildingIcons();
    setupPawnIcons();
  }

  private static void imageSetup(String path){
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

  private static void imagesImport(God god){

    String prefix = "src/resources/Santorini Images/SchermataSelezioneGod/";
    String suffix = ".png";

    imageSetup(prefix + god.getName() + suffix);

  }

  private static void createCells(){
    for (int i = 0; i < 25; i++){
      JButton cell = new JButton();
      cell.setBounds((int)((437+139*(i%5))*scale), (int)((142+139*(i/5))*scale), cellWidth, cellHeight);
      cell.setOpaque(false);
      cell.setContentAreaFilled(false);
      cell.setBorderPainted(false);
      cell.addActionListener(cellClickedListener);
      cell.setEnabled(true);
      gamePanel.add(cell);
      cells.add(cell);
    }
  }

  static ActionListener cellClickedListener = e -> {
    JButton clickedCell = (JButton) e.getSource();
    if (clickedCell!=null){
      switch (phase){
        case "Initial Positioning":
          if(clickedCell.getIcon()==null || clickedCell.getIcon().equals(myPawnIcon[0])){
            pawnPositioning(clickedCell);
            break;
          }
        case "Move Phase":
          if(clickedCell.getIcon()!=null && clickedCell.getIcon().equals(myPawnIcon[0])){
              if(activePawn!=null) cells.get(activePawn.getX()+(activePawn.getY()*5)).setIcon(myPawnIcon[0]);
            clickedCell.setIcon(myPawnIcon[1]);
            int i = cells.indexOf(clickedCell);
            activePawn = game.getMap()[i%5][i/5].getIsFull();
          }
          else if (clickedCell.getIcon()!=null && clickedCell.getIcon().equals(myPawnIcon[1])) {
            cells.get(activePawn.getX() + activePawn.getY() * 5).setIcon(myPawnIcon[0]);
            activePawn = null;

          }
          else if(activePawn!=null){
            String direction = direction(cells.get(activePawn.getX()+activePawn.getY()*5), clickedCell);
            Boolean valid=false;
            if(direction!=null) {
              try {
                valid = (Boolean) ServerAdapterGui.toServerGetObject("checkCanMove"+direction, game, activePawn, null);
              } catch (IOException ex) {
                ex.printStackTrace();
              }

              if(valid){
                activePawn.moves(getX(clickedCell), getY(clickedCell));
                synchronized(lockActivePawn) {
                  flagForActivePawn.set(1);
                  lockActivePawn.notifyAll();
                }
              }
            }
          }
          break;
        case "Build Phase":
          String direction = direction(cells.get(activePawn.getX()+activePawn.getY()*5), clickedCell);
            Boolean valid1=false;
          if(direction!=null) {
            try {
              valid1 = (Boolean) ServerAdapterGui.toServerGetObject("checkCanBuild" + direction, game, activePawn, null);
            } catch (IOException ex) {
              ex.printStackTrace();
            }
          }
          if(valid1){
             buildLocation [0]= getX(clickedCell);
             buildLocation [1]= getY(clickedCell);
            activePawn=null;
             waitGraphics();

            synchronized(lockBuilding) {
              flagForBuilding.set(1);
              lockBuilding.notifyAll();
            }
          }
      }
    }

  };

  private static void pawnPositioning(JButton selectedCell){
    int i = cells.indexOf(selectedCell);

    if (!selectedCells.contains(i) && selectedCells.size()<2){
      selectedCells.add(i);
      selectedCell.setIcon(myPawnIcon[0]);
      phaseInfo.setText(selectedCells.size() + "/2 pawns placed");
      if (selectedCells.size()==2) phaseButton.setEnabled(true);
    } else if (selectedCells.contains(i)){
      selectedCells.remove(((Integer) i));
      selectedCell.setIcon(null);
      phaseInfo.setText(selectedCells.size() + "/2 pawns placed");
      phaseButton.setEnabled(false);
    }

  }

  private static void setupBuildingIcons(){
    String path = "src/resources/Santorini Images/SchermataGioco/Floor1.png";
    ImageIcon image = new ImageIcon(path);
    Image img1 = image.getImage();
    Image newImg1 = img1.getScaledInstance( cellWidth, cellHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    building[0] = new ImageIcon(newImg1);

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

  public static void messageReceived(String newPhase, ArrayList<Object> parameters){
    if (newPhase!=null){
      switch (newPhase){
        case "Initial Positioning":
          phase = newPhase;
          initialPosGraphics();
          break;
        case "Move Phase":

          phase = newPhase;
          game = (Game) parameters.get(0);
          movePhaseGraphics();
          break;
        case "Build Phase":
          phase = newPhase;
          game = (Game) parameters.get(0);

          buildPhaseGraphics();
          break;
        case "Refresh Screen":
          if(myPlayer!=null)
          refreshScreen((Game) parameters.get(0));
          break;
      }
    }
  }


  private static void setupPhaseZone(){
    phaseLabel.setVisible(false);
    gamePanel.add(phaseLabel);

    phaseInfo.setVisible(false);
    gamePanel.add(phaseInfo);

    phaseButton.setVisible(false);
    gamePanel.add(phaseButton);
    phaseButton.addActionListener(phaseButtonListener);
  }

  private static void initialPosGraphics(){

    for (int i = 0; i < cells.size(); i++){
      cells.get(i).setEnabled(true);
    }

    phaseLabel.setText("Place your pawns");
    phaseLabel.setFont(lillyBelle);
    phaseLabel.setBounds((int)(60*scale), (int)(150*scale), (int)(250*scale), (int)(100*scale));
    phaseLabel.setForeground(darkBrown);
    phaseLabel.setHorizontalAlignment(CENTER);
    phaseLabel.setVisible(true);


    phaseInfo.setBounds((int)(55*scale), (int)(220*scale), (int)(255*scale), (int)((100)*scale));
    phaseInfo.setText("0/2 pawns placed");
    phaseInfo.setFont(minionProXSmall);
    phaseInfo.setHorizontalAlignment(CENTER);
    phaseInfo.setForeground(darkBrown);
    phaseInfo.setVisible(true);

    ImageIcon playImage = new ImageIcon("src/resources/Santorini Images/SchermataGioco/PhaseButtonDone.png");
    Image img1 = playImage.getImage();
    Image newImg1 = img1.getScaledInstance( (int)(150*scale), (int)((150/1.80)*scale),  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon playImageResized = new ImageIcon(newImg1);
    phaseButton.setIcon(playImageResized);
    phaseButton.setBounds((int)(110*scale), (int)(300*scale), (int)(150*scale), (int)((150/1.80)*scale));
    phaseButton.setOpaque(false);
    phaseButton.setContentAreaFilled(false);
    phaseButton.setBorderPainted(false);
    phaseButton.setVisible(true);
    phaseButton.setEnabled(false);


  }

  private static void waitGraphics(){
    phase = "Wait";

    for (int i = 0; i < cells.size(); i++){
      cells.get(i).setEnabled(true);
    }

    phaseLabel.setText("Wait");
    phaseLabel.setFont(lillyBelle);
    phaseLabel.setBounds((int)(60*scale), (int)(150*scale), (int)(250*scale), (int)(100*scale));
    phaseLabel.setForeground(darkBrown);
    phaseLabel.setHorizontalAlignment(CENTER);
    phaseLabel.setVisible(true);


    phaseInfo.setBounds((int)(55*scale), (int)(220*scale), (int)(255*scale), (int)((100)*scale));
    phaseInfo.setText("Other players are playing");
    phaseInfo.setFont(minionProXSmall);
    phaseInfo.setHorizontalAlignment(CENTER);
    phaseInfo.setForeground(darkBrown);
    phaseInfo.setVisible(true);

    phaseButton.setVisible(false);

  }

  private static void movePhaseGraphics(){
    phaseLabel.setText("Move Phase");
    phaseInfo.setText("Select one of your pawns");
    phaseButton.setEnabled(false);
  }

  private static void buildPhaseGraphics(){
    phaseLabel.setText("Build Phase");
    phaseInfo.setText("Select where to build");
    phaseButton.setEnabled(false);
  }

  public static void refreshScreen(Game game){
    for (int i=0; i<25; i++){
      Cell cell = game.getMap()[i%5][i/5];
      switch (cell.getFloor()){
        case 1:
          cells.get(i).setIcon(building[0]);
      }
      if (cell.getIsFull()!=null) {
        switch (cell.getIsFull().getPlayer().getColor()) {
          case ("\u001B[31m"):
            if (activePawn!=null && i==activePawn.getX()+(activePawn.getY()*5))
              cells.get(i).setIcon(redPawnIcon[1]);
            else
              cells.get(i).setIcon(redPawnIcon[0]);
            break;
          case ("\u001B[32m"):
            if (activePawn!=null &&  i==activePawn.getX()+(activePawn.getY()*5))
              cells.get(i).setIcon(greenPawnIcon[1]);
            else
              cells.get(i).setIcon(greenPawnIcon[0]);
            break;
          case ("\u001B[34m"):
            if (activePawn!=null &&  i==activePawn.getX()+(activePawn.getY()*5))
              cells.get(i).setIcon(bluePawnIcon[1]);
            else
              cells.get(i).setIcon(bluePawnIcon[0]);
            break;
        }
      }
      else cells.get(i).setIcon(null);
    }


  }

  public static int[] getCoords (Boolean first){
    if (first) return position1;
    else return position2;
  }

  public static int[] getBuildLocation(){return buildLocation;}

  public static  Pawn getActivePawn(){ return activePawn; }

  private static int getX (JButton cell){
    return cells.indexOf(cell)%5;
  }
  private static int getY (JButton cell){
    return cells.indexOf(cell)/5;
  }

  private static boolean near (JButton cell1, JButton cell2){
    if (getX(cell1)- getX(cell2)>=-1 && getX(cell1)-getX(cell2)<=1)
      if (getY(cell1)- getY(cell2)>=-1 && getY(cell1)-getY(cell2)<=1) return true;
    return false;
  }

  private static String direction (JButton centralCell, JButton newCell){
    if (near(centralCell, newCell)){
      switch (getX(centralCell)-getX(newCell)){
        case 1:
          switch (getY(centralCell)-getY(newCell)){
            case 1:
              return "NW";
            case 0:
              return "W";
            case -1:
              return "SW";
          }
          break;
        case 0:
          switch (getY(centralCell)-getY(newCell)){
            case 1:
              return "N";
            case 0:
              return null;
            case -1:
              return "S";
          }
          break;
        case -1:
          switch (getY(centralCell)-getY(newCell)){
            case 1:
              return "NE";
            case 0:
              return "E";
            case -1:
              return "SE";
          }
          break;
      }
    }
    return null;
  }
}

