package sde.actor.distribuzioni;
import java.util.*;

public class DistribuzioneNormale extends Distribuzione{
	
	/* mean è la media della distribuzione normale
	 * std è la deviazione standard, ossia la radice quadrata della varianza
	 * (sigma^2) della distribuzione normale
	 */
	
	private double mean, std;
	public DistribuzioneNormale( Random r, double mean, double std ){ 	
		super(r);
		this.mean=mean; this.std=std;
	}
	
	public double getMean(){ return mean; }
	public double getStd(){ return std; }
	
	public double prossimoCampione(){
	     double v1, v2, s, rnn1, y;
	     Random r=getRandomGenerator();
	     for(;;){
	        v1=2.0*r.nextDouble()-1.0;
	        v2=2.0*r.nextDouble()-1.0;
	        s=v1*v1-v2*v2;
	        if( s<1.0 ) break;
	     }
	     rnn1=v1*Math.sqrt( -2.0*Math.log(s)/s );
	     y=mean+rnn1*std;
	     return y;
	}//prossimoCampione
	
	public double prossimoCampione( double u ){
		return prossimoCampione();
	}//prossimoCampione
	
}//DistribuzioneNormale
