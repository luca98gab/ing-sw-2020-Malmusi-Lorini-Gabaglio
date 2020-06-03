package it.polimi.ingsw.PSP32.view.gui.scenes;

import it.polimi.ingsw.PSP32.client.ServerAdapterGui;
import it.polimi.ingsw.PSP32.model.*;
import it.polimi.ingsw.PSP32.view.gui.*;
import it.polimi.ingsw.PSP32.view.gui.components.gameSceneComponents.AboutPage;
import it.polimi.ingsw.PSP32.view.gui.components.gameSceneComponents.PlayersPopup;
import it.polimi.ingsw.PSP32.view.gui.components.gameSceneComponents.PopupRemovedPlayer;
import it.polimi.ingsw.PSP32.view.gui.components.gameSceneComponents.PopupWin;
import it.polimi.ingsw.PSP32.view.gui.components.generic.DisconnectedPopup;
import it.polimi.ingsw.PSP32.view.gui.components.generic.LobbyIsFullPopup;
import it.polimi.ingsw.PSP32.view.gui.components.generic.Toast;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static it.polimi.ingsw.PSP32.client.ServerAdapterGui.*;
import static javax.swing.SwingConstants.*;

public class GameScene extends Gui{

  static JLabel gamePanel = new JLabel();

  static JPanel menuBar = new JPanel();

  static JButton myCard = new JButton();
  static ImageIcon myCardIconFront;
  static ImageIcon myCardIconBack;
  static JLabel phaseLabel = new JLabel();
  static JLabel phaseInfo = new JLabel();
  static JButton phaseButton = new JButton();
  static JLabel athenaFlagImageContainer = new JLabel();
  static JLabel divider = new JLabel("- - - - - - - - - -");

  static Cell restrictedCell = null;

  static Boolean wantsToUsePower=false;


  static JLabel meLabel = new JLabel();
  static JLabel myColor = new JLabel();


  static int [] position1= {-1,-1};
  static int [] position2= {-1,-1};

  static int [] buildLocation ={-1,-1};

  public static Pawn activePawn;

  public static Game game;

  static ArrayList<JButton> cells = new ArrayList<>();
  static ArrayList<JLabel> buildIconsLayer = new ArrayList<>();


  public static int cardWidth = (int) (170*scale);
  public static int cardHeight = (int) (240*scale);

  public static int cellWidth = (int)(110*scale);
  public static int cellHeight = (int)(110*scale);

  static String phase;

  public static Player myPlayer;

  static ArrayList<Integer> selectedCells = new ArrayList<>();

  public static ImageIcon[] redPawnIcon = new ImageIcon[2];
  public static ImageIcon[] bluePawnIcon = new ImageIcon[2];
  public static ImageIcon[] greenPawnIcon = new ImageIcon[2];
  public static ImageIcon[] myPawnIcon = new ImageIcon[2];

  static ImageIcon[] buildingIcons = new ImageIcon[4];
  static ImageIcon[] buildingDomeIcons = new ImageIcon[5];

  static Boolean bordersAllowed = true;


  //setup

  public void show(){
    gamePanel.setBounds(0, 0, window.getWidth(), window.getHeight());
    window.setContentPane(gamePanel);
    window.setVisible(true);
  }

  public GameScene(Player player){

    myPlayer = player;

    sceneSetup();

    waitGraphics();

  }

  private static void menuSetup(){

    JButton rules = new JButton("Rules");
    rules.setVerticalAlignment(TOP);
    rules.setForeground(new Color(225, 224, 222));
    rules.setFont(minionProXSmall);
    rules.setContentAreaFilled(false);
    rules.setBorderPainted(false);

    rules.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (Desktop.isDesktopSupported()) {
          try {
            File myFile = new File(getClass().getResource("/Santorini Images/Santorini Rulebook.pdf").toURI());
            Desktop.getDesktop().open(myFile);
          } catch (IOException | URISyntaxException ex) {

          }
        }
      }
    });
    rules.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        rules.setOpaque(true);
        rules.setBackground(new Color(0x2D6772D5, true));
      }
      public void mouseExited(java.awt.event.MouseEvent evt) {
        rules.setOpaque(false);
        rules.setBackground(new Color(0,0,0,0));
      }
    });

    menuBar.add(rules);


    JButton about = new JButton("About");
    about.setVerticalAlignment(TOP);
    about.setOpaque(false);
    about.setContentAreaFilled(false);
    about.setBorderPainted(false);
    about.setFont(minionProXSmall);
    about.setForeground(new Color(225, 224, 222));
    about.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new AboutPage();
      }
    });
    about.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        about.setOpaque(true);
        about.setBackground(new Color(0x2D6772D5, true));
      }
      public void mouseExited(java.awt.event.MouseEvent evt) {
        about.setOpaque(false);
        about.setBackground(new Color(0,0,0,0));
      }
    });
    menuBar.add(about);

    JButton players = new JButton("Players");
    players.setVerticalAlignment(TOP);
    players.setOpaque(false);
    players.setContentAreaFilled(false);
    players.setBorderPainted(false);
    players.setFont(minionProXSmall);
    players.setForeground(new Color(225, 224, 222));
    players.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new PlayersPopup();
      }
    });
    players.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        players.setOpaque(true);
        players.setBackground(new Color(0x2D6772D5, true));
      }
      public void mouseExited(java.awt.event.MouseEvent evt) {
        players.setOpaque(false);
        players.setBackground(new Color(0,0,0,0));
      }
    });
    menuBar.add(players);

    menuBar.setLayout(new FlowLayout(FlowLayout.LEADING));
    menuBar.setBounds((int) (35*scale), (int) ((15)*scale), (int) (1133*scale), (int) (29*scale));
    menuBar.setOpaque(false);

    gamePanel.add(menuBar);
  }

  private static void sceneSetup(){

    menuSetup();

    ImageIcon background = new ImageIcon(GameScene.class.getResource("/Santorini Images/SchermataGioco/Sfondo.png"));
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
    setupBuildingLayer();
    setupPawnIcons();
    setupPhaseZone();

  }

  private static void imageSetup(String path){
    ImageIcon image = new ImageIcon(GameScene.class.getResource(path));
    Image img1 = image.getImage();
    Image newImg1 = img1.getScaledInstance( cardWidth, cardHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    myCardIconFront = new ImageIcon(newImg1);

    path=path.replace(".png", "Turned.png");

    image = new ImageIcon(GameScene.class.getResource(path));
    img1 = image.getImage();
    newImg1 = img1.getScaledInstance( cardWidth, cardHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    myCardIconBack = new ImageIcon(newImg1);
  }

  private static void imagesImport(God god){

    String prefix = "/Santorini Images/SchermataSelezioneGod/";
    String suffix = ".png";

    imageSetup(prefix + god.getName() + suffix);

  }

  private static void createCells(){
    for (int i = 0; i < 25; i++){
      JButton cell = new JButton();
      cell.setBounds((int)((448+140*(i%5))*scale), (int)((150+141*(i/5))*scale), cellWidth, cellHeight);
      cell.setOpaque(false);
      cell.setContentAreaFilled(false);
      cell.setBorderPainted(false);
      cell.addActionListener(cellClickedListener);
      cell.setEnabled(true);
      gamePanel.add(cell);
      cells.add(cell);
    }


  }

  private static void setupBuildingLayer(){


    String prefix = "/Santorini Images/SchermataGioco/Floor";
    String suffix = ".png";
    String dome = "wDome";

    String path = prefix + "0" + dome + suffix;
    ImageIcon image = new ImageIcon(GameScene.class.getResource(path));
    Image img1 = image.getImage();
    Image newImg1 = img1.getScaledInstance( cellWidth, cellHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    buildingDomeIcons[0] = new ImageIcon(newImg1);

    buildingIcons[0] = null;

    for (int i = 1; i < 4; i++) {

      path = prefix + i + suffix;
      image = new ImageIcon(GameScene.class.getResource(path));
      img1 = image.getImage();
      newImg1 = img1.getScaledInstance(cellWidth, cellHeight, java.awt.Image.SCALE_SMOOTH);
      buildingIcons[i] = new ImageIcon(newImg1);

      path = prefix + i + dome + suffix;
      image = new ImageIcon(GameScene.class.getResource(path));
      img1 = image.getImage();
      newImg1 = img1.getScaledInstance(cellWidth, cellHeight, java.awt.Image.SCALE_SMOOTH);
      buildingDomeIcons[i] = new ImageIcon(newImg1);
    }

    buildingDomeIcons[4] = buildingDomeIcons[3];


    for (int i = 0; i < 25; i++){
      JLabel cellSub = new JLabel();
      cellSub.setBounds((int)((448+140*(i%5))*scale), (int)((150+141*(i/5))*scale), cellWidth, cellHeight);
      cellSub.setOpaque(false);
      gamePanel.add(cellSub);
      buildIconsLayer.add(cellSub);
    }

  }

  private static void setupPawnIcons(){
    String path = "/Santorini Images/SchermataGioco/SegnapostoRosso.png";
    ImageIcon image = new ImageIcon(GameScene.class.getResource(path));
    Image img1 = image.getImage();
    Image newImg1 = img1.getScaledInstance( cellWidth, cellHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    redPawnIcon[0] = new ImageIcon(newImg1);

    path=path.replace(".png", "Selected.png");

    image = new ImageIcon(GameScene.class.getResource(path));
    img1 = image.getImage();
    newImg1 = img1.getScaledInstance( cellWidth, cellHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    redPawnIcon[1] = new ImageIcon(newImg1);

    path = "/Santorini Images/SchermataGioco/SegnapostoVerde.png";
    image = new ImageIcon(GameScene.class.getResource(path));
    img1 = image.getImage();
    newImg1 = img1.getScaledInstance( cellWidth, cellHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    greenPawnIcon[0] = new ImageIcon(newImg1);

    path=path.replace(".png", "Selected.png");

    image = new ImageIcon(GameScene.class.getResource(path));
    img1 = image.getImage();
    newImg1 = img1.getScaledInstance( cellWidth, cellHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    greenPawnIcon[1] = new ImageIcon(newImg1);

    path = "/Santorini Images/SchermataGioco/SegnapostoBlu.png";
    image = new ImageIcon(GameScene.class.getResource(path));
    img1 = image.getImage();
    newImg1 = img1.getScaledInstance( cellWidth, cellHeight,  java.awt.Image.SCALE_SMOOTH ) ;
    bluePawnIcon[0] = new ImageIcon(newImg1);

    path=path.replace(".png", "Selected.png");

    image = new ImageIcon(GameScene.class.getResource(path));
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

  private static void setupPhaseZone(){
    phaseLabel.setVisible(false);
    gamePanel.add(phaseLabel);

    phaseInfo.setVisible(false);
    gamePanel.add(phaseInfo);

    phaseButton.setVisible(false);
    gamePanel.add(phaseButton);
    phaseButton.addActionListener(phaseButtonListener);


    divider.setFont(lillyBelle);
    divider.setHorizontalTextPosition(CENTER);
    divider.setHorizontalAlignment(CENTER);
    divider.setForeground(darkBrown);
    divider.setBounds((int)(60*scale), (int)(366*scale), (int)(240*scale), (int)(20*scale));
    divider.setVisible(false);
    gamePanel.add(divider);

    ImageIcon athena = new ImageIcon(GameScene.class.getResource("/Santorini Images/SchermataGioco/AthenaFlag.png"));
    Image img = athena.getImage();
    Image newImg = img.getScaledInstance( (int)(80*scale), (int)(80*scale),  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon athenaIcon = new ImageIcon( newImg );
    athenaFlagImageContainer.setIcon(athenaIcon);
    athenaFlagImageContainer.setVisible(false);
    athenaFlagImageContainer.setOpaque(false);
    athenaFlagImageContainer.setBounds((int)(140*scale), (int)(380*scale), (int)(80*scale), (int)(80*scale));
    gamePanel.add(athenaFlagImageContainer);

    JLabel divider2 = new JLabel("- - - - - - - - - -");
    divider2.setFont(lillyBelle);
    divider2.setHorizontalTextPosition(CENTER);
    divider2.setHorizontalAlignment(CENTER);
    divider2.setForeground(darkBrown);
    divider2.setBounds((int)(60*scale), (int)(456*scale), (int)(240*scale), (int)(20*scale));
    divider2.setVisible(true);
    gamePanel.add(divider2);


    meLabel.setText("<html>Player Info: " + myPlayer.getName() + "<br/><br/>Color:      </html>");
    meLabel.setFont(minionProXSmall);
    meLabel.setHorizontalTextPosition(CENTER);
    meLabel.setVerticalAlignment(1);
    meLabel.setForeground(darkBrown);
    meLabel.setBounds((int)(120*scale), (int)(480*scale), (int)(200*scale), (int)(100*scale));
    meLabel.setVisible(true);
    gamePanel.add(meLabel);

    img = myPawnIcon[0].getImage().getScaledInstance( (int)(80*scale), (int)(80*scale),  java.awt.Image.SCALE_SMOOTH );
    ImageIcon myColorIcon = new ImageIcon( img );

    myColor.setIcon(myColorIcon);
    myColor.setBounds((int)(170*scale), (int)(484*scale), (int)(80*scale), (int)(80*scale));
    myColor.setOpaque(false);
    myColor.setVisible(true);
    gamePanel.add(myColor);


  }



  // phases graphic setup

  private static void initialPosGraphics(){

    for (int i = 0; i < cells.size(); i++){
      cells.get(i).setEnabled(true);
    }

    phaseLabel.setText("Place your pawns");
    phaseLabel.setVisible(true);


    phaseInfo.setText("0/2 pawns placed");

    phaseInfo.setVisible(true);

    ImageIcon playImage = new ImageIcon(GameScene.class.getResource("/Santorini Images/SchermataGioco/PhaseButtonDone.png"));
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

  private static void waitGraphics(String name){
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
    if (name!=null){
      phaseInfo.setText(name.toUpperCase() + " is playing");
    } else {
      phaseInfo.setText("Other players are playing");
    }
    phaseInfo.setFont(minionProXSmall);
    phaseInfo.setHorizontalAlignment(CENTER);
    phaseInfo.setForeground(darkBrown);
    phaseInfo.setVisible(true);

    phaseButton.setVisible(false);
  }
  private static void waitGraphics(){
    waitGraphics(null);
  }

  private static void movePhaseGraphics(){
    phaseLabel.setText("Move Phase");
    phaseInfo.setText("<html>Select one of your pawns, then<br/>click on a nearby spot to move.</html>");
    phaseInfo.setHorizontalTextPosition(CENTER);
    athenaFlagImageContainer.setVisible(game.getAthenaFlag());
    divider.setVisible(game.getAthenaFlag());
    phaseInfo.setVisible(true);

  }

  private static void buildPhaseGraphics(){
    phaseLabel.setText("Build Phase");
    phaseInfo.setText("Select where to build");
    phaseButton.setVisible(false);
    phaseInfo.setVisible(true);
  }

  private static void buildFirstPhaseGraphics(){
    phaseLabel.setText("Build Phase");
    phaseInfo.setText("<html>Select one of your pawns, then<br/>click on a nearby spot to build.</html>");
    phaseButton.setVisible(false);
    phaseInfo.setVisible(true);
  }



  // actions

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
          movePhaseClick(clickedCell);
          break;
        case "Build Phase":
          buildPhaseClick(clickedCell);
          break;
        case "Move Phase 2":
          movePhase2Click(clickedCell);
          break;
        case "Power":
          break;
        case "Build Phase 2":
          buildPhase2Click(clickedCell);
          break;
        case "BuildFirst":
          buildFirstPhaseClick(clickedCell);
          break;
      }
    }

  };
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

  /**Methods invoked by the cell's listener, based on the current phase
   *
   * @param clickedCell :JButton the clicked button(cell)
   */
  private static void movePhaseClick(JButton clickedCell){
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
        } else {
          new Toast("Invalid Move", gamePanel, 2000);
        }
      }
    }
  }
  private static void movePhase2Click(JButton clickedCell){

    if(clickedCell.getIcon()==null){
      String direction = direction(cells.get(activePawn.getX()+activePawn.getY()*5), clickedCell);
      Boolean valid=false;
      if(direction!=null) {
        try {
          valid = (Boolean) ServerAdapterGui.toServerGetObject("checkCanMove"+direction, game, activePawn, restrictedCell);
        } catch (IOException ex) {
          ex.printStackTrace();
        }

        if(valid){
          activePawn.moves(getX(clickedCell), getY(clickedCell));
          synchronized(lockMove2) {
            flagForMove2.set(1);
            lockMove2.notifyAll();
          }
        } else {
          new Toast("Invalid Move", gamePanel, 2000);
        }
      }
    }
  }
  private static void buildPhaseClick(JButton clickedCell){
    String direction = direction(cells.get(activePawn.getX()+activePawn.getY()*5), clickedCell);
    Boolean valid1=false;
    if(direction!=null) {
      try {
        valid1 = (Boolean) ServerAdapterGui.toServerGetObject("checkCanBuild" + direction, game, activePawn, null, true);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    if(valid1!=null && valid1){
      buildLocation [0]= getX(clickedCell);
      buildLocation [1]= getY(clickedCell);
      activePawn=null;
      waitGraphics();

      synchronized(lockBuilding) {
        flagForBuilding.set(1);
        lockBuilding.notifyAll();
      }
    } else {
      new Toast("Invalid Build Location", gamePanel, 2000);
    }
  }
  private static void buildPhase2Click(JButton clickedCell){
    String direction = direction(cells.get(activePawn.getX()+activePawn.getY()*5), clickedCell);
    Boolean valid1=false;
    if(direction!=null) {
      try {
        valid1 = (Boolean) ServerAdapterGui.toServerGetObject("checkCanBuild" + direction, game, activePawn, restrictedCell, bordersAllowed);
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
    } else {
      new Toast("Invalid Build Location", gamePanel, 2000);
    }
  }
  private static void buildFirstPhaseClick(JButton clickedCell){
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
          valid = (Boolean) ServerAdapterGui.toServerGetObject("checkCanBuild"+direction, game, activePawn, null);
        } catch (IOException ex) {
          ex.printStackTrace();
        }

        if(valid){
          buildLocation [0]= getX(clickedCell);
          buildLocation [1]= getY(clickedCell);
          synchronized(lockActivePawn) {
            flagForActivePawn.set(1);
            lockActivePawn.notifyAll();
          }
        } else {
          new Toast("Invalid Move", gamePanel, 2000);
        }
      }
    }
  }
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



  //events

  /**Method that handle phases, and the received parameters from the server
   *
   * @param newPhase :String the new phase we are going into
   * @param parameters :ArrayList<Object> parameters useful for the methods that are being called
   * @return Boolean (used to let the server know if the player decided to use the ability of his god)
   */
  public static Boolean messageReceived(String newPhase, ArrayList<Object> parameters){
    if (newPhase!=null){
      switch (newPhase){
        case "Initial Positioning":
          game = (Game) parameters.get(0);
          phase = newPhase;
          initialPosGraphics();
          break;
        case "Move Phase":
          phase = newPhase;
          game = (Game) parameters.get(0);
          movePhaseGraphics();
          break;
        case "Move Phase 2":
          phase = "Move Phase 2";
          game = (Game) parameters.get(0);
          activePawn= (Pawn) parameters.get(1);
          restrictedCell= (Cell) parameters.get(2);
          movePhaseGraphics();
          cells.get(activePawn.getX()+(activePawn.getY()*5)).setIcon(myPawnIcon[1]);
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
        case "Disconnection":
          new DisconnectedPopup(window);
          break;
        case "Endgame":
          new PopupWin(window, (Player) parameters.get(0));
          break;
        case "Remove Player":
          new PopupRemovedPlayer(window, (Player) parameters.get(0));
          break;
        case "Power":
          phase = newPhase;
          wantsToUsePower=askPower();
          return wantsToUsePower;
        case "Build Phase 2":
          phase = "Build Phase 2";
          game = (Game) parameters.get(0);
          activePawn= (Pawn) parameters.get(1);
          if (parameters.get(4) instanceof Cell) restrictedCell= (Cell) parameters.get(4);
          else bordersAllowed = (Boolean) parameters.get(4);
          buildPhaseGraphics();
          //cells.get(activePawn.getX()+(activePawn.getY()*5)).setIcon(myPawnIcon[1]);
          break;
        case "Playing Player":
          waitGraphics((String) parameters.get(0));
          break;
        case "Wait":
          waitGraphics();
          break;
        case"Lobby Is Full":
          new LobbyIsFullPopup(window);
        case "BuildFirst":
          game = (Game) parameters.get(0);
          buildFirstPhaseGraphics();
          phase=newPhase;
          break;
      }
    }
    return false;
  }



  // logic

  /**Method to know if two buttons are near each other in the grid
   *
   * @param cell1 :JButton first button
   * @param cell2 :JButton second button
   * @return Boolean (true if the 2 cells are close, false otherwise)
   */
  private static boolean near (JButton cell1, JButton cell2){
    if (getX(cell1)- getX(cell2)>=-1 && getX(cell1)-getX(cell2)<=1)
      if (getY(cell1)- getY(cell2)>=-1 && getY(cell1)-getY(cell2)<=1) return true;
    return false;
  }

  /**Method to know where is "newCell" compared to "centralCell", only if they are close(invoke 'near' method)
   *
   * @param centralCell :JButton point of reference for our direction
   * @param newCell :JButton cell whose position we want
   * @return String (the direction expressed with cardinal points: E,W,S,N,NE,.... Null if the cells aren't close)
   */
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
              return "Below";
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



  // utilities

  /**Method to refresh the screen with the latest info about the board
   *
   * @param game :Game current status of the game
   */
  public static void refreshScreen(Game game){
    for (int i=0; i<25; i++){
      Cell cell = game.getMap()[i%5][i/5];
      if (cell.getHasDome()) buildIconsLayer.get(i).setIcon(buildingDomeIcons[cell.getFloor()]);
      else buildIconsLayer.get(i).setIcon(buildingIcons[cell.getFloor()]);

      if (cell.getIsFull()!=null) {
        cells.get(i).setToolTipText("<html>" + cell.getIsFull().getPlayer().getName() + "<br/>"
                + cell.getIsFull().getPlayer().getGod().getName() + "<br/>" + cell.getIsFull().getPlayer().getGod().getAbility());
        switch (cell.getIsFull().getPlayer().getColor()) {
          case ("\u001B[31m"):
            if (activePawn!=null && i==activePawn.getX()+(activePawn.getY()*5)){
              cells.get(i).setIcon(redPawnIcon[1]);
            }
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
      else {
        cells.get(i).setIcon(null);
        cells.get(i).setToolTipText(null);
      }
    }


  }

  /** Method to know the pawn position
   *
   * @param first :Boolean to know if you are looking for the first or the second pawn
   * @return int[] Array of int containing the pawn position
   */
  public static int[] getCoords (Boolean first){
    if (first) return position1;
    else return position2;
  }

  /** Method to know the cell selected in the building phase
   *
   * @return int[] Array of int containing the position of the selected cell where to build
   */
  public static int[] getBuildLocation(){
    return buildLocation;}

  /** Method to know the active pawn
   *
   * @return Pawn active pawn
   */
  public static  Pawn getActivePawn(){ return activePawn; }

  /**Method to know the corresponding X coordinate of a button on the board
   *
   * @param cell :JButton
   * @return int x coord. of the button
   */
  private static int getX (JButton cell){
    return cells.indexOf(cell)%5;
  }

  /**Method to know the corresponding Y coordinate of a button on the board
   *
   * @param cell :JButton
   * @return int Y coord. of the button
   */
  private static int getY (JButton cell){
    return cells.indexOf(cell)/5;
  }

  public static Boolean getWantsToUsePower() {
    return wantsToUsePower;
  }


  private static Boolean askPower(){
    final Object lock = new Object();
    AtomicInteger flagForLock = new AtomicInteger(0);


    final Boolean[] result = new Boolean[1];
    ImageIcon playImage = new ImageIcon(GameScene.class.getResource("/Santorini Images/SchermataGioco/PhaseButtonYes.png"));
    Image img1 = playImage.getImage();
    Image newImg1 = img1.getScaledInstance( (int)(75*scale), (int)((75/1.80)*scale),  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon yesIcon = new ImageIcon(newImg1);

    playImage = new ImageIcon(GameScene.class.getResource("/Santorini Images/SchermataGioco/PhaseButtonNo.png"));
    img1 = playImage.getImage();
    newImg1 = img1.getScaledInstance( (int)(75*scale), (int)((75/1.80)*scale),  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon noIcon = new ImageIcon(newImg1);

    phaseInfo.setText("Use power?");

    JButton yesButton = new JButton();
    yesButton.setBounds((int)(95*scale), (int)(320*scale), (int)(75*scale), (int)((75/1.80)*scale));
    yesButton.setIcon(yesIcon);
    yesButton.setVisible(true);

    JButton noButton = new JButton();
    noButton.setBounds((int)(195*scale), (int)(320*scale), (int)(75*scale), (int)((75/1.80)*scale));
    noButton.setIcon(noIcon);
    noButton.setVisible(true);



    gamePanel.add(yesButton);
    gamePanel.add(noButton);

    gamePanel.revalidate();
    gamePanel.repaint();


    yesButton.addActionListener(e -> {

      phaseInfo.setVisible(false);
      yesButton.setVisible(false);
      noButton.setVisible(false);
      gamePanel.remove(yesButton);
      gamePanel.remove(noButton);
      result[0] =true;
      synchronized(lock){
        flagForLock.set(1);
        lock.notifyAll();
      }
    });
    noButton.addActionListener(e -> {

      //Do Something

      phaseInfo.setVisible(false);
      yesButton.setVisible(false);
      noButton.setVisible(false);
      gamePanel.remove(yesButton);
      gamePanel.remove(noButton);
      result[0]= false;
      synchronized(lock){
        flagForLock.set(1);
        lock.notifyAll();
      }
    });

    synchronized (lock){
      while(flagForLock.get()==0){
        try {
          lock.wait();
        }catch (InterruptedException e){}

      }

    }

    return result[0];
  }
}

