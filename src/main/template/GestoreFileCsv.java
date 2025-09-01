package main.template;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import main.modello.Libro;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class GestoreFileCsv extends GestoreFile{

    protected String formattaDati(List<Libro> libri){
        try{
            StringWriter sw = new StringWriter();
            StatefulBeanToCsv<Libro> sbc = new StatefulBeanToCsvBuilder<Libro>(sw).build();

            sbc.write(libri);
            return sw.toString();
        }catch(Exception e){
            System.err.println("Errore nella formattazione CSV "+e.getMessage());
            return "";
        }
    }

    protected List<Libro> estraiLibri(String datiCsv){
        try{
            StringReader sr = new StringReader(datiCsv);
            CsvToBean<Libro> cb = new CsvToBeanBuilder<Libro>(sr).withType(Libro.class).build();

            List<Libro> libriCaricati = cb.parse();
            if(libriCaricati==null){
                return new ArrayList<>();
            }else{
                return libriCaricati;
            }
        }catch(Exception e){
            System.err.println("Errore nell'estrazione da CSV "+e.getMessage());
            return new ArrayList<>();
        }
    }


}
