package main.strategy;

import main.modello.Libro;
import java.util.List;

public interface Strategy {

    List<Libro> filtra(List<Libro> libri);

}
