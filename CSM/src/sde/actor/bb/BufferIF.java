package sde.actor.bb;
import sde.actor.*;

public interface BufferIF extends MessageIF{
  public static class Get extends Message{
      public static class Reply extends Message{ int valore; }
      Actor sender;
      Reply reply;
      public Get( Actor sender )
      { reply=new Reply(); this.sender=sender; }
   }
   public static class Put extends Message{
      public Put( int valore ){ this.valore=valore; }
      int valore;
   }//put
}//BufferIF

