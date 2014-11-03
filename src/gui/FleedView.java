package gui;

import model.Fleed;
import model.SeaArea;
import model.Ship;
import org.pmw.tinylog.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by an unknown Java student on 11/3/14.
 */
public class FleedView extends JPanel
{
   private static final String MY_TITLE = "My Fleed";
   private static final String ENEMY_TITLE = "Enemy Fleed";
   private static final String[] ALPHA_SCALE = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
   private static final int DIM = SeaArea.DIM;
   private static final int GRID_SIZE = 380;

   protected JPanel nNumberScale;
   protected JPanel sNumberScale;
   protected JPanel eAlphaScale;
   protected JPanel wAlphaScale;
   protected JPanel seaGridPanel;

   private boolean isEnemy = false;

   protected SeaGridButton[][] gridButtons = new SeaGridButton[DIM][DIM];
   //private Color fillColor;

   public FleedView(final boolean isEnemy)
   {
      this.isEnemy = isEnemy;
      setupFleedView();
   }

   public void updateView(final Fleed fleedModel)
   {
      for (int j = 0; j < DIM; j++) {
         for (int i = 0; i < DIM; i++) {

            int gridValue = fleedModel.getSeaGrid()[i + 1][j + 1];

            if (gridValue > 0) {

               Ship ship = fleedModel.getShips()[gridValue - 1];

               gridButtons[i][j].setBackground(new Color(0, 0, 0));
               gridButtons[i][j].setBorder(Const.SHIP_BORDER);
               gridButtons[i][j].setText("" + ship.getSize());
            } else {
               gridButtons[i][j].setBackground(isEnemy ? Const.EMPTY_COLOR : Const.WATER_COLOR);
               gridButtons[i][j].setBorder(Const.WATER_BORDER);
               gridButtons[i][j].setText("");
            }
         }
      }

   }

   public void resetSeaGrid()
   {
      for (int j = 0; j < DIM; j++) {
         for (int i = 0; i < DIM; i++) {
            gridButtons[i][j].setBackground(isEnemy ? Const.EMPTY_COLOR : Const.WATER_COLOR);
            gridButtons[i][j].setBorder(Const.WATER_BORDER);
            gridButtons[i][j].setText("");
         }
      }
   }


   private void setupFleedView()
   {
      TitledBorder panelTitleBorder = BorderFactory.createTitledBorder(Const.PANEL_BORDER,
              (isEnemy ? ENEMY_TITLE : MY_TITLE), TitledBorder.CENTER, TitledBorder.TOP);
      setLayout(new BorderLayout());
      setPreferredSize(new Dimension(GRID_SIZE, GRID_SIZE));
      setBorder(panelTitleBorder);
      createSeaGrid();
      resetSeaGrid();
   }


   private void createSeaGrid()
   {
      GridButtonHandler gridButtonHandler = null;
      seaGridPanel = new JPanel(new GridLayout(DIM, DIM, 0, 0));
      gridButtons = new SeaGridButton[DIM][DIM];

      if (isEnemy) {
         gridButtonHandler = new GridButtonHandler();
      }

      for (int j = 0; j < DIM; j++) {
         for (int i = 0; i < DIM; i++) {
            gridButtons[i][j] = new SeaGridButton(j * DIM + i);
            if (isEnemy) {
               gridButtons[i][j].addActionListener(gridButtonHandler);
            }
            seaGridPanel.add(gridButtons[i][j]);
         }
      }

      nNumberScale = new JPanel(new GridLayout(1, DIM + 2, 0, 0));
      sNumberScale = new JPanel(new GridLayout(1, DIM + 2, 0, 0));
      eAlphaScale = new JPanel(new GridLayout(DIM, 1, 0, 0));
      wAlphaScale = new JPanel(new GridLayout(DIM, 1, 0, 0));

      nNumberScale.setPreferredSize(new Dimension(GRID_SIZE, 30));
      sNumberScale.setPreferredSize(new Dimension(GRID_SIZE, 30));
      eAlphaScale.setPreferredSize(new Dimension(30, GRID_SIZE));
      wAlphaScale.setPreferredSize(new Dimension(30, GRID_SIZE));

      Font scaleFont = new Font("SanSerif", Font.BOLD, 12);

      for (int i = 0; i < DIM + 2; i++) {
         JLabel l;
         l = new JLabel("", SwingConstants.CENTER);
         l.setFont(scaleFont);
         if (i > 0 && i < DIM + 1)
            l.setText(i + "");
         nNumberScale.add(l);
         l = new JLabel("", SwingConstants.CENTER);
         l.setFont(scaleFont);
         if (i > 0 && i < DIM + 1)
            l.setText(i + "");
         sNumberScale.add(l);

         if (i > 0 && i < DIM + 1) {
            l = new JLabel(ALPHA_SCALE[i - 1], SwingConstants.CENTER);
            l.setFont(scaleFont);
            eAlphaScale.add(l);
            l = new JLabel(ALPHA_SCALE[i - 1], SwingConstants.CENTER);
            l.setFont(scaleFont);
            wAlphaScale.add(l);
         }
      }

      add(nNumberScale, BorderLayout.NORTH);
      add(eAlphaScale, BorderLayout.EAST);
      add(seaGridPanel, BorderLayout.CENTER);
      add(wAlphaScale, BorderLayout.WEST);
      add(sNumberScale, BorderLayout.SOUTH);
   }

   public interface Listener
   {
      public void updateView(final Fleed fleedModel);
   }

   private class GridButtonHandler implements ActionListener
   {
      @Override
      public void actionPerformed(ActionEvent e)
      {
         String cmd = e.getActionCommand();
         int buttonIndex = Integer.parseInt(cmd);

         int i = (int) buttonIndex % SeaArea.DIM;
         int j = (int) buttonIndex / SeaArea.DIM;

         Logger.debug("Button {0},{1} clicked", i, j);


      }
   }


}
