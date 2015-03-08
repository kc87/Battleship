package controller.state;

import controller.GameEngine;

public class Stopped extends GameStateAdapter
{
   private GameEngine engine = null;

   public Stopped(final GameEngine engine)
   {
      this.engine = engine;
   }

   @Override
   public void startNetReceiver()
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
}
