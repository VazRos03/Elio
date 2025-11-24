package mx.unam.subclases;

import mx.unam.banco.*;

public class ClienteDebitoVIP extends ClienteDebito{
    public ClienteDebitoVIP(String nombre, int edad, double saldoCuenta) {
        super(nombre, edad, saldoCuenta);
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("Cliente Débito VIP: " + getNombre() +
                ", Edad: " + getEdad() +
                ", Saldo: $" + getSaldoCuenta());
    }
}
