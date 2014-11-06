package controller.state;

import controller.GameEngine;
import gui.Dialogs;

/**
 * Created by citizen4 on 04.11.2014.
 */
public class Connecting extends GameStateAdapter
{

   private GameEngine engine = null;

   public Connecting(final GameEngine engine)
   {
      this.engine = engine;
   }

   @Override
   public void startNetReveiver()
   {

   }

   @Override
   public void connectPeer()
   {
      Dialogs.showInfo("Connection in progress!");
   }

   @Override
   public void disconnectPeer()
   {
      engine.setState(new Disconnected(engine));
      Dialogs.showInfo("Connection attempt aborted by user!");
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
   public void stopNetReceiver()
   {
      engine.getNetController().stopReceiverThread();
      engine.setState(new Stopped(engine));
   }
}
