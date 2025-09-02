package strategy;

import modello.Libro;
import modello.StatoLettura;

import java.util.ArrayList;
import java.util.List;

public class FiltroPerStato implements Strategy {

    private StatoLettura stato;

    public FiltroPerStato(StatoLettura stato) {
        this.stato = stato;
    }

    public List<Libro> filtra(List<Libro> libri) {
        List<Libro> filtrati = new ArrayList<Libro>();
        for (Libro libro : libri) {
            if(libro.getStatoLettura()==stato) {
                filtrati.add(libro);
            }
        }
        return filtrati;
    }
}
