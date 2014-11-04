package controller.state;

import controller.GameEngine;
import gui.Dialogs;
import net.NetController;
import net.protocol.Message;

/**
 * Created by citizen4 on 04.11.2014.
 */
public class PeerReady implements IGameState
{

   private GameEngine engine = null;

   public PeerReady(final GameEngine engine)
   {
      this.engine = engine;
   }

   @Override
   public void startNetReveiver(final NetController netController)
   {

   }

   @Override
   public void connectPeer(final NetController netController)
   {
      Dialogs.showInfo("Disconnect current Player first!");
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
      Message newGameMsg = new Message();
      newGameMsg.TYPE = Message.GAME;
      newGameMsg.SUB_TYPE = Message.NEW;
      engine.getNetController().sendMessage(newGameMsg, engine.getConnectedPeerId().split(":")[0]);
      //engine.setState(new Playing(engine));
   }

   @Override
   public void abortGame()
   {
      Dialogs.showInfo("No running game to abort!");
   }

   @Override
   public void stopNetReceiver(final NetController netController)
   {
      disconnectPeer(netController);
      netController.stopReceiverThread();
      engine.setState(new Stopped(engine));
   }
}
