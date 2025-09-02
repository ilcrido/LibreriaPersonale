package template;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import modello.Libro;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GestoreFileJson extends GestoreFile{

    private Gson gson;

    public GestoreFileJson() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    protected String formattaDati(List<Libro> libri){
        return gson.toJson(libri);
    }

    protected List<Libro> estraiLibri(String datiJson){
        try{
            Type tipoListaLibri = new TypeToken<List<Libro>>(){}.getType();
            List<Libro> libriCaricati = gson.fromJson(datiJson, tipoListaLibri);
            if(libriCaricati==null){
                return new ArrayList<>();
            }else{
                return libriCaricati;
            }
        }catch(Exception e){
            System.err.println("Errore nell'estrazione da JSON "+e.getMessage());
            return new ArrayList<>();
        }
    }
}
