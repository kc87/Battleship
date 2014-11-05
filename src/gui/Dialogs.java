package gui;

import org.pmw.tinylog.Logger;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by an unknown Java student on 11/4/14.
 */
public class Dialogs
{

   public static void showInfoThread(final String infoMsg)
   {
      new Thread(new Runnable()
      {
         @Override
         public void run()
         {
            JOptionPane.showMessageDialog(null, infoMsg);
         }
      }).start();
   }


   public static void showInfo(final String infoMsg)
   {
      JOptionPane.showMessageDialog(null, infoMsg);
   }

   public static boolean confirmQuittingGame()
   {
      int option = JOptionPane.showConfirmDialog(null, "Quit Game?");
      return (option == JOptionPane.OK_OPTION);
   }

   public static String requestPeerIp()
   {
      String ip = JOptionPane.showInputDialog(null, "Player IP",
              "Please provide Player IP address", JOptionPane.QUESTION_MESSAGE);
      try {
         InetAddress a = InetAddress.getByName(ip);
      } catch (UnknownHostException e) {
         Logger.error(e);
         return null;
      }

      return ip;
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
