package gui;

import controller.ShotClock;
import model.AbstractFleetModel;

import javax.swing.border.TitledBorder;

public class EnemyFleetView extends AbstractFleetView implements ShotClock.TickListener
{
   public EnemyFleetView(final GridButtonHandler gridButtonHandler)
   {
      super(gridButtonHandler);
   }

   @Override
   public void updatePartialView(final AbstractFleetModel fleetModel, final int i, final int j)
   {
      int gridValue = fleetModel.getSeaGrid()[i + 1][j + 1];
      SeaGridButton gridButton = gridButtons[i][j];

      // Just water
      if (gridValue == 0 || gridValue == AbstractFleetModel.MISS) {
         gridButton.setBackground(gridValue == 0 ? GuiConstants.WATER_COLOR : GuiConstants.WATER_MISS_COLOR);
         gridButton.setBorder(GuiConstants.WATER_BORDER);
         gridButton.setText(gridValue == 0 ? "" : "x");
         return;
      }

      if (gridValue == AbstractFleetModel.HIT) {
         gridButton.setBackground(GuiConstants.HIT_COLOR);
         gridButton.setBorder(GuiConstants.SHIP_BORDER);
         return;
      }

      if (gridValue > 0 && gridValue < AbstractFleetModel.NUMBER_OF_SHIPS + 1) {
         gridButton.setBackground(GuiConstants.DESTROYED_COLOR);
         gridButton.setBorder(GuiConstants.DESTROYED_BORDER);
         gridButton.setText(GuiConstants.DEAD_SYMBOL);
      }

   }

   @Override
   public void onTick(int tick)
   {
      TitledBorder tb = (TitledBorder) getBorder();
      tb.setTitle(ENEMY_TITLE + " (" + tick + " sec. left)");
      setBorder(tb);
      repaint();
   }
}
