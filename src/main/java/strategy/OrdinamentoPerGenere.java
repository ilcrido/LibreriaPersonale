package strategy;

import modello.Libro;

import java.util.ArrayList;
import java.util.List;

public class OrdinamentoPerGenere implements Strategy {

    private boolean crescente;

    public OrdinamentoPerGenere(boolean crescente){
        this.crescente = crescente;
    }

    @Override
    public List<Libro> filtra(List<Libro> libri){
        List<Libro> listaOrdinata = new ArrayList<>(libri);
        if(crescente){
            listaOrdinata.sort((libro1,libro2)-> libro1.getGenere().toString().compareToIgnoreCase(libro2.getGenere().toString()));
        }else{
            listaOrdinata.sort((libro1,libro2)-> libro2.getGenere().toString().compareToIgnoreCase(libro1.getGenere().toString()));
        }
        return listaOrdinata;
    }
}
