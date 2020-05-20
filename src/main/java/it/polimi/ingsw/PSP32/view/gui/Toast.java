package it.polimi.ingsw.PSP32.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

import static it.polimi.ingsw.PSP32.view.gui.Gui.*;

public class Toast extends JLabel {

  JLabel messageLabel = new JLabel();

  private float alpha = 0.95f;

  private Image newImg;



  public Toast (String message, JLabel currentPanel, int timeShown) {





    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        showToast(message, currentPanel);
        try {
          Thread.sleep(timeShown);
          removeToast(currentPanel);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    thread.start();

  }

  public void showToast(String message, JLabel currentPanel){

    ImageIcon background = new ImageIcon("src/resources/Santorini Images/SchermataGioco/ToastBackground.png");
    Image img = background.getImage();
    newImg = img.getScaledInstance( (int)(500*scale), (int)(125*scale),  java.awt.Image.SCALE_SMOOTH );

    ImageIcon backgroundIcon = new ImageIcon( newImg );



    //setIcon(backgroundIcon);
    messageLabel.setText(message);
    messageLabel.setFont(minionProSmall);
    messageLabel.setHorizontalAlignment(CENTER);
    setVerticalAlignment(TOP);
    setBackground(new Color(0xCC6A634E, true));
    messageLabel.setForeground(new Color(0xFFC9E4D8, true));
    setOpaque(false);

    setBounds((int)(535*scale), (int)(760*scale), (int)(500*scale), (int)(125*scale));
    messageLabel.setBounds((int)(0*scale), (int)(35*scale), (int)(500*scale), (int)(25*scale));

    messageLabel.setVisible(true);
    this.add(messageLabel);

    setVisible(true);
    currentPanel.add(this);
    currentPanel.revalidate();
    currentPanel.repaint();
  }

  public void removeToast(JLabel currentPanel) throws InterruptedException {
    for (alpha = alpha; alpha > 0; alpha -= 0.05f) {
      Thread.sleep(50);
      repaint();
    }
    setVisible(false);
    currentPanel.remove(this);
  }


  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;



    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    g2d.drawImage(newImg, 0, 0, null);

  }


}
