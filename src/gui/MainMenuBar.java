package gui;

import main.GameContext;
import org.pmw.tinylog.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by citizen4 on 03.11.2014.
 */
public class MainMenuBar extends JMenuBar /*implements FleedView.Listener*/
{
   private static final String MENU_NAME = "Game";

   private JMenuItem connectItem = null;
   private JMenuItem disconnectItem = null;
   private JMenuItem newGameItem = null;
   private JMenuItem abortGameItem = null;
   private JMenuItem quitGameItem = null;
   private FleedView myFleedView = null;

   public MainMenuBar()
   {
      setupMenuBar();
   }

   private void setupMenuBar()
   {
      JMenu gameMenu = new JMenu(MENU_NAME);
      MenuAction menuAction = new MenuAction();

      connectItem = new JMenuItem("Connect Player...");
      connectItem.setActionCommand(ActionCmd.CONNECT_PEER_CMD);
      connectItem.addActionListener(menuAction);
      disconnectItem = new JMenuItem("Disconnect Player");
      disconnectItem.setActionCommand(ActionCmd.DISCONNECT_PEER_CMD);
      disconnectItem.addActionListener(menuAction);
      newGameItem = new JMenuItem("Start New Game");
      newGameItem.setActionCommand(ActionCmd.NEW_GAME_CMD);
      newGameItem.addActionListener(menuAction);
      abortGameItem = new JMenuItem("Abort Game");
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


   private class MenuAction implements ActionListener
   {

      @Override
      public void actionPerformed(ActionEvent e)
      {
         Logger.debug(e.getActionCommand());
         final String cmd = e.getActionCommand();

         switch (cmd) {
            case ActionCmd.NEW_GAME_CMD:
               GameContext.myFleed.placeNewFleed();
               break;
         }
      }
   }

}
