package gui;

import main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainView extends JFrame
{
   private static final int VIEW_WIDTH = 800;
   private static final int VIEW_HEIGHT = 520;

   public MainView(final String localIp)
   {
      setTitle(Main.TITLE + " v" + Main.VERSION + localIp);
      setSize(VIEW_WIDTH, VIEW_HEIGHT);
      setResizable(false);
      setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
      setupMainView();
   }

   private void setupMainView()
   {
      MainMenuBar menuBar = new MainMenuBar();
      GameBoardView gameBoardView = new GameBoardView();
      MainStatusBar statusBar = new MainStatusBar();

      setJMenuBar(menuBar);
      add(gameBoardView, BorderLayout.CENTER);
      add(statusBar, BorderLayout.SOUTH);

      addWindowListener(new WindowAdapter()
      {
         @Override
         public void windowClosing(WindowEvent e)
         {
            Main.quitApplication();
         }
      });
   }

}
