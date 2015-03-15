package csm;

import csm.util.CSM_Constraints;
import java.util.Random;

import sde.actor.ControlMachine;
import sde.actor.Simulation;
import sde.actor.distribuzioni.*;

public class CSM {

	public static void main(String[] args) {
		
		//distribuzioni
		Random r = new Random(2047);
		Distribuzione d0 = new DistribuzioneEsponenziale(CSM_Constraints.LAMBDA_RIFLESSIONE, r);
		Distribuzione d1 = new DistribuzioneEsponenziale(CSM_Constraints.LAMBDA_S1, r);
		Distribuzione d2 = new DistribuzioneEsponenziale(CSM_Constraints.LAMBDA_S2, r);
		Distribuzione d3 = new DistribuzioneErlang(CSM_Constraints.EARLANG_N, CSM_Constraints.LAMBDA_S3, r);
		Distribuzione d4 = new DistribuzioneIperEsponenziale(CSM_Constraints.PROB_IPER,
											CSM_Constraints.LAMB_IPER, r);
		
		ControlMachine cm = new Simulation(CSM_Constraints.tEND);
		//Stazioni 
		StazioneIF s_riflessione = new StazioneRiflessione();
		StazioneIF router = new Router();
		StazioneIF s2 = new StazioneReale();
		StazioneIF s3 = new StazioneReale();
		StazioneIF s4 = new StazioneReale();
		StazioneIF[] stazioni = { s_riflessione,s2 , s3 ,s4 };

		//bootstrap sazioni
		s_riflessione.send(new StazioneRiflessioneIF.Init(router, d0, CSM_Constraints.N_POP));
		router.send(new RouterIF.Init(stazioni, CSM_Constraints.PROB_INSTRADAMENTI, d1));
		s2.send(new StazioneRealeIF.Init(router, d2));
		s3.send(new StazioneRealeIF.Init(router, d3));
		s4.send(new StazioneRealeIF.Init(router, d4));
		cm.controller();
		
		
		System.out.println("tempo medio risposta sistema: "+s_riflessione.getOsservatore().getMediaTempoRisposta());
		System.out.println("tempo medio risposta router: "+router.getOsservatore().getMediaTempoRisposta());
		System.out.println("tempo medio risposta s2: "+s2.getOsservatore().getMediaTempoRisposta());
		System.out.println("tempo medio risposta s3: "+s3.getOsservatore().getMediaTempoRisposta());
		System.out.println("tempo medio risposta s4: "+s4.getOsservatore().getMediaTempoRisposta());
		System.out.println("**********************");
		System.out.println("tempo medio in coda router: "+router.getOsservatore().getMediaTempoCoda());
		System.out.println("tempo medio in coda s2: "+s2.getOsservatore().getMediaTempoCoda());
		System.out.println("tempo medio in coda s3: "+s3.getOsservatore().getMediaTempoCoda());
		System.out.println("tempo medio in coda s4: "+s4.getOsservatore().getMediaTempoCoda());
		System.out.println("**********************");
		System.out.println("tempo medio servizio router: "+router.getOsservatore().getMediaTempoServizio());
		System.out.println("tempo medio servizio s2: "+s2.getOsservatore().getMediaTempoServizio());
		System.out.println("tempo medio servizio s3: "+s3.getOsservatore().getMediaTempoServizio());
		System.out.println("tempo medio servizio s4: "+s4.getOsservatore().getMediaTempoServizio());
                
                System.out.println("s2.getOsservatore() risposta = " + ((OsservatoreStazione)s2.getOsservatore()).getT_risposta());
                System.out.println("s2.getOsservatore()  servizio= " + ((OsservatoreStazione)s2.getOsservatore()).getT_servizio());
                  System.out.println("s3.getOsservatore() risposta = " + ((OsservatoreStazione)s3.getOsservatore()).getT_risposta());
                System.out.println("s3.getOsservatore()  servizio= " + ((OsservatoreStazione)s3.getOsservatore()).getT_servizio());
                  System.out.println("s4.getOsservatore() risposta = " + ((OsservatoreStazione)s4.getOsservatore()).getT_risposta());
                System.out.println("s4.getOsservatore()  servizio= " + ((OsservatoreStazione)s4.getOsservatore()).getT_servizio());
                  System.out.println("router.getOsservatore() risposta = " + ((OsservatoreStazione)router.getOsservatore()).getT_risposta());
                System.out.println("router.getOsservatore()  servizio= " + ((OsservatoreStazione)router.getOsservatore()).getT_servizio());
                System.out.println(((OsservatoreSistema)s_riflessione.getOsservatore()).getMediaTempoRisposta());
                System.out.println(((OsservatoreSistema)s_riflessione.getOsservatore()).getUtilizzazione(CSM_Constraints.tEND));
        }
	
}
