package instruments.presentation.calibraciones;

import instruments.logic.Calibracion; // Importa la clase Instrumento en lugar de TipoInstrumento
import instruments.logic.Medicion;

import java.util.List;
import java.util.Observer;

public class CalibracionModel extends java.util.Observable {
    List<Calibracion> list; // Cambia el tipo de List a Instrumento
    static Calibracion current; // Cambia el tipo de current a Instrumento

    int changedProps = NONE;

    @Override
    public void addObserver(Observer o) {
        super.addObserver(o);
        commit();
    }

    public void commit() {
        setChanged();
        notifyObservers(changedProps);
        notifyObservers(LIST);
        changedProps = NONE;
    }

    public CalibracionModel() {
    }

    public void init(List<Calibracion> list) {
        setList(list);
        setCurrent(new Calibracion());
    }

    public List<Calibracion> getList() {
        return list;
    }

    public void setList(List<Calibracion> list) {
        this.list = list;
        changedProps += LIST;
    }

    public static Calibracion getCurrent() {
        return current;
    }

    public void setCurrent(Calibracion current) {
        changedProps += CURRENT;
        CalibracionModel.current = current;
    }

    public void delete(Calibracion instrumento) {
        list.remove(instrumento);
        setCurrent(new Calibracion()); // Crea una instancia de Instrumento en lugar de TipoInstrumento
        changedProps |= LIST;
        commit();
    }

    public void enableEditing() {
        setChanged();
        notifyObservers(CURRENT);
    }

    public void update(Calibracion calibracion) {
        int numero = calibracion.getNumero();
        String numeroStr = String.valueOf(numero);
        list.replaceAll(c -> String.valueOf(c.getNumero()).equals(numeroStr) ? calibracion : c);
        setChanged();
        notifyObservers(LIST);
    }

    public void updateMedicion(Medicion medicion) {
        // Busca la medicion en la lista y actualiza su valor de lectura
        for (int i = 0; i < current.getMediciones().size(); i++) {
            if (current.getMediciones().get(i) == medicion) {
                current.getMediciones().set(i, medicion);
                break;
            }
        }
        update(current);
        changedProps |= CURRENT;
        commit();
    }


    public static int NONE = 0;
    public static int LIST = 1;
    public static int CURRENT = 2;
}