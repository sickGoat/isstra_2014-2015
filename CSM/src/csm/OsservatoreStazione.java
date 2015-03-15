package csm;

import csm.util.Job;


public class OsservatoreStazione implements Osservatore {
    
    private double T_risposta = 0,
                   T_coda = 0,
                   T_servizio = 0;
    private int servizi = 0;
            
    @Override
    public void nextTempoRisposta(Job j, double now) {
        double delta = now - j.getArrivoStazione();
        T_risposta += delta;
        servizi++;
    }

    @Override
    public void nextTempoCoda(Job j, double now) {
        T_coda += ( now - j.getArrivoStazione() );
    }

    @Override
    public void nextTempoServizio(double servizio) { T_servizio += servizio; }

    @Override
    public double getThroughput(double tEnd) { return servizi/tEnd; }

    @Override
    public double getUtilizzazione(double tEnd) { return T_servizio/tEnd; }
  
    @Override
    public double getMediaTempoRisposta() { return T_risposta/servizi; }

    @Override
    public double getMediaTempoCoda() { return T_coda/servizi; }

    @Override
    public double getMediaTempoServizio() { return T_servizio/servizi; }

    public double getT_risposta() {
        return T_risposta;
    }

    public double getT_coda() {
        return T_coda;
    }

    public double getT_servizio() {
        return T_servizio;
    }
    
    
  }
