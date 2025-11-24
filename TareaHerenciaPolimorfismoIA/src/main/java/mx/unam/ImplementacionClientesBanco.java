package mx.unam;

import mx.unam.banco.*;
import mx.unam.subclases.*;

public class ImplementacionClientesBanco {
    public static void main(String[] args) {
        ClienteBancario[] clientes = new ClienteBancario[4];

        clientes[0] = new ClienteNominaInterno("Luis", 28, "Banco UNAM");
        clientes[1] = new ClienteNominaExterno("Ana", 35, "Empresa XYZ");
        clientes[2] = new ClienteDebitoVIP("Carlos", 40, 100000);
        clientes[3] = new ClienteDebitoRegular("María", 30, 5000);

        for (ClienteBancario c : clientes) {
            c.mostrarInformacion();
        }

        // Ejemplo de operación con cliente de débito
        ClienteDebito debito = (ClienteDebito) clientes[3];
        debito.depositar(2000);
        debito.retirar(3000);

        System.out.println("Después de operaciones:");
        debito.mostrarInformacion();
    }
}
