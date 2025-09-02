package command;

import observer.Libreria;
import modello.Libro;

public class ModificaLibro implements Command{

    private Libreria libreria;
    private Libro libro;

    public ModificaLibro(observer.Libreria libreria, Libro libro) {
        this.libreria = libreria;
        this.libro = libro;
    }

    public void execute() {
        libreria.modificaLibro(libro);
    }
}
