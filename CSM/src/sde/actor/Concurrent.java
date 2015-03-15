package sde.actor;
import java.util.*;  

public class Concurrent extends ControlMachine{
    private LinkedList<Message> MQ=new LinkedList<Message>(); //Message Queue
    private Message dispatch;
    private int oldStatus;
    public Concurrent(){
       Actor.setRuntime( this );    
       Timer.setRuntime( this );
    }
    public void reset(){ MQ.clear(); }
    public void schedule( Message m ){
       MQ.add( m );
    }//schedule
    public void unSchedule( Message m ){
    	Iterator<Message> it=MQ.iterator();
   	 	while( it.hasNext() ){
   	 		Message m0=it.next();
   	 		if( m0.equals(m) ){
   	 			it.remove(); break;
   	 		}
   	 	}
    }//unSchedule
    public void controller(){
       while( true ){//control loop
          if( MQ.size()==0 ) break;
          dispatch=MQ.removeFirst();
          oldStatus=dispatch.receiver.currentStatus();
          dispatch.receiver.handler( dispatch );
          if( dispatch.receiver.currentStatus()!=oldStatus ) {
              //catenate DMQ events in front of MQ
              while( dispatch.receiver.DMQ.size() != 0 ){
                 MQ.addFirst( dispatch.receiver.DMQ.removeLast() );
              }//while
          }//if
       }//while
    }//Controller
}//Concurrent

