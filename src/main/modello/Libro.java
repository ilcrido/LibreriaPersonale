package main.modello;

import java.util.Objects;

public class Libro {
    private String titolo;
    private String autore;
    private String isbn;
    private Genere genere;
    private StatoLettura stato;
    private int valutazione; //da 1 a 5

    public Libro(String titolo, String autore, String isbn, Genere genere) {
        this.titolo = titolo;
        this.autore = autore;
        this.isbn = isbn;
        this.genere = genere;
    }

    public Libro(String titolo, String autore, String isbn, Genere genere, StatoLettura stato, int valutazione) {
        this.titolo = titolo;
        this.autore = autore;
        this.isbn = isbn;
        this.genere = genere;
        this.stato = stato;
        this.valutazione = valutazione;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Genere getGenere() {
        return genere;
    }

    public void setGenere(Genere genere) {
        this.genere = genere;
    }

    public String getGenereName() {
        if (genere == null) return null;
        return genere.name();
    }

    public int getValutazione() {
        return valutazione;
    }

    public void setValutazione(int valutazione) {
        this.valutazione = valutazione;
    }

    public StatoLettura getStatoLettura() {
        return stato;
    }

    public void setStatoLettura(StatoLettura stato) {
        this.stato = stato;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return titolo.equals(libro.titolo) &&
                isbn.equals(libro.isbn) &&
                autore.equals(libro.autore) &&
                genere == libro.genere &&
                stato == libro.stato &&
                valutazione == libro.valutazione;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isbn);
    }
}
