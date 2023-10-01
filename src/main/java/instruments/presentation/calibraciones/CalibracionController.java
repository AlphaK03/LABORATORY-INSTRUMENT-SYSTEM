package instruments.presentation.calibraciones;


import instruments.logic.Calibracion;
import instruments.logic.Instrumento;
import instruments.logic.Service;

import java.util.List;

public class CalibracionController {
    CalibracionView calibracionView;
    CalibracionModel calibracionModel;

    Instrumento lastSelectedInstrumento;

    public void setLastSelectedInstrumento() {
        this.lastSelectedInstrumento = Service.instance().getLastSelectedInstrumentoSelectInstrumento();
        calibracionModel.init(lastSelectedInstrumento.getCalibracionList());
    }

    public CalibracionController(CalibracionView view, CalibracionModel model) {
        model.init(Service.instance().searchCalibracion(new Calibracion()));
        this.calibracionView = view;
        this.calibracionModel = model;
        view.setCalibracionController(this);
        view.setCalibracionModel(model);
    }

    public void search(Calibracion filter) throws Exception {
        List<Calibracion> rows = Service.instance().searchCalibracion(filter);
        if (rows.isEmpty()) {
            throw new Exception("NO RECORDS MATCH");
        }
        calibracionModel.setList(rows);
        calibracionModel.setCurrent(new Calibracion());
        calibracionModel.commit();
    }

    public void edit(int row) {
        Calibracion selectedCalibracion = calibracionModel.getList().get(row);
        calibracionModel.setCurrent(selectedCalibracion);
        calibracionView.enableEditing();
    }

    public void delete(Calibracion calibracion) throws Exception {
        Service.instance().deleteCalibracion(calibracion, lastSelectedInstrumento);
        calibracionModel.delete(calibracion);
    }

    public void create(Calibracion calibracion) {
        try {
            Service.instance().createCalibracion(calibracion, lastSelectedInstrumento);
            calibracionModel.init(lastSelectedInstrumento.getCalibracionList());
            calibracionModel.commit();
            saveData();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public void update(Calibracion calibracion) throws Exception {
        Service.instance().updateCalibracion(calibracion, lastSelectedInstrumento);
        calibracionModel.update(calibracion);
        saveData();
    }

    public void saveData() throws Exception {
        Service.instance().saveData();
    }

    public void generatePDFReport(List<Calibracion> calibracion) throws Exception {
        Service.instance().generatePDFReport(calibracion);
    }
}
