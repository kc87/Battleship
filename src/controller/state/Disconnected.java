package controller.state;

import controller.GameEngine;
import gui.Dialogs;
import net.NetController;
import net.protocol.Message;
import org.pmw.tinylog.Logger;

/**
 * Created by citizen4 on 04.11.2014.
 */
public class Disconnected implements IGameState
{

   private GameEngine engine = null;

   public Disconnected(final GameEngine engine)
   {
      this.engine = engine;
   }

   @Override
   public void startNetReveiver(final NetController netController)
   {
      Logger.warn("Net Receiver already up and running");
   }

   @Override
   public void connectPeer(final NetController netController)
   {
      String peerIp = null;
      if ((peerIp = Dialogs.requestPeerIp()) != null) {
         Message connectMsg = new Message();
         connectMsg.SUB_TYPE = Message.CONNECT;
         netController.sendMessage(connectMsg, peerIp);
         engine.setState(new Connecting(engine));
      }
   }

   @Override
   public void disconnectPeer(final NetController netController)
   {
      Dialogs.showInfo("No Player connected!");
   }

   @Override
   public void newGame()
   {
      Dialogs.showInfo("No Player connected!");
   }

   @Override
   public void abortGame()
   {
      Dialogs.showInfo("No Game running!");
   }

   @Override
   public void stopNetReceiver(final NetController netController)
   {
      netController.stopReceiverThread();
      engine.setState(new Stopped(engine));
   }

   /*
   private void connectPeerActive()
   {

   }

   private void connectPeerPassive()
   {

   }*/

}
