package instruments.logic;

public class Instrumento {
    private String serie;
    private String descripcion;
    private double tolerancia;
    private double maximo;
    private double minimo;
    private TipoInstrumento tipoInstrumento; // La referencia al TipoInstrumento asociado

    public Instrumento(String serie, String descripcion, double tolerancia, double maximo, double minimo, TipoInstrumento tipoInstrumento) {
        this.serie = serie;
        this.descripcion = descripcion;
        this.tolerancia = tolerancia;
        this.maximo = maximo;
        this.minimo = minimo;
        this.tipoInstrumento = tipoInstrumento;
    }

    public Instrumento() {
        this("", "", 0.0, 0.0, 0.0, null);
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
}
