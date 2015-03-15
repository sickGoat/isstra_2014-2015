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
public class Application {
    
    
    public static void main(String[] args) {
        Gestore g = new GestoreMH();
        ProcessoA a = new ProcessoA(0, g);
        ProcessoB b = new ProcessoB(1,g);
        new Thread(a).start();
        new Thread(b).start();
    }
}
