package instruments.logic;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@XmlAccessorType(XmlAccessType.FIELD)
public class Instrumento {
    @XmlID
    private String serie;
    private String descripcion;
    private double tolerancia;
    private double maximo;
    private double minimo;
    @XmlIDREF
    private TipoInstrumento tipoInstrumento; // La referencia al TipoInstrumento asociado
    @XmlElementWrapper(name = "calibraciones")
    @XmlElement(name = "calibracion")

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
        this.calibracionList = new ArrayList<>();
    }


    public Instrumento() {
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
        if (calibracionList == null) {
            calibracionList = new ArrayList<>();
        }
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
