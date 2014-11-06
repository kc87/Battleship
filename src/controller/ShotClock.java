package controller;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by citizen4 on 06.11.2014.
 */
public class ShotClock
{
   private static final int TIMEOUT = 9;
   private Thread shotClockThread = null;
   private volatile AtomicInteger timeout = new AtomicInteger(TIMEOUT);
   private volatile AtomicBoolean isStopped = new AtomicBoolean(false);
   private final Object waitLock = new Object();
   private TickListener tickListener = null;
   private TimeIsUpListener timeIsUpListener = null;

   public ShotClock()
   {
   }

   public void setTickListener(final TickListener listener)
   {
      tickListener = listener;
   }

   public void setTimeIsUpListener(final TimeIsUpListener listener)
   {
      timeIsUpListener = listener;
   }

   public void shutdown()
   {
      if (shotClockThread != null && shotClockThread.isAlive()) {
         shotClockThread.interrupt();
      }
   }

   public void stop()
   {
      tickListener.onTick(TIMEOUT + 1);
      isStopped.set(true);
   }

   public void reset()
   {
      timeout.set(TIMEOUT);
      isStopped.set(false);
      tickListener.onTick(TIMEOUT + 1);

      if (shotClockThread == null) {
         startThread();
      }

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
            timeout.set(TIMEOUT);

            while (!Thread.currentThread().isInterrupted()) {

               try {
                  Thread.sleep(1000);

                  if (isStopped.get()) {
                     continue;
                  }

                  if (tickListener != null) {
                     tickListener.onTick(timeout.intValue());
                  }

                  if (timeout.getAndDecrement() == 0) {

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
