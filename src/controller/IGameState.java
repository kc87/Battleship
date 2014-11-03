package controller;

/**
 * Created by citizen4 on 03.11.2014.
 * <p/>
 * Defines state transitions
 */
public interface IGameState
{
   public void connectPeer();

   public void disconnectPeer();

   public void newGame();

   public void abortGame();
   //...
}
