package sde.actor.bb;
import sde.actor.*;

public class BB{
   public static void main( String []args ){
      ControlMachine cm=new Realtime();

      BufferIF b=new BoundedBuffer( 5 );
      Producer p1=new Producer( b, 100000, 1 );
      Producer p2=new Producer( b, 0, 2 );
      Consumer c=new Consumer( b );
      //i costruttori delle classi schedulano i primi
      //messaggi

      cm.controller();
   }
}//BB

