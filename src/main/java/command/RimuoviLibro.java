package command;

import modello.Libro;
import observer.Libreria;

public class RimuoviLibro implements Command{

    private Libreria libreria;
    private Libro libro;
    private boolean eseguito = false;

    public RimuoviLibro(Libreria libreria,Libro libro) {
        this.libreria = libreria;
        this.libro = libro;
    }

    @Override
    public void execute() {
        if(!eseguito){
            libreria.rimuoviLibro(libro);
            eseguito = true;
        }
    }

    @Override
    public void undo() {
        if(eseguito){
            libreria.aggiungiLibro(libro);
            eseguito = false;
        }
    }

    @Override
    public String toString(){
        return "Rimosso: "+libro.getTitolo();
    }

}
