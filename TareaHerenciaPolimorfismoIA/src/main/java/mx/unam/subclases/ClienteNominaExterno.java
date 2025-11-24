package mx.unam.subclases;

import mx.unam.banco.*;

public class ClienteNominaExterno extends ClienteNomina{
    public ClienteNominaExterno(String nombre, int edad, String empresa) {
        super(nombre, edad, empresa);
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("Cliente Nómina Externo: " + getNombre() +
                ", Edad: " + getEdad() +
                ", Empresa: " + getEmpresa());
    }
}
