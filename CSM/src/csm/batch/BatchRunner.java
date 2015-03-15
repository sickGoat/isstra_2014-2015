/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csm.batch;

import csm.util.SimulationParameter;
import csm.util.Statistica;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 *
 * @author antonio
 */
public class BatchRunner implements Runnable{
    
    private final int RISPOSTA = 0,UTILIZZAZIONE = 1,THROUGHPUT = 2,
            SISTEMA = 3,ROUTER = 4,S2 = 5,S_ERLANG = 6 , S_IPER = 7;
    private int n_run;
    private SimulationParameter parameter;
    private final double CONFIDENCE_95 = 1.960;
    private LinkedList<Statistica> risultati = new LinkedList<>();
    
    
    public BatchRunner() {}

    public BatchRunner(int n_run, SimulationParameter parameter) {
        this.n_run = n_run;this.parameter = parameter;
    }

    public int getN_run() {return n_run;}

    public void setN_run(int n_run) {this.n_run = n_run;}

    public SimulationParameter getParameter() {return parameter;}

    public void setParameter(SimulationParameter parameter) {this.parameter = parameter;}
    
    @Override
    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        for( int i = 0 ; i < n_run ; i++ ){
            SimulationEngine run_instance = new SimulationEngine(parameter);
            Future<Statistica> risultato = executor.submit(run_instance);
            try{risultati.add(risultato.get());}
            catch(Exception e ){}
        }
        stampaRisultati();
        System.out.println("Batch Terminato, file generato in: "+System.getProperty("user.dir"));
        executor.shutdown();
    }
    
    
    private void stampaRisultati(){
        try {
            FileWriter wr = new FileWriter(System.getProperty("user.dir")+"/result_"+datePattern()+".csv");
            CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator("\n");
            CSVPrinter printer = new CSVPrinter(wr, format);
            printer.printRecord(new Object[]{"K="+parameter.n_popolazione+" Router="+parameter.lambda_router,"Media","Varianza","Intervallo di confidenza 95%"});
            double[] campioni_sistema = null,campioni_router = null,campioni_s2 = null,campioni_erl = null,campioni_iper = null;
            for( int i = 0 ; i < 3 ; i++ ){
                switch(i){
                    case RISPOSTA:
                        printer.printRecord("Risposta");
                        campioni_sistema = getCampioni(SISTEMA,RISPOSTA);
                        campioni_router = getCampioni(ROUTER,RISPOSTA);
                        campioni_s2 = getCampioni(S2,RISPOSTA);
                        campioni_erl = getCampioni(S_ERLANG,RISPOSTA);
                        campioni_iper = getCampioni(S_IPER,RISPOSTA);
                        break;
                    case UTILIZZAZIONE:
                        printer.printRecord("Utilizzazione");
                        campioni_sistema = getCampioni(SISTEMA,UTILIZZAZIONE);
                        campioni_router = getCampioni(ROUTER,UTILIZZAZIONE);
                        campioni_s2 = getCampioni(S2,UTILIZZAZIONE);
                        campioni_erl = getCampioni(S_ERLANG,UTILIZZAZIONE);
                        campioni_iper = getCampioni(S_IPER,UTILIZZAZIONE);
                        break;
                    case THROUGHPUT:
                        printer.printRecord("Throughput");
                        campioni_sistema = getCampioni(SISTEMA,THROUGHPUT);
                        campioni_router = getCampioni(ROUTER,THROUGHPUT);
                        campioni_s2 = getCampioni(S2,THROUGHPUT);
                        campioni_erl = getCampioni(S_ERLANG,THROUGHPUT);
                        campioni_iper = getCampioni(S_IPER,THROUGHPUT);
                }
                printer.print("Sistema");
                printer.printRecord(getStatisticheGlobali(campioni_sistema));
                printer.print("Router");
                printer.printRecord(getStatisticheGlobali(campioni_router));
                printer.print("S 2");
                printer.printRecord(getStatisticheGlobali(campioni_s2));
                printer.print("S Erlang");
                printer.printRecord(getStatisticheGlobali(campioni_erl));
                printer.print("S Iper. Exp.");
                printer.printRecord(getStatisticheGlobali(campioni_iper));
            }
            wr.flush();
            wr.close();
            printer.close();
        } catch (IOException ex) {}
      
        
    }
    
    
    private String datePattern(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.get(Calendar.DATE)+"_"+c.get(Calendar.MONTH)+"_"+c.get(Calendar.YEAR)
                +"_"+c.get(Calendar.HOUR)+"_"+c.get(Calendar.MINUTE)+"_"+c.get(Calendar.SECOND);
    }
    
    private double[] getCampioni(int stazione,int parametro){
        double[] campioni = new double[n_run];
        switch(stazione){
        
            case SISTEMA:
                switch(parametro){
                    case RISPOSTA:
                    for(int i = 0 ; i < n_run ; i++ ){ 
                    campioni[i] = risultati.get(i).risposta_sistema;
                    }
                    break;
                case UTILIZZAZIONE:
                    for (int i = 0; i < n_run; i++) {
                        campioni[i] = risultati.get(i).utilizzazione_sistema;
                    }
                    break;
                case THROUGHPUT:
                    for (int i = 0; i < n_run; i++) {
                        campioni[i] = risultati.get(i).throughput_sistema;
                    }
                }
                break;
            case ROUTER:
                switch(parametro){
                    case RISPOSTA:
                    for(int i = 0 ; i < n_run ; i++ ){ 
                    campioni[i] = risultati.get(i).risposta_router;
                    }
                    break;
                case UTILIZZAZIONE:
                    for (int i = 0; i < n_run; i++) {
                        campioni[i] = risultati.get(i).utilizzazione_router;
                    }
                    break;
                case THROUGHPUT:
                    for (int i = 0; i < n_run; i++) {
                        campioni[i] = risultati.get(i).throughput_router;
                    }
                }
                break;
            case S2:
                switch(parametro){
                    case RISPOSTA:
                    for(int i = 0 ; i < n_run ; i++ ){ 
                    campioni[i] = risultati.get(i).risposta_stazione2;
                    }
                    break;
                case UTILIZZAZIONE:
                    for (int i = 0; i < n_run; i++) {
                        campioni[i] = risultati.get(i).utilizzazione_stazione2;
                    }
                    break;
                case THROUGHPUT:
                    for (int i = 0; i < n_run; i++) {
                        campioni[i] = risultati.get(i).throughput_stazione2;
                    }
                }
                break;
            case S_ERLANG:
                switch(parametro){
                    case RISPOSTA:
                    for(int i = 0 ; i < n_run ; i++ ){ 
                    campioni[i] = risultati.get(i).risposta_stazione3;
                    }
                    break;
                case UTILIZZAZIONE:
                    for (int i = 0; i < n_run; i++) {
                        campioni[i] = risultati.get(i).utilizzazione_stazone3;
                    }
                    break;
                case THROUGHPUT:
                    for (int i = 0; i < n_run; i++) {
                        campioni[i] = risultati.get(i).throughput_stazone3;
                    }
                }
                break;
            case S_IPER:
                switch(parametro){
                    case RISPOSTA:
                    for(int i = 0 ; i < n_run ; i++ ){ 
                    campioni[i] = risultati.get(i).risposta_stazione4;
                    }
                    break;
                case UTILIZZAZIONE:
                    for (int i = 0; i < n_run; i++) {
                        campioni[i] = risultati.get(i).utilizzazione_stazione4;
                    }
                    break;
                case THROUGHPUT:
                    for (int i = 0; i < n_run; i++) {
                        campioni[i] = risultati.get(i).throughput_stazione4;
                    }
                }
        }
    
        return campioni;
    }
    
    public LinkedList<Double> getStatisticheGlobali(double[] campioni){
        LinkedList<Double> ris = new LinkedList<>();
        double media = 0;
        double varianza = 0;
        double lower_intervallo = 0;
        double up_intervallo = 0;
        for( double campione : campioni ){ media += campione;}
        media = media/n_run;
        double temp =0;
        for( double campione : campioni ){
            temp += Math.pow(campione-media,2);
        }
        System.out.println("temp = " + temp);
        varianza = Math.sqrt(temp*(n_run-1));
        System.out.println("varianza = " + varianza);
        System.out.println("media = " + media);
        lower_intervallo = media - CONFIDENCE_95*varianza/Math.sqrt(n_run);
        up_intervallo = media + CONFIDENCE_95*varianza/Math.sqrt(n_run);
        ris.add(media);ris.add(varianza);ris.add(lower_intervallo);ris.add(up_intervallo);
        return ris;
    }
    
    
    
    public static void main(String[] args) {
        /*Lettura parametri*/
        if( args.length == 0){ 
            System.out.println("Specificare nel seguente ordine: K, Lambda Router, Numero di run");
            System.exit(-1);
        }
        int n_run;
        if( (n_run = Integer.parseInt(args[2])) < 30 ){ System.out.println("Specificare un numero superiore a 30");System.exit(-1);}
        double lambda_router = Double.parseDouble(args[1]);
        int n_pop = Integer.parseInt(args[0]);
        SimulationParameter p = new SimulationParameter();
        p.alfa_iper_1 = 0.95;
        p.alfa_iper_2 = 0.05;
        p.lambda_iper_1 = 5;
        p.lambda_iper_2 = 0.5;
        p.lambda_s2 = 0.8;
        p.lambda_sr = 0.01;
        p.n_eralng = 16;
        p.lambda_erlang = 0.6;
        p.n_popolazione = n_pop;
        p.lambda_router = lambda_router;
        p.tEnd = 30000000;
        BatchRunner batch = new BatchRunner(n_run,p);
        new Thread(batch).start();
       
    }
    
}
