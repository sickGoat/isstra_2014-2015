package sde.actor.distribuzioni;
import java.util.*;

public class DistribuzioneUniforme extends Distribuzione {
	private double a, b;
	public DistribuzioneUniforme( Random r, double a, double b ){
		super(r);
		if( a<0 || b<0 || a>b ) throw new IllegalArgumentException();
		this.a=a; this.b=b;
	}
	public double getEstremoInferiore(){ return a; }
	public double getEstremoSuperiore(){ return b; }
	public double prossimoCampione(){
		return a+(b-a)*( getRandomGenerator().nextDouble() );
	}
	public double prossimoCampione( double u ){
		return a+(b-a)*u;
	}
	public String toString(){
		return "U["+a+","+b+"]";
	}
}//DistribuzioneUniforme
