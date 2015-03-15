//file RandomGenerator.java

package sde.actor;

public class RandomGenerator{
  private final int m=2147483647;
  private final int a=16807;
  private final int q=m/a; /*127773*/
  private final int r=m%a; /*2836*/
  private int seed;
  public RandomGenerator() { seed = (int)System.currentTimeMillis(); };
  public RandomGenerator( int seed ){ this.seed = seed; };
  public double random() {
     int tmpseed = a*(seed%q)-r*(seed/q);
     if ( tmpseed > 0 ) seed = tmpseed;
     else seed = tmpseed + m;
     return (double)seed*(1.0/m);
  }//random
  public double uniform( double lo, double up ){
     return (up-lo)*random()+lo;
  }//uniform
  public double exponential( double lamda ) throws ArithmeticException {
     return -(1/lamda)*Math.log( random() );
  }//exponential
  public double normal( double mean, double std ){
     double v1, v2, s, rnn1, y;
     for(;;){
        v1=2.0*random()-1.0;
        v2=2.0*random()-1.0;
        s=v1*v1-v2*v2;
        if( s<1.0 ) break;
     }
     rnn1=v1*Math.sqrt( -2.0*Math.log(s)/s );
     y=mean+rnn1*std;
     return y;
  }//normal
  public double poisson( double lambda ){
     double x=0.0, a=Math.exp( -lambda ), s=1.0;
     for(;;){
        s=s*random();
        if( s<a ) break;
        x += 1.0;
     }
     return x;
  }//poisson
}//RandomGenerator
