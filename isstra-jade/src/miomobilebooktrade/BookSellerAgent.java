/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miomobilebooktrade;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.HashMap;

/**
 *
 * @author antonio
 */
public class BookSellerAgent extends Agent {
    
    private HashMap<String,Integer> catalogo;
    private BookSellerGui gui;


    @Override
    protected void setup() {
        catalogo = new HashMap<>();
        gui = new BookSellerGui(this);
        gui.setVisible(true);
        DFAgentDescription desc = new DFAgentDescription();
        desc.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("book-selling");
        sd.setName("JADE-book-trading");
        sd.addProperties(new Property("CONTAINER", here()));
        desc.addServices(sd);
        try {
            DFService.register(this, desc);
        } catch (Exception e) {}
        addBehaviour(new OfferRequestServer());
        addBehaviour(new PurchaseRequestServer());
        
    }
    
    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (Exception e) {}
        System.out.println("Book seller agent: "+getAID()+" chiude");
        gui.dispose();
    }
    
    public void updateCatalogue(String titolo,int price){
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() { catalogo.put(titolo, price); }
        });
    }
    
    
    private class OfferRequestServer extends CyclicBehaviour{
        @Override
        public void action() {
            MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(template);
            if( msg != null ){
                ACLMessage replica = msg.createReply();
                String titolo = msg.getContent();
                Integer prezzo = catalogo.get(titolo);
                if( prezzo == null ){
                    replica.setPerformative(ACLMessage.REFUSE);
                    replica.setContent("non disponibile");
                }else{
                    replica.setPerformative(ACLMessage.PROPOSE);
                    replica.setContent(String.valueOf(prezzo.intValue()));
                }
                myAgent.send(replica);
            }else{ block();}
        }
    }
    
    private class PurchaseRequestServer extends CyclicBehaviour{
        @Override
        public void action() {
            MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(template);
            if( msg != null ){
                ACLMessage replica = msg.createReply();
                String titolo = msg.getContent();
                if( catalogo.get(titolo) != null ){
                    replica.setPerformative(ACLMessage.INFORM);
                    replica.setContent("Acquisto effettuato");
                }else{
                    replica.setPerformative(ACLMessage.FAILURE);
                    replica.setContent("Libro non più disponibile");
                }
                myAgent.send(replica);
            }else{ block(); }            
        }
        
        
        
    }
       

}
