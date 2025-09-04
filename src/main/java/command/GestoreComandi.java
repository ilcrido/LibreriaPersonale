package command;

import java.util.Stack;

public class GestoreComandi {

    private Stack<Command>  cronologiaUndo = new Stack<>();
    private Stack<Command>  cronologiaRedo = new Stack<>();
    private static final int MAX_CRONOLOGIA = 10;

    public void eseguiComando(Command comando){
        comando.execute();
        cronologiaUndo.push(comando);
        cronologiaRedo.clear();
        if(cronologiaUndo.size()>MAX_CRONOLOGIA){
            cronologiaUndo.remove(0);
        }
    }

    public boolean undo(){
        if(!cronologiaUndo.isEmpty()){
            Command comando = cronologiaUndo.pop();
            comando.undo();
            cronologiaRedo.push(comando);
            return true;
        }
        return false;
    }

    public boolean redo(){
        if(!cronologiaRedo.isEmpty()){
            Command comando = cronologiaRedo.pop();
            comando.execute();
            cronologiaUndo.push(comando);
            return true;
        }
        return false;
    }

    public boolean puoiFareUndo(){
        return !cronologiaUndo.isEmpty();
    }

    public boolean puoiFareRedo(){
        return !cronologiaRedo.isEmpty();
    }

    public String getUltimaAzioneUndo(){
        if(!cronologiaUndo.isEmpty()){
            return cronologiaUndo.peek().toString();
        }
        return "";
    }

    public String getUltimaAzioneRedo(){
        if(!cronologiaRedo.isEmpty()){
            return cronologiaRedo.peek().toString();
        }
        return "";
    }

    public void pulisciCronologia(){
        cronologiaUndo.clear();
        cronologiaRedo.clear();
    }

}
