package it.polimi.ingsw.PSP32.view.gui.components.generic;


import javax.swing.*;
import java.awt.*;

public class Hourglass extends JLabel {

  ImageIcon[] hourglassIcons = new ImageIcon[3];


  public Hourglass () {



    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          showHourglass();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    thread.start();

  }

  public void showHourglass() throws InterruptedException {

    for (int i = 1; i < 4 ; i++) {

      ImageIcon hourglass = new ImageIcon(getClass().getResource("/Santorini Images/Clessidra/Clessidra" + i +".png"));
      Image img = hourglass.getImage();
      Image newImg = img.getScaledInstance(this.getWidth(), this.getHeight(), java.awt.Image.SCALE_SMOOTH);

      hourglassIcons[i-1] = new ImageIcon(newImg);
    }


    setIcon(hourglassIcons[0]);
    setHorizontalAlignment(CENTER);
    setVerticalAlignment(CENTER);
    setVisible(true);
    setOpaque(false);




    while (true){
      for (int i = 0; i < 3; i++) {
        Thread.sleep(500);
        setIcon(hourglassIcons[i]);
        repaint();
      }
    }
  }




}
