package mx.unam.clases;

public class CuentaBancaria {
    private String titular;
    private String numeroCuenta;
    private double saldo;

    // Constructor
    public CuentaBancaria(String titular, String numeroCuenta, double saldoInicial) {
        this.titular = titular;
        this.numeroCuenta = numeroCuenta;
        if (saldoInicial >= 0) {
            this.saldo = saldoInicial;
        } else {
            throw new IllegalArgumentException("Saldo inicial no puede ser negativo");
        }
    }

    // Depositar dinero
    public void depositar(double monto) {
        if (monto > 0) {
            saldo += monto;
        } else {
            throw new IllegalArgumentException("El monto a depositar debe ser positivo");
        }
    }

    // Retirar dinero
    public void retirar(double monto) {
        if (monto > saldo) {
            throw new IllegalArgumentException("Saldo insuficiente");
        } else if (monto <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser positivo");
        } else {
            saldo -= monto;
        }
    }

    // Mostrar saldo
    public void mostrarSaldo() {
        System.out.printf("Cuenta: %s | Titular: %s | Saldo: $%.2f\n", numeroCuenta, titular, saldo);
    }
}
