package kc87.controller.state;

public interface GameState
{
   default void startNetReceiver()
   {
      throw new IllegalStateException("Invalid state transition");
   }

   default void connectPeer()
   {
      throw new IllegalStateException("Invalid state transition");
   }

   default void disconnectPeer()
   {
      throw new IllegalStateException("Invalid state transition");
   }

   default void newGame()
   {
      throw new IllegalStateException("Invalid state transition");
   }

   default void abortGame()
   {
      throw new IllegalStateException("Invalid state transition");
   }

   default void stopNetReceiver()
   {
      throw new IllegalStateException("Invalid state transition");
   }
}
