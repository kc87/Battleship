package gui.controller;

import javafx.scene.Node;
import model.AbstractFleetModel;
import model.Ship;

public class OwnFleetView extends AbstractFleetView
{

   public OwnFleetView()
   {
      getStyleClass().add("OwnFleetView");
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

      // Ship is undamaged or destroyed
      if (gridValue > 0) {
         Ship ship = fleetModel.getShips()[gridValue - 1];
         node.getStyleClass().setAll("SeaTile", ship.isDestroyed() ? "Destroyed" : "Ship");
         return;
      }

      // Ship is partially damaged
      if (gridValue < 0) {
         node.getStyleClass().setAll("SeaTile", "Hit");
      }
   }
}
