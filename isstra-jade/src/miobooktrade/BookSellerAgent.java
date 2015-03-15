/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miobooktrade;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;

/**
 *
 * @author antonio
 */
public class BookSellerAgent extends Agent {
    
    private Map<String,Integer> catalog;
    private BookSellerGui myGui;

 
    @Override
    protected void setup() {
        catalog = new HashMap<>();
        myGui = new BookSellerGui(this);
        myGui.setVisible(true);
        addBehaviour(new OfferRequestServer());
        addBehaviour(new PurchaseOrderServer());
    }

    @Override
    protected void takeDown() {
        myGui.setVisible(false);
        System.out.println(getAID()+" termina");
    }

    public void updateCatalogue(String titolo,int prezzo){ 
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() { 
                catalog.put(titolo, prezzo);
                if( catalog.get(titolo) != null ){ System.out.println("Libro inserito");}
            }
        });
    }

    private class OfferRequestServer extends CyclicBehaviour {
        @Override
        public void action() {
            MessageTemplate tmpl = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(tmpl);
            if( msg != null ){
                String book = msg.getContent();
                ACLMessage reply = msg.createReply();
                Integer price;
                if( (price = catalog.get(book)) != null ){
                    System.out.println("Offer:Libro trovato");
                    reply.setPerformative(ACLMessage.PROPOSE);
                    reply.setContent(String.valueOf(price));
                }else{
                    reply.setPerformative(ACLMessage.REFUSE);
                    reply.setContent("not-available");
                }
                myAgent.send(reply);
            }else{ block();}
        }
    }

    private class PurchaseOrderServer extends CyclicBehaviour {
        @Override
        public void action() {
            MessageTemplate tmpl = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(tmpl);
            if( msg != null ){
                String book = msg.getContent();
                ACLMessage reply = msg.createReply();
               // if( catalog.get(book) != null ){
                    System.out.println("Purchase:Libro trovato");
                    catalog.remove(book);
                    reply.setPerformative(ACLMessage.INFORM);
                    System.out.println(book+" venduto a: "+msg.getSender().getName());
                //}else{
                  /*  reply.setPerformative(ACLMessage.FAILURE);
                    reply.setContent("no more available");
                }*/
                myAgent.send(reply);
            }else{ block();} 
        }
    }
}
