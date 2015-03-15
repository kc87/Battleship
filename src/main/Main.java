package main;

import controller.GameEngine;
import gui.Dialogs;
import gui.controller.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.pmw.tinylog.Logger;

import java.util.Optional;

public class Main extends Application
{
   public static final String TITLE = "P2P Battleship (FX8)";
   public static final String VERSION = "v0.11";
   public static Optional<String> localBindAddress = Optional.empty();

   @Override
   public void init()
   {
      Logger.debug("Application.init()");
   }

   @Override
   public void start(Stage mainStage) throws Exception
   {
      Dialogs.init();
      localBindAddress = Dialogs.requestLocalBindIp();
      String title = TITLE + " " + VERSION +
                    (localBindAddress.isPresent() ? " ["+localBindAddress.get()+"]" : "");

      GameEngine.getInstance().startNetReceiver();

      mainStage.setTitle(title);
      mainStage.setScene(new Scene(new MainView()));;
      mainStage.show();
   }

   @Override
   public void stop()
   {
      Logger.debug("Application.stop()");
      GameEngine.getInstance().getShotClock().shutdown();
      GameEngine.getInstance().stopNetReceiver();
      Logger.info("Closing main window and exit.");
   }


   public static void main(String[] args)
   {
      Application.launch(args);
   }
}
