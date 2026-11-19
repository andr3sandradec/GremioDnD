package modelo;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import util.FileManager;
import static org.junit.Assert.*;

public class AventureroTest {

    @Test
    public void testSimularCombatePolimorfico() {
        List<Aventurero> grupo = Arrays.asList(
                new Guerrero("Aragorn", 5, 45, true),
                new Mago("Gandalf", 10, 60, 8),
                new Clerigo("Jester", 4, 32, "El Viajero"),
                new Monje("Beau", 6, 48, 6)
        );

        assertEquals("El Guerrero Aragorn usa Oleada de Acción para realizar múltiples ataques físicos continuos.",
                grupo.get(0).simularCombate());
        assertEquals("El Mago Gandalf gasta un espacio de conjuro para lanzar una poderosa Bola de Fuego.",
                grupo.get(1).simularCombate());
        assertEquals("El Clérigo Jester usa Canalizar Divinidad para curar a sus aliados y expulsar muertos vivientes.",
                grupo.get(2).simularCombate());
        assertEquals("El Monje Beau gasta puntos de Ki para ejecutar una Ráfaga de Golpes y un Ataque Aturdidor.",
                grupo.get(3).simularCombate());
    }

    @Test
    public void testSobrecargaConstructorYSubirNivel() {
        Guerrero novato = new Guerrero("Thorin", 20, false);
        assertEquals(1, novato.getNivel());

        novato.subirNivel();
        assertEquals(2, novato.getNivel());
        assertEquals(25, novato.getPuntosVida());

        novato.subirNivel(3);
        assertEquals(5, novato.getNivel());
        assertEquals(40, novato.getPuntosVida());
        assertTrue(novato.toString().contains("Armadura Pesada: No"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNombreNuloLanzaExcepcion() {
        new Guerrero(null, 3, 30, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNivelFueraDeRangoLanzaExcepcion() {
        new Mago("Merlin", 25, 20, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPuntosVidaNegativosLanzaExcepcion() {
        new Mago("Merlin", 5, -10, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAtributoEspecialInvalidoLanzaExcepcion() {
        new Monje("Lee", 3, 25, -2);
    }

    @Test
    public void testPersistenciaLecturaYEscritura() throws IOException {
        String archivoPrueba = "test_gremio_temp.txt";
        File temp = new File(archivoPrueba);
        if (temp.exists()) {
            temp.delete();
        }

        FileManager fm = new FileManager(archivoPrueba);
        fm.anexarRegistro("PRUEBA", "Registro de prueba unitaria");
        List<String> lineas = fm.leerHistorial();

        assertEquals(1, lineas.size());
        assertTrue(lineas.get(0).contains("Registro de prueba unitaria"));
        assertTrue(temp.delete());
    }
}