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

   public void updateView(final int i, final int j, final int result)
   {
      if (result == AbstractFleedModel.MISS) {
         gridButtons[i][j].setBackground(Const.WATER_COLOR);
         gridButtons[i][j].setBorder(Const.WATER_BORDER);
      }

      if (result == AbstractFleedModel.HIT) {
         gridButtons[i][j].setBackground(Const.HIT_COLOR);
         gridButtons[i][j].setBorder(Const.SHIP_BORDER);
      }

   }
}
