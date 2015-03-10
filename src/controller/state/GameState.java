package controller.state;

public interface GameState
{
   public void startNetReceiver();

   public void connectPeer();

   public void disconnectPeer();

   public void newGame();

   public void abortGame();

   public void stopNetReceiver();

}
