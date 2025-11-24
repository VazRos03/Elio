package mx.unam;

//importando las subclases
import mx.unam.subclases.*;
import mx.unam.universidad.Universitario;

public class ImplementacionUniversitario {
    public static void main(String[] args) {
        Universitario[] personas = new Universitario[4];

        personas[0] = new EstudianteMatriculado("Arturo", 20, "Ingeniería");
        personas[1] = new EstudianteADistancia("María", 25, "Derecho");
        personas[2] = new Profesor("Elena", 40, 15000);
        personas[3] = new DeBase("Carlos", 30, 10000);

        for (Universitario u : personas) {
            u.mostrarInformacion(); // Polimorfismo en acción
        }
    }
}
