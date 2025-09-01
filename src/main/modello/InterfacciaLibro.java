package main.modello;

public interface InterfacciaLibro {

    public String getTitolo();
    public String getAutore();
    public String getIsbn();
    public Genere getGenere();
    public String getGenereName();
    public StatoLettura getStatoLettura();
    public int getValutazione();

    public void setTitolo(String titolo);
    public void setAutore(String autore);
    public void setIsbn(String isbn);
    public void setGenere(Genere genere);
    public void setStatoLettura(StatoLettura stato);
    public void setValutazione(int valutazione);

    public String toString();
    public boolean equals(Object obj);
    public int hashCode();
}
