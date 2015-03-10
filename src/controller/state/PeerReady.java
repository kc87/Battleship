package controller.state;

import controller.GameEngine;
import gui.Dialogs;
import net.protocol.Message;


public class PeerReady implements GameState
{

   private GameEngine engine = null;

   public PeerReady(final GameEngine engine)
   {
      this.engine = engine;
   }

   @Override
   public void connectPeer()
   {
      Dialogs.showOkMsg("Disconnect current Player first!");
   }

   @Override
   public void disconnectPeer()
   {
      Message disconnectMsg = new Message();
      disconnectMsg.SUB_TYPE = Message.DISCONNECT;
      engine.getNetController().sendMessage(disconnectMsg, engine.getConnectedPeerIp());
      engine.setConnectedPeerId(null);
      engine.setState(new Disconnected(engine));
   }

   @Override
   public void newGame()
   {
      Message newGameMsg = new Message();
      newGameMsg.TYPE = Message.GAME;
      newGameMsg.SUB_TYPE = Message.NEW;
      engine.getNetController().sendMessage(newGameMsg, engine.getConnectedPeerIp());
   }

   @Override
   public void abortGame()
   {
      Dialogs.showOkMsg("No running game to abort!");
   }

   @Override
   public void stopNetReceiver()
   {
      disconnectPeer();
      engine.getNetController().stopReceiverThread();
      engine.setState(new Stopped(engine));
   }
}
