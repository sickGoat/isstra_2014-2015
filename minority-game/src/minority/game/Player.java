/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minority.game;

import minority.game.util.Strategy;
import com.google.gson.Gson;
import minority.game.util.MG_Utils;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.HashMap;
import java.util.Objects;
import minority.game.util.Giocata;

/**
 *
 * @author antonio
 */
public class Player extends Agent{
    
    private HashMap<Strategy, Integer> strategy_score;
    private final ACLMessage log = new ACLMessage(ACLMessage.INFORM);
    private int dim_memoria;
    private int memoria;
   
    
    @Override
    protected void setup() {
        System.out.println("Player pronto");
        Object[] args = getArguments();
        if( args == null ){
            System.out.println("Specificare argomenti");doDelete();
        }
        else if( args.length != 2 ){
            System.out.println("Numero argomenti sbagliato");doDelete();
        }
        else{
            Strategy[] strategie = (Strategy[]) args[0];
            this.dim_memoria = (int) args[1];
            int max_idx_memoria = (int) Math.pow(2, dim_memoria);//massimo indice della memoria
            this.memoria = (int) (Math.random() * (max_idx_memoria-1));//indice iniziale
            this.strategy_score = new HashMap<>();
            for (Strategy strat : strategie) { strategy_score.put(strat, 0); }//inizializzo il virtual score delle strategie
            addBehaviour(new EnvironmentKnowledge());
            addBehaviour(new CyclicBehaviour() {//behaviour per fermare il gioco
                @Override
                public void action() {
                    MessageTemplate tmpl = MessageTemplate.MatchPerformative(ACLMessage.CANCEL);
                    ACLMessage msg = myAgent.receive(tmpl);
                    if( msg != null ){ 
                        myAgent.doDelete();
                    }
                    else{ block();}
                }
            });
        }
    }

    @Override
    protected void takeDown() {
        System.out.println(getAID().getLocalName()+" muore");
    }
    
    private class EnvironmentKnowledge extends Behaviour{
        
        int step = 0;
        AID manager;
        MessageTemplate template;
        DFAgentDescription desc;
        ServiceDescription s_desc;
        
        @Override
        public void action() {
            switch( step ){
                case 0:
                    desc = new DFAgentDescription();
                    s_desc = new ServiceDescription();
                    s_desc.setName(MG_Utils.SERVICE_MANAGER_NAME);
                    s_desc.setType(MG_Utils.SERVICE_MANAGER_TYPE);
                    desc.addServices(s_desc);
                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, desc);
                        if( result != null ){ manager = result[0].getName(); step = 1;}
                    } catch (Exception e) {
                    }
                    break;
                case 1:
                    ACLMessage subscription = new ACLMessage(ACLMessage.SUBSCRIBE);
                    subscription.setReplyWith("subscription@"+System.currentTimeMillis());
                    subscription.addReceiver(manager);
                    myAgent.send(subscription);
                    template = MessageTemplate.and(MessageTemplate.MatchInReplyTo(subscription.getReplyWith()),
                                    MessageTemplate.MatchPerformative(ACLMessage.AGREE));
                    step = 2;
                    break;
                case 2://aspetta messaggio di conferma da parte del manager
                    ACLMessage reply = myAgent.receive(template);
                    if( reply != null ){
                        step = 3;
                    }else{ block();}
                    break;
                case 3://iscrizione al game logger
                    s_desc.setName(MG_Utils.SERVICE_GAMELOGGER_NAME);
                    s_desc.setType(MG_Utils.SERVICE_GAMELOGGER_TYPE);
                    try{
                        DFAgentDescription[] result1 = DFService.search(myAgent, desc);
                        if( result1 != null ){
                            AID logger = result1[0].getName();
                            log.addReceiver(logger);
                            step = 4;
                        }
                    }catch(Exception e){}
            }
        }

        @Override
        public boolean done() { 
            if( step == 4 ){ myAgent.addBehaviour(new GamePerformer());} //il player è ora pronto a giocare
            return step == 4; 
        }
    }    
    
    
    private class GamePerformer extends CyclicBehaviour{
        
        private int step = 0;
        private Strategy current_strategy;
        private MessageTemplate template;
        private final Giocata giocata = new Giocata(false,false);
        private final Gson gson = new Gson();
        private final int incremento = (int) Math.pow(2, dim_memoria-1);
        
        @Override
        public void action() {
            switch( step ){
                case 0:
                    template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                    ACLMessage msg = myAgent.receive(template);
                    if( msg != null ){
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        if( current_strategy == null ){ current_strategy = (Strategy) strategy_score.keySet().toArray()[0]; }
                        giocata.lato_scelto = current_strategy.getDecision(memoria);
                        reply.setContent(gson.toJson(giocata));//converto l'oggetto in una stringa e lo inserisco nel content della replica
                        log.setContent("sceglie "+giocata.lato_scelto+" "+current_strategy.toString());
                        myAgent.send(reply);
                        myAgent.send(log);
                        template = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                                MessageTemplate.MatchConversationId(msg.getConversationId()));
                        step = 1;
                    }else{ block();}
                    break;
                case 1:
                    ACLMessage result = myAgent.receive(template);
                    if( result != null ){
                        boolean play_result = getResult(giocata.lato_scelto,Boolean.parseBoolean(result.getContent()));
                        int before_memoria = memoria;
                        updateStrategy(play_result);
                        updateMemory(play_result);
                        log.setContent(MG_Utils.ESITO_GIOCATA+play_result+" "+
                                Integer.toBinaryString(before_memoria)+MG_Utils.AGGIORNAMENTO_MEMORIA+Integer.toBinaryString(memoria));
                        myAgent.send(log);
                        template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                        step = 0;
                    }else{ block();}
            }
        }
        

        private boolean getResult(boolean a,boolean b){ return !(a ^ b); }

        /**
         * Il metodo aggiorna i bit di memoria eseguendo uno shift
         * verso sinistra. Il numero codificato servirà da indice
         * per l'accesso all'oggetto strategy
         * @param result : esito dell'ultima giocata
         */
        private void updateMemory(boolean result){
            memoria = memoria >> 1;
            if( result ){ memoria += incremento;}
        }
        
        /**
         * Il metodo serve ad aggiornare gli score asociati ad ogni 
         * strategia, è trovare la prossima strategia col punteggio maggiore.
         * La strategia viene variata soltanto se result è false 
         * ( d'altronde se result è true vuol dire che l'ultima utlizzata è stata vincente )
         * @param result  : esito dell'ultima giocata
         */
        private void updateStrategy(boolean result){
            int current_score;
            boolean strategia_switch = false;
            for( Strategy stra : strategy_score.keySet() ){
                if( stra.getDecision(memoria) == giocata.lato_scelto && result){ 
                    current_score = strategy_score.get(stra);current_score++;
                    strategy_score.put(stra, current_score);
                    
                }
                else if( stra.getDecision(memoria) != giocata.lato_scelto && !result  ){
                    current_score = strategy_score.get(stra);current_score++;
                    strategy_score.put(stra,current_score);
                    if( strategy_score.get(stra) >= strategy_score.get(current_strategy) ){
                        current_strategy = stra;strategia_switch = true;
                    }
                }
                else{
                    current_score = strategy_score.get(stra);current_score--;
                    strategy_score.put(stra, current_score);
                }
            }
            giocata.cambio_strategia = strategia_switch;//memorizzio il cambio nell'oggetto giocata
        }
    }

}
