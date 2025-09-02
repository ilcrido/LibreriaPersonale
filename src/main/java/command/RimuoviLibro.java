package command;

import modello.Libro;
import observer.Libreria;

public class RimuoviLibro implements Command{

    private Libreria libreria;
    private Libro libro;

    public RimuoviLibro(Libreria libreria,Libro libro) {
        this.libreria = libreria;
        this.libro = libro;
    }

    public void execute() {
        libreria.rimuoviLibro(libro);
    }
}
