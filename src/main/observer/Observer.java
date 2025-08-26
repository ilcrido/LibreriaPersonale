package main.observer;

import main.modello.Libro;

import java.util.List;

public interface Observer {

    public void libroAggiunto(Libro libro);

    public void libroRimosso(Libro libro);

    public void libroModificato(Libro libro);

    public void libriAggiornati(List<Libro> libri);
}
