package gui;

import model.AbstractFleedModel;
import model.Ship;
import org.pmw.tinylog.Logger;

import java.awt.*;

/**
 * Created by an unknown Java student on 11/3/14.
 */
public class OwnFleedView extends AbstractFleedView
{
   public OwnFleedView()
   {
      super(null);
   }

   @Override
   public void updatePartialView(final AbstractFleedModel fleedModel, final int i, final int j)
   {
      int gridValue = fleedModel.getSeaGrid()[i + 1][j + 1];

      // Just water
      if (gridValue == 0 || gridValue == AbstractFleedModel.MISS) {
         gridButtons[i][j].setBackground(gridValue == 0 ? Const.WATER_COLOR : Const.WATER_MISS_COLOR);
         gridButtons[i][j].setBorder(Const.WATER_BORDER);
         gridButtons[i][j].setText(gridValue == 0 ? "" : "x");
         return;
      }

      // Ship is undamaged or destroyed
      if (gridValue > 0) {
         Ship ship = fleedModel.getShips()[gridValue - 1];
         gridButtons[i][j].setBackground(ship.isDestroyed() ? Const.DESTROYED_COLOR : Const.SHIP_COLOR);
         gridButtons[i][j].setBorder(ship.isDestroyed() ? Const.DESTROYED_BORDER : Const.SHIP_BORDER);
         gridButtons[i][j].setText(ship.isDestroyed() ? Const.DEAD_SYMBOL : "" + ship.getSize());
         return;
      }

      // Ship is partially damaged
      if (gridValue < 0) {
         gridButtons[i][j].setBackground(Const.HIT_COLOR);
         gridButtons[i][j].setBorder(Const.HIT_BORDER);
         return;
      }
   }
}
