package controller.state;

import net.NetController;

/**
 * Created by an unknown Java student on 11/4/14.
 */
public abstract class GameStateAdapter implements IGameState
{
   @Override
   public void startNetReveiver()
   {
   }

   @Override
   public void connectPeer()
   {
   }

   @Override
   public void disconnectPeer()
   {
   }

   @Override
   public void newGame()
   {
   }

   @Override
   public void abortGame()
   {
   }

   @Override
   public void stopNetReceiver()
   {
   }

   @Override
   public void shoot(final int i, final int j)
   {
   }
}
