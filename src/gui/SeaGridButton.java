package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by an unknown Java student on 11/3/14.
 */
public class SeaGridButton extends JButton
{
   private static final int SIZE = 50;

   public SeaGridButton(final int index)
   {
      setPreferredSize(new Dimension(SIZE, SIZE));
      setFocusPainted(false);
      setActionCommand("" + index);
      setFont(GuiConstants.GRID_BUTTON_FONT);
   }

}
