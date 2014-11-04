package model;

import java.util.Arrays;

/**
 * Created by an unknown Java student on 11/3/14.
 */
public abstract class AbstractFleedModel
{
   public static final int NUMBER_OF_SHIPS = 10;

   protected static final int DIM = SeaArea.DIM;
   protected int[][] seaGrid = new int[(DIM + 2)][(DIM + 2)];
   protected Ship[] ships = new Ship[NUMBER_OF_SHIPS];
   protected Listener listener = null;

   public AbstractFleedModel(final Listener listener)
   {
      this.listener = listener;
   }

   public abstract void updateModel();

   public int[][] getSeaGrid()
   {
      return seaGrid;
   }

   public Ship[] getShips()
   {
      return ships;
   }

   public void reset()
   {
      seaGrid = new int[(DIM + 2)][(DIM + 2)];
      ships = new Ship[NUMBER_OF_SHIPS];
   }

   public interface Listener
   {
      public void onUpdate(final AbstractFleedModel model);
   }

}
