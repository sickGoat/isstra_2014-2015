package csm;

import csm.util.Job;
import sde.actor.Actor;
import sde.actor.Message;
import sde.actor.distribuzioni.Distribuzione;

public class StazioneRiflessione extends Actor implements StazioneRiflessioneIF {
	
	private final byte JUST_CREATED = 0;
	private final byte GENERATING = 1;
	private final OsservatoreSistema osservatore = new OsservatoreSistema();
	private Distribuzione distribuzione;
	private StazioneIF stazione_uscita;
        private int count , pop;
        
	public StazioneRiflessione(){
		become(JUST_CREATED);
	}
	
	@Override
	public void handler(Message m) {
		double now = now();
                Job next;
		switch ( currentStatus() ) {
		case JUST_CREATED:
			if( m instanceof Init ){
                            distribuzione = ((Init) m).getDistribuzione();
                            stazione_uscita = ((Init) m).getStazione();
                            count = ((Init)m).getPopolazione();pop = ((Init)m).getPopolazione();
                            for( int i = 0 ; i < count ; i++ ){
                                send(new Departure(new Job(i)),now+distribuzione.prossimoCampione());
                            }
                            become(GENERATING);
			}
			break;
		
		case GENERATING:
			if( m instanceof Departure ){
                            if( count == pop ){ osservatore.start(now);}//setto inizio periodo di attività
                            next = ((Departure)m).job;
                            next.setArrivoSistema(now);
                            stazione_uscita.send(new Arrival(next));
                            count--;
                        }
			else if( m instanceof Arrival ){
                            next = ((Arrival)m).job;
                            send(new Departure(next),now+distribuzione.prossimoCampione());
                            osservatore.nextTempoRisposta(next,now);count++;
                            if( count == pop ){ osservatore.stop(now);}//al momento non ci sono utenti nel sistema
			}
		}
	}

	@Override
	public Osservatore getOsservatore() { return this.osservatore; }
}
