package instruments.presentation.calibraciones;

import instruments.logic.Medicion;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MedicionTableModel extends AbstractTableModel implements javax.swing.table.TableModel{
    private List<Medicion> rows;
    private int[] cols;
    private CalibracionModel calibracionModel; // Agrega una referencia a CalibracionModel

    public MedicionTableModel(int[] cols, List<Medicion> rows, CalibracionModel calibracionModel) {
        this.cols = cols;
        this.rows = rows;
        this.calibracionModel = calibracionModel; // Asigna la referencia a CalibracionModel
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

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == VALOR_LECTURA;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (col == VALOR_LECTURA) {
            try {
                double newValue = Double.parseDouble((String) value);
                Medicion medicion = rows.get(row);
                medicion.setValorLectura(newValue);
                calibracionModel.updateMedicion(medicion);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ingrese un valor numérico válido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
