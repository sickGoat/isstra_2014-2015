package sde.actor.bb;
import sde.actor.*;

public final class Producer extends Actor{
   private static class Generate extends Message{}
   //stati interni
   private final byte OPERATING=0;
   private Timer t=new Timer();
   private BufferIF b;
   private int num, id;
   public Producer( BufferIF b, int num, int id ){
	   this.b=b; this.num=num; this.id=id;
       t.set( new Generate(), this, Math.random()*100 );
	   become( OPERATING );
   }
   public void handler( Message m ){
	      switch( currentStatus() ){
	         case OPERATING:
	            if( m instanceof Generate ){
	               System.out.println("Producer #"+id+
	                 " generates item# "+num+" @"+now());
	               b.send( new BufferIF.Put( num ) ); num++;
	               t.set( m, m.receiver(), Math.random()*100 );
			    //in alternativa si invia m a se stessi come segue:
			    //send( m, now()+Math.random()*100 );
	            }
	      }//switch
	   }//handler
	}//Producer   

