package model;

/**
 * Created by an unknown Java student on 11/3/14.
 */
public class Ship
{
   private boolean destroyed = false;
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

   public boolean isDestroyed()
   {
      return destroyed;
   }

   public void hit()
   {
      if (!destroyed) {
         hits--;
      }

      destroyed = hits == 0;
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
