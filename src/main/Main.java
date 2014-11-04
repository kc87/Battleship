package main;

import controller.GameEngine;
import gui.Dialogs;
import gui.MainView;
import org.pmw.tinylog.Logger;

/**
 * Created by an unknown Java student on 11/2/14.
 */
public class Main
{

   public static void startApplication()
   {
      GameContext.localBindAddress = Dialogs.requestLocalBindIp();
      GameEngine.getInstance().start();

      javax.swing.SwingUtilities.invokeLater(new Runnable()
      {
         public void run()
         {
            Logger.debug("Invoking MainView...");
            GameContext.mainView = new MainView(GameContext.localBindAddress != null ? " [" + GameContext.localBindAddress + "]" : "");
            GameContext.mainView.setVisible(true);
         }
      });
   }

   public static void quitApplication()
   {
      if (Dialogs.confirmQuittingGame()) {
         Logger.debug("Closing main window and exit.");
         GameEngine.getInstance().stop();
         GameContext.mainView.dispose();
         System.exit(0);
      }
   }

   public static void main(final String[] args)
   {
      Main.startApplication();
   }

}
