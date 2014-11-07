package gui;

import controller.GameEngine;

import javax.swing.*;
import java.awt.*;

public class GameBoardView extends JPanel
{
   public GameBoardView()
   {
      setupGameBoard();
   }

   private void setupGameBoard()
   {
      setLayout(new BorderLayout());
      setBackground(GuiConstants.GAME_PANEL_COLOR);

      OwnFleedView ownFleedView = new OwnFleedView();
      EnemyFleedView enemyFleedView = new EnemyFleedView(new GridButtonHandler());

      add(new ScoreBoard(), BorderLayout.NORTH);
      add(ownFleedView, BorderLayout.WEST);
      add(enemyFleedView, BorderLayout.EAST);

      GameEngine.getInstance().setModelUpdateListener(ownFleedView, enemyFleedView);
      GameEngine.getInstance().getShotClock().setTickListener(enemyFleedView);
   }
}
