package instruments.presentation.tipos;
import instruments.logic.Service;
import instruments.logic.TipoInstrumento;
import instruments.logic.XmlPersister;

import java.util.List;

public class TiposController {
    TiposView tiposView;
    TiposModel tiposModel;

    public TiposController(TiposView tiposView, TiposModel tiposModel) {
        tiposModel.init(Service.instance().getData().getTipos());
        this.tiposView = tiposView;
        this.tiposModel = tiposModel;
        tiposView.setController(this);
        tiposView.setModel(tiposModel);
    }

    public void generatePDFReport(List<TipoInstrumento> tipos) throws Exception {
        Service.instance().generatePDFReport(tipos);
    }

    public void search(TipoInstrumento filter) throws  Exception{
        List<TipoInstrumento> rows = Service.instance().searchTipoInstrumento(filter);
        if (rows.isEmpty()){
            throw new Exception("NINGUN REGISTRO COINCIDE");
        }
        tiposModel.setList(rows);
        tiposModel.setCurrent(new TipoInstrumento());
        tiposModel.commit();
    }

    public void edit(int row) {
        TipoInstrumento selectedInstrumento = tiposModel.getList().get(row);
        tiposModel.setCurrent(selectedInstrumento);
        tiposView.enableEditing();
    }


    public void delete(TipoInstrumento tipoInstrumento) throws Exception {
        Service.instance().deleteTipoInstrumento(tipoInstrumento);
        tiposModel.delete(tipoInstrumento);
    }

    public void create(TipoInstrumento tipoInstrumento) {
        try {
            Service.instance().createTipoInstrumento(tipoInstrumento);
            tiposModel.commit();
            saveData();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public void update(TipoInstrumento tipoInstrumento) throws Exception {
        Service.instance().updateTipoInstrumento(tipoInstrumento);
        tiposModel.update(tipoInstrumento);
        saveData();
    }


    public void saveData() throws Exception {
        Service.instance().saveData();
    }


}
