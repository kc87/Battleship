package gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by citizen4 on 03.11.2014.
 */
public final class Const
{
   public static final String DEAD_SYMBOL = "\u2620";

   public static final Border PANEL_BORDER = BorderFactory.createLineBorder(new Color(0, 0, 0), 2);
   public static final Border SHIP_BORDER = BorderFactory.createLineBorder(new Color(0, 0, 0), 1);
   public static final Border WATER_BORDER = BorderFactory.createLineBorder(new Color(66, 66, 255), 1);
   public static final Border HIT_BORDER = BorderFactory.createLineBorder(new Color(240, 0, 0), 1);
   public static final Border DESTROYED_BORDER = BorderFactory.createLineBorder(new Color(188, 188, 188), 1);

   public static final Color GAME_PANEL_COLOR = new Color(222, 222, 222);
   public static final Color OWN_PANEL_COLOR = new Color(166, 255, 166);
   public static final Color ENEMY_PANEL_COLOR = new Color(255, 166, 166);

   public static final Color WATER_COLOR = new Color(122, 122, 255);
   public static final Color WATER_MISS_COLOR = new Color(88, 88, 255);
   public static final Color HIT_COLOR = new Color(250, 10, 10);
   public static final Color EMPTY_COLOR = new Color(188, 188, 188);
   public static final Color DESTROYED_COLOR = new Color(200, 200, 200);
   public static final Color SHIP_COLOR = new Color(0, 0, 0);

   private Const()
   {
   }
}
