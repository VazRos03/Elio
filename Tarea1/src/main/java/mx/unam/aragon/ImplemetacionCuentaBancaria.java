package mx.unam.aragon;

// UNA VEZ APLICANDO LOS EJEMPLOS MOSTRADOS EN EL CAPITULO 8
// CREAMOS UNA CLASE DE CUENTA BANCARIA EN UN PAQUETE Y LO IMPORTAMOS EN UNA CLASE DONDE
// CREAMOS UNA INSTANCIA PARA SU IMPLEMENTACION


//importando la cuenta bancaria
import mx.unam.clases.CuentaBancaria;

public class ImplemetacionCuentaBancaria {
    public static void main(String[] args) {
        // Crear cuentas
        CuentaBancaria cuenta1 = new CuentaBancaria("Arturo", "12345", 1000.0);
        CuentaBancaria cuenta2 = new CuentaBancaria("María", "67890", 500.0);

        // Mostrar saldo inicial
        cuenta1.mostrarSaldo();
        cuenta2.mostrarSaldo();
        System.out.println();

        // Operaciones
        cuenta1.depositar(200);
        cuenta2.retirar(100);

        // Mostrar saldo después de operaciones
        cuenta1.mostrarSaldo();
        cuenta2.mostrarSaldo();
        System.out.println();

        // Intentar retirar más de lo que hay
        try {
            cuenta2.retirar(1000);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
