package main;

import controlador.ControladorGremio;
import javax.swing.SwingUtilities;
import util.FileManager;
import vista.VistaGremio;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VistaGremio vista = new VistaGremio();
            FileManager fileManager = new FileManager();
            new ControladorGremio(vista, fileManager);
            vista.setVisible(true);
        });
    }
}