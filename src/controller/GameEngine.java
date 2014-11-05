package controller;

import controller.state.Disconnected;
import controller.state.IGameState;
import controller.state.PeerReady;
import controller.state.Playing;
import controller.state.Started;
import gui.Dialogs;
import main.GameContext;
import model.AbstractFleedModel;
import model.AbstractFleedModel.ModelUpdateListener;
import model.EnemyFleedModel;
import model.OwnFleedModel;
import model.Ship;
import net.NetController;
import net.protocol.Message;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;

/**
 * Created by citizen4 on 03.11.2014.
 *
 * Main Game Controller Singleton
 */
public final class GameEngine implements NetController.Listener
{
   private static final GameEngine INSTANCE = new GameEngine();
   private String connectedPeerId = null;
   private NetController netController = null;
   private OwnFleedModel ownFleedModel = null;
   private EnemyFleedModel enemyFleedModel = null;
   private ModelUpdateListener ownFleedModelUpdateListener = null;
   private ModelUpdateListener enemyFleedModelUpdateListener = null;
   private StateListener stateListener = null;
   private ScoreListener scoreListener = null;
   private boolean myTurnFlag = false;
   private IGameState currentState = new Started(this);


   private GameEngine()
   {
      this.netController = new NetController(this);
   }

   public static GameEngine getInstance()
   {
      return INSTANCE;
   }

   public void setModelUpdateListener(final ModelUpdateListener own, final ModelUpdateListener enemy)
   {
      ownFleedModelUpdateListener = own;
      enemyFleedModelUpdateListener = enemy;
   }

   public void setStateListener(final StateListener listener)
   {
      stateListener = listener;
   }

   public void setScoreListener(final ScoreListener listener)
   {
      scoreListener = listener;
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
                  myTurnFlag = msg.ACK_FLAG;
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

                  ArrayList payload = (ArrayList) msg.PAYLOAD;

                  int resultFlag;
                  int i = ((Double) payload.get(0)).intValue();
                  int j = ((Double) payload.get(1)).intValue();

                  if (msg.ACK_FLAG) {

                     resultFlag = ((Double) payload.get(2)).intValue();
                     Ship ship = msg.SHIP;

                     enemyFleedModel.update(i, j, resultFlag, ship);

                     if (enemyFleedModel.isFleedDestroyed()) {
                        Logger.debug("You are the Winner!");
                        Dialogs.showInfoThread("You are the Winner!");
                        scoreListener.onScoreUpdate(ownFleedModel.getShipsLeft(), 0);
                        currentState.abortGame();
                        return;
                     }

                     myTurnFlag = resultFlag == AbstractFleedModel.HIT || resultFlag == AbstractFleedModel.DESTROYED;

                  } else {

                     Object[] result = ownFleedModel.update(i, j);

                     resultFlag = (Integer) result[0];
                     Ship ship = (Ship) result[1];

                     msg.ACK_FLAG = true;
                     msg.PAYLOAD = new Object[]{i, j, resultFlag};
                     msg.SHIP = ship;
                     netController.sendMessage(msg, connectedPeerId.split(":")[0]);

                     if (ownFleedModel.isFleedDestroyed()) {
                        Logger.debug("You lose!");
                        Dialogs.showInfoThread("You lose!");
                        scoreListener.onScoreUpdate(0, enemyFleedModel.getShipsLeft());
                        return;
                     }

                     myTurnFlag = !(resultFlag == AbstractFleedModel.HIT || resultFlag == AbstractFleedModel.DESTROYED);
                  }

                  if (resultFlag == AbstractFleedModel.DESTROYED) {
                     scoreListener.onScoreUpdate(ownFleedModel.getShipsLeft(), enemyFleedModel.getShipsLeft());
                  }

                  setPlayerEnabled(myTurnFlag);

               }
            }

            break;
         default:
            //TODO
            break;
      }
   }

   private void startNewGame()
   {
      ownFleedModel = new OwnFleedModel(ownFleedModelUpdateListener);
      enemyFleedModel = new EnemyFleedModel(enemyFleedModelUpdateListener);
      scoreListener.onScoreUpdate(AbstractFleedModel.NUMBER_OF_SHIPS, AbstractFleedModel.NUMBER_OF_SHIPS);
      setPlayerEnabled(myTurnFlag);
   }

   private void setPlayerEnabled(final boolean enable)
   {
      GameContext.enemyFleedView.setEnabled(enable);
   }

   @Override
   public void onError(String errMsg)
   {
      //TODO
   }

   public interface StateListener
   {
      public void onStateChange(final IGameState newState);
   }

   public interface ScoreListener
   {
      public void onScoreUpdate(final int myShips, final int enemyShips);
   }

}
