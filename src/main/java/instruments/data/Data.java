package instruments.data;

import instruments.logic.Instrumento;
import instruments.logic.TipoInstrumento;
import instruments.logic.TipoInstrumentoXMLManager;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private List<TipoInstrumento> tipos;
    private List<Instrumento> instrumentos;

    public Data() {
        tipos = new ArrayList<>();
        instrumentos = new ArrayList<>();
        cargarDatosDesdeXML();
        cargarInstrumentosDesdeXML();
    }

    public List<TipoInstrumento> getTipos() {
        return tipos;
    }

    public List<Instrumento> getInstrumentos() {
        return instrumentos;
    }

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
            this.tipos = TipoInstrumentoXMLManager.cargarTiposInstrumento("files/TiposInstrumentos.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarInstrumentosDesdeXML() {
        try {
            this.instrumentos = TipoInstrumentoXMLManager.cargarInstrumentos("files/Instrumentos.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

