package instruments.presentation.instrumentos;

import instruments.logic.Instrumento;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class InstrumentosTableModel extends AbstractTableModel implements javax.swing.table.TableModel {
    List<Instrumento> rows;
    int[] cols;

    public InstrumentosTableModel(int[] cols, List<Instrumento> rows) {
        this.cols = cols;
        this.rows = rows;
        initColNames();
    }

    public int getColumnCount() {
        return cols.length;
    }

    public String getColumnName(int col) {
        return colNames[cols[col]];
    }

    public Class<?> getColumnClass(int col) {
        return super.getColumnClass(col);
    }

    public int getRowCount() {
        return rows.size();
    }

    public Object getValueAt(int row, int col) {
        Instrumento instrumento = rows.get(row);
        switch (cols[col]) {
            case SERIE:
                return instrumento.getSerie();
            case DESCRIPCION:
                return instrumento.getDescripcion();
            case TOLERANCIA:
                return instrumento.getTolerancia();
            case MAXIMO:
                return instrumento.getMaximo();
            case MINIMO:
                return instrumento.getMinimo();
            case TIPO_INSTRUMENTO:
                return instrumento.getTipoInstrumento().getNombre();
            default:
                return "";
        }
    }

    public Instrumento getRowAt(int row) {
        return rows.get(row);
    }

    public static final int SERIE = 0;
    public static final int DESCRIPCION = 1;
    public static final int TOLERANCIA = 2;
    public static final int MAXIMO = 3;
    public static final int MINIMO = 4;
    public static final int TIPO_INSTRUMENTO = 5;

    String[] colNames = new String[6];
    private void initColNames() {
        colNames[SERIE] = "Serie";
        colNames[DESCRIPCION] = "Descripción";
        colNames[TOLERANCIA] = "Tolerancia";
        colNames[MAXIMO] = "Máximo";
        colNames[MINIMO] = "Mínimo";
        colNames[TIPO_INSTRUMENTO] = "Tipo de Instrumento";
    }
}
