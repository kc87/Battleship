package gui;

import model.AbstractFleedModel;

/**
 * Created by an unknown Java student on 11/3/14.
 */
public class EnemyFleedView extends AbstractFleedView
{
   public EnemyFleedView(final GridButtonHandler gridButtonHandler)
   {
      super(gridButtonHandler);
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

      if (gridValue == AbstractFleedModel.HIT) {
         gridButtons[i][j].setBackground(Const.HIT_COLOR);
         gridButtons[i][j].setBorder(Const.SHIP_BORDER);
         return;
      }

      if (gridValue > 0 && gridValue < AbstractFleedModel.NUMBER_OF_SHIPS + 1) {
         gridButtons[i][j].setBackground(Const.DESTROYED_COLOR);
         gridButtons[i][j].setBorder(Const.DESTROYED_BORDER);
         gridButtons[i][j].setText(Const.DEAD_SYMBOL);
      }

   }
}
