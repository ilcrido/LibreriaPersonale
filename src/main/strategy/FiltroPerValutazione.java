package main.strategy;

import main.modello.Libro;

import java.util.ArrayList;
import java.util.List;

public class FiltroPerValutazione implements Strategy {

    private int valutazione;

    public FiltroPerValutazione(int valutazione) {
        this.valutazione = valutazione;
    }

    public List<Libro> filtra(List<Libro> libri) {
        List<Libro> filtrati = new ArrayList<Libro>();
        for(Libro libro : libri) {
            if(libro.getValutazione() == valutazione) {
                filtrati.add(libro);
            }
        }
        return filtrati;
    }
}
