package strategy;

import modello.Libro;

import java.util.ArrayList;
import java.util.List;

public class OrdinamentoPerAutore implements Strategy {

    private boolean crescente;

    public OrdinamentoPerAutore(boolean crescente){
        this.crescente = crescente;
    }

    @Override
    public List<Libro> filtra(List<Libro> libri){
        List<Libro> listaOrdinata = new ArrayList<>(libri);
        if(crescente){
            listaOrdinata.sort((libro1,libro2)-> libro1.getAutore().compareToIgnoreCase(libro2.getAutore()));
        }else{
            listaOrdinata.sort((libro1,libro2)-> libro2.getAutore().compareToIgnoreCase(libro1.getAutore()));
        }
        return listaOrdinata;
    }
}
