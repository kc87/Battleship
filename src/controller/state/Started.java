package controller.state;

import controller.GameEngine;
import org.pmw.tinylog.Logger;

public class Started implements GameState
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
   public void stopNetReceiver()
   {
      engine.setState(new Stopped(engine));
   }


}
