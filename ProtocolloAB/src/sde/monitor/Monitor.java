//file monitor.java

package sde.monitor;

import sde.semaforo.*;

public final class Monitor{
	
	private SemaforoBinario mutex=new SemaforoBinario(1);
	private SemaforoBinario urgent=new SemaforoBinario(0);
	private int urgentcount=0;
	private boolean fair=false;
	
	public Monitor( boolean fair ){
		this.fair=fair;
	}
	
	public Monitor(){
		this(false);
	}
	
	public void Enter(){
		mutex.P();
	}//Enter
	
   	public void Exit(){
      	if( urgentcount > 0 ) urgent.V();
      	else mutex.V();
   	}//Exit
   	
    public Condition newCondition(){ return new ConditionVariable(); }
	
    private class ConditionVariable implements Condition{
   		private SemaforoBinario xsem=new SemaforoBinario(0);
   		private int xcount=0;
   		public void Wait() {
      		xcount++;
      		if( urgentcount>0 ) urgent.V();
      		else{
      			if( fair ) try{ Thread.sleep(1); }
      					   catch( InterruptedException e ){} //Thread.yield();
      			mutex.V();
      		}
     		xsem.P();
     		xcount--;
   		}//Wait
   		public void Signal() {
      		if( xcount>0 ) {
         		urgentcount++;
      			if( fair ) try{ Thread.sleep(1); }
				   		   catch( InterruptedException e ){} //Thread.yield();
         		xsem.V();
         		urgent.P();
         		urgentcount--;
      		}
  		}//Signal
	}//ConditionVariable

	public ConditionP newConditionP(){ return new ConditionVariablePriorited(); }

	private class ConditionVariablePriorited implements ConditionP{
   		private SemaforoBinarioAPriorita xsem=new SemaforoBinarioAPriorita(0);
   		private int xcount=0;
		public void Wait( int priority ) {
      		xcount++;
      		if( urgentcount>0 ) urgent.V();
      		else{
      			if( fair ) try{ Thread.sleep(1); }
				           catch( InterruptedException e ){} //Thread.yield();
      			mutex.V();
      		}
      		xsem.P( priority );
      		xcount--;
   		}//Wait
   		public void Signal() {
      		if( xcount>0 ) {
         		urgentcount++;
      			if( fair ) try{ Thread.sleep(1); }
		   		           catch( InterruptedException e ){} //Thread.yield();         		
         		xsem.V();
         		urgent.P();
         		urgentcount--;
      		}
   		}//Signal
	}//ConditionVariablePriorited

}//Monitor
