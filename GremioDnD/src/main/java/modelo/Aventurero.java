package modelo;

public abstract class Aventurero implements IAccionCombate {

    public static final String SISTEMA_REGLAS = "D&D 5ª Edición";
    private static int contadorAventureros = 0;

    private final int id;
    private String nombre;
    private int nivel;
    private int puntosVida;

    // Constructor completo
    public Aventurero(String nombre, int nivel, int puntosVida) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del aventurero no puede estar vacío.");
        }
        if (nivel < 1 || nivel > 20) {
            throw new IllegalArgumentException("El nivel debe estar entre 1 y 20.");
        }
        if (puntosVida <= 0) {
            throw new IllegalArgumentException("Los puntos de vida deben ser mayores a 0.");
        }
        this.id = ++contadorAventureros;
        this.nombre = nombre.trim();
        this.nivel = nivel;
        this.puntosVida = puntosVida;
    }

    // Sobrecarga de constructor
    public Aventurero(String nombre, int puntosVida) {
        this(nombre, 1, puntosVida);
    }

    // Sobrecarga de métodos (Overload)
    public void subirNivel() {
        subirNivel(1);
    }

    public void subirNivel(int niveles) {
        if (niveles <= 0) {
            throw new IllegalArgumentException("Los niveles a subir deben ser mayores a 0.");
        }
        int nuevoNivel = Math.min(20, this.nivel + niveles);
        int nivelesGanados = nuevoNivel - this.nivel;
        this.nivel = nuevoNivel;
        this.puntosVida += nivelesGanados * 5;
    }

    // Método final: comportamiento común inmutable
    public final String getResumenBasico() {
        return String.format("[%s] #%d %s | Nivel: %d | PV: %d",
                getClasePersonaje(), id, nombre, nivel, puntosVida);
    }

    // Métodos abstractos para evitar instanceof en el controlador
    public abstract String getDetalleEspecial();
    public abstract String getClasePersonaje();

    // Getters y setters 
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        if (nivel < 1 || nivel > 20) {
            throw new IllegalArgumentException("El nivel debe estar entre 1 y 20.");
        }
        this.nivel = nivel;
    }

    public int getPuntosVida() {
        return puntosVida;
    }

    public void setPuntosVida(int puntosVida) {
        if (puntosVida <= 0) {
            throw new IllegalArgumentException("Los puntos de vida deben ser mayores a 0.");
        }
        this.puntosVida = puntosVida;
    }

    public static int getContadorAventureros() {
        return contadorAventureros;
    }

    @Override
    public String toString() {
        return getResumenBasico() + " | " + getDetalleEspecial();
    }
}