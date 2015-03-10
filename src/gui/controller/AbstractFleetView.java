package gui.controller;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import model.AbstractFleetModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractFleetView extends BorderPane implements Initializable, AbstractFleetModel.ModelUpdateListener
{
   private static final String FXML_FILE = "/gui/fxml/seagrid.fxml";
   protected static final int GRID_SIZE = 10;

   @FXML
   protected GridPane seaGrid;

   public AbstractFleetView()
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

   public abstract void updatePartialViewOnUi(final AbstractFleetModel fleetModel, final int i, final int j);

   public void updatePartialView(final AbstractFleetModel fleetModel, final int i, final int j)
   {
      // Make sure its running on the Ui-Thread
      if(Platform.isFxApplicationThread()){
         updatePartialViewOnUi(fleetModel,i,j);
      }else {
         Platform.runLater(() -> updatePartialViewOnUi(fleetModel,i,j));
      }
   }


   @Override
   public void onPartialUpdate(final AbstractFleetModel model, final int i, final int j, final int flag)
   {
      if (flag != AbstractFleetModel.AGAIN) {
         if (flag == AbstractFleetModel.MISS || flag == AbstractFleetModel.HIT) {
            updatePartialView(model, i, j);
         } else {
            onTotalUpdate(model);
         }
      }
   }

   @Override
   public void onTotalUpdate(final AbstractFleetModel model)
   {
      for (int j = 0; j < GRID_SIZE; j++) {
         for (int i = 0; i < GRID_SIZE; i++) {
            updatePartialView(model, i, j);
         }
      }
   }

   @Override
   public void initialize(URL location, ResourceBundle resources)
   {
      for (int row = 0; row < GRID_SIZE; row++) {
         for (int col = 0; col < GRID_SIZE; col++) {
            Region seaTile = new Region();
            seaTile.setCache(false);
            seaTile.getStyleClass().add("SeaTile");
            seaTile.setId(String.valueOf(GRID_SIZE * row + col));
            seaGrid.add(seaTile, row, col);
         }
      }
   }
}
