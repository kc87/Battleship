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
   private static final String DIALOG_CSS = "/gui/fxml/dialogs.css";
   private static Alert alert;
   private static TextInputDialog dialog;

   public static void init()
   {
      alert = new Alert(Alert.AlertType.INFORMATION);
      dialog = new TextInputDialog();

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

      Logger.debug("Alert result == CANCEL? " + (result.get() == ButtonType.CANCEL));

      return !(result.isPresent() && result.get() == ButtonType.CANCEL);
   }

   public static void showOkMsg(final String infoMsg)
   {
      Platform.runLater(() -> {
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

      return dialog.showAndWait().map(ip -> {
         try {
            //noinspection ResultOfMethodCallIgnored
            InetAddress.getByName(ip);
            return ip;
         } catch (UnknownHostException e) {
            Logger.error(e);
            return null;
         }
      }).orElse(null);
   }

   /**
    * For testing only!
    */
   public static Optional<String> requestLocalBindIp()
   {
      dialog.close();
      dialog.setTitle("Start as local only client?");
      dialog.setHeaderText("Choose a local unique client number");
      dialog.setContentText("[1..254]:");
      dialog.getEditor().setText(null);
      dialog.getEditor().requestFocus();

      return getLocalIpFromId(dialog.showAndWait());
   }


   public static Optional<String> getLocalIpFromId(final Optional<String> localId)
   {
      return localId.flatMap(Dialogs::stringToInt)
              .filter(id -> (id > 0 && id < 255))
              .map(id -> "127.0.0." + String.valueOf(id));
   }

   private static Optional<Integer> stringToInt(final String str)
   {
      try {
         return Optional.of(Integer.parseInt(str));
      } catch (NumberFormatException e) {
         return Optional.empty();
      }
   }

}
