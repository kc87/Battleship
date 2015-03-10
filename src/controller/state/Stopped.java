package controller.state;

import controller.GameEngine;

public class Stopped implements GameState
{
   private GameEngine engine = null;

   public Stopped(final GameEngine engine)
   {
      this.engine = engine;
   }
}
