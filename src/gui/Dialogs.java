package gui;


import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import org.pmw.tinylog.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;


public class Dialogs
{
   private static Alert openAlert = null;

   public static void closeCancelMsg()
   {
      Platform.runLater( () -> {
         if (Dialogs.openAlert != null) {
            Dialogs.openAlert.close();
            openAlert = null;
         }
      });
   }

   public static boolean showCancelMsg(final String infoMsg)
   {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Confirmation");
      alert.setHeaderText(null);
      alert.setContentText(infoMsg);
      alert.getButtonTypes().setAll(ButtonType.CANCEL);

      Dialogs.openAlert = alert;

      Optional<ButtonType> result = alert.showAndWait();

      if (result.isPresent() && result.get() == ButtonType.CANCEL) {
         Dialogs.openAlert = null;
         return false;
      } else {
         return true;
      }
   }

   public static void showOkMsg(final String infoMsg)
   {
      Platform.runLater( () -> {
         Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setTitle("Information");
         alert.setHeaderText(null);
         alert.setContentText(infoMsg);
         alert.showAndWait();
      });
   }


   public static String requestPeerIp()
   {
      TextInputDialog dialog = new TextInputDialog();
      dialog.setTitle("Peer IP");
      dialog.setHeaderText("Enter the IP of the Peer you want to play with");
      dialog.setContentText("Peer IP:");
      dialog.getEditor().requestFocus();

      Optional<String> ip = dialog.showAndWait();

      if(ip.isPresent()) {
         try {
            InetAddress.getByName(ip.get());
            return ip.get();
         } catch (UnknownHostException e) {
            Logger.error(e);
         }
      }

      return null;
   }

   /**
    * For testing only!
    */
   public static String requestLocalBindIp()
   {
      TextInputDialog dialog = new TextInputDialog();
      dialog.setTitle("Start as local only client?");
      dialog.setHeaderText("Choose a local unique client number");
      dialog.setContentText("[1..254]:");
      dialog.getEditor().requestFocus();

      Optional<String> lastOctet = dialog.showAndWait();

      if (lastOctet.isPresent()) {
         try {
            int octet = Integer.parseInt(lastOctet.get());
            if (octet > 0 && octet < 255) {
               return "127.0.0." + lastOctet.get();
            }
         } catch (NumberFormatException e) {
            /* IGNORED */
         }
      }

      return null;
   }

}
