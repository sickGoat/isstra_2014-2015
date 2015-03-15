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
public class GestoreAlternativoMH extends Gestore {
    
    private Monitor m = new Monitor();
    private Condition w_lim = m.newCondition();
    
   

    @Override
    public void request(int pid) {
        m.Enter();
        if( devoDormire(pid) ){
            w_lim.Wait();
        }
        accessi[pid]++;
        accessi[1-pid]--;
        attesa[pid] = accessi[pid] == 2;
    }

    @Override
    public void finish(int pid) {
       if( devoSvegliare(pid) ){ attesa[1-pid] = false; w_lim.Signal(); }
       m.Exit();
    }
    
    @Override
    protected boolean devoDormire(int pid) { return attesa[pid]; }

    @Override
    protected boolean devoSvegliare(int pid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
