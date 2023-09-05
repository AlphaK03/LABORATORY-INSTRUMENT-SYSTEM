package instruments.presentation.tipos;
import instruments.logic.Service;
import instruments.logic.TipoInstrumento;
import instruments.logic.TipoInstrumentoXMLManager;

import java.util.List;
import java.util.Objects;

public class Controller{
    View view;
    Model model;

    public Controller(View view, Model model) {
        model.init(Service.instance().search(new TipoInstrumento()));
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void generatePDFReport(List<TipoInstrumento> tipos) throws Exception {
        Service.instance().generatePDFReport(tipos);
    }

    public void search(TipoInstrumento filter) throws  Exception{
        List<TipoInstrumento> rows = Service.instance().search(filter);
        if (rows.isEmpty()){
            throw new Exception("NINGUN REGISTRO COINCIDE");
        }
        model.setList(rows);
        model.setCurrent(new TipoInstrumento());
        model.commit();
    }

    public void edit(int row) {
        TipoInstrumento selectedInstrumento = model.getList().get(row);
        model.setCurrent(selectedInstrumento);
        view.enableEditing();
    }


    public void delete(TipoInstrumento tipoInstrumento) throws Exception {
        Service.instance().delete(tipoInstrumento);
        model.delete(tipoInstrumento);
    }

    public void create(TipoInstrumento tipoInstrumento) {
        try {
            Service.instance().create(tipoInstrumento);
            model.getList().add(tipoInstrumento);
            model.commit();
            saveData("files/TiposInstrumentos.xml");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public void update(TipoInstrumento tipoInstrumento) throws Exception {
        Service.instance().update(tipoInstrumento);
        model.update(tipoInstrumento);
        saveData("files/TiposInstrumentos.xml");
    }


    public void saveData(String filePath) {
        TipoInstrumentoXMLManager.guardarTiposInstrumento(model.getList(), filePath);
    }


}
