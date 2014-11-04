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


   @Override
   public void updateView(final AbstractFleedModel fleedModel)
   {
      for (int j = 0; j < DIM; j++) {
         for (int i = 0; i < DIM; i++) {

            int gridValue = fleedModel.getSeaGrid()[i + 1][j + 1];

            if (gridValue > 0) {

               Ship ship = fleedModel.getShips()[gridValue - 1];

               gridButtons[i][j].setBackground(new Color(0, 0, 0));
               gridButtons[i][j].setBorder(Const.SHIP_BORDER);
               gridButtons[i][j].setText("" + ship.getSize());
            } else {
               gridButtons[i][j].setBackground(Const.WATER_COLOR);
               gridButtons[i][j].setBorder(Const.WATER_BORDER);
               gridButtons[i][j].setText("");
            }
         }
      }
   }
}
