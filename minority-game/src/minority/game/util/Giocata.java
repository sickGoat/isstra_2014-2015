/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minority.game.util;

/**
 *
 * @author antonio
 */
public class Giocata {
    
   public boolean lato_scelto;
   public boolean cambio_strategia;

    public Giocata() {}

    public Giocata(boolean lato_scelto, boolean cambio_strategia) {
        this.lato_scelto = lato_scelto;
        this.cambio_strategia = cambio_strategia;
    }
    
}
