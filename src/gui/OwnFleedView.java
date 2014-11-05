package gui;

import model.AbstractFleedModel;
import model.Ship;

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


   //@Override
   public void updateView(final AbstractFleedModel fleedModel)
   {
      for (int j = 0; j < DIM; j++) {
         for (int i = 0; i < DIM; i++) {

            int gridValue = fleedModel.getSeaGrid()[i + 1][j + 1];

            // Just water
            if (gridValue == 0) {
               gridButtons[i][j].setBackground(Const.WATER_COLOR);
               gridButtons[i][j].setBorder(Const.WATER_BORDER);
               gridButtons[i][j].setText("");
            }

            // Ship is undamaged or destroyed
            if (gridValue > 0) {
               Ship ship = fleedModel.getShips()[gridValue - 1];
               gridButtons[i][j].setBackground(ship.isDestroyed() ? Const.DESTROYED_COLOR : Const.SHIP_COLOR);
               gridButtons[i][j].setBorder(ship.isDestroyed() ? Const.DESTROYED_BORDER : Const.SHIP_BORDER);
               gridButtons[i][j].setText("" + ship.getSize());
            }

            // Ship is partially damaged
            if (gridValue < 0) {
               gridButtons[i][j].setBackground(Const.HIT_COLOR);
               gridButtons[i][j].setBorder(Const.SHIP_BORDER);
            }
         }
      }
   }
}
