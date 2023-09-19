package instruments.logic;

import java.util.ArrayList;
import java.util.List;

public class Instrumento {
    private String serie;
    private String descripcion;
    private double tolerancia;
    private double maximo;
    private double minimo;
    private TipoInstrumento tipoInstrumento; // La referencia al TipoInstrumento asociado

    private List<Calibracion> calibracionList;

    public Instrumento(String serie, String descripcion, double tolerancia, double maximo, double minimo, TipoInstrumento tipoInstrumento, List<Calibracion> calibracionList) {
        this.serie = serie;
        this.descripcion = descripcion;
        this.tolerancia = tolerancia;
        this.maximo = maximo;
        this.minimo = minimo;
        this.tipoInstrumento = tipoInstrumento;
        this.calibracionList = calibracionList;
    }

    public Instrumento(String serie, String descripcion, double tolerancia, double maximo, double minimo, TipoInstrumento tipoInstrumento) {
        this.serie = serie;
        this.descripcion = descripcion;
        this.tolerancia = tolerancia;
        this.maximo = maximo;
        this.minimo = minimo;
        this.tipoInstrumento = tipoInstrumento;
    }

    public Instrumento() {
        this("", "", 0.0, 0.0, 0.0, null, new ArrayList<>());
    }

    // Getters y setters para los atributos

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getTolerancia() {
        return tolerancia;
    }

    public void setTolerancia(double tolerancia) {
        this.tolerancia = tolerancia;
    }

    public double getMaximo() {
        return maximo;
    }

    public void setMaximo(double maximo) {
        this.maximo = maximo;
    }

    public double getMinimo() {
        return minimo;
    }

    public void setMinimo(double minimo) {
        this.minimo = minimo;
    }

    public TipoInstrumento getTipoInstrumento() {
        return tipoInstrumento;
    }

    public void setTipoInstrumento(TipoInstrumento tipoInstrumento) {
        this.tipoInstrumento = tipoInstrumento;
    }

    @Override
    public String toString() {
        return serie + " - " + descripcion + "( " + minimo + " - " + maximo + " " + tipoInstrumento.getUnidad() + " )";
    }

    public String getToleranciaAsString() {
        return String.valueOf(tolerancia);
    }

    public List<Calibracion> getCalibracionList() {
        return calibracionList;
    }

    public void setCalibracionList(List<Calibracion> calibracionList) {
        this.calibracionList = calibracionList;
    }

    public void agregarCalibracion(Calibracion calibracion) {
        calibracionList.add(calibracion);
    }

    public void eliminarCalibracion(Calibracion calibracion) {
        calibracionList.remove(calibracion);
    }
}
