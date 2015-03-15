/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocolloab;

import sde.monitor.Condition;
import sde.monitor.Monitor;

/**
 *
 * @author antonio
 */
public class GestoreMH extends Gestore {
    
    private Monitor m = new Monitor();
    private Condition wait = m.newCondition();
    private boolean occupato = false;
    
    @Override
    public void request(int pid) {
        m.Enter();
        try {
            if( devoDormire(pid) ){
                wait.Wait();
            }
            occupato = true;
            accessi[pid]++;
            accessi[1-pid]--;
            attesa[pid] = accessi[pid] == 2;
        } finally {
            m.Exit();
        }
    }

    @Override
    public void finish(int pid) {
        m.Enter();
        try {
            occupato = false;
            attesa[1-pid] = ( attesa[1-pid] && accessi[pid] == 0 )?false:attesa[1-pid];
            if( devoSvegliare(pid)){ wait.Signal();}
        } finally {
            m.Exit();
        }
    }
    
    @Override
    protected boolean devoDormire(int pid){ return occupato || attesa[pid]; }

    @Override
    protected boolean devoSvegliare(int pid) {  
        int altro = 1-pid;
        if( !attesa[altro] ) return true;
        return false;
    }

}
