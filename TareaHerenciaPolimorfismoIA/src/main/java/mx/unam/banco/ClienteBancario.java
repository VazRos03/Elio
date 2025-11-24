package mx.unam.banco;

public abstract class ClienteBancario {
    private String nombre;
    private int edad;

    public ClienteBancario(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }

    // Método abstracto que obligará a implementar información específica
    public abstract void mostrarInformacion();
}
