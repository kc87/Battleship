package controller.state;

import controller.GameEngine;
import org.pmw.tinylog.Logger;

public class Started extends GameStateAdapter
{
   private GameEngine engine = null;

   public Started(final GameEngine engine)
   {
      this.engine = engine;
   }

   @Override
   public void startNetReceiver()
   {
      engine.getNetController().startReceiverThread();
      engine.setState(new Disconnected(engine));
   }

   @Override
   public void connectPeer()
   {
      Logger.error("Wrong state transition");
   }

   @Override
   public void disconnectPeer()
   {
      Logger.error("Wrong state transition");
   }

   @Override
   public void newGame()
   {
      Logger.error("Wrong state transition");
   }

   @Override
   public void abortGame()
   {
      Logger.error("Wrong state transition");
   }

   @Override
   public void stopNetReceiver()
   {
      engine.setState(new Stopped(engine));
   }


}
