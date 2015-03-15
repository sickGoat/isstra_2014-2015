/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minority.game;

import com.google.gson.Gson;
import minority.game.util.MG_Utils;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import minority.game.GUI.StatisticsGUI;
import minority.game.util.Coordinate;
import minority.game.util.FileHandler;
import minority.game.util.Result;

/**
 *
 * @author antonio
 */
public class Statistics extends Agent{
    
    private final LinkedList<Coordinate> attendance_serie = new LinkedList<>();//numerosita lato home
    private final LinkedList<Coordinate> commutazione_serie = new LinkedList<>();//numero commutazione per istante di tempo
    private double fs;
    private int n_player,n_giocate,bit_memoria,spg;
    private StatisticsGUI gui;

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if( args != null ){
            n_giocate = (int) args[0];
            n_player = (int) args[1];
            bit_memoria = (int) args[2];
            spg = (int) args[3];
            gui = new StatisticsGUI();
            gui.setInputParameter(n_player, n_giocate, bit_memoria, spg);
            gui.setVisible(true);
            DFAgentDescription desc = new DFAgentDescription();
            desc.setName(getAID());
            ServiceDescription s_desc = new ServiceDescription();
            s_desc.setName(MG_Utils.SERVICE_STATS_NAME);
            s_desc.setType(MG_Utils.SERVICE_STATS_TYPE);
            desc.addServices(s_desc);
            try {
                DFService.register(this, desc);
            } catch (Exception e) {}
            addBehaviour(new CollectData());
        }else{
            System.out.println("Specificare argomenti");
            doDelete();
        }
    }
    
    @Override
    protected void takeDown() {
        try {
            gui.setVisible(true);
            DFService.deregister(this);
        } catch (FIPAException ex) {
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private class CollectData extends CyclicBehaviour{
        MessageTemplate template = MessageTemplate.or(MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                    MessageTemplate.MatchPerformative(ACLMessage.CANCEL)),MessageTemplate.MatchPerformative(ACLMessage.CONFIRM));
        private final Gson gson = new Gson();
        
        @Override
        public void action() {
            ACLMessage msg = myAgent.receive(template);
            if( msg != null ){
                switch( msg.getPerformative()){
                    case ACLMessage.INFORM:
                            Result r = gson.fromJson(msg.getContent(),Result.class);
                            attendance_serie.add(r.getAttendance());
                            commutazione_serie.add(r.getCambio_strategia());
                            fs += r.getFs();
                            break;
                    case ACLMessage.CANCEL:
                            gui.showSimulationEnd();
                            gui.setAttendance(attendance_serie);
                            gui.setFS(fs, n_giocate);
                            gui.setCommutazioni(commutazione_serie);
                            try{FileHandler.storeCSVFile(attendance_serie);}
                            catch(Exception e){}
                            attendance_serie.clear();fs = 0;commutazione_serie.clear();
                            break;
                    case ACLMessage.CONFIRM:
                            Engine.InputParameters in_params = gson.fromJson(msg.getContent(), Engine.InputParameters.class);
                            gui.setInputParameter(in_params.n_giocatori, in_params.n_giocate, in_params.bit_memoria, in_params.strategie_player);
                }
            }else{block();}
        }
    
    }
    
}
