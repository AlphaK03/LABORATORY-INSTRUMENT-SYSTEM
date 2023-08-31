package instruments.presentation.tipos;

import instruments.logic.Service;
import instruments.logic.TipoInstrumento;

import java.util.List;

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
        model.setCurrent(selectedInstrumento); // Establece el instrumento seleccionado como "current"
        view.enableEditing(); // Activa la edición en la vista
    }


    public void delete(TipoInstrumento tipoInstrumento) throws Exception {
        Service.instance().delete(tipoInstrumento);
        model.delete(tipoInstrumento);
    }

    public void create(TipoInstrumento tipoInstrumento) {
        try {
            Service.instance().create(tipoInstrumento);
            model.getList().add(tipoInstrumento); // Agregar el nuevo registro a la lista del modelo
            model.commit(); // Notificar a la vista que la lista ha cambiado
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public void update(TipoInstrumento tipoInstrumento) throws Exception {
        Service.instance().update(tipoInstrumento); // Actualiza el registro en la capa de lógica
        model.update(tipoInstrumento); // Actualiza el registro en el modelo
    }


}
