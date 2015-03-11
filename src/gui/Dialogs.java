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
   //private static Alert openAlert = null;
   private static final String DIALOG_CSS = "/gui/fxml/dialogs.css";
   private static final Alert alert = new Alert(Alert.AlertType.INFORMATION);
   private static final TextInputDialog dialog = new TextInputDialog();

   static {

      dialog.getDialogPane().getStylesheets().setAll(DIALOG_CSS);
      alert.getDialogPane().getStylesheets().setAll(DIALOG_CSS);

   }


   public static void closeCancelMsg()
   {
      Platform.runLater(Dialogs.alert::close);
   }

   public static boolean showCancelMsg(final String infoMsg)
   {
      alert.close();
      alert.setTitle("Confirmation");
      alert.setHeaderText(null);
      alert.setContentText(infoMsg);
      alert.getButtonTypes().setAll(ButtonType.CANCEL);

      Optional<ButtonType> result = alert.showAndWait();

      Logger.debug("Alert result == CANCEL? "+(result.get() == ButtonType.CANCEL));

      return !(result.isPresent() && result.get() == ButtonType.CANCEL);
   }

   public static void showOkMsg(final String infoMsg)
   {
      Platform.runLater( () -> {
         alert.close();
         alert.setTitle("Information");
         alert.setHeaderText(null);
         alert.setContentText(infoMsg);
         alert.showAndWait();
      });
   }


   public static String requestPeerIp()
   {
      dialog.close();
      dialog.setTitle("Peer IP");
      dialog.setHeaderText("Enter the IP of the Peer you want to play with");
      dialog.setContentText("Peer IP:");
      dialog.getEditor().setText(null);
      dialog.getEditor().requestFocus();

      Optional<String> ip = dialog.showAndWait();

      if(ip.isPresent()) {
         try {
            //noinspection ResultOfMethodCallIgnored
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
      dialog.close();
      dialog.setTitle("Start as local only client?");
      dialog.setHeaderText("Choose a local unique client number");
      dialog.setContentText("[1..254]:");
      dialog.getEditor().setText(null);
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
