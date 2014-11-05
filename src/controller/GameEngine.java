package controller;

import controller.state.Disconnected;
import controller.state.IGameState;
import controller.state.PeerReady;
import controller.state.Playing;
import controller.state.Stopped;
import controller.state.Started;
import gui.Dialogs;
import main.GameContext;
import model.AbstractFleedModel;
import model.EnemyFleedModel;
import model.OwnFleedModel;
import net.NetController;
import net.protocol.Message;
import org.pmw.tinylog.Logger;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by citizen4 on 03.11.2014.
 */
public final class GameEngine implements NetController.Listener, AbstractFleedModel.Listener
{
   private static final GameEngine INSTANCE = new GameEngine();
   private String connectedPeerId = null;
   private NetController netController = null;
   private OwnFleedModel ownFleedModel = null;
   private EnemyFleedModel enemyFleedModel = null;
   private StateListener stateListener = null;
   private ViewListener viewListener = null;

   private IGameState currentState = new Started(this);


   private GameEngine()
   {
      this.netController = new NetController(this);
   }

   public static GameEngine getInstance()
   {
      return INSTANCE;
   }

   public void setStateListener(final StateListener listener)
   {
      stateListener = listener;
   }

   public void setState(final IGameState newState)
   {
      currentState = newState;
      if (stateListener != null) {
         stateListener.onStateChange(newState);
      }
   }

   public NetController getNetController()
   {
      return netController;
   }

   public String getConnectedPeerId()
   {
      return connectedPeerId;
   }

   public String getStateName()
   {
      return currentState.getClass().getSimpleName();
   }

   public IGameState getState()
   {
      return currentState;
   }

   public void startNetReveiver()
   {
      currentState.startNetReveiver();
   }

   public void stopNetReceiver()
   {
      currentState.stopNetReceiver();
   }

   public void connectPeer()
   {
      currentState.connectPeer();
   }

   public void disconnectPeer()
   {
      currentState.disconnectPeer();
   }

   public void newGame()
   {
      currentState.newGame();
   }

   public void abortGame()
   {
      currentState.abortGame();
   }

   public void shoot(final int i, final int j)
   {
      currentState.shoot(i, j);
   }

   /*
   public void startApplication()
   {

   }

   public void quitApplication()
   {

   }*/

   @Override
   public void onMessage(Message msg, final String peerId)
   {
      switch (getStateName()) {
         case "Disconnected":
            if (msg.TYPE == Message.CTRL && msg.SUB_TYPE == Message.CONNECT) {
               msg.ACK_FLAG = true;
               msg.RST_FLAG = false;
               netController.sendMessage(msg, peerId.split(":")[0]);
               connectedPeerId = peerId;
               setState(new PeerReady(this));
            }
            break;
         case "Connecting":
            if (msg.TYPE == Message.CTRL && msg.SUB_TYPE == Message.CONNECT) {
               if (msg.ACK_FLAG && !msg.RST_FLAG) {
                  connectedPeerId = peerId;
                  setState(new PeerReady(this));
               }

               if (msg.ACK_FLAG && msg.RST_FLAG) {
                  setState(new Disconnected(this));
                  Dialogs.showInfo("Connection rejected!");
               }
            }
            break;
         case "PeerReady":
         case "Playing":
            if (msg.TYPE == Message.CTRL) {
               if (msg.SUB_TYPE == Message.CONNECT) {
                  msg.ACK_FLAG = true;
                  msg.RST_FLAG = true;
                  netController.sendMessage(msg, peerId.split(":")[0]);
               }

               if (msg.SUB_TYPE == Message.DISCONNECT) {
                  //disconnect from our peer?
                  if (connectedPeerId.equals(peerId)) {
                     connectedPeerId = null;
                     setState(new Disconnected(this));
                  }
               }
            }

            if (msg.TYPE == Message.GAME) {
               if (msg.SUB_TYPE == Message.NEW) {
                  if (!msg.ACK_FLAG && !msg.RST_FLAG) {
                     msg.ACK_FLAG = true;
                     netController.sendMessage(msg, peerId.split(":")[0]);
                  }
                  setState(new Playing(this));
                  startNewGame();
               }

               if (msg.SUB_TYPE == Message.ABORT) {
                  setState(new PeerReady(this));
               }

               if (msg.SUB_TYPE == Message.SHOOT) {

                  ArrayList<Double> payload = (ArrayList<Double>) msg.PAYLOAD;

                  int i = payload.get(0).intValue();
                  int j = payload.get(1).intValue();

                  if (msg.ACK_FLAG) {

                     int result = payload.get(2).intValue();
                     GameContext.enemyFleedView.updateView(i, j, result);

                  } else {

                     int result = ownFleedModel.update(i, j);

                     msg.ACK_FLAG = true;
                     msg.PAYLOAD = new int[]{i, j, result};
                     netController.sendMessage(msg, connectedPeerId.split(":")[0]);

                     // Optimized view update
                     if (result != AbstractFleedModel.AGAIN && result != AbstractFleedModel.MISS) {
                        if (result == AbstractFleedModel.HIT) {
                           GameContext.myFleedView.updatePartialView(ownFleedModel, i, j);
                        } else {
                           GameContext.myFleedView.updateTotalView(ownFleedModel);
                        }
                     }
                  }
               }
            }

            break;
         default:
            msg.ACK_FLAG = true;
            msg.RST_FLAG = true;
            netController.sendMessage(msg, peerId);
            break;
      }
   }


   private void startNewGame()
   {
      ownFleedModel = new OwnFleedModel();
      ownFleedModel.placeNewFleed();
      //enemyFleedModel = new EnemyFleedModel();
      GameContext.myFleedView.updateTotalView(ownFleedModel);
   }

   @Override
   public void onError(String errMsg)
   {

   }

   @Override
   public void onUpdate(final AbstractFleedModel model)
   {

   }

   public interface StateListener
   {
      public void onStateChange(final IGameState newState);
   }

   public interface ViewListener
   {
      public void onUpdateView();
   }

}
