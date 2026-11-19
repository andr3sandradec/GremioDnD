package modelo;

public class Clerigo extends Aventurero {

    private String diosVenerado;

    public Clerigo(String nombre, int nivel, int puntosVida, String diosVenerado) {
        super(nombre, nivel, puntosVida);
        if (diosVenerado == null || diosVenerado.trim().isEmpty()) {
            throw new IllegalArgumentException("El dios venerado no puede estar vacío.");
        }
        this.diosVenerado = diosVenerado.trim();
    }

    public String getDiosVenerado() {
        return diosVenerado;
    }

    @Override
    public String simularCombate() {
        return "El Clérigo " + getNombre()
                + " usa Canalizar Divinidad para curar a sus aliados y expulsar muertos vivientes.";
    }

    @Override
    public String getDetalleEspecial() {
        return "Dios Venerado: " + diosVenerado;
    }

    @Override
    public String getClasePersonaje() {
        return "Clérigo";
    }
}