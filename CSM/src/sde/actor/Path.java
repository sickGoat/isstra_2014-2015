//file \sde\actor\Path.java

package sde.actor;

public class Path{
  public int n=0;
  private double W=0, t=0;
  public void init(){ W=0; n=0; t=0; };
  public void up( double t ){ W += n*(t-this.t); n++; this.t=t; };
  public void down( double t ){ W += n*(t-this.t); n--; this.t=t; };
  public double mean( double tEnd ){ return (W+n*(tEnd-t))/tEnd; };
}//Path
