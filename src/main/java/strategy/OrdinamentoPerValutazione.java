package strategy;

import modello.Libro;

import java.util.ArrayList;
import java.util.List;

public class OrdinamentoPerValutazione implements Strategy{

    private boolean crescente;

    public OrdinamentoPerValutazione(boolean crescente){
        this.crescente = crescente;
    }

    @Override
    public List<Libro> filtra(List<Libro> libri){
        List<Libro> listaOrdinata = new ArrayList<>(libri);
        if(crescente){
            listaOrdinata.sort((libro1,libro2)-> Integer.compare(libro1.getValutazione(), libro2.getValutazione()));
        }else{
            listaOrdinata.sort((libro1,libro2)-> Integer.compare(libro2.getValutazione(), libro1.getValutazione()));
        }
        return listaOrdinata;
    }
}
