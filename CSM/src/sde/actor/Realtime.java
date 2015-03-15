//file Realtime.java

package sde.actor;
import java.util.List;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Iterator;

public class Realtime extends ControlMachine{
	
	private static class TimerComparator implements Comparator<Timer>{
	   public int compare( Timer t1, Timer t2 ){
		  if( t1.firetime()<t2.firetime() ) return -1;
		  if( t1.firetime()>t2.firetime() ) return +1;
		  if( t1!=t2 ){
			if( Math.random()<0.5 ) return -1;
			return +1;
		  }
		  return 0;
	   }
   }//TimerComparator	

   private PriorityQueue<Timer> TQ=
	   new PriorityQueue<Timer>(1000, new TimerComparator() );
   private LinkedList<Message> MQ=new LinkedList<Message>();
   
   private int oldstatus;
   private Message dispatch;
   private double current;
   private long last;
   private Object lock=new Object();
   
   public Realtime(){
      Actor.setRuntime( this );
      Timer.setRuntime( this );
      current=0;
      last=System.currentTimeMillis();
   }
   public double now(){
	  synchronized( lock ){
		  long currentTime=System.currentTimeMillis();
		  long elapsed = currentTime-last;
		  last = currentTime; current += elapsed;
		  return current;
	  }
   }
   public void reset(){ 
	   synchronized( lock ){
		   TQ.clear(); MQ.clear(); 
	   }
   }//reset
   public void schedule( Message m ){
	  synchronized( lock ){
		  if( m instanceof Timer ){
			  Timer t=(Timer)m;
			  TQ.remove(t);
			  TQ.add(t);
		  }
		  else{
			  MQ.add( m );
		  }
	  }
   }//schedule
   public void unSchedule( Message m ){
	  synchronized( lock ){
		  if( m instanceof Timer ){
			  TQ.remove(m);
		  }
		  else{
			  Iterator<Message> it=MQ.iterator();
			  while( it.hasNext() ){
				  Message m0=it.next();
				  if( m0.equals(m) ){
					  it.remove(); break;
				  }
			  }
		  }
	  }
   }//unSchedule

   public void controller(){
      while( true ){//control loop
         Timer mit=null; //most imminent timer
         if( TQ.size()!=0 ){
           //allow a timer to fire
           if( TQ.peek().firetime()<=now() ){
			  synchronized( lock ){
                 mit=TQ.poll();
		  	  }
           }
         }
         if(  mit!=null || MQ.size()>0 ){
            if( mit!=null ) { //priority to fired timer
               dispatch=mit.timeout(); mit=null;
            }
            else{
			   synchronized( lock ){
                  dispatch=MQ.removeFirst();
		       }
            }
            oldstatus=dispatch.receiver.currentStatus();
            dispatch.receiver.handler( dispatch );
            if( dispatch.receiver.currentStatus()!=oldstatus ){
                //catenate DMQ events in front of MQ
                while( dispatch.receiver.DMQ.size()!=0 ){
                   synchronized( lock ){ 
                   	  MQ.addFirst( dispatch.receiver.DMQ.removeLast() );
                   }
	           }
            }//if
         }//if - no dispatch message
      }//while
   }//Controller
}//Realtime
