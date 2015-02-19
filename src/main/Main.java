package main;

import controller.GameEngine;
import gui.Dialogs;
import gui.MainView;
import org.pmw.tinylog.Logger;

public class Main
{
   public static final String TITLE = "P2P Battleship";
   public static final String VERSION = "0.8";
   public static String localBindAddress = null;
   public static MainView mainView = null;

   public static void startApplication()
   {
      localBindAddress = Dialogs.requestLocalBindIp();
      GameEngine.getInstance().startNetReceiver();

      javax.swing.SwingUtilities.invokeLater(() -> {
         Logger.debug("Invoking MainView...");
         mainView = new MainView(localBindAddress != null ? " [" + localBindAddress + "]" : "");
         mainView.setVisible(true);
      });
   }

   public static void quitApplication()
   {
      //if (Dialogs.confirmQuittingGame()) {
      GameEngine.getInstance().getShotClock().shutdown();
      GameEngine.getInstance().stopNetReceiver();
      Logger.debug("Closing main window and exit.");
      mainView.dispose();
      System.exit(0);
      //}
   }

   public static void main(final String[] args)
   {
      Main.startApplication();
   }

}
