package kc87.controller.state;

import kc87.controller.GameEngine;
import kc87.gui.Dialogs;
import kc87.net.protocol.Message;

public class Playing implements GameState
{
   private GameEngine engine = null;

   public Playing(final GameEngine engine)
   {
      this.engine = engine;
   }

   @Override
   public void connectPeer()
   {
      Dialogs.showOkMsg("Already connected to Player: " + engine.getConnectedPeerIp().get());
   }

   @Override
   public void disconnectPeer()
   {
      Message disconnectMsg = new Message();
      disconnectMsg.SUB_TYPE = Message.DISCONNECT;
      engine.getNetController().sendMessage(disconnectMsg, engine.getConnectedPeerIp().get());
      engine.setConnectedPeerId(null);
      engine.getShotClock().stop();
      engine.setState(new Disconnected(engine));
   }

   @Override
   public void newGame()
   {
      Dialogs.showOkMsg("Abort running game first!");
   }

   @Override
   public void abortGame()
   {
      Message abortGameMsg = new Message();
      abortGameMsg.TYPE = Message.GAME;
      abortGameMsg.SUB_TYPE = Message.ABORT;
      engine.getNetController().sendMessage(abortGameMsg, engine.getConnectedPeerIp().get());
      engine.setPlayerEnabled(true);
      engine.getShotClock().stop();
      engine.setState(new PeerReady(engine));
   }


   @Override
   public void stopNetReceiver()
   {
      disconnectPeer();
      engine.getNetController().stopReceiverThread();
      engine.setState(new Stopped(engine));
   }
}
