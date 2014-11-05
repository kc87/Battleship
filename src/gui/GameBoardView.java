package gui;

import controller.GameEngine;
import main.GameContext;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by an unknown Java student on 11/3/14.
 */
public class GameBoardView extends JPanel
{
   public GameBoardView()
   {
      setupGameBoard();
   }

   private void setupGameBoard()
   {
      setLayout(new BorderLayout());
      setBackground(Const.GAME_PANEL_COLOR);

      add(new ScoreBoard(), BorderLayout.NORTH);
      add(GameContext.myFleedView, BorderLayout.WEST);
      add(GameContext.enemyFleedView, BorderLayout.EAST);

      GameEngine.getInstance().setModelUpdateListener(GameContext.myFleedView, GameContext.enemyFleedView);
   }
}
