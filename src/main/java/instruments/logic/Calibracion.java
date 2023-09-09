package instruments.logic;

import java.util.ArrayList;
import java.util.List;

public class Calibracion {
    private int numero; // Número de calibración (autogenerado)
    private Instrumento instrumentoCalibrado; // Instrumento calibrado
    private String fecha; // Fecha de la calibración
    private int cantidadMediciones; // Cantidad de medidas o tests
    private List<Medicion> mediciones; // Datos de cada medición

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
    }

    public List<Medicion> getMediciones() {
        return mediciones;
    }

    public void setMediciones(List<Medicion> mediciones) {
        this.mediciones = mediciones;
    }
}