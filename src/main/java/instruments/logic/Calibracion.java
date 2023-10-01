package instruments.logic;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Calibracion {
    private int numero;
    @XmlIDREF
    private Instrumento instrumentoCalibrado;
    private String fecha;
    private int cantidadMediciones;
    @XmlElementWrapper(name = "mediciones")
    @XmlElement(name = "medicion")
    private List<Medicion> mediciones;

    public Calibracion(int numero, Instrumento instrumentoCalibrado, String fecha, int cantidadMediciones, List<Medicion> mediciones) {
        this.numero = numero;
        this.instrumentoCalibrado = instrumentoCalibrado;
        this.fecha = fecha;
        this.cantidadMediciones = cantidadMediciones;
        this.mediciones = mediciones;
    }

    public Calibracion(int numero, String fecha, int cantidadMediciones, List<Medicion> mediciones) {
        this.numero = numero;
        this.fecha = fecha;
        instrumentoCalibrado = new Instrumento();
        this.cantidadMediciones = cantidadMediciones;
        this.mediciones = mediciones;
    }

    public Calibracion() {
        this.mediciones = new ArrayList<>();
    }

    // Getters y setters

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Instrumento getInstrumentoCalibrado() {
        return instrumentoCalibrado;
    }

    public void setInstrumentoCalibrado(Instrumento instrumentoCalibrado) {
        this.instrumentoCalibrado = instrumentoCalibrado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCantidadMediciones() {
        return cantidadMediciones;
    }

    public void setCantidadMediciones(int cantidadMediciones) {
        this.cantidadMediciones = cantidadMediciones;
        this.mediciones.clear();
        generateMediciones();

    }

    public void generateMediciones() {
        if (instrumentoCalibrado != null) {
            double rangoMinimo = instrumentoCalibrado.getMinimo();
            double rangoMaximo = instrumentoCalibrado.getMaximo();

            double paso = (rangoMaximo - rangoMinimo) / (cantidadMediciones);
            for (int i = 0; i < cantidadMediciones; i++) {
                double valorReferencia = rangoMinimo + i * paso;
                this.mediciones.add(new Medicion(valorReferencia, 0.0));
            }
        }
    }


    public List<Medicion> getMediciones() {
        return mediciones;
    }

    public void setMediciones(List<Medicion> mediciones) {
        this.mediciones = mediciones;
    }
}