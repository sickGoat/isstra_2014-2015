//file SemaforoBinarioAPriorita.java

package sde.semaforo;
import java.util.*;

public class SemaforoBinarioAPriorita implements SemaforoAPriorita{
   private static class Elemento implements Comparable<Elemento>{
	   private Thread t;
	   private int priority;
	   public Elemento( Thread t, int priority ){
	      this.t=t; this.priority=priority;
	   }
	   public Thread getThread(){ return t; }
	   public int getPriority(){ return priority; }
	   public int compareTo( Elemento e ){
		  return this.priority-e.priority;
	   }//compareTo
	   public String toString(){
		   return t.toString()+" priority:"+priority;
	   }//toString
   }//Elemento
   private PriorityQueue<Elemento> codaAPriorita=new PriorityQueue<Elemento>();
   private int permessi, daSvegliare=0;
   private Object lock=new Object();
   public SemaforoBinarioAPriorita( int permessi ){
      if( permessi<0 || permessi>1 )
        throw new IllegalArgumentException();
      this.permessi=permessi;
   }
   public void P( int priority ){
	   synchronized( lock ){
		   if( permessi==0 ){
			   codaAPriorita.add( new Elemento( Thread.currentThread(), priority ));
			   while( true ){
				   try{
					   lock.wait();
				   }catch(InterruptedException e){}
				   if( daSvegliare>0 && 
					   codaAPriorita.peek().getThread()==Thread.currentThread() ){
					   daSvegliare--;
					   codaAPriorita.poll();
					   if( daSvegliare>0 ){
						   if( codaAPriorita.size()>0 ) lock.notifyAll();
						   else{
							   permessi=1;
							   daSvegliare=0;
						   }
					   }
					   break;
				   }
			   }
			   
		   }
		   else permessi=0;
	   }
   }//P
   public void V(){
	   synchronized( lock ){
		   if( codaAPriorita.size()==0 ) permessi=1;
		   else{ daSvegliare++; lock.notifyAll(); }
	   }
   }//V
}//SemaforoBinarioAPriorita
