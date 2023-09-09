package instruments.logic;

public class Medicion {
    private double valorReferencia; // Valor de referencia que debió haber marcado
    private double valorLectura; // Valor de lectura que en realidad marcó

    // Constructor
    public Medicion(double valorReferencia, double valorLectura) {
        this.valorReferencia = valorReferencia;
        this.valorLectura = valorLectura;
    }

    // Getters y setters

    public double getValorReferencia() {
        return valorReferencia;
    }

    public void setValorReferencia(double valorReferencia) {
        this.valorReferencia = valorReferencia;
    }

    public double getValorLectura() {
        return valorLectura;
    }

    public void setValorLectura(double valorLectura) {
        this.valorLectura = valorLectura;
    }
}