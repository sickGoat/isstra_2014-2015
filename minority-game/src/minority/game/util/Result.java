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
public class Result {
    
    private Coordinate attendance;
    private Coordinate cambio_strategia;
    private double fs;
    private int n_player,n_giocate,bit_memoria,n_strategie;

    public Result() {}

    public Coordinate getAttendance() { return attendance; }

    public void setAttendance(Coordinate attendance) { this.attendance = attendance; }

    public double getFs() { return fs;}

    public void setFs(double fs) { this.fs = fs;}

    public int getN_player() { return n_player; }

    public void setN_player(int n_player) { this.n_player = n_player; }

    public int getN_giocate() { return n_giocate; }

    public void setN_giocate(int n_giocate) { this.n_giocate = n_giocate; }

    public int getBit_memoria() { return bit_memoria; }

    public void setBit_memoria(int bit_memoria) { this.bit_memoria = bit_memoria; }

    public int getN_strategie() { return n_strategie; }

    public void setN_strategie(int n_strategie) { this.n_strategie = n_strategie; }
    
    public Coordinate getCambio_strategia() { return cambio_strategia; }

    public void setCambio_strategia(Coordinate cambio_strategia) {this.cambio_strategia = cambio_strategia; }
    
}