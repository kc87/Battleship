package gui;

import controller.GameEngine;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by an unknown Java student on 11/5/14.
 */

public class ScoreBoard extends JPanel implements GameEngine.ScoreListener
{
   private JLabel leftLabel = null;
   private JLabel rightLabel = null;

   public ScoreBoard()
   {
      setupScoreBoard();
   }

   private void setupScoreBoard()
   {
      setLayout(new BorderLayout());
      setPreferredSize(new Dimension(800, 60));
      setBorder(BorderFactory.createTitledBorder(Const.SCORE_BOARD_BORDER, "Score Board", TitledBorder.CENTER, TitledBorder.TOP));
      setBackground(Const.GAME_PANEL_COLOR);

      leftLabel = new JLabel("My Ships:");
      leftLabel.setPreferredSize(new Dimension(360, 0));
      leftLabel.setFont(Const.MESSAGE_BOARD_FONT);
      leftLabel.setHorizontalAlignment(SwingConstants.CENTER);

      rightLabel = new JLabel("Enemy Ships:");
      rightLabel.setPreferredSize(new Dimension(360, 0));
      rightLabel.setFont(Const.MESSAGE_BOARD_FONT);
      rightLabel.setHorizontalAlignment(SwingConstants.CENTER);

      add(leftLabel, BorderLayout.WEST);
      add(rightLabel, BorderLayout.EAST);

      GameEngine.getInstance().setScoreListener(this);
   }

   @Override
   public void onScoreUpdate(int myShips, int enemyShips)
   {
      leftLabel.setText("My Ships: " + myShips);
      rightLabel.setText("Enemy Ships: " + enemyShips);
   }
}
