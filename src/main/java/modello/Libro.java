package modello;

import com.opencsv.bean.CsvBindByName;

import java.util.Objects;

public class Libro{
    @CsvBindByName(column = "Titolo")
    private String titolo;
    @CsvBindByName(column = "Autore")
    private String autore;
    @CsvBindByName(column = "ISBN")
    private String isbn;
    @CsvBindByName(column = "Genere")
    private Genere genere;
    @CsvBindByName(column = "Stato")
    private StatoLettura stato;
    @CsvBindByName(column = "Valutazione")
    private int valutazione; //da 1 a 5

    public Libro(){
        //opneCSV usa questo costruttore e poi i metodi setter
    }

    public Libro(String titolo, String autore, String isbn, Genere genere) {
        if(!isbnValido(isbn)){
            throw new IllegalArgumentException("ISBN deve essere di 13 cifre numeriche");
        }
        this.titolo = titolo;
        this.autore = autore;
        this.isbn = isbn;
        this.genere = genere;
    }

    public Libro(String titolo, String autore, String isbn, Genere genere, StatoLettura stato, int valutazione) {
        if(!isbnValido(isbn)){
            throw new IllegalArgumentException("ISBN deve essere di 13 cifre numeriche");
        }
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
        if(!isbnValido(isbn)){
            throw new IllegalArgumentException("ISBN deve essere di 13 cifre numeriche");
        }
        this.isbn = isbn;
    }

    public Genere getGenere() {
        return genere;
    }

    public void setGenere(Genere genere) {
        this.genere = genere;
    }

    public String getGenereName() {
        if (genere == null) {
            return null;
        }
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

    private boolean isbnValido(String isbn){
        return isbn != null && isbn.matches("\\d{13}");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return isbn.equals(libro.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isbn);
    }

    @Override
    public String toString() {
        return String.format("Libro{titolo='%s', autore='%s', genere='%s', stato='%s', valutazione='%s'}",titolo, autore, genere, stato, valutazione);
    }
}
