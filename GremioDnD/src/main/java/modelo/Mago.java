package modelo;

public class Mago extends Aventurero {

    private int espaciosConjuro;

    public Mago(String nombre, int nivel, int puntosVida, int espaciosConjuro) {
        super(nombre, nivel, puntosVida);
        if (espaciosConjuro < 0) {
            throw new IllegalArgumentException("Los espacios de conjuro no pueden ser negativos.");
        }
        this.espaciosConjuro = espaciosConjuro;
    }

    public int getEspaciosConjuro() {
        return espaciosConjuro;
    }

    @Override
    public String simularCombate() {
        return "El Mago " + getNombre()
                + " gasta un espacio de conjuro para lanzar una poderosa Bola de Fuego.";
    }

    @Override
    public String getDetalleEspecial() {
        return "Espacios de Conjuro: " + espaciosConjuro;
    }

    @Override
    public String getClasePersonaje() {
        return "Mago";
    }
}