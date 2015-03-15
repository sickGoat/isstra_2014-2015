package sde.actor.distribuzioni;
import java.util.*;

public abstract class Distribuzione {
	private Random r;
	public Distribuzione( Random r ){ 	this.r=r; }
	public Random getRandomGenerator(){ return r; }
	public abstract double prossimoCampione();
	public abstract double prossimoCampione( double u );
}//Distribuzione
