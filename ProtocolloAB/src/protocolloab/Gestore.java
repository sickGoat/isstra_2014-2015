/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocolloab;

/**
 *
 * @author antonio
 */
public abstract class Gestore  implements GestoreIF{
    
    protected int[] accessi = { 0 , 0 };
    protected boolean[] attesa = { false , false };
    
    protected abstract boolean devoSvegliare(int pid);
    
    protected abstract boolean devoDormire(int pid);
}
