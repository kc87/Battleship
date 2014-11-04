package main;

import gui.EnemyFleedView;
import gui.GridButtonHandler;
import gui.MainView;
import gui.OwnFleedView;
import model.EnemyFleedModel;
import model.OwnFleedModel;

/**
 * Created by citizen4 on 03.11.2014.
 */
public final class GameContext
{
   public static final String TITLE = "P2P Battleship";
   public static final String VERSION = "0.1";
   //XXX: for debugging only!!
   public static String localBindAddress = null;
   public static MainView mainView = null;

   //global for now
   public static OwnFleedView myFleedView = new OwnFleedView();
   public static EnemyFleedView enemyFleedView = new EnemyFleedView(new GridButtonHandler());

   private GameContext()
   {
   }
}
