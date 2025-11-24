package mx.unam.subclases;

//importando la sublase empleado
import mx.unam.universidad.Empleado;

public class DeBase extends Empleado{
    public DeBase(String nombre, int edad, double salario) {
        super(nombre, edad, salario);
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("Empleado de Base: " + getNombre() +
                ", Edad: " + getEdad() + ", Salario: $" + getSalario());
    }
}
