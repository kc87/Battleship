package controller.state;

import controller.GameEngine;
import net.NetController;
import sun.nio.ch.Net;

/**
 * Created by citizen4 on 04.11.2014.
 */
public class Stopped implements IGameState
{
   private GameEngine engine = null;

   public Stopped(final GameEngine engine)
   {
      this.engine = engine;
   }

   @Override
   public void startNetReveiver(final NetController netController)
   {

   }

   @Override
   public void connectPeer(final NetController netController)
   {

   }

   @Override
   public void disconnectPeer(final NetController netController)
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
   public void stopNetReceiver(final NetController netController)
   {

   }
}
