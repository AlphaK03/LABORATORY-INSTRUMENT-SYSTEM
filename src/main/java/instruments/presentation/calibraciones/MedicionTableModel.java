package instruments.presentation.calibraciones;

import instruments.logic.Medicion;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MedicionTableModel extends AbstractTableModel {
    private List<Medicion> rows;
    private int[] cols;

    public MedicionTableModel(int[] cols, List<Medicion> rows) {
        this.cols = cols;
        this.rows = rows;
        initColNames();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }

    @Override
    public String getColumnName(int col) {
        return colNames[cols[col]];
    }

    @Override
    public Class<?> getColumnClass(int col) {
        switch (cols[col]) {
            default:
                return super.getColumnClass(col);
        }
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        Medicion medicion = rows.get(row);
        switch (cols[col]) {
            case VALOR_REFERENCIA:
                return String.valueOf(medicion.getValorReferencia());
            case VALOR_LECTURA:
                return String.valueOf(medicion.getValorLectura());
            default:
                return "";
        }
    }

    public Medicion getRowAt(int row) {
        return rows.get(row);
    }

    public static final int VALOR_REFERENCIA = 0;
    public static final int VALOR_LECTURA = 1;

    private String[] colNames = new String[2];
    private void initColNames() {
        colNames[VALOR_REFERENCIA] = "Valor de Referencia";
        colNames[VALOR_LECTURA] = "Valor de Lectura";
    }
}
