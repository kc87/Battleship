package gui.controller;

import javafx.scene.Node;
import model.AbstractFleetModel;
import model.Ship;

public class OwnFleetView extends AbstractFleetView
{

   public OwnFleetView()
   {}


   @Override
   public void updatePartialView(final AbstractFleetModel fleetModel, final int i, final int j)
   {
      int gridValue = fleetModel.getSeaGrid()[i + 1][j + 1];
      int nodeId = GRID_SIZE * j + i;
      Node node = seaGrid.lookup("#" + nodeId);


      // Just water
      if (gridValue == 0 || gridValue == AbstractFleetModel.MISS) {
         node.getStyleClass().add(1, gridValue == 0 ? "Water" : "Miss");
         return;
      }

      // Ship is undamaged or destroyed
      if (gridValue > 0) {
         Ship ship = fleetModel.getShips()[gridValue - 1];
         node.getStyleClass().add(1, ship.isDestroyed() ? "Destroyed" : "Ship");
         return;
      }

      // Ship is partially damaged
      if (gridValue < 0) {
         node.getStyleClass().add(1, "Hit");
      }
   }
}
