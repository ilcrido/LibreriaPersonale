package template;

import modello.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@DisplayName("Test per il Template Method Pattern")
public class GestoreFileTest {

    private GestoreFileCsv gestoreCsv;
    private GestoreFileJson gestoreJson;
    private List<Libro> libriTest;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        gestoreCsv = new GestoreFileCsv();
        gestoreJson = new GestoreFileJson();

        Libro libro1 = new Libro("Nonloso1", "Nonloso1", "9788845292613", Genere.FANTASY, StatoLettura.LETTO, 5);
        Libro libro2 = new Libro("Nonloso2", "Nonloso2", "9788845292614", Genere.HORROR, StatoLettura.IN_LETTURA, 4);

        libriTest = Arrays.asList(libro1, libro2);
    }

    @Test
    @DisplayName("Salvataggio e caricamento file CSV")
    void testSalvataggioCaricamentoCsv() {
        Path fileCsv = tempDir.resolve("test.csv");

        gestoreCsv.salvaLibreria(libriTest, fileCsv.toString());
        List<Libro> libriCaricati = gestoreCsv.caricaLibreria(fileCsv.toString());

        assertNotNull(libriCaricati);
        assertEquals(2, libriCaricati.size());

        Libro primo = libriCaricati.get(0);
        assertEquals("Nonloso1", primo.getTitolo());
        assertEquals("Nonloso1", primo.getAutore());
        assertEquals(Genere.FANTASY, primo.getGenere());
        assertEquals(StatoLettura.LETTO, primo.getStatoLettura());
        assertEquals(5, primo.getValutazione());
    }

    @Test
    @DisplayName("Salvataggio e caricamento file JSON")
    void testSalvataggioCaricamentoJson() {
        Path fileJson = tempDir.resolve("test.json");

        gestoreJson.salvaLibreria(libriTest, fileJson.toString());
        List<Libro> libriCaricati = gestoreJson.caricaLibreria(fileJson.toString());

        assertNotNull(libriCaricati);
        assertEquals(2, libriCaricati.size());

        Libro primo = libriCaricati.get(0);
        assertEquals("Nonloso1", primo.getTitolo());
        assertEquals("Nonloso1", primo.getAutore());
        assertEquals(Genere.FANTASY, primo.getGenere());
        assertEquals(StatoLettura.LETTO, primo.getStatoLettura());
        assertEquals(5, primo.getValutazione());
    }

    @Test
    @DisplayName("Caricamento file inesistente restituisce null")
    void testCaricamentoFileInesistente() {
        List<Libro> risultato = gestoreCsv.caricaLibreria("file_inesistente.csv");
        assertNull(risultato);

        risultato = gestoreJson.caricaLibreria("file_inesistente.json");
        assertNull(risultato);
    }

    @Test
    @DisplayName("Salvataggio lista vuota")
    void testSalvataggioListaVuota() {
        Path fileCsv = tempDir.resolve("vuoto.csv");
        Path fileJson = tempDir.resolve("vuoto.json");

        gestoreCsv.salvaLibreria(Arrays.asList(), fileCsv.toString());
        gestoreJson.salvaLibreria(Arrays.asList(), fileJson.toString());

        List<Libro> csvVuoto = gestoreCsv.caricaLibreria(fileCsv.toString());
        List<Libro> jsonVuoto = gestoreJson.caricaLibreria(fileJson.toString());

        assertNotNull(csvVuoto);
        assertNotNull(jsonVuoto);
        assertTrue(csvVuoto.isEmpty());
        assertTrue(jsonVuoto.isEmpty());
    }

    @Test
    @DisplayName("Consistenza dati tra CSV e JSON")
    void testConsistenzaDatiCsvJson() {
        Path fileCsv = tempDir.resolve("consistenza.csv");
        Path fileJson = tempDir.resolve("consistenza.json");

        gestoreCsv.salvaLibreria(libriTest, fileCsv.toString());
        gestoreJson.salvaLibreria(libriTest, fileJson.toString());

        List<Libro> libriCsv = gestoreCsv.caricaLibreria(fileCsv.toString());
        List<Libro> libriJson = gestoreJson.caricaLibreria(fileJson.toString());

        assertEquals(libriCsv.size(), libriJson.size());

        for (int i = 0; i < libriCsv.size(); i++) {
            Libro csv = libriCsv.get(i);
            Libro json = libriJson.get(i);

            assertEquals(csv.getTitolo(), json.getTitolo());
            assertEquals(csv.getAutore(), json.getAutore());
            assertEquals(csv.getIsbn(), json.getIsbn());
            assertEquals(csv.getGenere(), json.getGenere());
            assertEquals(csv.getStatoLettura(), json.getStatoLettura());
            assertEquals(csv.getValutazione(), json.getValutazione());
        }
    }
}