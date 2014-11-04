package controller.state;

import controller.GameEngine;
import gui.Dialogs;
import net.NetController;

/**
 * Created by citizen4 on 04.11.2014.
 */
public class Connecting implements IGameState
{

   private GameEngine engine = null;

   public Connecting(final GameEngine engine)
   {
      this.engine = engine;
   }

   @Override
   public void startNetReveiver(NetController netController)
   {

   }

   @Override
   public void connectPeer(NetController netController)
   {
      Dialogs.showInfo("Connection in progress!");
   }

   @Override
   public void disconnectPeer(NetController netController)
   {

   }

   @Override
   public void newGame()
   {
      Dialogs.showInfo("No Player connected yet!");
   }

   @Override
   public void abortGame()
   {
      Dialogs.showInfo("No Game running!");
   }

   @Override
   public void stopNetReceiver(NetController netController)
   {
      netController.stopReceiverThread();
      engine.setState(new Stopped(engine));
   }
}
