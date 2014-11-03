package gui;

import main.GameContext;
import main.Main;
import org.pmw.tinylog.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by an unknown Java student on 11/3/14.
 */
public class MainView extends JFrame
{
   private static final int VIEW_WIDTH = 800;
   private static final int VIEW_HEIGHT = 560;
   private boolean isLocal = false;
   private MainMenuBar menuBar = null;
   private MainStatusBar statusBar = null;
   private GameBoardView gameBoardView = null;

   public MainView()
   {
      setTitle(GameContext.TITLE + " v" + GameContext.VERSION);
      setSize(VIEW_WIDTH, VIEW_HEIGHT);
      setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
      setupMainView();
      //askIsLocal();
      setVisible(true);
   }


   private void setupMainView()
   {
      menuBar = new MainMenuBar();
      gameBoardView = new GameBoardView();
      statusBar = new MainStatusBar();

      setJMenuBar(menuBar);
      add(gameBoardView, BorderLayout.CENTER);
      add(statusBar, BorderLayout.SOUTH);


      addWindowListener(new WindowAdapter()
      {
         @Override
         public void windowClosing(WindowEvent e)
         {
            Logger.debug("Closing main window and exit.");

            int option = JOptionPane.showConfirmDialog(null, "Quit Game?");
            if (option == JOptionPane.OK_OPTION) {
               dispose();
               System.exit(0);
            }
         }
      });
   }


   private void setupMenu()
   {


   }


   private void setupStatusBar()
   {

   }

   /**
    * For testing only!
    */
   private void askIsLocal()
   {
      String lastOctet = JOptionPane.showInputDialog(null, "Choose unique client number [1..254]:",
              "Start as local only client?", JOptionPane.QUESTION_MESSAGE);

      if (lastOctet != null) {

         try {
            int octet = Integer.parseInt(lastOctet);
            if (octet > 254 || octet < 1) {
               return;
            }
         } catch (NumberFormatException e) {
            return;
         }

         GameContext.localBindAddress = "127.0.0." + lastOctet;
         setTitle(getTitle() + " [" + GameContext.localBindAddress + "]");
         isLocal = true;
      }
   }

}
