package kc87.net.protocol;

import kc87.model.Ship;

public final class Message
{
   //Message TYPEs:
   public static final int CTRL = 0;
   public static final int GAME = 1;
   //Message CTRL SUB_TYPEs
   public static final int KEEP_ALIVE = 0;
   public static final int CONNECT = 1;
   public static final int DISCONNECT = 2;
   //Message GAME SUB_TYPEs
   public static final int NEW = 3;
   public static final int ABORT = 4;
   public static final int SHOOT = 5;
   public static final int TIMEOUT = 6;


   public boolean ACK_FLAG = false;
   public boolean RST_FLAG = false;
   public int TYPE = CTRL;
   public int SUB_TYPE = KEEP_ALIVE;
   public Object PAYLOAD = null;
   public Ship SHIP = null;
   public long SEQ = 0L;
}
