package controller;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ShotClock
{
   private static final int TIMEOUT = 9;
   private Thread shotClockThread = null;
   private volatile AtomicInteger timeout = new AtomicInteger(TIMEOUT);
   private volatile AtomicBoolean isStopped = new AtomicBoolean(false);
   private final Object waitLock = new Object();
   private Listener listener;

   public ShotClock(final Listener listener)
   {
      this.listener = listener;
   }

   public void shutdown()
   {
      if (shotClockThread != null && shotClockThread.isAlive()) {
         shotClockThread.interrupt();
      }
   }


   public void start()
   {
      timeout.set(TIMEOUT);
      isStopped.set(false);

      synchronized (waitLock) {
         waitLock.notify();
      }

      if (shotClockThread == null) {
         startThread();
      }
   }

   public void stop()
   {
      isStopped.set(true);

      if (listener != null) {
         listener.onTick(0);
      }
   }

   public void pause()
   {
      isStopped.set(true);
   }

   public void resume()
   {
      isStopped.set(false);
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
                  synchronized (waitLock) {
                     waitLock.wait(1000);
                  }
                  if (!isStopped.get()) {
                     if (listener != null) {
                        listener.onTick(timeout.intValue());
                     }
                     if (timeout.getAndDecrement() <= 0) {
                        if (listener != null) {
                           listener.onTimeIsUp();
                        }
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

   /*
   public void stop()
   {
      if(listener != null) {
         listener.onTick(TIMEOUT + 1);
      }
      isStopped.set(true);
   }

   public void reset()
   {
      timeout.set(TIMEOUT);
      isStopped.set(false);
      if(listener != null) {
         listener.onTick(TIMEOUT + 1);
      }

      if (shotClockThread == null) {
         startThread();
      }

      synchronized (waitLock) {
         waitLock.notify();
      }
   }

   private void startThread()
   {
      shotClockThread = new Thread(() -> {
         timeout.set(TIMEOUT);

         while (!Thread.currentThread().isInterrupted()) {

            try {
               Thread.sleep(1000);

               if (isStopped.get()) {
                  continue;
               }

               if (listener != null) {
                  listener.onTick(timeout.intValue());
               }

               if (timeout.getAndDecrement() == 0) {

                  if (listener != null) {
                     listener.onTimeIsUp();
                  }
                  synchronized (waitLock) {
                     waitLock.wait();
                  }
               }

            } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
            }
         }
      }, "ShotClockThread");

      shotClockThread.start();
   }*/


   public interface Listener
   {
      public void onTick(final int tick);
      public void onTimeIsUp();
   }
}
