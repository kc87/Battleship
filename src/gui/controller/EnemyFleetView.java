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

      if (gridValue == 0) {
         node.getStyleClass().setAll("SeaTile", "Water");
         return;
      }

      // Just water
      if (gridValue == AbstractFleetModel.MISS) {
         setTileStyle(node, "Miss");
         return;
      }

      if (gridValue == AbstractFleetModel.HIT) {
         setTileStyle(node, "Hit");
         return;
      }

      if (gridValue > 0 && gridValue < AbstractFleetModel.NUMBER_OF_SHIPS + 1) {
         setTileStyle(node, "Destroyed");
      }
   }


   @Override
   public void initialize(URL location, ResourceBundle resources)
   {
      super.initialize(location, resources);
      seaGrid.setOnMouseClicked(this::seaTileHandler);
   }


   private void seaTileHandler(final MouseEvent event)
   {
      int nodeId;
      final Node targetNode = (Node) event.getTarget();

      try {
         nodeId = Integer.parseInt(targetNode.getId());
      } catch (final NumberFormatException e) {
         return;
      }

      int i = nodeId % GRID_SIZE;
      int j = nodeId / GRID_SIZE;

      GameEngine.getInstance().shoot(i, j);

      event.consume();
   }

}
