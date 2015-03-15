/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocolloab;

import java.util.logging.Logger;

/**
 *
 * @author antonio
 */
public class GestoreMN extends Gestore {
    
    private final Object lock = new Object();
    boolean occupato = false;
    
    @Override
    public void request(int pid) {
        synchronized(lock){
            while( devoDormire(pid) ){
                try { lock.wait(); }
                catch (InterruptedException ex) {}
            }
            occupato = true;
            accessi[pid]++;
            accessi[1-pid]--;
            attesa[pid] = accessi[pid] == 2;
        }
    }

    @Override
    public void finish(int pid) {
        synchronized(lock){
            if( devoSvegliare(pid) ){ attesa[1-pid] = false; }
            occupato = false;
            lock.notifyAll();
        }
    }
    
    @Override
    protected boolean devoDormire(int pid){ return occupato || attesa[pid]; }

    @Override
    protected boolean devoSvegliare(int pid) {
        int altro = 1-pid;
        return attesa[altro] && accessi[pid] == 0;    
    }
    
}
