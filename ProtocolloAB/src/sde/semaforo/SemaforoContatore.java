package sde.semaforo;
import java.util.*;

public class SemaforoContatore implements Semaforo{
   private int permessi, daSvegliare=0;
   private Object lock=new Object();
   private List<Thread> listaDiAttesa=new LinkedList<Thread>();
   public SemaforoContatore( int permessi ){
       if( permessi<0 )
    	   throw new IllegalArgumentException("Permessi negativi!");
	   this.permessi=permessi;
   }
   public void P(){
	   synchronized( lock ){
		   if( permessi==0 ){
			   listaDiAttesa.add( Thread.currentThread() );
			   while( true ){
				   try{
					   lock.wait();
				   }catch(InterruptedException e){}
				   if( daSvegliare>0 && 
					   listaDiAttesa.get(0)==Thread.currentThread() ){
					   daSvegliare--;
					   listaDiAttesa.remove(0);
					   if( daSvegliare>0 ){
						   if( listaDiAttesa.size()>0 ) lock.notifyAll();
						   else{
							   permessi=daSvegliare;
							   daSvegliare=0;
						   }
					   }
					   break;
				   }
			   }
		   }
		   else permessi--;
	   }
   }//P
   public void V(){
	   synchronized( lock ){
		   if( listaDiAttesa.size()==0 ) permessi++;
		   else{ daSvegliare++; lock.notifyAll(); }
	   }
   }//V
}//SemaforoContatore
