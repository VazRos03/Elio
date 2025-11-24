package mx.unam.subclases;

import mx.unam.banco.*;

public class ClienteNominaInterno extends ClienteNomina{
    public ClienteNominaInterno(String nombre, int edad, String empresa) {
        super(nombre, edad, empresa);
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("Cliente Nómina Interno: " + getNombre() +
                ", Edad: " + getEdad() +
                ", Empresa: " + getEmpresa());
    }
}
