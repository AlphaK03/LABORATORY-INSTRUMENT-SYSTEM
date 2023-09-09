package instruments.presentation.instrumentos;

import instruments.logic.Instrumento; // Importa la clase Instrumento en lugar de TipoInstrumento

import java.util.List;
import java.util.Observer;

public class InstrumentosModel extends java.util.Observable {
    List<Instrumento> list; // Cambia el tipo de List a Instrumento
    Instrumento current; // Cambia el tipo de current a Instrumento

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

    public InstrumentosModel() {
    }

    public void init(List<Instrumento> list) {
        setList(list);
        setCurrent(new Instrumento()); // Crea una instancia de Instrumento en lugar de TipoInstrumento
    }

    public List<Instrumento> getList() {
        return list;
    }

    public void setList(List<Instrumento> list) {
        this.list = list;
        changedProps += LIST;
    }

    public Instrumento getCurrent() {
        return current;
    }

    public void setCurrent(Instrumento current) {
        changedProps += CURRENT;
        this.current = current;
    }

    public void delete(Instrumento instrumento) {
        list.remove(instrumento);
        setCurrent(new Instrumento()); // Crea una instancia de Instrumento en lugar de TipoInstrumento
        changedProps |= LIST;
        commit();
    }

    public void enableEditing() {
        setChanged();
        notifyObservers(CURRENT);
    }

    public void update(Instrumento instrumento) {
        list.replaceAll(i -> i.getSerie().equals(instrumento.getSerie()) ? instrumento : i);
        setChanged();
        notifyObservers(LIST);
    }

    public static int NONE = 0;
    public static int LIST = 1;
    public static int CURRENT = 2;
}
