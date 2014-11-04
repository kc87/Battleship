package controller.state;

import controller.GameEngine;
import gui.Dialogs;
import net.NetController;
import net.protocol.Message;

/**
 * Created by citizen4 on 04.11.2014.
 */
public class Playing implements IGameState
{
   private GameEngine engine = null;

   public Playing(final GameEngine engine)
   {
      this.engine = engine;
   }

   @Override
   public void startNetReveiver(NetController netController)
   {

   }

   @Override
   public void connectPeer(final NetController netController)
   {
      Dialogs.showInfo("Already connected to Player: " + engine.getConnectedPeerId());
   }

   @Override
   public void disconnectPeer(final NetController netController)
   {
      Message disconnectMsg = new Message();
      disconnectMsg.SUB_TYPE = Message.DISCONNECT;
      netController.sendMessage(disconnectMsg, engine.getConnectedPeerId().split(":")[0]);
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
      engine.setState(new PeerReady(engine));
   }

   @Override
   public void stopNetReceiver(NetController netController)
   {
      disconnectPeer(netController);
      netController.stopReceiverThread();
      engine.setState(new Stopped(engine));
   }
}
