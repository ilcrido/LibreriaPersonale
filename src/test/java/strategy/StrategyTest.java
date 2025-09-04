package strategy;

import modello.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

@DisplayName("Test per il Strategy Pattern")
public class StrategyTest {

    private List<Libro> libri;

    @BeforeEach
    void setUp() {
        Libro libro1 = new Libro("A", "Autore A", "9788845292613", Genere.FANTASY, StatoLettura.LETTO, 5);
        Libro libro2 = new Libro("B", "Autore B", "9788845292614", Genere.HORROR, StatoLettura.IN_LETTURA, 3);
        Libro libro3 = new Libro("C", "Autore C", "9788845292615", Genere.FANTASY, StatoLettura.DA_LEGGERE, 4);

        libri = Arrays.asList(libro1, libro2, libro3);
    }

    @Test
    @DisplayName("Filtro per genere Fantasy")
    void testFiltroPerGenere() {
        Strategy filtro = new FiltroPerGenere(Genere.FANTASY);
        List<Libro> risultato = filtro.filtra(libri);

        assertEquals(2, risultato.size());
        assertTrue(risultato.stream().allMatch(l -> l.getGenere() == Genere.FANTASY));
    }

    @Test
    @DisplayName("Filtro per stato lettura")
    void testFiltroPerStato() {
        Strategy filtro = new FiltroPerStato(StatoLettura.LETTO);
        List<Libro> risultato = filtro.filtra(libri);

        assertEquals(1, risultato.size());
        assertEquals(StatoLettura.LETTO, risultato.get(0).getStatoLettura());
    }

    @Test
    @DisplayName("Filtro per valutazione specifica")
    void testFiltroPerValutazione() {
        Strategy filtro = new FiltroPerValutazione(4);
        List<Libro> risultato = filtro.filtra(libri);

        assertEquals(1, risultato.size());
        assertEquals(4, risultato.get(0).getValutazione());
    }

    @Test
    @DisplayName("Ordinamento per titolo crescente")
    void testOrdinamentoPerTitoloCrescente() {
        Strategy ordinamento = new OrdinamentoPerTitolo(true);
        List<Libro> risultato = ordinamento.filtra(libri);

        assertEquals("A", risultato.get(0).getTitolo());
        assertEquals("B", risultato.get(1).getTitolo());
        assertEquals("C", risultato.get(2).getTitolo());
    }

    @Test
    @DisplayName("Ordinamento per titolo decrescente")
    void testOrdinamentoPerTitoloDecrescente() {
        Strategy ordinamento = new OrdinamentoPerTitolo(false);
        List<Libro> risultato = ordinamento.filtra(libri);

        assertEquals("C", risultato.get(0).getTitolo());
        assertEquals("B", risultato.get(1).getTitolo());
        assertEquals("A", risultato.get(2).getTitolo());
    }

    @Test
    @DisplayName("Ordinamento per autore crescente")
    void testOrdinamentoPerAutoreCrescente() {
        Strategy ordinamento = new OrdinamentoPerAutore(true);
        List<Libro> risultato = ordinamento.filtra(libri);

        assertEquals("Autore A", risultato.get(0).getAutore());
        assertEquals("Autore B", risultato.get(1).getAutore());
        assertEquals("Autore C", risultato.get(2).getAutore());
    }

    @Test
    @DisplayName("Ordinamento per valutazione crescente")
    void testOrdinamentoPerValutazioneCrescente() {
        Strategy ordinamento = new OrdinamentoPerValutazione(true);
        List<Libro> risultato = ordinamento.filtra(libri);

        assertEquals(3, risultato.get(0).getValutazione());
        assertEquals(4, risultato.get(1).getValutazione());
        assertEquals(5, risultato.get(2).getValutazione());
    }

    @Test
    @DisplayName("Ordinamento per valutazione decrescente")
    void testOrdinamentoPerValutazioneDecrescente() {
        Strategy ordinamento = new OrdinamentoPerValutazione(false);
        List<Libro> risultato = ordinamento.filtra(libri);

        assertEquals(5, risultato.get(0).getValutazione());
        assertEquals(4, risultato.get(1).getValutazione());
        assertEquals(3, risultato.get(2).getValutazione());
    }

    @Test
    @DisplayName("Ordinamento per genere crescente")
    void testOrdinamentoPerGenereCrescente() {
        Strategy ordinamento = new OrdinamentoPerGenere(true);
        List<Libro> risultato = ordinamento.filtra(libri);

        assertTrue(risultato.get(0).getGenere() == Genere.FANTASY ||
                risultato.get(1).getGenere() == Genere.FANTASY);
        assertEquals(Genere.HORROR, risultato.get(2).getGenere());
    }
}
