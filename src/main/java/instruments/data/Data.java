package instruments.data;

import instruments.logic.Instrumento;
import instruments.logic.TipoInstrumento;
import instruments.logic.XmlPersister;
import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Data {

    @XmlElementWrapper(name = "instrumentos")
    @XmlElement(name = "instrumento")
    private List<Instrumento> instrumentos;
    @XmlElementWrapper(name = "tipos")
    @XmlElement(name = "tipo")
    private List<TipoInstrumento> tipos;


    public Data() {
        this.tipos = new ArrayList<>();
        this.instrumentos = new ArrayList<>();
    }

    public Data(List<TipoInstrumento> tipos, List<Instrumento> instrumentos) {
        this.tipos = tipos;
        this.instrumentos = instrumentos;
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

//    private void cargarDatosDesdeXML() {
//        try {
//            this.tipos = XmlPersister.cargarTiposInstrumento("files/XMLData/TiposInstrumentos.xml");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void cargarInstrumentosDesdeXML() {
//        try {
//            this.instrumentos = XmlPersister.cargarInstrumentos("files/XMLData/Instrumentos.xml");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}



