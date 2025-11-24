package mx.unam.subclases;

//importando la sublase empleado
import mx.unam.universidad.Empleado;

public class Profesor extends Empleado{
    public Profesor(String nombre, int edad, double salario) {
        super(nombre, edad, salario);
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("Profesor: " + getNombre() +
                ", Edad: " + getEdad() + ", Salario: $" + getSalario());
    }
}
