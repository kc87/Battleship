package net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import main.Main;
import net.protocol.Message;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class NetController
{
   private static final int PORT = 60000;
   private DatagramSocket rcvSocket = null;
   private DatagramSocket sendSocket = null;
   private Thread receiverThread = null;
   private Listener listener = null;


   public NetController(final Listener listener)
   {
      this.listener = listener;
      setupSockets();
   }

   public void startReceiverThread()
   {
      if (receiverThread == null || !receiverThread.isAlive()) {

         receiverThread = new Thread(() -> {
            Logger.info("Receiver thread started...");
            String msg, id;

            while (!receiverThread.isInterrupted()) {
               byte[] packetData = new byte[1024];
               DatagramPacket packet = new DatagramPacket(packetData, packetData.length);
               try {
                  // blocking call
                  rcvSocket.receive(packet);
                  id = packet.getAddress().getHostAddress() + ":" + packet.getPort();
                  msg = new String(packet.getData(), "UTF-8");
                  parsePacket(msg, id);
               } catch (IOException e) {
                  if (!(e instanceof SocketTimeoutException)) {
                     Logger.error(e);
                  }
               }
            }

            rcvSocket.close();
            rcvSocket = null;

            Logger.debug("Out of while loop");
         }, "ReceiverThread");

         receiverThread.start();
      }
   }

   public void stopReceiverThread()
   {
      Logger.info("Stopping Receiver Thread");
      if (receiverThread != null && receiverThread.isAlive()) {
         receiverThread.interrupt();
      }
   }


   public void sendMessage(final Message message, final String peerAddress)
   {
      final Gson gson = new Gson();
      Logger.debug("Send: " + gson.toJson(message) + " to: " + peerAddress);

      new Thread(() -> {
         try {
            byte[] pktData = gson.toJson(message).getBytes("UTF-8");
            DatagramPacket packet = new DatagramPacket(pktData, pktData.length,
                    InetAddress.getByName(peerAddress), PORT);
            sendSocket.send(packet);
         } catch (IOException e) {
            Logger.error(e);
         }
      }, "SendThread").start();
   }


   private void parsePacket(String jsonMsg, final String peerId)
   {
      Gson gson = new GsonBuilder().serializeNulls().create();//new Gson();

      try {
         jsonMsg = jsonMsg.trim();
         Logger.debug("Rcv.:" + jsonMsg + " from: " + peerId);
         Message newMsg = gson.fromJson(jsonMsg, Message.class);
         listener.onMessage(newMsg, peerId);
      } catch (JsonSyntaxException e) {
         Logger.error(e);
      }
   }


   private void setupSockets()
   {
      InetSocketAddress sendSockAddress;
      InetSocketAddress rcvSockAddress;

      // Bind receiver socket first to prevent port collision
      try {

         rcvSocket = new DatagramSocket(null);

         if (Main.localBindAddress == null) {
            Main.localBindAddress = InetAddress.getLocalHost().getHostAddress();
            rcvSockAddress = new InetSocketAddress(PORT);
            rcvSocket.bind(rcvSockAddress);
            sendSocket = new DatagramSocket(0);
         } else {
            rcvSockAddress = new InetSocketAddress(Main.localBindAddress, PORT);
            rcvSocket.bind(rcvSockAddress);

            sendSockAddress = new InetSocketAddress(Main.localBindAddress, 0);
            sendSocket = new DatagramSocket(null);
            sendSocket.bind(sendSockAddress);
         }

         Logger.debug("Bind address is: " + Main.localBindAddress);

         rcvSocket.setSoTimeout(500);

      } catch (SocketException | UnknownHostException e) {
         Logger.error(e);
      }
   }

   public interface Listener
   {
      public void onMessage(final Message newMsg, final String peerId);

      public void onError(final String errMsg);
   }
}
