package kc87.model;

public class EnemyFleetModel extends AbstractFleetModel
{
   public EnemyFleetModel(final ModelUpdateListener updateListener)
   {
      super(updateListener);
      listener.ifPresent(l -> l.onTotalUpdate(this));
   }

   public void update(final int i, final int j, final int resultFlag, final Ship ship)
   {
      if (ship != null) {
         shipsDestroyed++;
         ships[ship.getNumber() - 1] = ship;
         for (int m = 0, ix = ship.getStartI(), jy = ship.getStartJ(); m < ship.getSize(); m++) {
            seaGrid[ix][jy] = ship.getNumber();
            if(listener.isPresent()) {
               listener.get().onPartialUpdate(this, ix - 1, jy - 1, AbstractFleetModel.DESTROYED);
            }
            ix += (ship.getDir() == 0) ? 1 : 0;
            jy += (ship.getDir() != 0) ? 1 : 0;
         }
      } else {
         seaGrid[i + 1][j + 1] = resultFlag;
         listener.ifPresent(l -> l.onPartialUpdate(this, i, j, resultFlag));
      }
   }
}
