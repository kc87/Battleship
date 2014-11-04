import java.util.Arrays;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.BorderFactory;
import javax.swing.border.*;

public class Ships
{
   public static void main(String[] args)
   {
      new Game();
   }
}

class Game
{
   public static GameBoard player1, player2;

   public Game()
   {
      SwingUtilities.invokeLater(new Runnable()
      {
         public void run()
         {
            player1 = new GameBoard("Ships v0.1", 0, 0, 1);
            player2 = new GameBoard("Ships v0.1", 480, 490, 2);
         }
      });
   }
}


class GridButton extends JButton
{
   public GridButton(int n, int player)
   {
      this.setPreferredSize(new Dimension(50, 50));
      this.setFocusPainted(false);
      this.setActionCommand(n + "," + player);
   }
}

class Ship
{
   private int n, si, sj, dir, size, hits;


   public Ship(int n, int si, int sj, int size, int dir)
   {
      this.n = n;
      this.si = si;
      this.sj = sj;
      this.dir = dir;
      this.size = size;
      this.hits = size;
   }

   public int hit()
   {
      hits--;
      return hits;
   }

   public int getSize()
   {
      return size;
   }

   public int getDir()
   {
      return dir;
   }

   public int getStartI()
   {
      return si;
   }

   public int getStartJ()
   {
      return sj;
   }

   public int getNumber()
   {
      return n;
   }
}


class GameBoard extends JFrame
{
   //private final int SHIPS = 8;
   public static final Border PANEL_BORDER = BorderFactory.createLineBorder(new Color(0, 0, 0), 2);

   private JPanel hudPanel, ctrlPanel;
   private JButton newButton, quitButton;

   public JLabel shipsDestoyedLabel;
   public SeaGrid seaGrid;
   public CtrlGrid ctrlGrid;

   public GameBoard(String title, int x, int y, int player)
   {
      super(title + "[Player " + player + "]");

      setBounds(x, y, 800, 500);
      setResizable(false);
      setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      setBackground(new Color(250, 250, 250));

      addWindowListener(new WindowAdapter()
      {
         @Override
         public void windowClosing(WindowEvent event)
         {
            int option = JOptionPane.showConfirmDialog(null, "Quit Game?");
            if (option == JOptionPane.OK_OPTION)
               System.exit(0);
         }
      });

      seaGrid = new SeaGrid("My Fleet");
      ctrlGrid = new CtrlGrid("Enemy Fleet", player);

      hudPanel = new JPanel(new FlowLayout());
      hudPanel.setPreferredSize(new Dimension(800, 60));
      hudPanel.setBorder(BorderFactory.createTitledBorder(PANEL_BORDER, "Player " + player, TitledBorder.CENTER, TitledBorder.TOP));

      shipsDestoyedLabel = new JLabel("Ships destroyed: 0");
      //scoreLabel          = new JLabel("Score: 0");

      shipsDestoyedLabel.setFont(new Font("SanSerif", Font.BOLD, 20));
      //scoreLabel.setFont(new Font("SanSerif", Font.BOLD, 20));

      hudPanel.add(shipsDestoyedLabel);
      //hudPanel.add(scoreLabel);

      ctrlPanel = new JPanel();
      ctrlPanel.setPreferredSize(new Dimension(800, 40));

      newButton = new JButton("New");
      newButton.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent evt)
         {
            Game.player1.ctrlGrid.resetGameGrid();
            while (Game.player1.seaGrid.createFleet() < 10) ;
            Game.player2.ctrlGrid.resetGameGrid();
            while (Game.player2.seaGrid.createFleet() < 10) ;

            Game.player1.shipsDestoyedLabel.setText("Ships destroyed: 0");
            Game.player2.shipsDestoyedLabel.setText("Ships destroyed: 0");
            //scoreLabel.setText("Score: 0");
         }
      });

      quitButton = new JButton("Quit");
      quitButton.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent evt)
         {
            Container cp = getContentPane();
            WindowEvent event = new WindowEvent(GameBoard.this, WindowEvent.WINDOW_CLOSING);
            dispatchEvent(event);
         }
      });

      ctrlPanel.add(newButton);
      ctrlPanel.add(quitButton);

      add(hudPanel, BorderLayout.NORTH);
      add(ctrlGrid, BorderLayout.EAST);
      add(seaGrid, BorderLayout.WEST);
      add(ctrlPanel, BorderLayout.SOUTH);

      setVisible(true);
   }

}

class GameGrid extends JPanel
{
   private Color fillColor;

   public static final Border SHIP_BORDER = BorderFactory.createLineBorder(new Color(0, 0, 0), 3);
   public static final Border WATER_BORDER = BorderFactory.createLineBorder(new Color(66, 66, 166), 1);
   public static final Border DESTROYED_BORDER = BorderFactory.createLineBorder(new Color(166, 166, 166), 1);

   public static final int GRID_SIZE = 380;
   public static final int DIM = 10;
   public static final int SIZE = DIM * DIM;

   public static final Color WATER_COLOR = new Color(100, 100, 255);
   public static final Color HIT_COLOR = new Color(250, 50, 50);
   public static final Color EMPTY_COLOR = new Color(188, 188, 188);
   public static final Color DESTROYED_COLOR = new Color(166, 166, 166);
   public static final Color SHIP_COLOR = new Color(0, 0, 0);

   private final String[] ALPHA_SCALE = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
   private TitledBorder panelTitleBorder;
   private ActionListener buttonHandler;

   protected JPanel nPanel, sPanel, ePanel, wPanel, gridPanel;

   protected GridButton[][] gridButtons = new GridButton[DIM][DIM];
   public int[][] gridArray = new int[(DIM + 2)][(DIM + 2)];


   public GameGrid(String title, Color fillColor, ActionListener bh, int player)
   {
      this.fillColor = fillColor;
      this.buttonHandler = bh;

      panelTitleBorder = BorderFactory.createTitledBorder(GameBoard.PANEL_BORDER, title,
            TitledBorder.CENTER, TitledBorder.TOP);

      setLayout(new BorderLayout());
      setPreferredSize(new Dimension(GRID_SIZE, GRID_SIZE));
      setBorder(panelTitleBorder);

      buildGameGrid(player);
      resetGameGrid();
   }

   public void buildGameGrid(int player)
   {
      gridPanel = new JPanel(new GridLayout(DIM, DIM, 0, 0));
      gridButtons = new GridButton[DIM][DIM];

      for (int j = 0; j < DIM; j++) {
         for (int i = 0; i < DIM; i++) {
            gridButtons[i][j] = new GridButton(j * DIM + i, player);
            gridButtons[i][j].addActionListener(buttonHandler);
            gridPanel.add(gridButtons[i][j]);
         }
      }

      nPanel = new JPanel(new GridLayout(1, DIM + 2, 0, 0));
      sPanel = new JPanel(new GridLayout(1, DIM + 2, 0, 0));
      ePanel = new JPanel(new GridLayout(DIM, 1, 0, 0));
      wPanel = new JPanel(new GridLayout(DIM, 1, 0, 0));

      nPanel.setPreferredSize(new Dimension(GRID_SIZE, 30));
      sPanel.setPreferredSize(new Dimension(GRID_SIZE, 30));
      ePanel.setPreferredSize(new Dimension(30, GRID_SIZE));
      wPanel.setPreferredSize(new Dimension(30, GRID_SIZE));

      Font scaleFont = new Font("SanSerif", Font.BOLD, 12);

      for (int i = 0; i < DIM + 2; i++) {
         JLabel l;
         l = new JLabel("", SwingConstants.CENTER);
         l.setFont(scaleFont);
         if (i > 0 && i < DIM + 1)
            l.setText(i + "");
         nPanel.add(l);
         l = new JLabel("", SwingConstants.CENTER);
         l.setFont(scaleFont);
         if (i > 0 && i < DIM + 1)
            l.setText(i + "");
         sPanel.add(l);

         if (i > 0 && i < DIM + 1) {
            l = new JLabel(ALPHA_SCALE[i - 1], SwingConstants.CENTER);
            l.setFont(scaleFont);
            ePanel.add(l);
            l = new JLabel(ALPHA_SCALE[i - 1], SwingConstants.CENTER);
            l.setFont(scaleFont);
            wPanel.add(l);
         }
      }

      add(nPanel, BorderLayout.NORTH);
      add(ePanel, BorderLayout.EAST);
      add(gridPanel, BorderLayout.CENTER);
      add(wPanel, BorderLayout.WEST);
      add(sPanel, BorderLayout.SOUTH);
   }

   public void resetGameGrid()
   {
      for (int[] row : gridArray)
         Arrays.fill(row, 0);

      for (int j = 0; j < DIM; j++) {
         for (int i = 0; i < DIM; i++) {
            gridButtons[i][j].setBackground(fillColor);
            gridButtons[i][j].setBorder(WATER_BORDER);
            gridButtons[i][j].setText("");
         }
      }
   }

}

class SeaGrid extends GameGrid
{
   public int ships = 10;
   public Ship[] shipArray = new Ship[10];


   public SeaGrid(String title)
   {
      super(title, GameGrid.WATER_COLOR, null, 0);
      while (createFleet() < 10) ;
   }

   public int createFleet()
   {
      int i = 0, k = 0;
      int[] shipTypes = {5, 2, 3, 4, 2, 4, 2, 3, 2, 3};

      resetGameGrid();

      while (i < 10 && k++ < 10000) {
         int si = 1 + (int) (DIM * Math.random());
         int sj = 1 + (int) (DIM * Math.random());

         if (checkAndPlaceShip(i + 1, si, sj, shipTypes[i], si % 2)) {
            shipArray[i] = new Ship(i + 1, si, sj, shipTypes[i], si % 2);
            i++;
         }
      }

      ships = 10;
      return i;
   }

   private boolean checkAndPlaceShip(int n, int si, int sj, int size, int dir)
   {
      int ci1 = si - 1;
      int cj1 = sj - 1;
      int ci2 = (dir == 0) ? si + size : si + 1;
      int cj2 = (dir == 1) ? sj + size : sj + 1;

      if (ci2 > DIM + 1 || cj2 > DIM + 1)
         return false;

      for (int j = cj1; j < cj2 + 1; j++) {
         for (int i = ci1; i < ci2 + 1; i++) {
            if (gridArray[i][j] != 0)
               return false;
         }
      }

      for (int k = 0, i = si, j = sj; k < size; k++) {
         gridArray[i][j] = n;
         gridButtons[i - 1][j - 1].setBackground(new Color(0, 0, 0));
         gridButtons[i - 1][j - 1].setBorder(SHIP_BORDER);
         gridButtons[i - 1][j - 1].setText(size + "");

         i += (dir == 0) ? 1 : 0;
         j += (dir != 0) ? 1 : 0;
      }

      return true;
   }

}

class CtrlGrid extends GameGrid
{
   public CtrlGrid(String title, int player)
   {
      super(title, GameGrid.EMPTY_COLOR, new CtrlGridHandler(), player);
   }
}

class CtrlGridHandler implements ActionListener
{
   public void actionPerformed(ActionEvent evt)
   {
      String[] cmds = (evt.getActionCommand()).split(",");

      int k = Integer.parseInt(cmds[0]);
      int p = Integer.parseInt(cmds[1]);

      int i = (int) k % GameGrid.DIM;
      int j = (int) k / GameGrid.DIM;

      GameBoard activePlayer = (p == 1) ? Game.player1 : Game.player2;
      GameBoard enemyPlayer = (p == 1) ? Game.player2 : Game.player1;

      int l = enemyPlayer.seaGrid.gridArray[i + 1][j + 1];

      if (activePlayer.ctrlGrid.gridArray[i + 1][j + 1] == 255)
         return;

      if (l != 0 && l != 255) {
         // we hit something
         Ship ship = enemyPlayer.seaGrid.shipArray[l - 1];
         int size = ship.getSize();
         int si = ship.getStartI();
         int sj = ship.getStartJ();
         int dir = ship.getDir();

         enemyPlayer.seaGrid.gridArray[i + 1][j + 1] = 255;
         enemyPlayer.seaGrid.gridButtons[i][j].setBackground(GameGrid.HIT_COLOR);

         activePlayer.ctrlGrid.gridButtons[i][j].setBackground(GameGrid.HIT_COLOR);
         activePlayer.ctrlGrid.gridButtons[i][j].setBorder(GameGrid.SHIP_BORDER);
         //GameBoard.scoreLabel.setText("Score: "+);


         if (ship.hit() == 0) {
            enemyPlayer.seaGrid.ships--;
            for (int m = 0, ix = si, jy = sj; m < size; m++) {
               activePlayer.ctrlGrid.gridButtons[ix - 1][jy - 1].setBackground(GameGrid.SHIP_COLOR);
               enemyPlayer.seaGrid.gridButtons[ix - 1][jy - 1].setBackground(GameGrid.DESTROYED_COLOR);
               enemyPlayer.seaGrid.gridButtons[ix - 1][jy - 1].setBorder(GameGrid.DESTROYED_BORDER);
               activePlayer.shipsDestoyedLabel.setText("Ships destroyed: " + (10 - enemyPlayer.seaGrid.ships));

               ix += (dir == 0) ? 1 : 0;
               jy += (dir != 0) ? 1 : 0;
            }
            if (enemyPlayer.seaGrid.ships > 0) {
               ;
            } else {
               JOptionPane.showMessageDialog(null, "Player " + p + " hat alle Schiffe versenkt!",
                     "Well done!", JOptionPane.INFORMATION_MESSAGE);
            }
         }

      } else {
         activePlayer.ctrlGrid.gridButtons[i][j].setBackground(GameGrid.WATER_COLOR);
      }

      activePlayer.ctrlGrid.gridArray[i + 1][j + 1] = 255;
   }
}
