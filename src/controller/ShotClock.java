package controller;

import org.pmw.tinylog.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by citizen4 on 06.11.2014.
 */
public class ShotClock
{
   private static final int TIMEOUT = 10;
   private Thread shotClockThread = null;
   private volatile AtomicInteger timeout;
   private Object waitLock = new Object();
   private TickListener tickListener = null;
   private TimeIsUpListener timeIsUpListener = null;

   public ShotClock()
   {
      startThread();
   }

   public void setTickListener(final TickListener listener)
   {
      tickListener = listener;
   }

   public void setTimeIsUpListener(final TimeIsUpListener listener)
   {
      timeIsUpListener = listener;
   }

   public void stop()
   {
      if (shotClockThread != null && shotClockThread.isAlive()) {
         shotClockThread.interrupt();
      }
   }

   public void reset()
   {
      timeout = new AtomicInteger(TIMEOUT);
      synchronized (waitLock) {
         waitLock.notify();
      }
   }

   private void startThread()
   {
      shotClockThread = new Thread(new Runnable()
      {
         @Override
         public void run()
         {
            timeout = new AtomicInteger(TIMEOUT);

            while (!Thread.currentThread().isInterrupted()) {

               try {
                  Thread.sleep(1000);
                  Logger.debug("TICK:" + timeout);
                  if (tickListener != null) {
                     tickListener.onTick(timeout.intValue());
                  }

                  if (timeout.decrementAndGet() == 0) {
                     //trigger time out event
                     Logger.debug("Time is up");
                     if (timeIsUpListener != null) {
                        timeIsUpListener.onTimeIsUp();
                     }
                     synchronized (waitLock) {
                        waitLock.wait();
                     }
                  }

               } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
               }
            }
         }
      }, "ShotClockThread");

      shotClockThread.start();
   }


   public interface TickListener
   {
      public void onTick(final int tick);
   }

   public interface TimeIsUpListener
   {
      public void onTimeIsUp();
   }
}
