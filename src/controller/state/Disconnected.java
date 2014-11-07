package controller.state;

import controller.GameEngine;
import gui.Dialogs;
import net.protocol.Message;
import org.pmw.tinylog.Logger;

public class Disconnected extends GameStateAdapter
{

   private GameEngine engine = null;

   public Disconnected(final GameEngine engine)
   {
      this.engine = engine;
   }

   @Override
   public void startNetReveiver()
   {
      Logger.warn("Net Receiver already up and running");
   }

   @Override
   public void connectPeer()
   {
      String peerIp;
      if ((peerIp = Dialogs.requestPeerIp()) != null) {
         Message connectMsg = new Message();
         connectMsg.SUB_TYPE = Message.CONNECT;
         engine.getNetController().sendMessage(connectMsg, peerIp);
         engine.setState(new Connecting(engine));
         if (Dialogs.showCancelMsg("Connecting...") == 0) {
            //connection attempt aborted by user
            engine.setState(new Disconnected(engine));
         }
      }
   }

   @Override
   public void disconnectPeer()
   {
      Dialogs.showOkMsg("No Player connected!");
   }

   @Override
   public void newGame()
   {
      Dialogs.showOkMsg("No Player connected!");
   }

   @Override
   public void abortGame()
   {
      Dialogs.showOkMsg("No Game running!");
   }

   @Override
   public void stopNetReceiver()
   {
      engine.getNetController().stopReceiverThread();
      engine.setState(new Stopped(engine));
   }

}
