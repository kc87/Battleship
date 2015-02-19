package controller.state;

public interface IGameState
{
   public void startNetReceiver();

   public void connectPeer();

   public void disconnectPeer();

   public void newGame();

   public void abortGame();

   public void stopNetReceiver();

   public void shoot(final int i, final int j);
}
