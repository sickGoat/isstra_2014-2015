package csm.util;

public class Job {
	
	private final int id;
	private double arrivo_sistema;
	private double arrivo_stazione;
	
	public Job(int id){ this.id = id; }
	
	public void setArrivoSistema(double now){ this.arrivo_sistema = now;this.arrivo_stazione=now; }
	public int getId() { return this.id; }
	public void setArrivoStazione(double now){ this.arrivo_stazione = now; }
	public double getArrivoSistema(){ return this.arrivo_sistema; }
	public double getArrivoStazione(){ return this.arrivo_stazione; }
	
	@Override
	public boolean equals(Object other){
		if( other == null ){ return false; }
		if( other.getClass() != this.getClass() ){ return false;}
		Job o = (Job) other;
		
		return o.getId() == this.id;
	}
	
}
