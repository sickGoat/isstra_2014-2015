package sde.actor.distribuzioni;
import java.util.*;

public class DistribuzioneEsponenziale extends Distribuzione {
	private double lambda;
	public DistribuzioneEsponenziale( double lambda, Random r ){
		super(r);
		if( lambda<=0 ) throw new IllegalArgumentException();
		this.lambda=lambda;
	}
	public double getLambda(){ return lambda; }
	public double prossimoCampione(){
		double u = this.getRandomGenerator().nextDouble();
		return -(1/lambda)*Math.log(u); //o 1-u
	}
	public double prossimoCampione( double u ){
		return -(1/lambda)*Math.log(u); //o 1-u
	}
	public String toString(){
		return "EXP( "+lambda+" )";
	}
	public static void main( String[] args ){
		DistribuzioneEsponenziale de=
			new DistribuzioneEsponenziale( 0.2, new Random( System.currentTimeMillis() ) );
		for( int i=0; i<10000; i++ )
			System.out.println( de.prossimoCampione() );
	}
}//DistribuzioneEsponenziale
