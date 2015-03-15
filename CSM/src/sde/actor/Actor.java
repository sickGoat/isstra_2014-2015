//file Actor.java

package sde.actor;
import java.util.*;

public abstract class Actor{
   /*data*/
   private int status;
   LinkedList<Message> DMQ=new LinkedList<Message>(); //Deferred Message Queue
   private static ControlMachine cm;
   /*methods*/
   static void setRuntime( ControlMachine cm ){ Actor.cm=cm; }
   public double now(){ return cm.now(); }
   protected void become( int status ){ this.status=status; } 
   public void send( Message m, double... at ){
	   if( at.length==0 ){
	   	   m.receiver=this; cm.schedule( m );
	   }
	   else if( at.length==1 ){
		   sde.actor.Timer t=new sde.actor.Timer();
		   t.set( m, this, at[0]-now() );
	   }
	   else assert false : "too many arguments in send";
   }//send
   public int currentStatus() { return status; }
   protected void defer( Message m ){ DMQ.add( m ); }
   public abstract void handler( Message m );
}//Actor 

