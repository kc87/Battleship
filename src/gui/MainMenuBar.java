package gui;

import javax.swing.*;

/**
 * Created by citizen4 on 03.11.2014.
 */
public class MainMenuBar extends JMenuBar
{
   private static final String MENU_NAME = "Game";

   private JMenuItem connectItem = null;
   private JMenuItem disconnectItem = null;
   private JMenuItem newGameItem = null;
   private JMenuItem abortGameItem = null;
   private JMenuItem quitGameItem = null;


   public MainMenuBar()
   {
      setupMenuBar();
   }

   private void setupMenuBar()
   {
      JMenu gameMenu = new JMenu(MENU_NAME);

      connectItem = new JMenuItem("Connect Player...");
      connectItem.setActionCommand(ActionCmd.CONNECT_PEER_CMD);
      disconnectItem = new JMenuItem("Disconnect Player");
      disconnectItem.setActionCommand(ActionCmd.DISCONNECT_PEER_CMD);
      newGameItem = new JMenuItem("Start game");
      newGameItem.setActionCommand(ActionCmd.NEW_GAME_CMD);
      abortGameItem = new JMenuItem("Abort game");
      abortGameItem.setActionCommand(ActionCmd.ABORT_GAME_CMD);
      quitGameItem = new JMenuItem("Quit");
      quitGameItem.setActionCommand(ActionCmd.QUIT_APP_CMD);

      gameMenu.add(connectItem);
      gameMenu.add(disconnectItem);
      gameMenu.add(newGameItem);
      gameMenu.add(abortGameItem);
      gameMenu.add(quitGameItem);

      add(gameMenu);
   }

}
