package mx.unam.subclases;

//importando la sublase estudiante
import mx.unam.universidad.Estudiante;

public class EstudianteMatriculado extends Estudiante{
    public EstudianteMatriculado(String nombre, int edad, String carrera) {
        super(nombre, edad, carrera);
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("Estudiante Matriculado: " + getNombre() +
                ", Edad: " + getEdad() + ", Carrera: " + getCarrera());
    }
}
