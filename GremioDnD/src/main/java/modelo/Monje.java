package modelo;

public class Monje extends Aventurero {

    private int puntosKi;

    public Monje(String nombre, int nivel, int puntosVida, int puntosKi) {
        super(nombre, nivel, puntosVida);
        if (puntosKi < 0) {
            throw new IllegalArgumentException("Los puntos de Ki no pueden ser negativos.");
        }
        this.puntosKi = puntosKi;
    }

    public int getPuntosKi() {
        return puntosKi;
    }

    @Override
    public String simularCombate() {
        return "El Monje " + getNombre()
                + " gasta puntos de Ki para ejecutar una Ráfaga de Golpes y un Ataque Aturdidor.";
    }

    @Override
    public String getDetalleEspecial() {
        return "Puntos de Ki: " + puntosKi;
    }

    @Override
    public String getClasePersonaje() {
        return "Monje";
    }
}