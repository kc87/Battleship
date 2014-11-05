package model;

import org.pmw.tinylog.Logger;

/**
 * Created by an unknown Java student on 11/3/14.
 */
public class EnemyFleedModel extends AbstractFleedModel
{
   public EnemyFleedModel()
   {
      super(null);
   }

   //@Override
   public void update(final int i, final int j, final int resultFlag, final Ship ship)
   {
      if (ship != null) {
         shipsDestroyed++;
         ships[ship.getNumber() - 1] = ship;
         for (int m = 0, ix = ship.getStartI(), jy = ship.getStartJ(); m < ship.getSize(); m++) {
            seaGrid[ix][jy] = ship.getNumber();
            ix += (ship.getDir() == 0) ? 1 : 0;
            jy += (ship.getDir() != 0) ? 1 : 0;
         }
      } else {
         seaGrid[i + 1][j + 1] = resultFlag;
      }
   }
}
