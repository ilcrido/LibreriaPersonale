package main.command;

import main.Libreria;
import main.modello.Libro;

public class ModificaLibro implements Command{

    private Libreria libreria;
    private Libro libro;

    public ModificaLibro(Libreria libreria, Libro libro) {
        this.libreria = libreria;
        this.libro = libro;
    }

    public void execute() {
        libreria.modificaLibro(libro);
    }
}
