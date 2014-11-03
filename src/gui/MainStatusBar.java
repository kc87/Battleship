package gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by citizen4 on 03.11.2014.
 */
public class MainStatusBar extends JPanel
{
   public static final int HEIGHT = 20;
   private static final Color BAR_BG_COLOR = new Color(200, 200, 200);
   private static final Border LABEL_BORDER = BorderFactory.createLineBorder(new Color(88, 88, 88), 1);
   private static final Border BAR_BORDER = BorderFactory.createLineBorder(new Color(0, 0, 0), 1);


   private JLabel connectionStateLabel = null;
   private JLabel gameStateLabel = null;
   private JLabel infoLabel = null;

   public MainStatusBar()
   {
      setupStatusBar();
   }

   private void setupStatusBar()
   {
      setLayout(new BorderLayout(2, 2));
      setBackground(BAR_BG_COLOR);
      setBorder(BAR_BORDER);
      setPreferredSize(new Dimension(0, HEIGHT));

      connectionStateLabel = new JLabel("Connection State:");
      connectionStateLabel.setPreferredSize(new Dimension(200, 0));
      gameStateLabel = new JLabel("Game State:");
      gameStateLabel.setPreferredSize(new Dimension(160, 0));

      infoLabel = new JLabel();

      infoLabel.setBorder(LABEL_BORDER);
      connectionStateLabel.setBorder(LABEL_BORDER);
      gameStateLabel.setBorder(LABEL_BORDER);

      add(gameStateLabel, BorderLayout.WEST);
      add(connectionStateLabel, BorderLayout.EAST);
      add(infoLabel, BorderLayout.CENTER);
   }

   public void setConnectionState()
   {

   }

   public void setGameState()
   {


   }

   public void setInfoMessage()
   {

   }

}