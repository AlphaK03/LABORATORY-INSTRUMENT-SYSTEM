package instruments.data;

import instruments.logic.Calibracion;
import instruments.logic.Instrumento;
import instruments.logic.TipoInstrumento;
import instruments.logic.TipoInstrumentoXMLManager;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private List<TipoInstrumento> tipos;
    private List<Instrumento> instrumentos;

    private List<Calibracion> calibraciones;

    public Data() {
        tipos = new ArrayList<>();
        instrumentos = new ArrayList<>();
        calibraciones = new ArrayList<>();
        cargarDatosDesdeXML();
        cargarInstrumentosDesdeXML();
        cargarCalibracionesDesdeXML();
    }

    public List<TipoInstrumento> getTipos() {
        return tipos;
    }

    public List<Instrumento> getInstrumentos() {
        return instrumentos;
    }


    public List<Calibracion> getCalibraciones() { return calibraciones; }

    public void addTipoInstrumento(TipoInstrumento tipo) {
        tipos.add(tipo);
    }

    public void removeTipoInstrumento(TipoInstrumento tipo) {
        tipos.remove(tipo);
    }

    public TipoInstrumento findTipoInstrumentoByCodigo(String codigo) {
        return tipos.stream()
                .filter(t -> t.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    public void addInstrumento(Instrumento instrumento) {
        instrumentos.add(instrumento);
    }

    public void removeInstrumento(Instrumento instrumento) {
        instrumentos.remove(instrumento);
    }

    public Instrumento findInstrumentoByCodigo(String serie) {
        return instrumentos.stream()
                .filter(i -> i.getSerie().equals(serie))
                .findFirst()
                .orElse(null);
    }

    private void cargarDatosDesdeXML() {
        try {
            this.tipos = TipoInstrumentoXMLManager.cargarTiposInstrumento("files/XMLData/TiposInstrumentos.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarInstrumentosDesdeXML() {
        try {
            this.instrumentos = TipoInstrumentoXMLManager.cargarInstrumentos("files/XMLData/Instrumentos.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarCalibracionesDesdeXML() {
        try {
            this.calibraciones = TipoInstrumentoXMLManager.cargarCalibraciones("files/XMLData/Calibraciones.xml", instrumentos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}



