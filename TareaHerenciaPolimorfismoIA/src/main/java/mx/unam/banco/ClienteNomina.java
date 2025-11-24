package mx.unam.banco;

public abstract class ClienteNomina extends ClienteBancario{
    private String empresa;

    public ClienteNomina(String nombre, int edad, String empresa) {
        super(nombre, edad);
        this.empresa = empresa;
    }

    public String getEmpresa() { return empresa; }
}
