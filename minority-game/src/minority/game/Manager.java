/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minority.game;

import com.google.gson.Gson;
import minority.game.util.MG_Utils;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import minority.game.util.Coordinate;
import minority.game.util.Giocata;
import minority.game.util.Result;

/**
 *
 * @author antonio
 */
public class Manager extends Agent {
    
    private int time_step,n_player;
    private final ACLMessage log = new ACLMessage(ACLMessage.INFORM);
    private final ACLMessage sample = new ACLMessage(ACLMessage.INFORM);
    private final ACLMessage end_play = new ACLMessage(ACLMessage.INFORM);
    private final ACLMessage request_play = new ACLMessage(ACLMessage.REQUEST);
    private final ACLMessage end_game = new ACLMessage(ACLMessage.CANCEL);
    private final Result result = new Result();
    
    @Override
    protected void setup() {
        System.out.println("Manager pronto");
        Object[] args = getArguments();
        if( args != null ){
            if( args.length == 2 ){
                try{
                    this.n_player = (int) args[0];
                    this.time_step = (int) args[1];
                    DFAgentDescription desc = new DFAgentDescription();
                    desc.setName(getAID());
                    ServiceDescription s_desc = new ServiceDescription();
                    s_desc.setName(MG_Utils.SERVICE_MANAGER_NAME);
                    s_desc.setType(MG_Utils.SERVICE_MANAGER_TYPE);
                    desc.addServices(s_desc);
                    try{
                        DFService.register(this, desc);
                    }catch(Exception e){}
                    SequentialBehaviour threeStep = new SequentialBehaviour(this);
                    threeStep.addSubBehaviour(new EnvironmentKnowledge());
                    threeStep.addSubBehaviour(new GameServer());
                    threeStep.addSubBehaviour(new GameQuitPerformer());
                    addBehaviour(threeStep);
                }catch(Exception e){}
            }else{ System.out.println("Numero di argomenti sbagliato");doDelete();}
        }else{System.out.println("Specificare argomenti");doDelete(); } 
    }
    
    @Override
    protected void takeDown() {
        try{
            DFService.deregister(this);
        }catch(Exception e){}
        System.out.println("Manager muore");
    }
    
    private class EnvironmentKnowledge extends Behaviour{
        private int step = 0, owned_player = 0;
        DFAgentDescription desc = new DFAgentDescription();
        ServiceDescription s_desc = new ServiceDescription();
        @Override
        public void action() {
            switch( step ){
                case 0://ricerca player
                    MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE);
                    ACLMessage msg = myAgent.receive(template);
                    if( msg != null ){ 
                        ACLMessage reply = msg.createReply();//creo il messaggio di replica
                        reply.setPerformative(ACLMessage.AGREE);
                        AID player = msg.getSender();
                        end_play.addReceiver(player);
                        request_play.addReceiver(player);
                        end_game.addReceiver(player); owned_player++;
                        myAgent.send(reply);//informo dell'avvenuta sottoscrizione
                        if( owned_player == n_player ){ step = 1;}//quando ho riconosciuto tutti player passo al prossimo step
                    }
                    else{ block();}
                    break;
                case 1://ricerca game logger
                    desc = new DFAgentDescription();
                    s_desc = new ServiceDescription();
                    s_desc.setName(MG_Utils.SERVICE_GAMELOGGER_NAME);
                    s_desc.setType(MG_Utils.SERVICE_GAMELOGGER_TYPE);
                    desc.addServices(s_desc);
                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, desc);
                        if( result != null ){ log.addReceiver(result[0].getName());step=2;}
                    } catch (Exception e) {}
                    break;
                case 2://ricerca game stats
                    s_desc.setName(MG_Utils.SERVICE_STATS_NAME);
                    s_desc.setType(MG_Utils.SERVICE_STATS_TYPE);
                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, desc);
                        if( result != null ){ 
                        sample.addReceiver(result[0].getName());
                        step=3;
                        }
                    } catch (Exception e) {}
            }
        }

        @Override
        public boolean done() { return step == 3; }
    }
        
    private class GameServer extends Behaviour{
        
        private int step = 0,p_bar = 0 ,p_home = 0,msg_received = 0,n_giocate = 0,n_commutazioni = 0;
        private MessageTemplate template;
        private final Coordinate c_att = new Coordinate();
        private final Coordinate c_comm = new Coordinate();
        private final Gson gson = new Gson();
        
        @Override
        public void action() {
            switch(step){
                case 0://richiesta giocata
                    log.setContent(MG_Utils.GAME_ITERATION+n_giocate);
                    request_play.setConversationId(MG_Utils.GAME_CONVERSATION_ID+n_giocate);
                    request_play.setReplyWith(MG_Utils.GAME_CONVERSATION_ID+(n_giocate));
                    myAgent.send(request_play);
                    myAgent.send(log);
                    template = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                                    MessageTemplate.MatchInReplyTo(request_play.getReplyWith()));    
                    step = 1;
                    break;
                case 1://ricezione giocate
                    ACLMessage reply = myAgent.receive(template);
                    if( reply != null ){
                        Giocata play = gson.fromJson(reply.getContent(), Giocata.class);
                        if( play.lato_scelto == MG_Utils.GO_BAR ){ p_bar++; }
                        else{ p_home++; }
                        if( play.cambio_strategia){ n_commutazioni++;}
                        msg_received++;
                        if( msg_received == n_player ){ 
                            step = 2;
                            int attendance;
                            boolean lato_minoritario;
                            if( p_home < p_bar ){ 
                                lato_minoritario = MG_Utils.STAY_HOME;
                                result.setFs((double)p_home/(double)n_player);attendance=p_home;
                            }
                            else{ 
                                lato_minoritario = MG_Utils.GO_BAR;
                                result.setFs((double)p_bar/(double)n_player);attendance=p_bar;
                            }
                            c_att.setY(p_home);//imposto la y delle coordinate di attendance
                            c_comm.setY(n_commutazioni);
                            log.setContent(MG_Utils.LATO_MINORITARIO+lato_minoritario+" attendance: "+attendance);
                            end_play.setContent(Boolean.toString(lato_minoritario));
                            end_play.setConversationId(MG_Utils.GAME_CONVERSATION_ID+(n_giocate));
                            myAgent.send(end_play);
                            myAgent.send(log);
                        }
                        }else{ block();}
                        break;
                case 2://inoltro statistiche
                       c_att.setX(n_giocate);
                       c_comm.setX(n_giocate);
                       result.setAttendance(c_att);
                       result.setCambio_strategia(c_comm);
                       sample.setContent(gson.toJson(result));
                       myAgent.send(sample);
                       n_giocate++;
                       resetStep();
                    }
               }
            
        @Override
        public boolean done() { return time_step == n_giocate-1; }
        
        private void resetStep(){
            this.step = 0;
            this.p_bar = 0;
            this.p_home = 0;
            this.msg_received = 0;
            this.n_commutazioni = 0;
        }
    }
    
    private class GameQuitPerformer extends OneShotBehaviour{

        @Override
        public void action() {
            sample.setPerformative(ACLMessage.CANCEL);
            myAgent.send(end_game);
            myAgent.send(sample);
            myAgent.doDelete();
        }
    }
}



