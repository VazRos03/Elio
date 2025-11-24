package mx.unam.aragon;

//importando la clase Tiempo que esta en el paquete clases
import  mx.unam.clases.Tiempo;

public class ImplementacionDeTiempo {
    public static void main(String[] args) {
        // Creamos un objeto de la clase Tiempo
        Tiempo tiempo = new Tiempo();

        // Mostramos la hora inicial
        System.out.print("La hora universal inicial es: ");
        System.out.println(tiempo.aStringUniversal());
        System.out.print("La hora estándar inicial es: ");
        System.out.println(tiempo.toString());
        System.out.println();

        // Cambiamos la hora
        tiempo.establecerTiempo(13, 27, 6);
        System.out.print("La hora universal después de establecerTiempo es: ");
        System.out.println(tiempo.aStringUniversal());
        System.out.print("La hora estándar después de establecerTiempo es: ");
        System.out.println(tiempo.toString());
        System.out.println();

        // Intentamos establecer una hora inválida
        try {
            tiempo.establecerTiempo(99, 99, 99);
        } catch (IllegalArgumentException e) {
            System.out.printf("Excepción: %s\n\n", e.getMessage());
        }

        // Mostramos la hora final después del error
        System.out.println("Después de intentar ajustes inválidos:");
        System.out.print("Hora universal: ");
        System.out.println(tiempo.aStringUniversal());
        System.out.print("Hora estándar: ");
        System.out.println(tiempo.toString());
    }
}
