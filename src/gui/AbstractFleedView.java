package gui;

import model.AbstractFleedModel;
import model.SeaArea;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by an unknown Java student on 11/3/14.
 */
public abstract class AbstractFleedView extends JPanel
{
   private static final String[] ALPHA_SCALE = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
   private static final String MY_TITLE = "My Fleed";
   private static final String ENEMY_TITLE = "Enemy Fleed";
   //private static final int DIM = SeaArea.DIM;
   private static final int GRID_SIZE = 380;

   private JPanel nNumberScale;
   private JPanel sNumberScale;
   private JPanel eAlphaScale;
   private JPanel wAlphaScale;

   private GridButtonHandler gridButtonHandler = null;
   private boolean isEnemy = false;

   protected static final int DIM = SeaArea.DIM;
   protected JPanel seaGridPanel;
   protected SeaGridButton[][] gridButtons = new SeaGridButton[DIM][DIM];

   public AbstractFleedView(final GridButtonHandler gridButtonHandler)
   {
      this.gridButtonHandler = gridButtonHandler;
      this.isEnemy = (gridButtonHandler != null);

      setupFleedView();
   }

   //public abstract void updateView(final AbstractFleedModel fleedModel);

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
      seaGridPanel = new JPanel(new GridLayout(DIM, DIM, 0, 0));
      gridButtons = new SeaGridButton[DIM][DIM];

      for (int j = 0; j < DIM; j++) {
         for (int i = 0; i < DIM; i++) {
            gridButtons[i][j] = new SeaGridButton(j * DIM + i);
            gridButtons[i][j].addActionListener(gridButtonHandler);
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


}
