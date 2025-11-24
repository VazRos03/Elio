package mx.unam.banco;

public abstract class ClienteDebito extends ClienteBancario{
    private double saldoCuenta;

    public ClienteDebito(String nombre, int edad, double saldoCuenta) {
        super(nombre, edad);
        this.saldoCuenta = saldoCuenta;
    }

    public double getSaldoCuenta() { return saldoCuenta; }

    public void depositar(double monto) {
        saldoCuenta += monto;
    }

    public void retirar(double monto) {
        if (monto > saldoCuenta) {
            System.out.println("Saldo insuficiente");
        } else {
            saldoCuenta -= monto;
        }
    }
}
