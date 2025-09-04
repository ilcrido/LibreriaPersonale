package strategy;

import modello.Libro;

import java.util.ArrayList;
import java.util.List;

public class OrdinamentoPerTitolo implements Strategy{

    private boolean crescente;

    public OrdinamentoPerTitolo(boolean crescente){
        this.crescente = crescente;
    }

    @Override
    public List<Libro> filtra(List<Libro> libri){
        List<Libro> listaOrdinata = new ArrayList<>(libri);
        if(crescente){
            listaOrdinata.sort((libro1,libro2)-> libro1.getTitolo().compareToIgnoreCase(libro2.getTitolo()));
        }else{
            listaOrdinata.sort((libro1,libro2)-> libro2.getTitolo().compareToIgnoreCase(libro1.getTitolo()));
        }
        return listaOrdinata;
    }
}
