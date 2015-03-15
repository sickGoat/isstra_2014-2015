package sde.actor.distribuzioni;
import java.util.*;

public class DistribuzionePoisson extends Distribuzione{
	private double lambda;
	
	public DistribuzionePoisson( Random r, double lambda ){
		super(r);
		this.lambda=lambda;
	}
	
	public double getLambda(){ return lambda; }
	
	public double prossimoCampione(){
		Random r=getRandomGenerator();
		double x=0.0, a=Math.exp( -lambda ), s=1.0;
		for(;;){
			s=s*r.nextDouble();
			if( s<a ) break;
			x += 1.0;
		}
		return x;
	}//prossimoCampione
	
	public double prossimoCampione( double u ){
		return prossimoCampione();
	}//prossimoCampione
	
}//DistribuzionePoisson
