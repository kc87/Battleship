package gui;

import model.AbstractFleedModel;
import model.SeaArea;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

public abstract class AbstractFleedView extends JPanel implements AbstractFleedModel.ModelUpdateListener
{
   private static final String[] ALPHA_SCALE = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
   private static final String MY_TITLE = "My Fleed";
   private static final int GRID_SIZE = 380;
   private JPanel nNumberScale;

   private JPanel sNumberScale;
   private JPanel eAlphaScale;
   private JPanel wAlphaScale;
   private GridButtonHandler gridButtonHandler = null;

   private boolean hideInactiveGrid = false;
   private boolean isEnemy = false;
   private boolean isEnabled = true;
   private ArrayList<Color> gridButtonColors = new ArrayList<>();

   protected static final int DIM = SeaArea.DIM;
   protected static final String ENEMY_TITLE = "Enemy Fleed";
   protected JPanel seaGridPanel;
   protected SeaGridButton[][] gridButtons = new SeaGridButton[DIM][DIM];
   protected TitledBorder panelTitleBorder = null;

   public AbstractFleedView(final GridButtonHandler gridButtonHandler)
   {
      this.gridButtonHandler = gridButtonHandler;
      this.isEnemy = (gridButtonHandler != null);

      setupFleedView();
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

      nNumberScale.setBackground(isEnabled ? GuiConstants.PANEL_GRAY_COLOR : GuiConstants.ENEMY_PANEL_COLOR);
      sNumberScale.setBackground(isEnabled ? GuiConstants.PANEL_GRAY_COLOR : GuiConstants.ENEMY_PANEL_COLOR);
      eAlphaScale.setBackground(isEnabled ? GuiConstants.PANEL_GRAY_COLOR : GuiConstants.ENEMY_PANEL_COLOR);
      wAlphaScale.setBackground(isEnabled ? GuiConstants.PANEL_GRAY_COLOR : GuiConstants.ENEMY_PANEL_COLOR);

      for (int j = 0, k = 0; j < DIM; j++) {
         for (int i = 0; i < DIM; i++) {
            SeaGridButton gridButton = gridButtons[i][j];
            if (isEnabled) {
               // gray out grid to mark it inactive
               Color bgColor = gridButton.getBackground();
               float[] bgRgb = bgColor.getRGBColorComponents(null);
               //TODO: Make it a user selectable option
               float gray = hideInactiveGrid ? 0.6f : 0.2126f * bgRgb[0] + 0.7152f * bgRgb[1] + 0.0722f * bgRgb[2];
               gridButton.setBackground(new Color(gray, gray, gray));
               gridButton.setBorder(GuiConstants.WATER_BORDER_GRAY);
               gridButton.setCursor(GuiConstants.PASSIVE_GRID_CURSOR);
               gridButton.setEnabled(false);
               // save color state of the grid
               gridButtonColors.add(bgColor);
            } else {
               // restore color state of the grid
               gridButton.setBackground(gridButtonColors.get(k++));
               gridButton.setBorder(GuiConstants.WATER_BORDER);
               gridButton.setCursor(GuiConstants.ACTIVE_GRID_CURSOR);
               gridButton.setEnabled(true);
            }
         }
      }

      isEnabled = enable;
   }


   public void resetSeaGrid()
   {
      for (int j = 0; j < DIM; j++) {
         for (int i = 0; i < DIM; i++) {
            gridButtons[i][j].setBackground(GuiConstants.WATER_COLOR);
            gridButtons[i][j].setBorder(GuiConstants.WATER_BORDER);
            gridButtons[i][j].setText("");
         }
      }
   }


   private void setupFleedView()
   {
      panelTitleBorder = BorderFactory.createTitledBorder(GuiConstants.GRID_PANEL_BORDER,
              (isEnemy ? ENEMY_TITLE : MY_TITLE), TitledBorder.CENTER, TitledBorder.TOP);
      setLayout(new BorderLayout());
      setPreferredSize(new Dimension(GRID_SIZE, GRID_SIZE));
      setBorder(panelTitleBorder);
      setBackground(GuiConstants.GAME_PANEL_COLOR);
      createSeaGrid();
      resetSeaGrid();
   }


   private void createSeaGrid()
   {
      seaGridPanel = new JPanel(new GridLayout(DIM, DIM, 0, 0));
      seaGridPanel.setBackground(GuiConstants.GAME_PANEL_COLOR);
      gridButtons = new SeaGridButton[DIM][DIM];

      for (int j = 0; j < DIM; j++) {
         for (int i = 0; i < DIM; i++) {
            SeaGridButton gridButton = new SeaGridButton(j * DIM + i);
            gridButton.addActionListener(gridButtonHandler);
            gridButton.setEnabled(isEnemy);
            gridButton.setCursor(isEnemy ? GuiConstants.ACTIVE_GRID_CURSOR : GuiConstants.PASSIVE_GRID_CURSOR);
            gridButtons[i][j] = gridButton;
            seaGridPanel.add(gridButton);
         }
      }

      nNumberScale = new JPanel(new GridLayout(1, DIM + 2, 0, 0));
      sNumberScale = new JPanel(new GridLayout(1, DIM + 2, 0, 0));
      eAlphaScale = new JPanel(new GridLayout(DIM, 1, 0, 0));
      wAlphaScale = new JPanel(new GridLayout(DIM, 1, 0, 0));

      nNumberScale.setBackground(isEnemy ? GuiConstants.ENEMY_PANEL_COLOR : GuiConstants.OWN_PANEL_COLOR);
      sNumberScale.setBackground(isEnemy ? GuiConstants.ENEMY_PANEL_COLOR : GuiConstants.OWN_PANEL_COLOR);
      eAlphaScale.setBackground(isEnemy ? GuiConstants.ENEMY_PANEL_COLOR : GuiConstants.OWN_PANEL_COLOR);
      wAlphaScale.setBackground(isEnemy ? GuiConstants.ENEMY_PANEL_COLOR : GuiConstants.OWN_PANEL_COLOR);

      nNumberScale.setPreferredSize(new Dimension(GRID_SIZE, 30));
      sNumberScale.setPreferredSize(new Dimension(GRID_SIZE, 30));
      eAlphaScale.setPreferredSize(new Dimension(30, GRID_SIZE));
      wAlphaScale.setPreferredSize(new Dimension(30, GRID_SIZE));

      for (int i = 0; i < DIM + 2; i++) {
         JLabel scaleLabel;
         scaleLabel = new JLabel("", SwingConstants.CENTER);
         scaleLabel.setFont(GuiConstants.SCALE_FONT);
         if (i > 0 && i < DIM + 1)
            scaleLabel.setText(i + "");
         nNumberScale.add(scaleLabel);
         scaleLabel = new JLabel("", SwingConstants.CENTER);
         scaleLabel.setFont(GuiConstants.SCALE_FONT);
         if (i > 0 && i < DIM + 1)
            scaleLabel.setText(i + "");
         sNumberScale.add(scaleLabel);

         if (i > 0 && i < DIM + 1) {
            scaleLabel = new JLabel(ALPHA_SCALE[i - 1], SwingConstants.CENTER);
            scaleLabel.setFont(GuiConstants.SCALE_FONT);
            eAlphaScale.add(scaleLabel);
            scaleLabel = new JLabel(ALPHA_SCALE[i - 1], SwingConstants.CENTER);
            scaleLabel.setFont(GuiConstants.SCALE_FONT);
            wAlphaScale.add(scaleLabel);
         }
      }

      add(nNumberScale, BorderLayout.NORTH);
      add(eAlphaScale, BorderLayout.EAST);
      add(seaGridPanel, BorderLayout.CENTER);
      add(wAlphaScale, BorderLayout.WEST);
      add(sNumberScale, BorderLayout.SOUTH);
   }

}
