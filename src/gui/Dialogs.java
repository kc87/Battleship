package gui;

import org.pmw.tinylog.Logger;

import javax.swing.*;

/**
 * Created by an unknown Java student on 11/4/14.
 */
public class Dialogs
{

   public static boolean confirmQuittingGame()
   {
      int option = JOptionPane.showConfirmDialog(null, "Quit Game?");
      return (option == JOptionPane.OK_OPTION);
   }


   /**
    * For testing only!
    */
   public static String requestLocalBindIp()
   {
      String lastOctet = JOptionPane.showInputDialog(null, "Choose unique client number [1..254]:",
            "Start as local only client?", JOptionPane.QUESTION_MESSAGE);

      if (lastOctet != null) {

         try {
            int octet = Integer.parseInt(lastOctet);
            if (octet > 254 || octet < 1) {
               return null;
            }
         } catch (NumberFormatException e) {
            return null;
         }

         return "127.0.0." + lastOctet;
      }

      return null;
   }

}
