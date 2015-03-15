/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minority.game.util;

import java.util.Scanner;
import minority.game.util.MG_Utils;

/**
 *
 * @author antonio
 */ 
public class Strategy {
    
    private final boolean[] strategy;
    private final int this_id;
    private static int id = 0;
    
    public Strategy(int memory_size) {
        int str_dimension = (int)Math.pow(2, memory_size);
        this.strategy = new boolean[str_dimension];
        createStrategy();
        this.this_id = id;
        ++id;
    }
    
    /**
     * Crea la strategia in modo randomatico
     */
    private void createStrategy(){
        for( int i = 0 ; i < strategy.length ; i++ ){
            double monetina = Math.random();
            if( monetina < .5 ){ strategy[i] = MG_Utils.STAY_HOME; }
            else{ strategy[i] = MG_Utils.GO_BAR;}
        }
    }
    
  
    public static int getId() { return id;}
    
    public boolean[] getStrategy() { return strategy; }

    public int getMemory_size() { return strategy.length; }
    
    public boolean getDecision(int index){ return strategy[index];}
    
        
    public static Strategy[] createRandomStrategies(int num_strategie,int memory_size ){
        Strategy[] strategie = new Strategy[num_strategie];
        for( int i = 0 ; i < num_strategie ; i++ ){
            strategie[i] = new Strategy(memory_size);
        }
        return strategie;
    }

    @Override
    public String toString() {
        return "Strategy:["+this.this_id+"]"; //To change body of generated methods, choose Tools | Templates.
    }
   
    
}
