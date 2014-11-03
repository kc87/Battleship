package main;

import gui.Const;
import gui.FleedView;
import model.Fleed;

/**
 * Created by citizen4 on 03.11.2014.
 */
public class GameContext
{
   public static final String TITLE = "P2P Battleship";
   public static final String VERSION = "0.1";
   //XXX: for debugging only!!
   public static String localBindAddress = null;

   //global for now
   public static Fleed myFleed = new Fleed();
   public static Fleed enemyFleed = new Fleed();
   public static FleedView myFleedView = new FleedView(false);
   public static FleedView enemyFleedView = new FleedView(true);


}
