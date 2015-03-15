/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocolloab;

import sde.semaforo.Semaforo;
import sde.semaforo.SemaforoBinario;

/**
 *
 * @author antonio
 */
public class GestoreS extends Gestore{
    
    private Semaforo mutex = new SemaforoBinario(1);
    private Semaforo wait = new SemaforoBinario(0);
    private int w = 0;
    
    
    @Override
    public void request(int pid) {
       mutex.P();
       if( attesa[pid] ){
           w++;
           mutex.V();
           wait.P();
           w--;
       }
       accessi[pid]++;
       accessi[1-pid]--;
       attesa[pid] = accessi[pid] == 2;
    }

    @Override
    public void finish(int pid) {
        if( devoSvegliare(pid)){
            attesa[1-pid] = false;
            if( w > 0 ){ wait.V();}
            else{ mutex.V();}
        }else{
            mutex.V();
        }
    }

    @Override
    protected boolean devoDormire(int pid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean devoSvegliare(int pid) {
        int altro = 1-pid;
        return attesa[altro] && accessi[pid] == 0;    
    }
    
}
