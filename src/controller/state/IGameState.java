package controller.state;

import net.NetController;

/**
 * Created by citizen4 on 03.11.2014.
 * <p/>
 * Defines state transitions
 */
public interface IGameState
{
   public void startNetReveiver(final NetController netController);

   public void connectPeer(final NetController netController);

   public void disconnectPeer(final NetController netController);

   public void newGame();

   public void abortGame();

   public void stopNetReceiver(final NetController netController);
   //...
}
