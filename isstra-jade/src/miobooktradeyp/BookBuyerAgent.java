/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miobooktradeyp;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

/**
 *
 * @author antonio
 */
public class BookBuyerAgent extends Agent{

    private String book;
    private AID[] sellers;
    
    @Override
    protected void setup() {
        System.out.println(getAID().getName()+" buyer agent pronto");
        Object[] args = getArguments();
        if( args != null ){
            book = (String) args[0];
            System.out.println("Trying to buy: "+book);
            addBehaviour(new TickerBehaviour(this, 60000) {
                @Override
                protected void onTick() {
                    /*imposto un filtro di ricerca, in modo analogo a quanto
                        avviene nel buyer                   */
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription desc = new ServiceDescription();
                    desc.setType("book-selling");
                    template.addServices(desc);
                    try {
                        DFAgentDescription[] ricerca = DFService.search(myAgent, template);
                        sellers = new AID[ricerca.length];
                        for (int i = 0; i < ricerca.length; i++) {
                            sellers[i] = ricerca[i].getName();
                        }
                        
                    } catch (Exception e) { e.printStackTrace(); }
                }
            });
        }else{
            doDelete();
        }
    }
    
    @Override
    protected void takeDown() {
    }
    
    
    private class RequestPerformer extends Behaviour{
        
        
        
        @Override
        public void action() {
        }

        @Override
        public boolean done() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    
    
    }
    
}
