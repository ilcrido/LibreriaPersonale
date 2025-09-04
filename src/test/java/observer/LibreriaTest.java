package observer;

import modello.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

@DisplayName("Test per la classe Libreria")
public class LibreriaTest {

    private Libreria libreria;
    private Libro libro1;
    private Libro libro2;

    @Mock
    private Observer observer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        libreria = new Libreria();
        libro1 = new Libro("1984", "George Orwell", "9788845292613", Genere.FANTASCIENZA);
        libro2 = new Libro("Il Nome della Rosa", "Umberto Eco", "9788845292614", Genere.GIALLO);

        libreria.aggiungiObserver(observer);
    }

    @Test
    @DisplayName("Aggiunta libro con successo")
    void testAggiungiLibro() {
        assertTrue(libreria.aggiungiLibro(libro1));
        assertEquals(1, libreria.getNumeroLibri());
        assertTrue(libreria.getTuttiLibri().contains(libro1));
        verify(observer).libroAggiunto(libro1);
    }

    @Test
    @DisplayName("Aggiunta libro null non valida")
    void testAggiungiLibroNull() {
        assertFalse(libreria.aggiungiLibro(null));
        assertEquals(0, libreria.getNumeroLibri());
    }

    @Test
    @DisplayName("Aggiunta libro duplicato solleva eccezione")
    void testAggiungiLibroDuplicato() {
        libreria.aggiungiLibro(libro1);

        assertThrows(IllegalArgumentException.class, () -> {
            libreria.aggiungiLibro(libro1);
        });
    }

    @Test
    @DisplayName("Rimozione libro esistente")
    void testRimuoviLibro() {
        libreria.aggiungiLibro(libro1);
        assertTrue(libreria.rimuoviLibro(libro1));
        assertEquals(0, libreria.getNumeroLibri());
        assertFalse(libreria.getTuttiLibri().contains(libro1));
        verify(observer).libroRimosso(libro1);
    }

    @Test
    @DisplayName("Rimozione libro inesistente")
    void testRimuoviLibroInesistente() {
        assertFalse(libreria.rimuoviLibro(libro1));
    }

    @Test
    @DisplayName("Rimozione libro per ISBN")
    void testRimuoviLibroPerIsbn() {
        libreria.aggiungiLibro(libro1);
        assertTrue(libreria.rimuoviLibroPerIsbn("9788845292613"));
        assertEquals(0, libreria.getNumeroLibri());
    }

    @Test
    @DisplayName("Modifica libro esistente")
    void testModificaLibro() {
        libreria.aggiungiLibro(libro1);

        Libro libroModificato = new Libro("1984 - Edizione Speciale", "George Orwell",
                "9788845292613", Genere.FANTASCIENZA,
                StatoLettura.LETTO, 5);

        assertTrue(libreria.modificaLibro(libroModificato));
        verify(observer).libroModificato(libroModificato);
    }

    @Test
    @DisplayName("Ricerca libro per ISBN")
    void testTrovaLibroPerIsbn() {
        libreria.aggiungiLibro(libro1);

        Libro trovato = libreria.trovaLibroPerIsbn("9788845292613");
        assertNotNull(trovato);
        assertEquals(libro1, trovato);

        assertNull(libreria.trovaLibroPerIsbn("0000000000000"));
    }

    @Test
    @DisplayName("Ricerca libri per titolo")
    void testTrovaLibriPerTitolo() {
        libro1.setTitolo("Il Signore degli Anelli");
        libro2.setTitolo("Il Nome della Rosa");

        libreria.aggiungiLibro(libro1);
        libreria.aggiungiLibro(libro2);

        List<Libro> risultati = libreria.trovaLibriPerTitolo("il");
        assertEquals(2, risultati.size());

        risultati = libreria.trovaLibriPerTitolo("signore");
        assertEquals(1, risultati.size());
        assertEquals(libro1, risultati.get(0));
    }

    @Test
    @DisplayName("Ricerca libri per autore")
    void testTrovaLibriPerAutore() {
        libreria.aggiungiLibro(libro1);
        libreria.aggiungiLibro(libro2);

        List<Libro> risultati = libreria.trovaLibriPerAutore("orwell");
        assertEquals(1, risultati.size());
        assertEquals(libro1, risultati.get(0));
    }

    @Test
    @DisplayName("Pattern Observer - notifiche multiple")
    void testObserverPattern() {
        Observer observer2 = mock(Observer.class);
        libreria.aggiungiObserver(observer2);

        libreria.aggiungiLibro(libro1);

        verify(observer).libroAggiunto(libro1);
        verify(observer2).libroAggiunto(libro1);

        libreria.rimuoviObserver(observer2);
        libreria.rimuoviLibro(libro1);

        verify(observer).libroRimosso(libro1);
        verify(observer2, never()).libroRimosso(libro1);
    }
}
