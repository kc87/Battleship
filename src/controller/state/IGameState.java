package controller.state;

import net.NetController;

/**
 * Created by citizen4 on 03.11.2014.
 * <p/>
 * Defines state transitions
 */
public interface IGameState
{
   public void startNetReveiver();

   public void connectPeer();

   public void disconnectPeer();

   public void newGame();

   public void abortGame();

   public void stopNetReceiver();

   public void shoot(final int i, final int j);
   //...
}
