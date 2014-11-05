package model;

import java.util.Arrays;

/**
 * Created by an unknown Java student on 11/3/14.
 */
public abstract class AbstractFleedModel
{
   public static final int NUMBER_OF_SHIPS = 10;
   public static final int MISS = 400;
   public static final int HIT = 100;
   public static final int AGAIN = 200;
   public static final int DESTROYED = 500;

   protected static final int DIM = SeaArea.DIM;
   protected int[][] seaGrid = new int[(DIM + 2)][(DIM + 2)];
   protected Ship[] ships = new Ship[NUMBER_OF_SHIPS];
   protected int shipsDestroyed = 0;
   protected Listener listener = null;

   public AbstractFleedModel(final Listener listener)
   {
      this.listener = listener;
   }

   public boolean isFleedDestroyed()
   {
      return shipsDestroyed == NUMBER_OF_SHIPS;
   }

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
