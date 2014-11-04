package controller;

import model.AbstractFleedModel;
import model.EnemyFleedModel;
import model.OwnFleedModel;
import net.NetController;
import net.protocol.Message;

/**
 * Created by citizen4 on 03.11.2014.
 */
public final class GameEngine implements NetController.Listener, AbstractFleedModel.Listener
{
   private static final GameEngine INSTANCE = new GameEngine();
   private NetController netController = null;
   private OwnFleedModel ownFleedModel = null;
   private EnemyFleedModel enemyFleedModel = null;
   private IGameState currentState = null;


   private GameEngine()
   {
      this.netController = new NetController(this);
   }

   public static GameEngine getInstance()
   {
      return INSTANCE;
   }

   public void start()
   {
      netController.startReceiverThread();
   }

   public void stop()
   {
      netController.stopReceiverThread();
   }

   public void setState(final IGameState newState)
   {

   }


   @Override
   public void onMessage(Message newMsg, String peerId)
   {

   }

   @Override
   public void onError(String errMsg)
   {

   }

   @Override
   public void onUpdate()
   {

   }

   public interface StateListener
   {
      public void onStateChange();
   }

   public interface ViewListener
   {
      public void onUpdateView();
   }

}
