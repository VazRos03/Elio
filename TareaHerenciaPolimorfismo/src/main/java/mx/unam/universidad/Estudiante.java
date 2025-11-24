package mx.unam.universidad;

//SUBCLASE DE UNIVERSITARTIO

public abstract class Estudiante extends Universitario{
    private String carrera;

    public Estudiante(String nombre, int edad, String carrera) {
        super(nombre, edad);
        this.carrera = carrera;
    }

    public String getCarrera() { return carrera; }
}
