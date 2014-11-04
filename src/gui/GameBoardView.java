package gui;

import main.GameContext;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by an unknown Java student on 11/3/14.
 */
public class GameBoardView extends JPanel
{
   private static final Border PANEL_BORDER = BorderFactory.createLineBorder(new Color(0, 0, 0), 2);
   private JPanel msgPanel = null;

   public GameBoardView(/*final FleedView ownFleedModel, final FleedView enemyFleedModel*/)
   {
      setupGameBoard(/*ownFleedModel, enemyFleedModel*/);
   }


   private void setupGameBoard(/*final FleedView ownFleedModel, final FleedView enemyFleedModel*/)
   {
      setLayout(new BorderLayout());

      msgPanel = new JPanel(new FlowLayout());
      msgPanel.setPreferredSize(new Dimension(800, 60));
      msgPanel.setBorder(BorderFactory.createTitledBorder(PANEL_BORDER, "Message Board", TitledBorder.CENTER, TitledBorder.TOP));

      add(msgPanel, BorderLayout.NORTH);
      add(GameContext.myFleedView, BorderLayout.WEST);
      add(GameContext.enemyFleedView, BorderLayout.EAST);
   }


}
