package csm;

import csm.exception.RouterException;
import sde.actor.Message;
import sde.actor.distribuzioni.Distribuzione;

public interface RouterIF extends StazioneIF {
	
	public static class Init extends Message{ 
		
		private StazioneIF[] stazioni_uscita;
		private double[] probabilita_stazioni;
		private Distribuzione distribuzione;
		
		public Init(StazioneIF[] s,double[] p,Distribuzione d){
			if( s.length != p.length )
					throw new RouterException(RouterException.EXC_STAZIONI);
			if( !controllaProbabilita(p) )
					throw new RouterException(RouterException.EXC_PROBABILITA);
			this.stazioni_uscita = s;
			this.distribuzione = d;
			this.probabilita_stazioni = p;
		}
		
		public StazioneIF[] getStazioni(){ return this.stazioni_uscita; }
		public double[] getProbabilita(){ return this.probabilita_stazioni; }
		public Distribuzione getDistribuzione(){ return this.distribuzione; }
		
		private boolean controllaProbabilita(double[] p){
			double somma = 0;
			for( int i = 0 ; i < p.length ; i++ ){
				somma += p[i];
			}
			return somma == 1;
		}
	}
}
