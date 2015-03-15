//file Simulation.java

package sde.actor;
import java.util.List;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Iterator;

public class Simulation extends ControlMachine{
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
 
   private PriorityQueue<Timer> TQ=new PriorityQueue<Timer>(1000, new TimerComparator() );
   private LinkedList<Message> MQ=new LinkedList<Message>();
   private int oldstatus;
   private Message dispatch;
   private double virtualTime, simPeriod;
   public Simulation( double simPeriod ){
      this.simPeriod=simPeriod; virtualTime=0;
      Actor.setRuntime( this );
      Timer.setRuntime( this );
   }
   public double now(){ return virtualTime; }
   public void reset(){ TQ.clear(); MQ.clear(); }
   public void schedule( Message m ){
      if( m instanceof Timer ){
         Timer t=(Timer)m;
         TQ.remove(t);
         TQ.add( t );
      }
      else
         MQ.add( m );
   }
   public void unSchedule( Message m ){
      if( m instanceof Timer ){
         TQ.remove( m );
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
   }//unSchedule
   public void controller(){
      while( true ){
         if( MQ.size()==0 && TQ.size()==0 ) break;
         if( virtualTime > simPeriod ) break;
         if( MQ.size()==0 ){
            //force firing of most imminent timer
            Timer t=TQ.poll();
            if( virtualTime > t.firetime() ){
            	throw new RuntimeException("Qualche messaggio è schedulato nel passato");
            }
            virtualTime=t.firetime(); dispatch=t.timeout();
         }
         else {
            dispatch=MQ.removeFirst();
         }
         oldstatus=dispatch.receiver.currentStatus();
         dispatch.receiver.handler( dispatch );
         if( dispatch.receiver.currentStatus()!=oldstatus ) {
            //catenate DMQ events in front of MQ
            while( dispatch.receiver.DMQ.size() != 0 ){
               MQ.addFirst( dispatch.receiver.DMQ.removeLast() );
            }
         }
      }
      if( MQ.size()==0 && TQ.size()==0  )
         System.out.println("Simulation deadlock!");
   }
}//Simulation

