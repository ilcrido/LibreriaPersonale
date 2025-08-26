package main;

import main.modello.Libro;
import main.observer.Observer;
import main.observer.Subject;

import java.util.ArrayList;
import java.util.List;

public class Libreria implements Subject {

    private List<Libro> libri;

    private List<Observer> observers;

    public Libreria(){
        this.libri = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public void aggiungiLibro(Libro libro){
        if(libro != null && !libri.contains(libro)){
            libri.add(libro);
            notificaLibroAggiunto(libro);
        }
    }

    public boolean rimuoviLibro(Libro libro){
        boolean rimosso = libri.remove(libro);
        if(rimosso){
            notificaLibroRimosso(libro);
        }
        return rimosso;
    }

    public boolean rimuoviLibroPerIsbn(String isbn){
        Libro libro = trovaLibroPerIsbn(isbn);
        if(libro != null){
            return rimuoviLibro(libro);
        }
        return false;
    }

    public boolean modificaLibro(Libro libroModificato){
        for(int i = 0; i < libri.size(); i++){
            if(libri.get(i).equals(libroModificato)){
                libri.set(i, libroModificato);
                notificaLibroModificato(libroModificato);
                return true;
            }
        }
        return false;
    }

    public List<Libro> getTuttiLibri(){
        return new ArrayList<>(libri);
    }

    public Libro trovaLibroPerIsbn(String isbn){
        for(Libro libro : libri){
            if(libro.getIsbn().equals(isbn)){
                return libro;
            }
        }
        return null;
    }

    public List<Libro> trovaLibriPerTitolo(String titolo){
        List<Libro> trovati = new ArrayList<>();
        for(Libro libro : libri){
            if(libro.getTitolo().toLowerCase().contains(titolo.toLowerCase())){
                trovati.add(libro);
            }
        }
        return trovati;
    }

    public List<Libro> trovaLibriPerAutore(String autore){
        List<Libro> trovati = new ArrayList<>();
        for(Libro libro : libri){
            if(libro.getAutore().toLowerCase().contains(autore.toLowerCase())){
                trovati.add(libro);
            }
        }
        return trovati;
    }

    public int getNumeroLibri(){
        return libri.size();
    }

    public void aggiungiObserver(Observer observer){
        if(observer != null && !observers.contains(observer)){
            observers.add(observer);
        }
    }

    public void rimuoviObserver(Observer observer){
        if(observer != null && !observers.contains(observer)){
            observers.remove(observer);
        }
    }

    public void notificaLibroAggiunto(Libro libro){
        for(Observer observer : observers){
            observer.libroAggiunto(libro);
        }
    }

    public void notificaLibroRimosso(Libro libro){
        for(Observer observer : observers){
            observer.libroRimosso(libro);
        }
    }

    public void notificaLibroModificato(Libro libro){
        for(Observer observer : observers){
            observer.libroModificato(libro);
        }
    }

    public void notificaLibriAggiornati(List<Libro> libri){
        for(Observer observer : observers){
            observer.libriAggiornati(libri);
        }
    }

}