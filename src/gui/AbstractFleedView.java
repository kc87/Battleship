package gui;

import model.AbstractFleedModel;
import model.SeaArea;
import org.pmw.tinylog.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by an unknown Java student on 11/3/14.
 */
public abstract class AbstractFleedView extends JPanel implements AbstractFleedModel.ModelUpdateListener
{
   private static final String[] ALPHA_SCALE = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
   private static final String MY_TITLE = "My Fleed View";
   protected static final String ENEMY_TITLE = "Enemy Fleed Ctrl";
   //private static final int DIM = SeaArea.DIM;
   private static final int GRID_SIZE = 380;

   private JPanel nNumberScale;
   private JPanel sNumberScale;
   private JPanel eAlphaScale;
   private JPanel wAlphaScale;

   private GridButtonHandler gridButtonHandler = null;
   private boolean isEnemy = false;
   private boolean isEnabled = true;
   private ArrayList<Color> gridButtonColors = new ArrayList<>();

   protected static final int DIM = SeaArea.DIM;
   protected JPanel seaGridPanel;
   protected SeaGridButton[][] gridButtons = new SeaGridButton[DIM][DIM];
   protected TitledBorder panelTitleBorder = null;

   public AbstractFleedView(final GridButtonHandler gridButtonHandler)
   {
      this.gridButtonHandler = gridButtonHandler;
      this.isEnemy = (gridButtonHandler != null);

      setupFleedView();
   }

   public void setIsEnabled(final boolean isEnabled)
   {
      this.isEnabled = isEnabled;
   }

   public abstract void updatePartialView(final AbstractFleedModel fleedModel, final int i, final int j);

   @Override
   public void onPartialUpdate(final AbstractFleedModel model, final int i, final int j, final int flag)
   {
      if (flag != AbstractFleedModel.AGAIN) {
         if (flag == AbstractFleedModel.MISS || flag == AbstractFleedModel.HIT) {
            updatePartialView(model, i, j);
         } else {
            onTotalUpdate(model);
         }
      }
   }

   @Override
   public void onTotalUpdate(final AbstractFleedModel model)
   {
      for (int j = 0; j < DIM; j++) {
         for (int i = 0; i < DIM; i++) {
            updatePartialView(model, i, j);
         }
      }
   }

   // Override swing method because default impl. is rather useless!!
   @Override
   public void setEnabled(final boolean enable)
   {

      if (isEnabled == enable) {
         return;
      }

      super.setEnabled(enable);
      if (gridButtonHandler != null) {
         gridButtonHandler.setEnabled(enable);
      }

      if (isEnabled) {
         gridButtonColors.clear();
      }

      //setBackground(isEnabled ? Const.PANEL_GRAY_COLOR : Const.ENEMY_PANEL_COLOR);
      nNumberScale.setBackground(isEnabled ? Const.PANEL_GRAY_COLOR : Const.ENEMY_PANEL_COLOR);
      sNumberScale.setBackground(isEnabled ? Const.PANEL_GRAY_COLOR : Const.ENEMY_PANEL_COLOR);
      eAlphaScale.setBackground(isEnabled ? Const.PANEL_GRAY_COLOR : Const.ENEMY_PANEL_COLOR);
      wAlphaScale.setBackground(isEnabled ? Const.PANEL_GRAY_COLOR : Const.ENEMY_PANEL_COLOR);


      for (int j = 0, k = 0; j < DIM; j++) {
         for (int i = 0; i < DIM; i++) {
            SeaGridButton gridButton = gridButtons[i][j];
            if (isEnabled) {
               Color bgColor = gridButton.getBackground();
               float[] bgRgb = bgColor.getRGBColorComponents(null);
               float gray = 0.2126f * bgRgb[0] + 0.7152f * bgRgb[1] + 0.0722f * bgRgb[2];
               gridButton.setBackground(new Color(gray, gray, gray));
               gridButton.setBorder(Const.WATER_BORDER_GRAY);
               //gridButton.repaint();
               gridButtonColors.add(bgColor);
            } else {
               // restore colors
               gridButton.setBackground(gridButtonColors.get(k++));
               gridButton.setBorder(Const.WATER_BORDER);
            }
         }
      }

      isEnabled = enable;
   }


   public void resetSeaGrid()
   {
      for (int j = 0; j < DIM; j++) {
         for (int i = 0; i < DIM; i++) {
            gridButtons[i][j].setBackground(Const.WATER_COLOR);
            gridButtons[i][j].setBorder(Const.WATER_BORDER);
            gridButtons[i][j].setText("");
         }
      }
   }


   private void setupFleedView()
   {
      panelTitleBorder = BorderFactory.createTitledBorder(Const.GRID_PANEL_BORDER,
              (isEnemy ? ENEMY_TITLE : MY_TITLE), TitledBorder.CENTER, TitledBorder.TOP);
      setLayout(new BorderLayout());
      setPreferredSize(new Dimension(GRID_SIZE, GRID_SIZE));
      setBorder(panelTitleBorder);
      setBackground(Const.GAME_PANEL_COLOR);
      createSeaGrid();
      resetSeaGrid();
   }


   private void createSeaGrid()
   {
      seaGridPanel = new JPanel(new GridLayout(DIM, DIM, 0, 0));
      seaGridPanel.setBackground(Const.GAME_PANEL_COLOR);
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

      nNumberScale.setBackground(isEnemy ? Const.ENEMY_PANEL_COLOR : Const.OWN_PANEL_COLOR);
      sNumberScale.setBackground(isEnemy ? Const.ENEMY_PANEL_COLOR : Const.OWN_PANEL_COLOR);
      eAlphaScale.setBackground(isEnemy ? Const.ENEMY_PANEL_COLOR : Const.OWN_PANEL_COLOR);
      wAlphaScale.setBackground(isEnemy ? Const.ENEMY_PANEL_COLOR : Const.OWN_PANEL_COLOR);

      nNumberScale.setPreferredSize(new Dimension(GRID_SIZE, 30));
      sNumberScale.setPreferredSize(new Dimension(GRID_SIZE, 30));
      eAlphaScale.setPreferredSize(new Dimension(30, GRID_SIZE));
      wAlphaScale.setPreferredSize(new Dimension(30, GRID_SIZE));

      for (int i = 0; i < DIM + 2; i++) {
         JLabel l;
         l = new JLabel("", SwingConstants.CENTER);
         l.setFont(Const.SCALE_FONT);
         if (i > 0 && i < DIM + 1)
            l.setText(i + "");
         nNumberScale.add(l);
         l = new JLabel("", SwingConstants.CENTER);
         l.setFont(Const.SCALE_FONT);
         if (i > 0 && i < DIM + 1)
            l.setText(i + "");
         sNumberScale.add(l);

         if (i > 0 && i < DIM + 1) {
            l = new JLabel(ALPHA_SCALE[i - 1], SwingConstants.CENTER);
            l.setFont(Const.SCALE_FONT);
            eAlphaScale.add(l);
            l = new JLabel(ALPHA_SCALE[i - 1], SwingConstants.CENTER);
            l.setFont(Const.SCALE_FONT);
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
