/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miobooktrade;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 *
 * @author antonio
 */
public class BookBuyerAgent extends Agent{
    
    private String book;
    private final AID[] sellers_aid = { 
            new AID("s1", AID.ISLOCALNAME),
            new AID("s2",AID.ISLOCALNAME)
    };
    private boolean book_bought = false;
    private int bestPrice = 0;
    
    @Override
    protected void setup() {
        System.out.println("Ciao sono: "+getAID().getName());
        Object[] args = getArguments();
        if( args == null ){ System.out.println("specifica nome libro"); doDelete();}
        else{
            book = (String) args[0];
            System.out.println("provo a comprare: "+book);
            addBehaviour(new TickerBehaviour(this, 10000) {
                
                @Override
                protected void onTick() {
                    myAgent.addBehaviour(new RequestPerformer());
                }
            });
        }
    }
    
    @Override
    protected void takeDown() { 
        if( book_bought ) System.out.println("Libro comprato");
        else System.out.println("takeDown acquisto fallito");
     }
    
    private class RequestPerformer extends Behaviour{
        
        private AID bestSeller;
        private int repliesCount = 0;
        private int step = 0;
        private MessageTemplate template;
        
        @Override
        public void action() {
            switch(step){
            
                case 0:
                    //System.out.println("step"+step);
                    ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                    for (int i = 0; i < sellers_aid.length; i++) { 
                        cfp.addReceiver(sellers_aid[i]);                
                    }
                    cfp.setContent(book);
                    cfp.setConversationId("book-trade");
                    cfp.setReplyWith("cfp"+System.currentTimeMillis());
                    myAgent.send(cfp);
                    template = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"), 
                            MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
                    step = 1;
                    break;
                case 1:
                    //System.out.println("step"+step);
                    ACLMessage msg = myAgent.receive(template);
                    if( msg != null ){
                        if( msg.getPerformative() == ACLMessage.PROPOSE ){
                            int price = Integer.valueOf(msg.getContent());
                            if(bestSeller == null || price < bestPrice ){ 
                                bestPrice = price;bestSeller = msg.getSender();
                            }
                        }
                        repliesCount++;
                        if( repliesCount == sellers_aid.length ){ step = 2;}    
                    }else{ block();}
                    break;
                case 2:
                    //System.out.println("step"+step);
                    ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                    order.addReceiver(bestSeller);
                    order.setConversationId("book-trade");
                    order.setReplyWith("order"+System.currentTimeMillis());
                    myAgent.send(order);
                    template = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"), 
                            MessageTemplate.MatchInReplyTo(order.getReplyWith()));
                    step = 3;
                    break;
                case 3:
                    //System.out.println("step"+step);
                    ACLMessage resp = myAgent.receive(template);
                    if( resp != null ){
                        if( resp.getPerformative() == ACLMessage.INFORM ){
                            System.out.println("Libro acquistato a "+bestPrice);
                            book_bought = true;
                            myAgent.doDelete();
                        }else{
                            System.out.println("Acquisto fallito");
                        }
                        step = 4;
                    }else{block();}
            }
        }     

        @Override
        public boolean done() {
            return ( step == 4 || ( step == 2 && bestSeller == null ));
        }
    }
  }