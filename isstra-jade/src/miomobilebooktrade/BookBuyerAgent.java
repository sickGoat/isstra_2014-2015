/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miomobilebooktrade;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.Location;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Iterator;

/**
 *
 * @author antonio
 */
public class BookBuyerAgent extends Agent{
    
    private AID[] sellers;
    private ContainerID[] journey;
    private int position=0;//indice con cui accedere a journey
    private Location source;
    private AID bestSeller;
    private int bestPrice;
    private String book;

   
    @Override
    protected void setup() {
        source = here();
        Object[] args = getArguments();
        if( args != null && args.length > 0 ){
            book = (String) args[0];
            addBehaviour(new WakerBehaviour(this, 5000) {
                @Override
                protected void onWake() {
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription ds = new ServiceDescription();
                    ds.setType("book-selling");
                    template.addServices(ds);
                    try{
                        DFAgentDescription[] result = DFService.search(myAgent, template);
                        sellers = new AID[result.length];
                        journey = new ContainerID[result.length];
                        for( int i = 0 ; i < result.length ; i++ ){
                            sellers[i] = result[i].getName();
                            Iterator<ServiceDescription> it = result[i].getAllServices();
                            ds = it.next();
                            Iterator<Property> it_p = ds.getAllProperties();
                            Property p = it_p.next();
                            String value = (String) p.getValue();
                            //estrapolo dalla string l'ID del container
                            String name_con = value.substring(0,value.indexOf("@"));
                            String addr_con = value.substring(value.indexOf("@")+1, value.length()-1);
                            journey[i] = new ContainerID();
                            journey[i].setName(name_con);
                            journey[i].setAddress(addr_con);
                        }
                    }catch(Exception e){}
                    myAgent.addBehaviour(new Journey());
                }
                
            });
        }
    }
    
    @Override
    protected void takeDown() {
        
    }

    @Override
    protected void afterMove() {
        if( position < journey.length ){
            System.out.println("Spostamento");
            addBehaviour(new RequestPerformer());
        }else{
            System.out.println("Prova a comprare");
            addBehaviour(new PurchasePerformer());
        }

    }

    
    /**
     * Behaviour classes
     */
    private class Journey extends OneShotBehaviour{

        @Override
        public void action() {
            System.out.println(position);
            System.out.println(journey);
            if( position == journey.length ){
                myAgent.doMove(source);
            }else{
                myAgent.doMove(journey[position]);
            }
        }
    }
    
    private class RequestPerformer extends Behaviour{
        
        private int step = 0;
        private MessageTemplate template;
        
        @Override
        public void action() {
            switch( step ){
                case 0:
                    ACLMessage msg = new ACLMessage(ACLMessage.CFP);
                    msg.addReceiver(sellers[position]);
                    msg.setContent(book);
                    msg.setConversationId("book-trade");
                    msg.setReplyWith("cfp"+System.currentTimeMillis());
                    myAgent.send(msg);
                    template = MessageTemplate.and(MessageTemplate.MatchInReplyTo(msg.getReplyWith()),
                                MessageTemplate.MatchConversationId("book-trade"));
                    step = 1;
                    break;
                
                case 1:
                    ACLMessage reply = myAgent.receive(template);
                    if( reply != null ){
                        if( reply.getPerformative() == ACLMessage.PROPOSE ){
                            int price = Integer.parseInt(reply.getContent());
                            if( bestSeller == null || bestPrice > price ){
                                bestPrice = price; bestSeller = reply.getSender();
                            }
                        }
                    }else{ block();}
                    step = 2;
            }
        }

        @Override
        public boolean done() { 
            if ( step == 2 ){
                position++;
                myAgent.addBehaviour(new Journey());
            }
            return step == 2;
        }
    }
    
    private class PurchasePerformer extends Behaviour {
        private MessageTemplate template;
        private int step = 0;
        
        @Override
        public void action() {
            if( bestSeller == null ){
                        System.out.println("Nessun venditore possiede il libro");
                        step = 2;
                        return;
                    }
            switch( step ){
                
                case 0:
                    ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                    order.addReceiver(bestSeller);
                    order.setContent(book);
                    order.setConversationId("book-trade");
                    order.setReplyWith("order"+System.currentTimeMillis());
                    myAgent.send(order);
                    template = MessageTemplate.and(MessageTemplate.MatchInReplyTo(order.getReplyWith()), 
                                            MessageTemplate.MatchConversationId("book-trade"));
                    step = 1;
                    break;
                    
                case 1:
                    ACLMessage reply = myAgent.receive(template);
                    if( reply != null ){
                        if( reply.getPerformative() == ACLMessage.INFORM ){
                            System.out.println("Libro acquistato al prezzo: "+bestPrice);
                            myAgent.doDelete();
                        }
                        step = 2;
                    }else{ block();} 
            }
        }

        @Override
        public boolean done() { return step == 2; }
    }
}
