package instruments.presentation.instrumentos;

import instruments.logic.Instrumento;
import instruments.logic.Service;
import instruments.logic.XmlPersister;

import java.util.List;

public class InstrumentosController {
    InstrumentosView instrumentosView;
    InstrumentosModel instrumentosModel;




    public InstrumentosController(InstrumentosView view, InstrumentosModel model) {
        model.init(Service.instance().getData().getInstrumentos());
        this.instrumentosView = view;
        this.instrumentosModel = model;
        view.setInstrumentosController(this);
        view.setInstrumentosModel(model);
    }

    public void search(Instrumento filter) throws  Exception{
        List<Instrumento> rows = Service.instance().searchInstrumento(filter);
        if (rows.isEmpty()){
            throw new Exception("NINGUN REGISTRO COINCIDE");
        }
        instrumentosModel.setList(rows);
        instrumentosModel.setCurrent(new Instrumento());
        instrumentosModel.commit();
    }

    public void edit(int row) {
        Instrumento selectedInstrumento = instrumentosModel.getList().get(row);
        instrumentosModel.setCurrent(selectedInstrumento);
        instrumentosView.enableEditing();
    }


    public void delete(Instrumento instrumento) throws Exception {
        Service.instance().deleteInstrumento(instrumento);
        instrumentosModel.delete(instrumento);
        instrumentosModel.current = null;
        onSelectInstrumento();
    }

    public void create(Instrumento instrumento) {
        try {
            Service.instance().createInstrumento(instrumento);
            instrumentosModel.commit();
            saveData();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void update(Instrumento instrumento) throws Exception {
        Service.instance().updateInstrumento(instrumento);
        instrumentosModel.update(instrumento);
        saveData();
    }

    public void saveData() throws Exception {
        Service.instance().saveData();
    }

    public void generatePDFReport(List<Instrumento> instrumentos) throws Exception {
        Service.instance().generatePDFReport(instrumentos);
    }


    public void onSelectInstrumento() {
        Service.instance().onSelectInstrumento(instrumentosModel.getCurrent());
    }

    public Instrumento getSelectedInstrumento(){
        return Service.instance().getLastSelectedInstrumentoSelectInstrumento();
    }
}
