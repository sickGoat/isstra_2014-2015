package csm;

import csm.util.Job;

public interface Osservatore {
    
    public void nextTempoRisposta(Job j, double now);
    public void nextTempoCoda(Job j,double now);
    public void nextTempoServizio(double servizio);
    public double getThroughput(double tEnd);
    public double getUtilizzazione(double tEnd);
    public double getMediaTempoRisposta();
    public double getMediaTempoCoda();
    public double getMediaTempoServizio();
    	
}
