package app;

import gui.InterfacciaLibreria;
import javax.swing.SwingUtilities;

public class LibreriaApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfacciaLibreria().setVisible(true);
        });
    }
}