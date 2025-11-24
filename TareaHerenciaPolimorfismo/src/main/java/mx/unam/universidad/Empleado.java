package mx.unam.universidad;

// SUBCLASE EMPLEADO DE LA CLASE UNIVERSITARIO

public abstract class Empleado extends Universitario {
    private double salario;

    public Empleado(String nombre, int edad, double salario) {
        super(nombre, edad);
        this.salario = salario;
    }

    public double getSalario() { return salario; }
}
