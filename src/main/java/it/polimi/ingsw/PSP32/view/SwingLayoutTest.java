package it.polimi.ingsw.PSP32.view;

import javax.swing.*;
import java.awt.*;

public class SwingLayoutTest {


  public static void main(String[] args) {
    JFrame window = new JFrame("Prova");
    
    JPanel panel = new JPanel();
    
    panel.setLayout(new BorderLayout());

    JMenu menu = new JMenu("top menu");
    JPanel bottomPanel = new JPanel();
    JLabel b3 = new JLabel("Actions");
    JLabel b4 = new JLabel("Your Player");
    JLabel b5 = new JLabel();

    bottomPanel.setLayout(new FlowLayout());

    JButton lev1 = new JButton("A");
    JButton lev2 = new JButton("B");
    JButton lev3 = new JButton("C");
    JButton dome = new JButton("D");



    bottomPanel.add(lev1);
    bottomPanel.add(lev2);
    bottomPanel.add(lev3);
    bottomPanel.add(dome);


    panel.add(menu, BorderLayout.NORTH);
    panel.add(bottomPanel, BorderLayout.SOUTH);
    panel.add(b3, BorderLayout.WEST);
    panel.add(b4, BorderLayout.EAST);
    panel.add(b5, BorderLayout.CENTER);


    ImageIcon boardImage = new ImageIcon("src/images/Santorini Images/Board/Cliff_v001.png");
    Image img = boardImage.getImage() ;

    Image newImg = img.getScaledInstance( 600, 600,  java.awt.Image.SCALE_SMOOTH ) ;
    ImageIcon boardImageResized = new ImageIcon( newImg );

    b5.setIcon(boardImageResized);

    b5.setHorizontalAlignment(SwingConstants.CENTER);
    b5.setVerticalAlignment(SwingConstants.CENTER);
    b5.setSize(new Dimension(600, 600));


    window.setContentPane(panel);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setResizable(false);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    window.setPreferredSize(new Dimension(dim.height * 8/9 , dim.height * 2/3));
    window.pack();
    window.setLocation(dim.width/2-window.getSize().width/2, dim.height/2-window.getSize().height/2);
    window.setVisible(true);

  }
}
