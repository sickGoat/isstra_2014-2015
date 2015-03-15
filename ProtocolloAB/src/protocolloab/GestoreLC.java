/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocolloab;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author antonio
 */
public class GestoreLC extends Gestore {
    
    private Lock l = new ReentrantLock();
    private Condition c = l.newCondition();
    private boolean occupato = false;
    
    @Override
    public void request(int pid) {
        l.lock();
        try {
            while( devoDormire(pid) ){
                try { c.await(); } catch (InterruptedException ex) {}
            }
            occupato = true;
            accessi[pid]++;
            accessi[1-pid]--;
            attesa[pid] = accessi[pid] == 2;
        } finally {
            l.unlock();
        }
    }

    @Override
    public void finish(int pid) {
        l.lock();
        try {
            if( devoSvegliare(pid) ){ attesa[1-pid] = false ;}
            occupato = false;
            c.signalAll();
        } finally {
            l.unlock();
        }
    }

    @Override
    protected boolean devoDormire(int pid) { return occupato || attesa[pid]; }

    @Override
    protected boolean devoSvegliare(int pid) {
       int altro = 1-pid;
       return attesa[altro] && accessi[pid] == 0; 
    }
}
