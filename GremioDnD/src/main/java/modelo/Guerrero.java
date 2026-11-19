package modelo;

public class Guerrero extends Aventurero {

    private boolean armaduraPesada;

    public Guerrero(String nombre, int nivel, int puntosVida, boolean armaduraPesada) {
        super(nombre, nivel, puntosVida);
        this.armaduraPesada = armaduraPesada;
    }

    public Guerrero(String nombre, int puntosVida, boolean armaduraPesada) {
        super(nombre, puntosVida);
        this.armaduraPesada = armaduraPesada;
    }

    public boolean isArmaduraPesada() {
        return armaduraPesada;
    }

    @Override
    public String simularCombate() {
        return "El Guerrero " + getNombre()
                + " usa Oleada de Acción para realizar múltiples ataques físicos continuos.";
    }

    @Override
    public String getDetalleEspecial() {
        return "Armadura Pesada: " + (armaduraPesada ? "Sí" : "No");
    }

    @Override
    public String getClasePersonaje() {
        return "Guerrero";
    }
}