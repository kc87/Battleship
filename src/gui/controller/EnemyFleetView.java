package gui.controller;

import controller.GameEngine;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import model.AbstractFleetModel;

import java.net.URL;
import java.util.ResourceBundle;


public class EnemyFleetView extends AbstractFleetView
{
   public EnemyFleetView()
   {
      getStyleClass().add("EnemyFleetView");
   }


   @Override
   public void updatePartialViewOnUi(final AbstractFleetModel fleetModel, final int i, final int j)
   {
      int gridValue = fleetModel.getSeaGrid()[i + 1][j + 1];
      int nodeId = GRID_SIZE * j + i;
      Node node = seaGrid.lookup("#" + nodeId);

      // Just water
      if (gridValue == 0 || gridValue == AbstractFleetModel.MISS) {
         node.getStyleClass().setAll("SeaTile", gridValue == 0 ? "Water" : "Miss");
         return;
      }

      if (gridValue == AbstractFleetModel.HIT) {
         node.getStyleClass().setAll("SeaTile", "Hit");
         return;
      }

      if (gridValue > 0 && gridValue < AbstractFleetModel.NUMBER_OF_SHIPS + 1) {
         node.getStyleClass().setAll("SeaTile", "Destroyed");
      }
   }


   @Override
   public void initialize(URL location, ResourceBundle resources)
   {
      super.initialize(location, resources);
      seaGrid.setOnMouseClicked(this::seaTileHandler);
   }


   private void seaTileHandler(MouseEvent event)
   {
      int nodeId;
      Node targetNode = (Node) event.getTarget();

      try {
         nodeId = Integer.parseInt(targetNode.getId());
      } catch (NumberFormatException ex) {
         return;
      }

      int i = nodeId % GRID_SIZE;
      int j = nodeId / GRID_SIZE;

      GameEngine.getInstance().shoot(i, j);

      event.consume();
   }

}
