
package it.polimi.ingsw.PSP32.view;

import it.polimi.ingsw.PSP32.model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

import static it.polimi.ingsw.PSP32.view.Gui.*;

public class DisconnectedPopup{



  public DisconnectedPopup (JFrame window) {

    JDialog popup = new JDialog(window, "Disconnection Detected", true);

    popup.setResizable(false);

    popup.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        System.exit(0);
      }
    });

    popup.setLayout(new FlowLayout());

    JLabel label = new JLabel("<html>One of the players lost connection,<br/>the game is being shut down</html>");

    popup.add(label);

    popup.setUndecorated(false);
    popup.setSize(300, 80);
    popup.setLocationRelativeTo(null);
    popup.setVisible(true);

  }

}
