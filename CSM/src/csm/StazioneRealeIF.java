package csm;

import sde.actor.Message;
import sde.actor.distribuzioni.Distribuzione;

public interface StazioneRealeIF extends StazioneIF {
	
	public static class Init extends Message{ 
		
		private StazioneIF stazione_uscita;
		private Distribuzione distribuzione;
		
		public Init(StazioneIF s,Distribuzione d){
			this.stazione_uscita = s;
			this.distribuzione = d;
		}
		
		public StazioneIF getStazione(){ return this.stazione_uscita; }
		public Distribuzione getDistribuzione(){ return this.distribuzione; }
	}
	
}
