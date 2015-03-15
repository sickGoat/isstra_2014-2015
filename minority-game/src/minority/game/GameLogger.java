/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minority.game;

import minority.game.util.MG_Utils;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.logging.Level;
import java.util.logging.Logger;
import minority.game.GUI.GameLoggerGUI;

/**
 *
 * @author antonio
 */
public class GameLogger extends Agent {
 
    private GameLoggerGUI myGui;
    
    @Override
    protected void setup() {
        myGui = new GameLoggerGUI();
        myGui.setVisible(true);
        DFAgentDescription desc = new DFAgentDescription();
        desc.setName(getAID());
        ServiceDescription s_desc = new ServiceDescription();
        s_desc.setName(MG_Utils.SERVICE_GAMELOGGER_NAME);
        s_desc.setType(MG_Utils.SERVICE_GAMELOGGER_TYPE);
        desc.addServices(s_desc);
        try {
            DFService.register(this, desc);
        } catch (Exception e) { }
        addBehaviour(new UpdateConsoleServer());
    }
    
    @Override
    protected void takeDown() {
        try {
            myGui.setVisible(false);
            DFService.deregister(this);
        } catch (FIPAException ex) {
            Logger.getLogger(GameLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private class UpdateConsoleServer extends CyclicBehaviour{
        
        MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                
        public void action() {
            ACLMessage msg = myAgent.receive(template);
            if( msg != null ){
                String message = msg.getContent();
                myGui.logEvent(msg.getSender(), message);
            }else{ block();}
        }
    }
    
}
