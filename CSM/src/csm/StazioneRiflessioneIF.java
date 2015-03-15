package csm;

import java.util.List;

import sde.actor.Message;
import sde.actor.distribuzioni.Distribuzione;

public interface StazioneRiflessioneIF extends StazioneIF {
	
	public static class Init extends Message{
		
                private final int n_popolazione;
                private final Distribuzione distribuzione;
		private final StazioneIF stazione_uscita;
		
		public Init(StazioneIF s,Distribuzione d,int p){
			this.stazione_uscita = s;
			this.distribuzione = d;
			this.n_popolazione = p;
		}
		
		public StazioneIF getStazione(){ return this.stazione_uscita; }
		public Distribuzione getDistribuzione(){ return this.distribuzione; }
		public int getPopolazione(){ return this.n_popolazione; }
		
	}
}
