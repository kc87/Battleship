package gui;

import controller.GameEngine;
import main.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuBar extends JMenuBar
{
   private static final String MENU_APP = "App";
   private static final String MENU_PLAYER = "Player";
   private static final String MENU_GAME = "Game";

   public MainMenuBar()
   {
      setupMenuBar();
   }

   private void setupMenuBar()
   {
      JMenu appMenu = new JMenu(MENU_APP);
      JMenu playerMenu = new JMenu(MENU_PLAYER);
      JMenu gameMenu = new JMenu(MENU_GAME);

      MenuAction menuAction = new MenuAction();

      JMenuItem connectItem = new JMenuItem("Connect...");
      connectItem.setActionCommand(ActionCmd.CONNECT_PEER_CMD);
      connectItem.addActionListener(menuAction);
      JMenuItem disconnectItem = new JMenuItem("Disconnect");
      disconnectItem.setActionCommand(ActionCmd.DISCONNECT_PEER_CMD);
      disconnectItem.addActionListener(menuAction);
      JMenuItem newGameItem = new JMenuItem("Start New Game");
      newGameItem.setActionCommand(ActionCmd.NEW_GAME_CMD);
      newGameItem.addActionListener(menuAction);
      JMenuItem abortGameItem = new JMenuItem("Abort Game");
      abortGameItem.setActionCommand(ActionCmd.ABORT_GAME_CMD);
      abortGameItem.addActionListener(menuAction);
      JMenuItem quitGameItem = new JMenuItem("Quit");
      quitGameItem.setActionCommand(ActionCmd.QUIT_APP_CMD);
      quitGameItem.addActionListener(menuAction);

      playerMenu.add(connectItem);
      playerMenu.add(disconnectItem);
      gameMenu.add(newGameItem);
      gameMenu.add(abortGameItem);
      appMenu.add(quitGameItem);

      add(appMenu);
      add(playerMenu);
      add(gameMenu);
   }


   private class MenuAction implements ActionListener
   {

      @Override
      public void actionPerformed(ActionEvent e)
      {
         final String cmd = e.getActionCommand();

         switch (cmd) {
            case ActionCmd.CONNECT_PEER_CMD:
               GameEngine.getInstance().connectPeer();
               break;
            case ActionCmd.DISCONNECT_PEER_CMD:
               GameEngine.getInstance().disconnectPeer();
               break;
            case ActionCmd.NEW_GAME_CMD:
               GameEngine.getInstance().newGame();
               break;
            case ActionCmd.ABORT_GAME_CMD:
               GameEngine.getInstance().abortGame();
               break;
            case ActionCmd.QUIT_APP_CMD:
               Main.quitApplication();
               break;
         }
      }
   }

}
