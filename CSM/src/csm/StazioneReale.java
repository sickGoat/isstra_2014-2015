package csm;

import csm.util.Job;
import java.util.LinkedList;

import sde.actor.Actor;
import sde.actor.Message;
import sde.actor.distribuzioni.Distribuzione;

public class StazioneReale extends Actor implements StazioneRealeIF {
	
	private final byte JUST_CREATED = 0;
	private final byte FREE = 1;
	private final byte BUSY = 2;
	
	private final LinkedList<Job> waiting_line = new LinkedList<>();
	private final OsservatoreStazione osservatore = new OsservatoreStazione();
	private final Departure partenza = new Departure();
	private StazioneIF stazione_uscita;
	private Distribuzione distribuzione;
	
	public StazioneReale(){
		become(JUST_CREATED);
	}
	
	@Override
	public void handler(Message m) {
		double now = now();
                double t_servizio;
                Job j;
		switch ( currentStatus() ) {
		case JUST_CREATED:
			if( m instanceof Init ){
                            stazione_uscita = ((Init) m).getStazione();
                            distribuzione = ((Init) m).getDistribuzione();
                            become(FREE);
			}
			break;
		
		case FREE:
			if( m instanceof Arrival ){
                            j = ((Arrival)m).job;
                            t_servizio = distribuzione.prossimoCampione();
                            partenza.setJob(j);
                            send(partenza,now+t_servizio);
                            osservatore.nextTempoServizio(t_servizio);
                            become(BUSY);
                        }
			break;
		
		case BUSY:
			if( m instanceof Arrival ){
                            j = ((Arrival)m).job;
                            waiting_line.addLast(j);
			}
			else if( m instanceof Departure ){
                            j = ((Departure)m).job;
                            osservatore.nextTempoRisposta(j, now);
                            j.setArrivoStazione(now);
                            stazione_uscita.send(new Arrival(j));
                            if( waiting_line.isEmpty() ){
                                become(FREE);
                            }else{
                                j = waiting_line.poll();
                                partenza.setJob(j);
                                t_servizio = distribuzione.prossimoCampione();
                                send(partenza,now+t_servizio);
                                osservatore.nextTempoCoda(j, now);
                                osservatore.nextTempoServizio(t_servizio);
                            }
                        }
		}
	}

	@Override
	public Osservatore getOsservatore() { return this.osservatore; }

}
