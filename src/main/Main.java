package main;

import gui.MainView;
import org.pmw.tinylog.Logger;

/**
 * Created by an unknown Java student on 11/2/14.
 */
public class Main
{


   public static void main(final String[] args)
   {

      javax.swing.SwingUtilities.invokeLater(new Runnable()
      {
         public void run()
         {
            Logger.debug("Invoking MainView...");
            new MainView();
         }
      });
   }

}
