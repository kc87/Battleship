package gui;

import org.pmw.tinylog.Logger;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by an unknown Java student on 11/4/14.
 */
public class Dialogs
{
   // is used as a handel to get JOptionPane window
   public static final JLabel MSG_LABEL = new JLabel();

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

   public static void closeMsgDialog()
   {
      Window window = SwingUtilities.getWindowAncestor(MSG_LABEL);
      if (window != null) {
         window.dispose();
      }
   }

   public static int showCancelMsg(final String infoMsg)
   {
      Object[] options = {"Abort"};
      MSG_LABEL.setText(infoMsg);
      return JOptionPane.showOptionDialog(null,
            MSG_LABEL, "",
            JOptionPane.PLAIN_MESSAGE,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]);
   }

   public static void showOkMsg(final String infoMsg)
   {
      MSG_LABEL.setText(infoMsg);
      JOptionPane.showMessageDialog(null, MSG_LABEL);
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
      Object[] options = {"Ok"};
      String lastOctet = (String) JOptionPane.showInputDialog(null,
            "Choose unique client number [1..254]:",
            "Start as local only client?",
            JOptionPane.PLAIN_MESSAGE, null, null, null);


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
