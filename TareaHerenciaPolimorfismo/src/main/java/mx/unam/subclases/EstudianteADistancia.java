package mx.unam.subclases;

//importando la sublase estudiante
import mx.unam.universidad.Estudiante;

public class EstudianteADistancia extends Estudiante{
    public EstudianteADistancia(String nombre, int edad, String carrera) {
        super(nombre, edad, carrera);
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("Estudiante a Distancia: " + getNombre() +
                ", Edad: " + getEdad() + ", Carrera: " + getCarrera());
    }
}
