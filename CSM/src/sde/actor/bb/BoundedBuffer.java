package sde.actor.bb;
import sde.actor.*;
public final class BoundedBuffer extends Actor implements BufferIF{
   private final static byte EMPTY=0, PARTIAL=1, FULL=2;
   private int[] buffer;
   private int n, in, out, size;
   public BoundedBuffer( int n ){
	   this.n=n; buffer=new int[n]; in=0; out=0; size=0;
	   become( EMPTY );
   }
   //metodi di servizio
   private int get(){ 
	  int valore=buffer[out]; out=(out+1)%n; size--; return valore; 
   }//get
   private void put( int valore ){ 
	  buffer[in]=valore; in=(in+1)%n; size++; 
   }//put
   public void handler( Message m ){
	      switch( currentStatus() ){
		      case EMPTY:
	            if( m instanceof Get ) defer( m );
	            else if( m instanceof Put && size<n-1 ){
	               	put( ((Put)m).valore ); become( PARTIAL );
	            }
	            else if( m instanceof Put && size==n-1 ){
	               put( ((Put)m).valore ); become( FULL );
	            } break;
		  	case PARTIAL:
	            if( m instanceof Get && size>1 ){
	               ((Get)m).reply.valore=get();
	               ((Get)m).sender.send( ((Get)m).reply );
	            }
	            else if( m instanceof Get && size==1 ){
	               ((Get)m).reply.valore=get();
	               ((Get)m).sender.send( ((Get)m).reply );
	               become( EMPTY );
	            }
	            else if( m instanceof Put && size<n-1 ){
	               put( ((Put)m).valore );
	            }
	            else if( m instanceof Put && size==n-1 ){
	               put( ((Put)m).valore ); become( FULL );
	            }
	            break;
		 	case FULL:
	            if( m instanceof Put ) defer( m );
	            else if( m instanceof Get && size==1 ) {
	               ((Get)m).reply.valore=get();
	               ((Get)m).sender.send( ((Get)m).reply );
	               become( EMPTY );
	            }
	            else if( m instanceof Get && size>1 ){
	               ((Get)m).reply.valore=get();
	               ((Get)m).sender.send( ((Get)m).reply );
	               become( PARTIAL );
	            }
	      }//switch
   }//handler
}//BoundedBuffer

