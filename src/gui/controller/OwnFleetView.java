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

      if (gridValue == 0) {
         node.getStyleClass().setAll("SeaTile", "Water");
         return;
      }

      if (gridValue == AbstractFleetModel.MISS) {
         setTileStyle(node, "Miss");
         return;
      }

      // Ship is undamaged or destroyed
      if (gridValue > 0) {
         Ship ship = fleetModel.getShips()[gridValue - 1];
         if(ship.isDestroyed()) {
            setTileStyle(node, "Destroyed");
         }else{
            node.getStyleClass().setAll("SeaTile", "Ship");
         }
         return;
      }

      // Ship is partially damaged
      if (gridValue < 0) {
         setTileStyle(node, "Hit");
      }
   }
}
