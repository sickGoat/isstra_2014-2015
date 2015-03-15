package csm;

import csm.util.Job;

public class OsservatoreSistema implements Osservatore {
    
    private double T_risposta = 0;
    private int servizi = 0;
    private double T_attività = 0;
    private double last_start;
                   
    @Override
    public void nextTempoRisposta(Job j, double now) {
        T_risposta += (now - j.getArrivoSistema());
        servizi++;
    }

    @Override
    public void nextTempoCoda(Job j, double now) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void nextTempoServizio(double servizio) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getThroughput(double tEnd) { return servizi/tEnd; }

    @Override
    public double getUtilizzazione(double tEnd) { 
        if( last_start != -1 && T_attività == 0 ){ T_attività = tEnd - last_start;}
        else if( last_start == -1 && T_attività != 0){ T_attività += (tEnd - last_start);}
        return T_attività/tEnd;
    }

    @Override
    public double getMediaTempoRisposta() { return T_risposta/servizi;  }

    @Override
    public double getMediaTempoCoda() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getMediaTempoServizio() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public double getT_risposta() { return T_risposta;}
    
    public void start(double now){ last_start = now; }
    
    public void stop(double now){ T_attività += ( now - last_start); last_start = -1; }
    
}
