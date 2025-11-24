package mx.unam.subclases;

import mx.unam.banco.*;

public class ClienteDebitoRegular extends ClienteDebito{
    public ClienteDebitoRegular(String nombre, int edad, double saldoCuenta) {
        super(nombre, edad, saldoCuenta);
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("Cliente Débito Regular: " + getNombre() +
                ", Edad: " + getEdad() +
                ", Saldo: $" + getSaldoCuenta());
    }
}
