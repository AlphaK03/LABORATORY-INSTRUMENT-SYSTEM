package instruments.data;

import instruments.logic.TipoInstrumento;
import instruments.logic.TipoInstrumentoXMLManager;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private List<TipoInstrumento> tipos;

    public Data() {
        tipos = new ArrayList<>();
        /*
         tipos.add(new TipoInstrumento("TER","Termómetro","Grados Celcius") );
         tipos.add(new TipoInstrumento("BAR","Barómetro","PSI") );
        */
        cargarDatosDesdeXML();

    }

    public List<TipoInstrumento> getTipos() {
        return tipos;
    }
    private void cargarDatosDesdeXML() {
        try {
            this.tipos = TipoInstrumentoXMLManager.cargarTiposInstrumento("files/TiposInstrumentos.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 }
