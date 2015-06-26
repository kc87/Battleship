package kc87.controller;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ShotClock
{
   private static final int TIMEOUT = 9;
   private Thread shotClockThread = null;
   private volatile AtomicInteger timeout = new AtomicInteger(TIMEOUT);
   private volatile AtomicBoolean isStopped = new AtomicBoolean(false);
   private final Object waitLock = new Object();
   private Optional<Listener> listener = Optional.empty();

   public ShotClock(final Listener listener)
   {
      this.listener = Optional.ofNullable(listener);
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
      listener.ifPresent(l -> l.onTick(0));
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
      shotClockThread = new Thread(() -> {
         timeout.set(TIMEOUT);

         while (!Thread.currentThread().isInterrupted()) {
            try {
               synchronized (waitLock) {
                  waitLock.wait(1000);
               }
               if (!isStopped.get()) {
                  listener.ifPresent(l -> l.onTick(timeout.intValue()));
                  if (timeout.getAndDecrement() <= 0) {
                     listener.ifPresent(Listener::onTimeIsUp);
                  }
               }
            } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
            }
         }
      }, "ShotClockThread");

      shotClockThread.start();
   }


   public interface Listener
   {
      public void onTick(final int tick);
      public void onTimeIsUp();
   }
}
