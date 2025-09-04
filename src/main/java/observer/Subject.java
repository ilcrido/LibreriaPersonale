package observer;

import modello.Libro;

public interface Subject {

    void aggiungiObserver(Observer observer);

    void rimuoviObserver(Observer observer);

    void notificaLibroAggiunto(Libro libro);

    void notificaLibroRimosso(Libro libro);

    void notificaLibroModificato(Libro libro);

}
