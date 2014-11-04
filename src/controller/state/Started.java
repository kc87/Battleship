package controller.state;

import controller.GameEngine;
import net.NetController;
import org.pmw.tinylog.Logger;

/**
 * Created by citizen4 on 04.11.2014.
 */
public class Started implements IGameState
{
   private GameEngine engine = null;

   public Started(final GameEngine engine)
   {
      this.engine = engine;
   }


   @Override
   public void startNetReveiver(final NetController netController)
   {
      netController.startReceiverThread();
      engine.setState(new Disconnected(engine));
   }

   @Override
   public void connectPeer(final NetController netController)
   {
      Logger.error("Wrong state transition");
   }

   @Override
   public void disconnectPeer(final NetController netController)
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
   public void stopNetReceiver(final NetController netController)
   {
      engine.setState(new Stopped(engine));
   }


}
