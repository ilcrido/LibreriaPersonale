package observer;

import modello.Libro;

public interface Observer {

    public void libroAggiunto(Libro libro);

    public void libroRimosso(Libro libro);

    public void libroModificato(Libro libro);

}
