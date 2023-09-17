package instruments.presentation.calibraciones;

import instruments.logic.Calibracion;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import instruments.logic.Medicion;


public class CalibracionTableModel extends AbstractTableModel implements javax.swing.table.TableModel{
    private List<Calibracion> rows;
    private int[] cols;

    public CalibracionTableModel(int[] cols, List<Calibracion> rows) {
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
        Calibracion calibracion = rows.get(row);
        switch (cols[col]) {
            case NUMERO:
                return calibracion.getNumero();
            case FECHA:
                return calibracion.getFecha();
            case MEDICIONES:
                return calibracion.getCantidadMediciones();
            default:
                return "";
        }
    }

    public Calibracion getRowAt(int row) {
        return rows.get(row);
    }

    public static final int NUMERO = 0;
    public static final int FECHA = 1;

    public static final int MEDICIONES = 2;

    private String[] colNames = new String[3];
    private void initColNames() {
        colNames[NUMERO] = "Número";
        colNames[FECHA] = "Fecha";
        colNames[MEDICIONES] = "Mediciones";
    }


}
