package gui;

import model.Sea;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by an unknown Java student on 11/3/14.
 */
public class FleedView extends JPanel
{
   public static final Border SHIP_BORDER = BorderFactory.createLineBorder(new Color(0, 0, 0), 3);
   public static final Border WATER_BORDER = BorderFactory.createLineBorder(new Color(66, 66, 166), 1);
   public static final Border DESTROYED_BORDER = BorderFactory.createLineBorder(new Color(166, 166, 166), 1);

   public static final Color WATER_COLOR = new Color(100, 100, 255);
   public static final Color HIT_COLOR = new Color(250, 50, 50);
   public static final Color EMPTY_COLOR = new Color(188, 188, 188);
   public static final Color DESTROYED_COLOR = new Color(166, 166, 166);
   public static final Color SHIP_COLOR = new Color(0, 0, 0);

   protected JPanel nPanel, sPanel, ePanel, wPanel, gridPanel;

   protected SeaGridButton[][] gridButtons = new SeaGridButton[Sea.DIM][Sea.DIM];
   //public int[][] gridArray = new int[(DIM+2)][(DIM+2)];

   private TitledBorder panelTitleBorder;


   public FleedView(final String title)
   {


   }

}
