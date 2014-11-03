package gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by citizen4 on 03.11.2014.
 */
public class Const
{
   public static final Border PANEL_BORDER = BorderFactory.createLineBorder(new Color(0, 0, 0), 2);
   public static final Border SHIP_BORDER = BorderFactory.createLineBorder(new Color(0, 0, 0), 3);
   public static final Border WATER_BORDER = BorderFactory.createLineBorder(new Color(66, 66, 166), 1);
   public static final Border DESTROYED_BORDER = BorderFactory.createLineBorder(new Color(166, 166, 166), 1);

   public static final Color WATER_COLOR = new Color(100, 100, 255);
   public static final Color HIT_COLOR = new Color(250, 50, 50);
   public static final Color EMPTY_COLOR = new Color(188, 188, 188);
   public static final Color DESTROYED_COLOR = new Color(166, 166, 166);
   public static final Color SHIP_COLOR = new Color(0, 0, 0);
}
