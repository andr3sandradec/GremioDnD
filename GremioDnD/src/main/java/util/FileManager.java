package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String ARCHIVO_DEFAULT = "registro_gremio.txt";
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String rutaArchivo;

    public FileManager() {
        this.rutaArchivo = ARCHIVO_DEFAULT;
    }

    public FileManager(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo no puede estar vacía.");
        }
        this.rutaArchivo = rutaArchivo;
    }

    /**
     * Anexa una línea con marca de tiempo al archivo .txt sin borrar lo anterior.
     */
    public void anexarRegistro(String tipoEvento, String contenido) throws IOException {
        String marcaTiempo = LocalDateTime.now().format(FORMATO_FECHA);
        String linea = String.format("[%s] [%s] %s", marcaTiempo, tipoEvento, contenido);

        // El parámetro 'true' activa el modo append (anexar) y try-with-resources cierra el flujo automáticamente
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            writer.write(linea);
            writer.newLine();
        }
    }

    /**
     * Lee todas las líneas registradas en el archivo de historial.
     */
    public List<String> leerHistorial() throws IOException {
        List<String> lineas = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return lineas;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                lineas.add(linea);
            }
        }
        return lineas;
    }
}