package strategy;

import modello.Genere;
import modello.Libro;

import java.util.ArrayList;
import java.util.List;

public class FiltroPerGenere implements Strategy {

    private Genere genere;

    public FiltroPerGenere(Genere genere) {
        this.genere = genere;
    }

    public List<Libro> filtra(List<Libro> libri) {
        List<Libro> filtrati = new ArrayList<>();
        for (Libro libro : libri) {
            if(libro.getGenere()==genere) {
                filtrati.add(libro);
            }
        }
        return filtrati;
    }
}
