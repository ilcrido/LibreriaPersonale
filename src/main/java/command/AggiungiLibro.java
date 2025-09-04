package command;

import modello.Libro;
import observer.Libreria;

public class AggiungiLibro implements Command {

    private Libreria libreria;
    private Libro libro;
    private boolean eseguito = false;

    public AggiungiLibro(Libreria libreria, Libro libro) {
        this.libreria = libreria;
        this.libro = libro;
    }

    @Override
    public void execute() {
        if(!eseguito) {
            boolean aggiunto = libreria.aggiungiLibro(libro);
            if(aggiunto) {
                eseguito = true;
            }
        }
    }

    @Override
    public void undo() {
        if(eseguito) {
            libreria.rimuoviLibro(libro);
            eseguito = false;
        }
    }

    @Override
    public String toString() {
        return "Aggiunto: "+libro.getTitolo();
    }
}
