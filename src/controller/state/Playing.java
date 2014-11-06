package controller.state;

import controller.GameEngine;
import gui.Dialogs;
import net.NetController;
import net.protocol.Message;

/**
 * Created by citizen4 on 04.11.2014.
 */
public class Playing extends GameStateAdapter
{
   private GameEngine engine = null;

   public Playing(final GameEngine engine)
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
      Dialogs.showInfo("Already connected to Player: " + engine.getConnectedPeerId());
   }

   @Override
   public void disconnectPeer()
   {
      Message disconnectMsg = new Message();
      disconnectMsg.SUB_TYPE = Message.DISCONNECT;
      engine.getNetController().sendMessage(disconnectMsg, engine.getConnectedPeerId().split(":")[0]);
      engine.setState(new Disconnected(engine));
   }

   @Override
   public void newGame()
   {
      Dialogs.showInfo("Abort running game first!");
   }

   @Override
   public void abortGame()
   {
      Message abortGameMsg = new Message();
      abortGameMsg.TYPE = Message.GAME;
      abortGameMsg.SUB_TYPE = Message.ABORT;
      engine.getNetController().sendMessage(abortGameMsg, engine.getConnectedPeerId().split(":")[0]);
      engine.setPlayerEnabled(true);
      engine.setState(new PeerReady(engine));
   }

   @Override
   public void shoot(final int i, final int j)
   {
      Message bombMsg = new Message();
      bombMsg.TYPE = Message.GAME;
      bombMsg.SUB_TYPE = Message.SHOOT;
      bombMsg.PAYLOAD = new Object[]{i, j};
      engine.getNetController().sendMessage(bombMsg, engine.getConnectedPeerId().split(":")[0]);
      engine.getShotClock().reset();
   }


   @Override
   public void stopNetReceiver()
   {
      disconnectPeer();
      engine.getNetController().stopReceiverThread();
      engine.setState(new Stopped(engine));
   }
}
