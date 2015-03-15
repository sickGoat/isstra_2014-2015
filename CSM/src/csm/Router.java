package csm;

import csm.util.Job;
import java.util.LinkedList;
import java.util.Random;

import sde.actor.Actor;
import sde.actor.Message;
import sde.actor.distribuzioni.Distribuzione;

public class Router extends Actor implements RouterIF {
	
	private final byte JUST_CREATED = 0;
	private final byte FREE = 1;
	private final byte BUSY = 2;
	private final Departure partenza = new Departure();
	private final Osservatore osservatore = new OsservatoreStazione();
	private final Random r = new Random();
	private final LinkedList<Job> waiting_line = new LinkedList<>();
	private Distribuzione distribuzione;
	private double[] probabilita;
	private StazioneIF[] stazioni_uscita;
	
	public Router(){
		become(JUST_CREATED);
	}
	
	@Override
	public void handler(Message m) {
		double now = now();
                double t_servizio;
                Job j = null;
		switch ( currentStatus() ) {
		case JUST_CREATED:
			if( m instanceof Init ){
                            stazioni_uscita = ((Init) m).getStazioni();
                            probabilita = ((Init) m).getProbabilita();
                            distribuzione = ((Init) m).getDistribuzione();
                            become(FREE);
			}
			break;
		
		case FREE:
			if( m instanceof Arrival ){
                            j = ((Arrival)m).job;
                            partenza.setJob(j);
                            t_servizio = distribuzione.prossimoCampione();
                            send(partenza, now+t_servizio);
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
                            int next_station = guessNextStation();
                            stazioni_uscita[next_station].send(new Arrival(j));
                            if( waiting_line.isEmpty() ){ 
                                become(FREE);
                            }
                            else{
                                j = waiting_line.poll();
                                partenza.setJob(j);
                                t_servizio = distribuzione.prossimoCampione();
                                send(partenza,now+t_servizio);
                                osservatore.nextTempoServizio(t_servizio);
                                osservatore.nextTempoCoda(j, now);
                            }
			}
		}
	}
	
	private int guessNextStation(){
		double monetina = r.nextDouble();
		double tacca = 0;
		int i = 0;
		for( ; i < probabilita.length ; i++ ){
			if( monetina >= tacca && monetina < tacca+probabilita[i] ){
				return i;
			}else{
				tacca += probabilita[i];
			}
		}
		return i;
	}

	@Override
	public Osservatore getOsservatore() { return this.osservatore;	}
}
