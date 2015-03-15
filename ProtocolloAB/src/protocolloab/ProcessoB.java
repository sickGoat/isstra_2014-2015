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
public class ProcessoB implements Runnable{
    
    private final int MAX_DO_SMT = 5000 , MIN_CS = 300 , MAX_CS = 500;
    private final int pid;
    private Gestore g;
    
    public ProcessoB(int pid,Gestore g){ this.pid = pid; this.g = g; }
    
    @Override
    public void run() {
        while(true){
            doSomething();
            g.request(pid);
            System.out.println("B");
            inside();
            g.finish(pid);
        }
    }
    
    private void doSomething(){
        try {
            Thread.sleep( (int)(Math.random()*(MAX_DO_SMT)));
        } catch (InterruptedException ex) {}
    }
    
    private void inside(){
        try {
            Thread.sleep((int) (Math.random()*(MAX_CS - MIN_CS)+MIN_CS ));
        } catch (InterruptedException ex) {}
    }

    
}
