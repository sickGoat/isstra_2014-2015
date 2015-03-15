package csm;

import csm.util.Job;
import sde.actor.Message;
import sde.actor.MessageIF;

public abstract interface StazioneIF extends MessageIF {
	
	public static class Arrival extends Message{
		
		Job job;
		
		public Arrival(Job job){ this.job = job; }
		public void setJob(Job job){ this.job = job;} 
		
	}//arrival
	
	public static class Departure extends Message{
		
		Job job;
		
		public Departure(){}
		public Departure(Job job){ this.job = job; }
		public void setJob(Job job){ this.job = job; }
	
	}//departure
	
	public Osservatore getOsservatore();
        
}
