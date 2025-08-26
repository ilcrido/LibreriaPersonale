package main.observer;

import main.modello.Libro;

import java.util.List;

public interface Subject {

    void aggiungiObserver(Observer observer);

    void rimuoviObserver(Observer observer);

    void notificaLibroAggiunto(Libro libro);

    void notificaLibroRimosso(Libro libro);

    void notificaLibroModificato(Libro libro);

    void notificaLibriAggiornati(List<Libro> libri);

}
