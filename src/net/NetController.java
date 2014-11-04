package net;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import main.GameContext;
import net.protocol.Message;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Created by an unknown Java student on 11/3/14.
 */
public class NetController
{
   private static final int PORT = 60000;
   private DatagramSocket recvSocket = null;
   private DatagramSocket sendSocket = null;
   private Thread receiverThread = null;
   private Listener listener = null;


   public NetController(final Listener listener)
   {
      this.listener = listener;

      try {
         sendSocket = new DatagramSocket(0);
      } catch (SocketException e) {
         Logger.error(e);
      }
   }

   public void startReceiverThread()
   {
      if (receiverThread == null || !receiverThread.isAlive()) {

         if (recvSocket == null) {
            try {
               recvSocket = new DatagramSocket(null);
               recvSocket.setSoTimeout(500);
               //recvSocket.setReuseAddress(true);
               InetSocketAddress bindAddress = (GameContext.localBindAddress != null) ?
                     new InetSocketAddress(GameContext.localBindAddress, PORT) : new InetSocketAddress(PORT);
               recvSocket.bind(bindAddress);
            } catch (SocketException e) {
               Logger.error(e);
               return;
            }
         }

         receiverThread = new Thread(new Runnable()
         {
            @Override
            public void run()
            {
               Logger.info("Receiver thread started...");
               String msg, id;

               while (!receiverThread.isInterrupted()) {
                  byte[] packetData = new byte[1024];
                  DatagramPacket packet = new DatagramPacket(packetData, packetData.length);
                  msg = id = "";
                  try {
                     // blocking call
                     recvSocket.receive(packet);
                     id = packet.getAddress().getHostAddress() + ":" + packet.getPort();
                     msg = new String(packet.getData(), "UTF-8");
                     parsePacket(msg, id);
                  } catch (IOException e) {
                     if (!(e instanceof SocketTimeoutException)) {
                        Logger.error(e);
                     }
                  }
               }

               recvSocket.close();
               recvSocket = null;

               Logger.debug("Out of while loop");
            }
         }, "ReceiverThread");

         receiverThread.start();
      }
   }

   public void stopReceiverThread()
   {
      Logger.info("Stopping Reveiver Thread");
      if (receiverThread != null && receiverThread.isAlive()) {
         receiverThread.interrupt();
      }
   }


   public void sendMessage(final Message message, final String peerAddress)
   {
      final Gson gson = new Gson();
      Logger.debug("Msg: " + gson.toJson(message) + " to: " + peerAddress);

      new Thread(new Runnable()
      {
         @Override
         public void run()
         {
            try {
               byte[] pktData = gson.toJson(message).getBytes("UTF-8");
               DatagramPacket packet = new DatagramPacket(pktData, pktData.length,
                     InetAddress.getByName(peerAddress), PORT);
               sendSocket.send(packet);
            } catch (IOException e) {
               Logger.error(e);
            }
         }
      }, "SendThread").start();
   }


   private void parsePacket(String jsonMsg, final String peerId)
   {
      Gson gson = new Gson();

      try {
         jsonMsg = jsonMsg.trim();
         Message newMsg = gson.fromJson(jsonMsg, Message.class);
         listener.onMessage(newMsg, peerId);
      } catch (JsonSyntaxException e) {
         Logger.error(e);
      }
   }


   public interface Listener
   {
      public void onMessage(final Message newMsg, final String peerId);

      public void onError(final String errMsg);
   }
}
