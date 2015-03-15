/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minority.game.util;

import java.awt.List;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 *
 * @author antonio
 */
public class FileHandler {
    
    
    public static void storeCSVFile(LinkedList<Coordinate>dati) throws IOException{
        FileWriter wr = new FileWriter("/home/antonio/minority_game.csv");
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator("\n");
        CSVPrinter printer = new CSVPrinter(wr, format);
        printer.printRecord(new Object[]{"X","Y"});
        for( Coordinate c : dati ){
            ArrayList record = new ArrayList();
            record.add(String.valueOf(c.getX()));record.add(String.valueOf(c.getY()));
            printer.printRecord(record);
        }
        wr.flush();
        wr.close();
        printer.close();
    }
    
}
