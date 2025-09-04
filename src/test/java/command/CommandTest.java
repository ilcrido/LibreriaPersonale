package command;

import modello.*;
import observer.Libreria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test per il Command Pattern")
public class CommandTest {

    private Libreria libreria;
    private Libro libro;
    private GestoreComandi gestoreComandi;

    @BeforeEach
    void setUp() {
        libreria = new Libreria();
        libro = new Libro("Nonloso", "Nonloso", "9788845292613", Genere.ROMANZO);
        gestoreComandi = new GestoreComandi();
    }

    @Test
    @DisplayName("Comando Aggiungi Libro")
    void testComandoAggiungiLibro() {
        Command comando = new AggiungiLibro(libreria, libro);
        comando.execute();

        assertEquals(1, libreria.getNumeroLibri());
        assertTrue(libreria.getTuttiLibri().contains(libro));

        comando.undo();
        assertEquals(0, libreria.getNumeroLibri());
        assertFalse(libreria.getTuttiLibri().contains(libro));
    }

    @Test
    @DisplayName("Comando Rimuovi Libro")
    void testComandoRimuoviLibro() {
        libreria.aggiungiLibro(libro);

        Command comando = new RimuoviLibro(libreria, libro);
        comando.execute();

        assertEquals(0, libreria.getNumeroLibri());
        assertFalse(libreria.getTuttiLibri().contains(libro));

        comando.undo();
        assertEquals(1, libreria.getNumeroLibri());
        assertTrue(libreria.getTuttiLibri().contains(libro));
    }

    @Test
    @DisplayName("Comando Modifica Libro")
    void testComandoModificaLibro() {
        libreria.aggiungiLibro(libro);

        Libro libroModificato = new Libro("Nonloso2", "Nonloso2",
                "9788845292613", Genere.ROMANZO, StatoLettura.LETTO, 5);

        Command comando = new ModificaLibro(libreria, libroModificato);
        comando.execute();

        Libro trovato = libreria.trovaLibroPerIsbn("9788845292613");
        assertEquals("Nonloso2", trovato.getTitolo());
        assertEquals(StatoLettura.LETTO, trovato.getStatoLettura());

        comando.undo();

        Libro ripristinato = libreria.trovaLibroPerIsbn("9788845292613");
        assertEquals("Nonloso", ripristinato.getTitolo());
        assertNull(ripristinato.getStatoLettura());
    }

    @Test
    @DisplayName("Gestore Comandi - Undo e Redo")
    void testGestoreComandi() {
        Command comando1 = new AggiungiLibro(libreria, libro);

        assertFalse(gestoreComandi.puoiFareUndo());
        assertFalse(gestoreComandi.puoiFareRedo());

        gestoreComandi.eseguiComando(comando1);

        assertTrue(gestoreComandi.puoiFareUndo());
        assertFalse(gestoreComandi.puoiFareRedo());
        assertEquals(1, libreria.getNumeroLibri());

        gestoreComandi.undo();

        assertFalse(gestoreComandi.puoiFareUndo());
        assertTrue(gestoreComandi.puoiFareRedo());
        assertEquals(0, libreria.getNumeroLibri());

        gestoreComandi.redo();

        assertTrue(gestoreComandi.puoiFareUndo());
        assertFalse(gestoreComandi.puoiFareRedo());
        assertEquals(1, libreria.getNumeroLibri());
    }

    @Test
    @DisplayName("Comando doppia esecuzione non ha effetto")
    void testComandoDoppiaEsecuzione() {
        Command comando = new AggiungiLibro(libreria, libro);
        comando.execute();
        comando.execute(); // Seconda esecuzione non dovrebbe avere effetto

        assertEquals(1, libreria.getNumeroLibri());
    }

    @Test
    @DisplayName("Undo senza comandi eseguiti")
    void testUndoSenzaComandi() {
        assertFalse(gestoreComandi.undo());
    }

    @Test
    @DisplayName("Redo senza comandi annullati")
    void testRedoSenzaComandi() {
        assertFalse(gestoreComandi.redo());
    }

    @Test
    @DisplayName("Limite cronologia comandi")
    void testLimiteCronologia() {
        // Esegui 12 comandi per superare il limite di 10
        for (int i = 0; i < 12; i++) {
            // Genera ISBN validi
            String isbn = String.format("97888452926%02d", i);
            Libro libroTemp = new Libro("Nonloso" + i, "Nonloso", isbn, Genere.ROMANZO);
            Command comando = new AggiungiLibro(libreria, libroTemp);
            gestoreComandi.eseguiComando(comando);
        }

        int undoCount = 0;
        while (gestoreComandi.puoiFareUndo()) {
            gestoreComandi.undo();
            undoCount++;
        }
        assertEquals(10, undoCount);
    }

    @Test
    @DisplayName("Pulizia cronologia comandi")
    void testPulisciCronologia() {
        Command comando = new AggiungiLibro(libreria, libro);
        gestoreComandi.eseguiComando(comando);

        assertTrue(gestoreComandi.puoiFareUndo());

        gestoreComandi.pulisciCronologia();

        assertFalse(gestoreComandi.puoiFareUndo());
        assertFalse(gestoreComandi.puoiFareRedo());
        assertEquals("", gestoreComandi.getUltimaAzioneUndo());
        assertEquals("", gestoreComandi.getUltimaAzioneRedo());
    }

    @Test
    @DisplayName("Descrizione ultima azione")
    void testGetUltimaAzione() {
        Command comando = new AggiungiLibro(libreria, libro);
        gestoreComandi.eseguiComando(comando);

        assertTrue(gestoreComandi.getUltimaAzioneUndo().contains("Nonloso"));

        gestoreComandi.undo();
        assertTrue(gestoreComandi.getUltimaAzioneRedo().contains("Nonloso"));
    }
}
