package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import modelo.Aventurero;
import modelo.Clerigo;
import modelo.Guerrero;
import modelo.Mago;
import modelo.Monje;
import util.FileManager;
import vista.VistaGremio;

public class ControladorGremio {

    private final List<Aventurero> gremio;
    private final VistaGremio vista;
    private final FileManager fileManager;

    public ControladorGremio(VistaGremio vista, FileManager fileManager) {
        this.gremio = new ArrayList<>();
        this.vista = vista;
        this.fileManager = fileManager;
        inicializarEventos();
        actualizarCampoEspecial();
    }

    private void inicializarEventos() {
        vista.getCmbClase().addActionListener(e -> actualizarCampoEspecial());
        vista.getBtnRegistrar().addActionListener(e -> registrarAventurero());
        vista.getBtnSimularCombate().addActionListener(e -> simularCombate());
        vista.getBtnReportes().addActionListener(e -> mostrarReportes());
        vista.getBtnVerHistorial().addActionListener(e -> mostrarHistorial());
    }

    private void actualizarCampoEspecial() {
        String clase = (String) vista.getCmbClase().getSelectedItem();
        if (clase == null) {
            return;
        }
        boolean esGuerrero = clase.equals("Guerrero");
        vista.getChkArmaduraPesada().setVisible(esGuerrero);
        vista.getTxtAtributoEspecial().setVisible(!esGuerrero);
        vista.getTxtAtributoEspecial().setText("");

        switch (clase) {
            case "Guerrero":
                vista.getLblAtributoEspecial().setText("Armadura Pesada:");
                break;
            case "Mago":
                vista.getLblAtributoEspecial().setText("Espacios de Conjuro:");
                break;
            case "Clérigo":
                vista.getLblAtributoEspecial().setText("Dios Venerado:");
                break;
            case "Monje":
                vista.getLblAtributoEspecial().setText("Puntos de Ki:");
                break;
        }
    }

    public void registrarAventurero() {
        try {
            String clase = (String) vista.getCmbClase().getSelectedItem();
            String nombre = vista.getTxtNombre().getText().trim();
            int nivel = Integer.parseInt(vista.getTxtNivel().getText().trim());
            int puntosVida = Integer.parseInt(vista.getTxtPuntosVida().getText().trim());

            Aventurero nuevo;
            switch (clase) {
                case "Guerrero":
                    boolean armadura = vista.getChkArmaduraPesada().isSelected();
                    nuevo = new Guerrero(nombre, nivel, puntosVida, armadura);
                    break;
                case "Mago":
                    int conjuros = Integer.parseInt(vista.getTxtAtributoEspecial().getText().trim());
                    nuevo = new Mago(nombre, nivel, puntosVida, conjuros);
                    break;
                case "Clérigo":
                    String dios = vista.getTxtAtributoEspecial().getText().trim();
                    nuevo = new Clerigo(nombre, nivel, puntosVida, dios);
                    break;
                case "Monje":
                    int ki = Integer.parseInt(vista.getTxtAtributoEspecial().getText().trim());
                    nuevo = new Monje(nombre, nivel, puntosVida, ki);
                    break;
                default:
                    throw new IllegalArgumentException("Clase de aventurero no reconocida.");
            }

            gremio.add(nuevo);
            fileManager.anexarRegistro("REGISTRO", nuevo.toString());
            actualizarVistaGremio();
            limpiarFormulario();
            JOptionPane.showMessageDialog(vista,
                    "Aventurero registrado exitosamente.", "Gremio D&D",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista,
                    "Error: Nivel, Puntos de Vida y atributos numéricos deben ser números enteros válidos.",
                    "Error de Validación", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage(),
                    "Datos Inválidos", JOptionPane.WARNING_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(vista,
                    "No se pudo guardar en registro_gremio.txt: " + ex.getMessage(),
                    "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void simularCombate() {
        if (gremio.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                    "No hay aventureros registrados para simular un combate.",
                    "Gremio Vacío", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder bitacora = new StringBuilder("=== SIMULACIÓN DE COMBATE ("
                + Aventurero.SISTEMA_REGLAS + ") ===\n");

        // Invocación polimórfica directa sobre la colección sin instanceof
        for (Aventurero aventurero : gremio) {
            String accion = aventurero.simularCombate();
            bitacora.append("• ").append(accion).append("\n");
            try {
                fileManager.anexarRegistro("COMBATE", accion);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(vista,
                        "Error al escribir combate en archivo: " + ex.getMessage(),
                        "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
            }
        }

        vista.getTxtAreaCombate().setText(bitacora.toString());
    }

    public String generarReporteEstadisticas() {
        if (gremio.isEmpty()) {
            return "No hay datos para el reporte de estadísticas.";
        }
        int sumaNiveles = 0;
        int sumaVida = 0;
        Aventurero masResistente = gremio.get(0);

        for (Aventurero a : gremio) {
            sumaNiveles += a.getNivel();
            sumaVida += a.getPuntosVida();
            if (a.getPuntosVida() > masResistente.getPuntosVida()) {
                masResistente = a;
            }
        }
        double nivelPromedio = (double) sumaNiveles / gremio.size();
        return String.format(
                "--- REPORTE 1: ESTADÍSTICAS DEL GREMIO ---\n"
                + "Total de Héroes: %d\n"
                + "Nivel Promedio: %.2f\n"
                + "Puntos de Vida Totales del Grupo: %d\n"
                + "Héroe más resistente: %s (%d PV)\n",
                gremio.size(), nivelPromedio, sumaVida,
                masResistente.getNombre(), masResistente.getPuntosVida());
    }

    public String generarReportePorClase() {
        if (gremio.isEmpty()) {
            return "No hay datos para el reporte por clase.";
        }
        Map<String, Integer> conteo = new LinkedHashMap<>();
        for (Aventurero a : gremio) {
            String tipo = a.getClasePersonaje();
            conteo.put(tipo, conteo.getOrDefault(tipo, 0) + 1);
        }
        StringBuilder sb = new StringBuilder("--- REPORTE 2: DISTRIBUCIÓN POR CLASE ---\n");
        for (Map.Entry<String, Integer> entry : conteo.entrySet()) {
            sb.append(String.format("• %s: %d aventurero(s)\n", entry.getKey(), entry.getValue()));
        }
        return sb.toString();
    }

    private void mostrarReportes() {
        String reporteCompleto = generarReporteEstadisticas() + "\n" + generarReportePorClase();
        vista.getTxtAreaCombate().setText(reporteCompleto);
    }

    public void mostrarHistorial() {
        try {
            List<String> lineas = fileManager.leerHistorial();
            if (lineas.isEmpty()) {
                vista.getTxtAreaCombate().setText("El archivo registro_gremio.txt aún no tiene registros.");
                return;
            }
            StringBuilder sb = new StringBuilder("=== HISTORIAL PERSISTIDO (registro_gremio.txt) ===\n");
            for (String linea : lineas) {
                sb.append(linea).append("\n");
            }
            vista.getTxtAreaCombate().setText(sb.toString());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(vista,
                    "Error al leer el historial: " + ex.getMessage(),
                    "Error de Lectura", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarVistaGremio() {
        StringBuilder sb = new StringBuilder();
        for (Aventurero a : gremio) {
            sb.append(a.toString()).append("\n");
        }
        vista.getTxtAreaGremio().setText(sb.toString());
    }

    private void limpiarFormulario() {
        vista.getTxtNombre().setText("");
        vista.getTxtNivel().setText("");
        vista.getTxtPuntosVida().setText("");
        vista.getTxtAtributoEspecial().setText("");
        vista.getChkArmaduraPesada().setSelected(false);
        vista.getTxtNombre().requestFocus();
    }
}