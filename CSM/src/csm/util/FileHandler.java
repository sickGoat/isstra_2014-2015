/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csm.util;

import java.io.File;
import java.util.concurrent.CyclicBarrier;
import jxl.Cell;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.Number;
import jxl.write.WritableCell;
            


/**
 *
 * @author antonio
 */
public class FileHandler {
    
    private static final String TEMPLATE_PATH = "/template/modello.xls";
    private static final String FOGLIO_RISPOSTA = "Risposta",FOGLIO_SERVIZIO = "Servizio",
                                FOGLIO_ATTESA = "Attesa" , FOGLIO_UTILIZZAZIONE = "Utilizzazione",
                                FOGLIO_THROUGHPUT = "Throughput";
    private File output;
    private Statistica result;

    public FileHandler() { }
    
    public FileHandler(File output,Statistica result) { this.output = output;this.result = result; }

    public File getOutput() {
        return output;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    public Statistica getResult() {
        return result;
    }

    public void setResult(Statistica result) {
        this.result = result;
    }
    
    public void creaFile(){
        String user_dir = System.getProperty("user.dir");
        File template = new File(user_dir+TEMPLATE_PATH);
        Workbook w;
        try{
            w = Workbook.getWorkbook(template);
            WritableWorkbook nuovo = Workbook.createWorkbook(output, w);
            CyclicBarrier barrier = new CyclicBarrier(w.getNumberOfSheets()+1);
            lanciaThread(nuovo,barrier);
            barrier.await();
            nuovo.write();
            nuovo.close();
        }catch(Exception e){}
    }
    
    public void sovrascriviFile(){
        try {
            WritableWorkbook ww = Workbook.createWorkbook(output,Workbook.getWorkbook(output));
            CyclicBarrier barrier = new CyclicBarrier(ww.getNumberOfSheets()+1);
            lanciaThread(ww, barrier);
            barrier.await();
            ww.write();
            ww.close();
        } catch (Exception ex) {System.out.println(ex.getMessage());}
        
    
    }
    
    private void lanciaThread(WritableWorkbook nuovo,CyclicBarrier barrier){
        String[] fogli = nuovo.getSheetNames();
        for (String nome_foglio : fogli) {
            switch( nome_foglio ){
                case FOGLIO_UTILIZZAZIONE:
                    new Thread(new WriterUtilizzazione(barrier,nuovo.getSheet(nome_foglio))).start();
                    break;
                case FOGLIO_RISPOSTA:
                    new Thread(new WriterRisposta(barrier, nuovo.getSheet(nome_foglio))).start();
                    break;
                case FOGLIO_SERVIZIO:
                    new Thread(new WriterServizio(barrier, nuovo.getSheet(nome_foglio))).start();
                    break;
                case FOGLIO_ATTESA:
                    new Thread(new WriterAttesa(barrier, nuovo.getSheet(nome_foglio))).start();
                    break;
                case FOGLIO_THROUGHPUT:
                    new Thread(new WriterThroughput(barrier, nuovo.getSheet(nome_foglio))).start();
            }
        }
    }
    
    private class WriterUtilizzazione implements Runnable{
        private CyclicBarrier barrier;
        private WritableSheet sheet;

        public WriterUtilizzazione(CyclicBarrier barrier,WritableSheet s) {
            this.barrier = barrier; this.sheet = s;
        }

        @Override
        public void run() {
            try {
                double rate_router = result.rate_router;
                System.out.println(rate_router);
                int row;
                if( rate_router == 1 ){
                    Cell pop_cell = sheet.findCell(Integer.toString(result.n_popolazione), 0, 5, 0, 16, false);
                    row = pop_cell.getRow();
                }else{
                    Cell pop_cell = sheet.findCell(Integer.toString(result.n_popolazione),0,26,0,40,false);
                    row = pop_cell.getRow();                    
                }
                sheet.addCell((WritableCell)new Number(1,row,result.utilizzazione_sistema));
                sheet.addCell((WritableCell)new Number(2,row,result.utilizzazione_router));
                sheet.addCell((WritableCell)new Number(3,row,result.utilizzazione_stazione2));
                sheet.addCell((WritableCell)new Number(4,row,result.utilizzazione_stazone3));
                sheet.addCell((WritableCell)new Number(5,row,result.utilizzazione_stazione4));
                barrier.await();
            } catch (Exception ex) {}
        }
    }
    
    
    private class WriterRisposta implements Runnable{
    
        private CyclicBarrier barrier;
        private WritableSheet sheet;

        public WriterRisposta(CyclicBarrier barrier, WritableSheet sheet) {
            this.barrier = barrier; this.sheet = sheet;
        }
        
        @Override
        public void run() {
             try {
                double rate_router = result.rate_router;
                int row;
                if( rate_router == 1 ){
                    Cell pop_cell = sheet.findCell(Integer.toString(result.n_popolazione), 0, 5, 0, 16, false);
                    row = pop_cell.getRow();
                }else{
                    Cell pop_cell = sheet.findCell(Integer.toString(result.n_popolazione),0,26,0,40,false);
                    row = pop_cell.getRow();
                }
                sheet.addCell((WritableCell)new Number(1,row,result.risposta_sistema));
                sheet.addCell((WritableCell)new Number(2,row,result.risposta_router));
                sheet.addCell((WritableCell)new Number(3,row,result.risposta_stazione2));
                sheet.addCell((WritableCell)new Number(4,row,result.risposta_stazione3));
                sheet.addCell((WritableCell)new Number(5,row,result.risposta_stazione4));
                barrier.await();
            } catch (Exception ex) {}
        }
    }
    
    private class WriterAttesa implements Runnable{
        
        private CyclicBarrier barrier;
        private WritableSheet sheet;

        public WriterAttesa(CyclicBarrier barrier, WritableSheet sheet) {
            this.barrier = barrier; this.sheet = sheet;
        }
        
        @Override
        public void run() {
            try {
                double rate_router = result.rate_router;
                int row;
                if( rate_router == 1 ){
                    Cell pop_cell = sheet.findCell(Integer.toString(result.n_popolazione), 0, 5, 0, 16, false);
                    row = pop_cell.getRow();
                }else{
                    Cell pop_cell = sheet.findCell(Integer.toString(result.n_popolazione),0,26,0,40,false);
                    row = pop_cell.getRow();
                }
                sheet.addCell((WritableCell)new Number(1,row,-1));
                sheet.addCell((WritableCell)new Number(2,row,result.attesa_router));
                sheet.addCell((WritableCell)new Number(3,row,result.attesa_stazione2));
                sheet.addCell((WritableCell)new Number(4,row,result.attesa_stazione3));
                sheet.addCell((WritableCell)new Number(5,row,result.attesa_stazione4));
                barrier.await();
            } catch (Exception ex) {}
        }
        
    
    }
    
    private class WriterServizio implements Runnable{
        
        private CyclicBarrier barrier;
        private WritableSheet sheet;

        public WriterServizio(CyclicBarrier barrier, WritableSheet sheet) {
            this.barrier = barrier; this.sheet = sheet;
        }
        
        
        @Override
        public void run() {
            try {
                double rate_router = result.rate_router;
                int row;
                if( rate_router == 1 ){
                    Cell pop_cell = sheet.findCell(Integer.toString(result.n_popolazione), 0, 5, 0, 16, false);
                    row = pop_cell.getRow();
                }else{
                    Cell pop_cell = sheet.findCell(Integer.toString(result.n_popolazione),0,26,0,40,false);
                    row = pop_cell.getRow();
                }
                sheet.addCell((WritableCell)new Number(1,row,-1));
                sheet.addCell((WritableCell)new Number(2,row,result.servizio_router));
                sheet.addCell((WritableCell)new Number(3,row,result.servizio_stazione2));
                sheet.addCell((WritableCell)new Number(4,row,result.servizio_stazione3));
                sheet.addCell((WritableCell)new Number(5,row,result.servizio_stazione4));
                barrier.await();
            } catch (Exception ex) {}
        }
    
    }
    
    private class WriterThroughput implements Runnable{

        private CyclicBarrier barrier;
        private WritableSheet sheet;

        public WriterThroughput(CyclicBarrier barrier, WritableSheet sheet) {
            this.barrier = barrier; this.sheet = sheet;
        }
        
        @Override
        public void run() {
            try {
                double rate_router = result.rate_router;
                int row;
                if( rate_router == 1 ){
                    Cell pop_cell = sheet.findCell(Integer.toString(result.n_popolazione), 0, 5, 0, 16, false);
                    row = pop_cell.getRow();
                }else{
                    Cell pop_cell = sheet.findCell(Integer.toString(result.n_popolazione),0,26,0,40,false);
                    row = pop_cell.getRow();
                }
                sheet.addCell((WritableCell)new Number(1,row,result.throughput_sistema));
                sheet.addCell((WritableCell)new Number(2,row,result.throughput_router));
                sheet.addCell((WritableCell)new Number(3,row,result.throughput_stazione2));
                sheet.addCell((WritableCell)new Number(4,row,result.throughput_stazone3));
                sheet.addCell((WritableCell)new Number(5,row,result.throughput_stazione4));
                barrier.await();
            } catch (Exception ex) {}
        }
    
    }
    
}
