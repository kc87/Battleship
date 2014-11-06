package gui;

import controller.ShotClock;
import model.AbstractFleedModel;

import javax.swing.border.TitledBorder;

/**
 * Created by an unknown Java student on 11/3/14.
 */
public class EnemyFleedView extends AbstractFleedView implements ShotClock.TickListener
{
   public EnemyFleedView(final GridButtonHandler gridButtonHandler)
   {
      super(gridButtonHandler);
   }

   @Override
   public void updatePartialView(final AbstractFleedModel fleedModel, final int i, final int j)
   {
      int gridValue = fleedModel.getSeaGrid()[i + 1][j + 1];
      SeaGridButton gridButton = gridButtons[i][j];

      // Just water
      if (gridValue == 0 || gridValue == AbstractFleedModel.MISS) {
         gridButton.setBackground(gridValue == 0 ? GuiConstants.WATER_COLOR : GuiConstants.WATER_MISS_COLOR);
         gridButton.setBorder(GuiConstants.WATER_BORDER);
         gridButton.setText(gridValue == 0 ? "" : "x");
         return;
      }

      if (gridValue == AbstractFleedModel.HIT) {
         gridButton.setBackground(GuiConstants.HIT_COLOR);
         gridButton.setBorder(GuiConstants.SHIP_BORDER);
         return;
      }

      if (gridValue > 0 && gridValue < AbstractFleedModel.NUMBER_OF_SHIPS + 1) {
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
