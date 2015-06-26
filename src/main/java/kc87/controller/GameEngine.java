package kc87.controller;

import kc87.controller.state.Disconnected;
import kc87.controller.state.GameState;
import kc87.controller.state.PeerReady;
import kc87.controller.state.Playing;
import kc87.controller.state.Started;
import kc87.gui.Dialogs;
import kc87.gui.controller.MainView;
import kc87.model.AbstractFleetModel;
import kc87.model.AbstractFleetModel.ModelUpdateListener;
import kc87.model.EnemyFleetModel;
import kc87.model.OwnFleetModel;
import kc87.model.Ship;
import kc87.net.NetController;
import kc87.net.protocol.Message;

import java.util.ArrayList;
import java.util.Optional;


public final class GameEngine implements NetController.Listener, ShotClock.Listener
{
   private static final GameEngine INSTANCE = new GameEngine();
   private ShotClock shotClock = null;
   private String connectedPeerId = null;
   private NetController netController = null;
   private OwnFleetModel ownFleetModel = null;
   private EnemyFleetModel enemyFleetModel = null;
   private Optional<MainView> mainView = Optional.empty();
   private ModelUpdateListener ownFleetModelUpdateListener = null;
   private ModelUpdateListener enemyFleetModelUpdateListener = null;
   private boolean myTurnFlag = false;
   private boolean gotAWinner = false;
   private GameState currentState = new Started(this);


   private GameEngine()
   {
      this.netController = new NetController(this);
      this.shotClock = new ShotClock(this);
   }

   public static GameEngine getInstance()
   {
      return INSTANCE;
   }

   public void setMainView(final MainView mainView)
   {
      this.mainView = Optional.ofNullable(mainView);
   }

   public void setModelUpdateListener(final ModelUpdateListener own, final ModelUpdateListener enemy)
   {
      ownFleetModelUpdateListener = own;
      enemyFleetModelUpdateListener = enemy;
   }

   public void setState(final GameState newState)
   {
      currentState = newState;
      mainView.ifPresent(MainView::updateState);
   }

   public NetController getNetController()
   {
      return netController;
   }

   public Optional<String> getConnectedPeerIp()
   {
      return (connectedPeerId != null) ? Optional.of(connectedPeerId.split(":")[0]) : Optional.<String>empty();
   }

   public void setConnectedPeerId(final String newPeerId)
   {
      connectedPeerId = newPeerId;
   }

   public String getStateName()
   {
      return currentState.getClass().getSimpleName();
   }

   public ShotClock getShotClock()
   {
      return shotClock;
   }

   public void startNetReceiver()
   {
      currentState.startNetReceiver();
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
      if (getStateName().equals("Playing")) {
         Message bombMsg = new Message();
         bombMsg.TYPE = Message.GAME;
         bombMsg.SUB_TYPE = Message.SHOOT;
         bombMsg.PAYLOAD = new Object[]{i, j};
         netController.sendMessage(bombMsg, getConnectedPeerIp().get());
         shotClock.start();
      }
   }

   // FIXME: This method obviously needs some refactoring ;)
   @Override
   public void onMessage(Message msg, final String peerId)
   {
      switch (getStateName()) {
         case "Disconnected":
            if (msg.TYPE == Message.CTRL && msg.SUB_TYPE == Message.CONNECT) {
               msg.ACK_FLAG = true;
               msg.RST_FLAG = false;
               connectedPeerId = peerId;
               netController.sendMessage(msg, getConnectedPeerIp().get());
               setState(new PeerReady(this));
            }
            break;
         case "Connecting":
            if (msg.TYPE == Message.CTRL && msg.SUB_TYPE == Message.CONNECT) {

               Dialogs.closeCancelMsg();

               if (msg.ACK_FLAG && !msg.RST_FLAG) {
                  connectedPeerId = peerId;
                  setState(new PeerReady(this));
               }

               if (msg.ACK_FLAG && msg.RST_FLAG) {
                  setState(new Disconnected(this));
                  Dialogs.showOkMsg("Connection rejected!");
               }
            }
            break;
         case "PeerReady":
         case "Playing":

            // Ignore any but our peer
            if (!connectedPeerId.equals(peerId)) {
               return;
            }

            if (msg.TYPE == Message.CTRL) {
               if (msg.SUB_TYPE == Message.DISCONNECT) {
                  connectedPeerId = null;
                  shotClock.stop();
                  setState(new Disconnected(this));
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
                  setPlayerEnabled(true);
                  shotClock.stop();
                  setState(new PeerReady(this));
                  //FIXME
                  if (!gotAWinner) {
                     Dialogs.showOkMsg("Game aborted by peer!");
                  }
               }

               if (msg.SUB_TYPE == Message.TIMEOUT) {
                  setPlayerEnabled(true);
               }

               if (msg.SUB_TYPE == Message.SHOOT) {

                  ArrayList payload = (ArrayList) msg.PAYLOAD;

                  int resultFlag;
                  int i = ((Double) payload.get(0)).intValue();
                  int j = ((Double) payload.get(1)).intValue();

                  if (msg.ACK_FLAG) {

                     resultFlag = ((Double) payload.get(2)).intValue();
                     Ship ship = msg.SHIP;

                     enemyFleetModel.update(i, j, resultFlag, ship);

                     if (enemyFleetModel.isFleetDestroyed()) {
                        gotAWinner = true;
                        mainView.ifPresent(mv -> mv.updateScore(ownFleetModel.getShipsLeft(), 0));
                        //FIXME: should be currentState.finishGame();
                        currentState.abortGame();
                        Dialogs.showOkMsg("You are the Winner!");
                        return;
                     }

                     myTurnFlag = resultFlag == AbstractFleetModel.HIT || resultFlag == AbstractFleetModel.DESTROYED;

                  } else {

                     Object[] result = ownFleetModel.update(i, j);

                     resultFlag = (Integer) result[0];
                     Ship ship = (Ship) result[1];

                     msg.ACK_FLAG = true;
                     msg.PAYLOAD = new Object[]{i, j, resultFlag};
                     msg.SHIP = ship;
                     netController.sendMessage(msg, getConnectedPeerIp().get());

                     if (ownFleetModel.isFleetDestroyed()) {
                        gotAWinner = true;
                        mainView.ifPresent(mv -> mv.updateScore(0, enemyFleetModel.getShipsLeft()));
                        Dialogs.showOkMsg("You lose!");
                        return;
                     }

                     myTurnFlag = !(resultFlag == AbstractFleetModel.HIT || resultFlag == AbstractFleetModel.DESTROYED);
                  }

                  if (resultFlag == AbstractFleetModel.DESTROYED) {
                     mainView.ifPresent(mv -> mv.updateScore(ownFleetModel.getShipsLeft(),
                             enemyFleetModel.getShipsLeft()));
                  }

                  setPlayerEnabled(myTurnFlag);
               }
            }

            break;
         default:
            //TODO: Maybe send some sort of reject message ??
            break;
      }
   }

   private void startNewGame()
   {
      gotAWinner = false;
      ownFleetModel = new OwnFleetModel(ownFleetModelUpdateListener);
      enemyFleetModel = new EnemyFleetModel(enemyFleetModelUpdateListener);
      mainView.ifPresent(mv -> mv.updateScore(AbstractFleetModel.NUMBER_OF_SHIPS, AbstractFleetModel.NUMBER_OF_SHIPS));
      setPlayerEnabled(myTurnFlag);
   }

   public void setPlayerEnabled(final boolean enable)
   {
      if (enable) {
         shotClock.start();
      } else {
         shotClock.stop();
      }
      mainView.ifPresent(mv -> mv.enableEnemyView(enable));
   }

   @Override
   public void onTick(final int tick)
   {
      mainView.ifPresent(mv -> mv.updateShotClock(tick));
   }

   @Override
   public void onTimeIsUp()
   {
      Message timeoutMsg = new Message();
      timeoutMsg.TYPE = Message.GAME;
      timeoutMsg.SUB_TYPE = Message.TIMEOUT;
      netController.sendMessage(timeoutMsg, getConnectedPeerIp().get());
      setPlayerEnabled(false);
   }
}
