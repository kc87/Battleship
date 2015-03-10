package controller.state;

import org.pmw.tinylog.Logger;

public interface GameState
{
   default void startNetReceiver()
   {
      Logger.error("Invalid state transition");
   }

   default void connectPeer()
   {
      Logger.error("Invalid state transition");
   }

   default void disconnectPeer()
   {
      Logger.error("Invalid state transition");
   }

   default void newGame()
   {
      Logger.error("Invalid state transition");
   }

   default void abortGame()
   {
      Logger.error("Invalid state transition");
   }

   default void stopNetReceiver()
   {
      Logger.error("Invalid state transition");
   }
}
