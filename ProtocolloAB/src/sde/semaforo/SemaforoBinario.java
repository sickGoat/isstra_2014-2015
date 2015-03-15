//file SemaforoBinario.java

package sde.semaforo;
import java.util.*;

public class SemaforoBinario implements Semaforo{
  private int permessi, daSvegliare=0;
  private List<Thread> listaDiAttesa=new LinkedList<Thread>();
  private Object m=new Object(); //lucchetto-monitor
  public SemaforoBinario( int permessi ){
     if( permessi<0 || permessi>1 )
    	 throw new IllegalArgumentException();
     this.permessi=permessi;
  }
  public void P(){
	  synchronized( m ){
		  if( permessi==0 ){
			  listaDiAttesa.add( Thread.currentThread() );
			  while( true ){
				  try{
					  m.wait();
				  }catch(InterruptedException e){}
				  if( daSvegliare>0 && 
					  listaDiAttesa.get(0)==Thread.currentThread() ){
					  daSvegliare--;
					  listaDiAttesa.remove(0);
					  if( daSvegliare>0 ){
						  if( listaDiAttesa.size()>0 )m.notifyAll();
						  else{
							  permessi=1;
							  daSvegliare=0;
						  }
					  }
					  break;
				  }
			  }
			  
		  }
		  else permessi=0;
	  }
  }//P
  public void V(){
	  synchronized( m ){
		  if( listaDiAttesa.size()==0 ) permessi=1;
		  else{ daSvegliare++; m.notifyAll(); }
	  }
  }//V
}//SemaforoBinario
