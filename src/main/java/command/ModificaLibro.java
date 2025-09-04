package command;

import observer.Libreria;
import modello.Libro;

public class ModificaLibro implements Command{

    private Libreria libreria;
    private Libro libroNuovo;
    private Libro libroVecchio;
    private boolean eseguito = false;

    public ModificaLibro(observer.Libreria libreria, Libro libroNuovo) {
        this.libreria = libreria;
        this.libroNuovo = libroNuovo;
        this.libroVecchio = libreria.trovaLibroPerIsbn(libroNuovo.getIsbn());
    }

    @Override
    public void execute() {
        if(!eseguito && libroVecchio!=null){
            libreria.modificaLibro(libroNuovo);
            eseguito = true;
        }
    }

    @Override
    public void undo() {
        if(eseguito && libroVecchio!=null){
            libreria.modificaLibro(libroVecchio);
            eseguito = false;
        }
    }

    @Override
    public String toString(){
        return "Modificato: "+libroNuovo.getTitolo();
    }
}
