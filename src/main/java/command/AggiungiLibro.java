package command;

import modello.Libro;
import observer.Libreria;

public class AggiungiLibro implements Command {

    private Libreria libreria;
    private Libro libro;

    public AggiungiLibro(Libreria libreria, Libro libro) {
        this.libreria = libreria;
        this.libro = libro;
    }

    @Override
    public void execute() {
        libreria.aggiungiLibro(libro);
    }
}
