package gui.controller;

import controller.GameEngine;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainView extends VBox
{
   private static final String FXML_FILE = "/gui/fxml/main.fxml";

   @FXML
   private Menu fileMenu;
   @FXML
   private Menu playerMenu;
   @FXML
   private Menu gameMenu;
   @FXML
   private HBox fleetViewContainer;

   public MainView()
   {
      FXMLLoader fxmlLoader = new FXMLLoader(EnemyFleetView.class.getResource(FXML_FILE));
      fxmlLoader.setRoot(this);
      fxmlLoader.setController(this);
      try {
         fxmlLoader.load();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }


   @FXML
   public void initialize()
   {
      fileMenu.setOnAction(this::menuActionHandler);
      playerMenu.setOnAction(this::menuActionHandler);
      gameMenu.setOnAction(this::menuActionHandler);

      EnemyFleetView enemyFleetView = new EnemyFleetView();
      OwnFleetView ownFleetView = new OwnFleetView();

      //ownFleetView.setDisable(true);
      GameEngine.getInstance().setModelUpdateListener(ownFleetView, enemyFleetView);

      fleetViewContainer.getChildren().addAll(enemyFleetView, ownFleetView);
   }


   private void menuActionHandler(final ActionEvent e)
   {
      final MenuItem menuItem = (MenuItem)e.getTarget();

      switch(menuItem.getId()){
         case "quitAppItem":
            Platform.exit();
            break;
         case "connectPlayerItem":
            GameEngine.getInstance().connectPeer();
            break;
         case "disconnectPlayerItem":
            GameEngine.getInstance().disconnectPeer();
            break;
         case "newGameItem":
            GameEngine.getInstance().newGame();
            break;
         case "abortGameItem":
            GameEngine.getInstance().abortGame();
            break;
         case "quitGameItem":
            break;
         default:
            throw new UnsupportedOperationException("Not yet implemented!");
      }

      e.consume();
   }
}
