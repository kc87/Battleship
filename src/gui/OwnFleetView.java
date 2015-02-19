package gui;

import model.AbstractFleetModel;
import model.Ship;


public class OwnFleetView extends AbstractFleetView
{
   public OwnFleetView()
   {
      super(null);
   }

   @Override
   public void updatePartialView(final AbstractFleetModel fleetModel, final int i, final int j)
   {
      int gridValue = fleetModel.getSeaGrid()[i + 1][j + 1];

      // Just water
      if (gridValue == 0 || gridValue == AbstractFleetModel.MISS) {
         gridButtons[i][j].setBackground(gridValue == 0 ? GuiConstants.WATER_COLOR : GuiConstants.WATER_MISS_COLOR);
         gridButtons[i][j].setBorder(GuiConstants.WATER_BORDER);
         gridButtons[i][j].setText(gridValue == 0 ? "" : "x");
         return;
      }

      // Ship is undamaged or destroyed
      if (gridValue > 0) {
         Ship ship = fleetModel.getShips()[gridValue - 1];
         gridButtons[i][j].setBackground(ship.isDestroyed() ? GuiConstants.DESTROYED_COLOR : GuiConstants.SHIP_COLOR);
         gridButtons[i][j].setBorder(ship.isDestroyed() ? GuiConstants.DESTROYED_BORDER : GuiConstants.SHIP_BORDER);
         gridButtons[i][j].setText(ship.isDestroyed() ? GuiConstants.DEAD_SYMBOL : "" + ship.getSize());
         return;
      }

      // Ship is partially damaged
      if (gridValue < 0) {
         gridButtons[i][j].setBackground(GuiConstants.HIT_COLOR);
         gridButtons[i][j].setBorder(GuiConstants.HIT_BORDER);
      }
   }
}
