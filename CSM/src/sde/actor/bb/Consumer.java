package sde.actor.bb;
import sde.actor.*;

public final class Consumer extends Actor{
   private static class Consume extends Message{}
   //stati interni
   private final byte WAITING=0, CONSUMING=1;
   private BufferIF b;
   private int num=0;
   private Timer t=new Timer();
   private Consume consume=new Consume();
   public Consumer( BufferIF b ){
	   this.b=b;
	   b.send( new BufferIF.Get(this) ); //prima richiesta al buffer
	   become( WAITING );
   }
   public void handler( Message m ){
	      switch( currentStatus() ){
	         case WAITING:
	            if( m instanceof BufferIF.Get.Reply ){
	               num=((BufferIF.Get.Reply)m).valore;
	               System.out.println("Consumer got item#"+num+" @"+now());
	               become( CONSUMING );
	               t.set( consume, this, Math.random()*100 );
	            } break;
	         case CONSUMING:
	             if( m instanceof Consume ){
	               b.send( new BufferIF.Get(this) );
	               become( WAITING );
	            }
	      }//switch
   }//handler
}//Consumer

