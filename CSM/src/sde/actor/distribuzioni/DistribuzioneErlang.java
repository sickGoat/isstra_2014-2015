package sde.actor.distribuzioni;
import java.util.*;

public class DistribuzioneErlang extends Distribuzione{
	/*
	 * E' la somma di n distribuzioni esponenziali ciascuna con
	 * la stessa rate lambda.
	 * La rate della Erlang �: lambda/n
	 */
	private double lambda;
	private int n;
	
	public DistribuzioneErlang( int n, double lambda, Random r ){
		super(r);
		if( n<1 || lambda<=0 ) throw new IllegalArgumentException();
		this.n=n;
		this.lambda=lambda;
	}
	
	public double getLambda(){ return lambda; }
	
	public int getN(){ return n; }
	
	public double prossimoCampione(){
		double u=1;
		Random r=getRandomGenerator();
		for( int i=0; i<n; i++ ){
			double ui = r.nextDouble();
			u *= ui; //o 1-ui
		}
		return -(1/lambda)*Math.log( u );
	}
	public double prossimoCampione( double u ){
		return prossimoCampione();
	}
	public String toString(){
		return "ERLANG( n="+n+", lambda="+lambda+" )";
	}	

}//DistribuzioneErlang
