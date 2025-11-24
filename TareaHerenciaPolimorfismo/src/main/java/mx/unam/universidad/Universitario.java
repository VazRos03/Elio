package mx.unam.universidad;

public abstract class Universitario {
    private String nombre;
    private int edad;

    public Universitario(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    // Métodos comunes
    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }

    // Método abstracto que debe implementarse en subclases
    public abstract void mostrarInformacion();
}
