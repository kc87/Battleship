package gui;

import controller.GameEngine;
import model.SeaArea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GridButtonHandler implements ActionListener
{
   private boolean isEnabled = true;

   @Override
   public void actionPerformed(ActionEvent e)
   {
      if (!isEnabled) {
         return;
      }

      String cmd = e.getActionCommand();
      int buttonIndex = Integer.parseInt(cmd);

      int i = (int) buttonIndex % SeaArea.DIM;
      int j = (int) buttonIndex / SeaArea.DIM;

      GameEngine.getInstance().shoot(i, j);
   }

   public void setEnabled(final boolean enabled)
   {
      isEnabled = enabled;
   }

}
