package gui.controller;

import controller.GameEngine;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class MainView extends VBox
{
   private static final String FXML_FILE = "/gui/fxml/main.fxml";
   private static final int FADE_DURATION = 300;
   private final GameEngine gameEngine = GameEngine.getInstance();
   private EnemyFleetView enemyFleetView;
   private FadeTransition fadeTransition;

   @FXML
   private Menu fileMenu;
   @FXML
   private Menu playerMenu;
   @FXML
   private Menu gameMenu;
   @FXML
   private HBox fleetViewContainer;
   @FXML
   private Label stateLabel;
   @FXML
   private Label peerIpLabel;
   @FXML
   private Label shotClockLabel;
   @FXML
   private Label enemyScoreLabel;
   @FXML
   private Label ownScoreLabel;


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

   public void updateState()
   {
      Platform.runLater(() -> {
         stateLabel.setText(gameEngine.getStateName());
         if(gameEngine.getConnectedPeerIp() != null){
            peerIpLabel.setText(gameEngine.getConnectedPeerIp());
         }else {
            peerIpLabel.setText("N/A");
         }
      });
   }

   public void updateShotClock(final int tick)
   {
      Platform.runLater(() -> shotClockLabel.setText(String.valueOf(tick)));
   }

   public void updateScore(final int ownShips,final int enemyShips)
   {
      Platform.runLater(() -> {
         ownScoreLabel.setText(String.valueOf(ownShips));
         enemyScoreLabel.setText(String.valueOf(enemyShips));
      });
   }

   public void enableEnemyView(final boolean enabled)
   {
      if(enemyFleetView.isDisabled() == enabled) {
         enemyFleetView.setDisable(!enabled);
         fadeTransition.setFromValue(enabled ? 0.2 : 1.0);
         fadeTransition.setToValue(enabled ? 1.0 : 0.2);
         fadeTransition.play();
      }
   }

   @FXML
   private void initialize()
   {
      fileMenu.setOnAction(this::menuActionHandler);
      playerMenu.setOnAction(this::menuActionHandler);
      gameMenu.setOnAction(this::menuActionHandler);
      enemyFleetView = new EnemyFleetView();

      fadeTransition = new FadeTransition(Duration.millis(FADE_DURATION),enemyFleetView);
      fadeTransition.setDelay(Duration.millis(200));

      OwnFleetView ownFleetView = new OwnFleetView();

      gameEngine.setModelUpdateListener(ownFleetView, enemyFleetView);
      gameEngine.setMainView(this);

      fleetViewContainer.getChildren().addAll(enemyFleetView, ownFleetView);

      updateState();
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
         default:
            throw new UnsupportedOperationException("Not yet implemented!");
      }

      e.consume();
   }
}
