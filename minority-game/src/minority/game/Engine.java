/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minority.game;

import minority.game.util.Strategy;
import com.google.gson.Gson;
import jade.core.AID;
import minority.game.GUI.EngineGUI;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import minority.game.util.MG_Utils;

/**
 *
 * @author antonio
 */
public class Engine extends Agent{
       
    private int n_giocatori;
    private int dim_memoria;
    private int stra_per_player;
    private int n_giocate;
    private boolean first_initialization = true;
    private EngineGUI gui;
    
    @Override
    protected void setup() {
        gui = new EngineGUI(this);
        gui.setVisible(true);
    }
    
    @Override
    protected void takeDown() {
        gui.setVisible(false);
        System.out.println("Engine termina");
    }
    
    public void startGame(int giocatori, int dim_mem, int stra_per_gio,int n_giocate){
        this.n_giocatori = giocatori;
        this.dim_memoria = dim_mem;
        this.stra_per_player = stra_per_gio;
        this.n_giocate = n_giocate;
        addBehaviour(new BootstrapBehaviour());
    }

    
    
    /**
     * Behaviour per inizializzare il gioco
     */
    private class BootstrapBehaviour extends OneShotBehaviour{
        
        @Override
        public void action() {
            ContainerController controller;
            AgentController a_controller;
            try {
                controller = getContainerController();
                for( int i = 0; i < n_giocatori ; i++ ){
                    Strategy[] random_strategy = Strategy.createRandomStrategies(stra_per_player, dim_memoria);
                    a_controller = controller.createNewAgent(MG_Utils.PLAYERS_NAME_PATTERN+i, "minority.game.Player",
                            new Object[]{random_strategy,dim_memoria});
                    a_controller.start();
                }
                a_controller = controller.createNewAgent(MG_Utils.MANAGER_NAME, "minority.game.Manager",
                                    new Object[]{ n_giocatori,n_giocate});
                a_controller.start();
                if( first_initialization ){
                    a_controller = controller.createNewAgent(MG_Utils.LOGGER_NAME, "minority.game.GameLogger", null);
                    a_controller.start();
                    a_controller = controller.createNewAgent(MG_Utils.STATS_NAME, "minority.game.Statistics", 
                        new Object[]{n_giocate,n_giocatori,dim_memoria,stra_per_player});
                    a_controller.start();
                    first_initialization = false;
                }else{
                    ACLMessage init_msg = new ACLMessage(ACLMessage.CONFIRM);
                    AID stats = new AID(MG_Utils.STATS_NAME, AID.ISLOCALNAME);
                    InputParameters params = new InputParameters(n_giocate, n_giocatori, dim_memoria, stra_per_player);
                    init_msg.addReceiver(stats);
                    init_msg.setContent(new Gson().toJson(params));
                    myAgent.send(init_msg);
                }
            } catch (Exception e) {}
        }
    }
    
    public static class InputParameters{
    
        int n_giocate;
        int n_giocatori;
        int bit_memoria;
        int strategie_player;
        
        public InputParameters() {
        }

        public InputParameters(int n_giocate, int n_giocatori, int bit_memoria, int strategie_player) {
            this.n_giocate = n_giocate;
            this.n_giocatori = n_giocatori;
            this.bit_memoria = bit_memoria;
            this.strategie_player = strategie_player;
        }
        
        
        public int getN_giocate() {
            return n_giocate;
        }

        public void setN_giocate(int n_giocate) {
            this.n_giocate = n_giocate;
        }

        public int getN_giocatori() {
            return n_giocatori;
        }

        public void setN_giocatori(int n_giocatori) {
            this.n_giocatori = n_giocatori;
        }

        public int getBit_memoria() {
            return bit_memoria;
        }

        public void setBit_memoria(int bit_memoria) {
            this.bit_memoria = bit_memoria;
        }

        public int getStrategie_player() {
            return strategie_player;
        }

        public void setStrategie_player(int strategie_player) {
            this.strategie_player = strategie_player;
        }
        
        
        
    
    }
}
