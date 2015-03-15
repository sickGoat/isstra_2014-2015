/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csm.batch;

import csm.Router;
import csm.RouterIF;
import csm.StazioneIF;
import csm.StazioneReale;
import csm.StazioneRealeIF;
import csm.StazioneRiflessione;
import csm.StazioneRiflessioneIF;
import csm.util.SimulationParameter;
import csm.util.Statistica;
import csm.util.CSM_Constraints;
import java.util.Random;
import java.util.concurrent.Callable;
import sde.actor.ControlMachine;
import sde.actor.Simulation;
import sde.actor.distribuzioni.Distribuzione;
import sde.actor.distribuzioni.DistribuzioneErlang;
import sde.actor.distribuzioni.DistribuzioneEsponenziale;
import sde.actor.distribuzioni.DistribuzioneIperEsponenziale;

/**
 *
 * @author antonio
 */
public class SimulationEngine implements Callable<Statistica>{
    
    private final SimulationParameter parameter;
    
    public SimulationEngine(SimulationParameter p) { this.parameter = p; }
    
    @Override
    public Statistica call() throws Exception {
        Statistica s = new Statistica();
        double[] iper_lam = {
            parameter.lambda_iper_1,
            parameter.lambda_iper_2
        };
        
        double[] iper_prob = {
            parameter.alfa_iper_1,
            parameter.alfa_iper_2
        };
        int tEnd = (int) parameter.tEnd;
        
        Random r = new Random(System.currentTimeMillis());
        Distribuzione d0 = new DistribuzioneEsponenziale(parameter.lambda_sr,r);
        Distribuzione d1 = new DistribuzioneEsponenziale(parameter.lambda_router, r);
        Distribuzione d2 = new DistribuzioneEsponenziale(parameter.lambda_s2, r);
        Distribuzione d3 = new DistribuzioneErlang(parameter.n_eralng,parameter.lambda_erlang, r);
        Distribuzione d4 = new DistribuzioneIperEsponenziale(iper_prob,iper_lam,r);
        ControlMachine cm = new Simulation(tEnd);
        //Stazioni 
        StazioneIF s_riflessione = new StazioneRiflessione();
        StazioneIF router = new Router();
        StazioneIF s2 = new StazioneReale();
        StazioneIF s3 = new StazioneReale();
        StazioneIF s4 = new StazioneReale();
        StazioneIF[] stazioni = { s_riflessione,s2 , s3 ,s4 };
        //bootstrap sazioni
        s_riflessione.send(new StazioneRiflessioneIF.Init(router, d0, parameter.n_popolazione));
        router.send(new RouterIF.Init(stazioni, CSM_Constraints.PROB_INSTRADAMENTI, d1));
        s2.send(new StazioneRealeIF.Init(router, d2));
        s3.send(new StazioneRealeIF.Init(router, d3));
        s4.send(new StazioneRealeIF.Init(router, d4));
        cm.controller();
        s.rate_router = parameter.lambda_router;
        s.n_popolazione = parameter.n_popolazione;
        s.risposta_sistema = s_riflessione.getOsservatore().getMediaTempoRisposta();
        s.risposta_router = router.getOsservatore().getMediaTempoRisposta();
        s.risposta_stazione2 = s2.getOsservatore().getMediaTempoRisposta();
        s.risposta_stazione3 = s3.getOsservatore().getMediaTempoRisposta();
        s.risposta_stazione4 = s4.getOsservatore().getMediaTempoRisposta();
        s.throughput_sistema = s_riflessione.getOsservatore().getThroughput(tEnd);
        s.throughput_router = router.getOsservatore().getThroughput(tEnd);
        s.throughput_stazione2 = s2.getOsservatore().getThroughput(tEnd);
        s.throughput_stazone3 = s3.getOsservatore().getThroughput(tEnd);
        s.throughput_stazione4 = s4.getOsservatore().getThroughput(tEnd);
        s.utilizzazione_sistema = s_riflessione.getOsservatore().getUtilizzazione(tEnd);
        s.utilizzazione_router = router.getOsservatore().getUtilizzazione(tEnd);
        s.utilizzazione_stazione2 = s2.getOsservatore().getUtilizzazione(tEnd);
        s.utilizzazione_stazone3 = s3.getOsservatore().getUtilizzazione(tEnd);
        s.utilizzazione_stazione4 = s4.getOsservatore().getUtilizzazione(tEnd);
        s.attesa_router = router.getOsservatore().getMediaTempoCoda();
        s.attesa_stazione2 = s2.getOsservatore().getMediaTempoCoda();
        s.attesa_stazione3 = s3.getOsservatore().getMediaTempoCoda();
        s.attesa_stazione4 = s4.getOsservatore().getMediaTempoCoda();
        s.servizio_router = router.getOsservatore().getMediaTempoServizio();
        s.servizio_stazione2 = s2.getOsservatore().getMediaTempoServizio();
        s.servizio_stazione3 = s3.getOsservatore().getMediaTempoServizio();
        s.servizio_stazione4 = s4.getOsservatore().getMediaTempoServizio();
        return s;
    }
    
}
