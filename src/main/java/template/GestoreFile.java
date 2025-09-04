package template;

import modello.Libro;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public abstract class GestoreFile {
    public final void salvaLibreria(List<Libro> libri, String percorso){
        try{
            String datiFormattati = formattaDati(libri);
            scriviSuFile(datiFormattati,percorso);
            System.out.println("Libreria salvata in: "+percorso);
        }catch (IOException e){
            System.err.println("Errore nel salvataggio della libreria: "+e.getMessage());
        }
    }
    public final List<Libro> caricaLibreria(String percorso){
        try {
            String datiGrezzi = leggiDaFile(percorso);
            List<Libro> libri = estraiLibri(datiGrezzi);
            System.out.println("Libreria caricata da: " + percorso);
            return libri;
        }catch(IOException e){
            System.err.println("Errore nel caricare il file: "+e.getMessage());
            return null;
        }
    }
    protected abstract String formattaDati(List<Libro> libri);
    protected abstract List<Libro> estraiLibri(String datiGrezzi);

    protected void scriviSuFile(String dati, String percorso) throws IOException {
        try(BufferedWriter bw = Files.newBufferedWriter(Paths.get(percorso))){
            bw.write(dati);
        }
    }
    protected String leggiDaFile(String percorso) throws IOException {
        return new String(Files.readAllBytes(Paths.get(percorso)));
    }

}

