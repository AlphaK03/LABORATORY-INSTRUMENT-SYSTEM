package instruments.presentation.tipos;

import instruments.logic.TipoInstrumento;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableModel2 extends AbstractTableModel implements javax.swing.table.TableModel {
    List<TipoInstrumento> rows;
    int[] cols;

    public TableModel2(int[] cols, List<TipoInstrumento> rows){
        this.cols=cols;
        this.rows=rows;
        initColNames();
    }

    public int getColumnCount() {
        return cols.length;
    }

    public String getColumnName(int col){
        return colNames[cols[col]];
    }

    public Class<?> getColumnClass(int col){
        switch (cols[col]){
            default: return super.getColumnClass(col);
        }
    }

    public int getRowCount() {
        return rows.size();
    }

    public Object getValueAt(int row, int col) {
        TipoInstrumento sucursal = rows.get(row);
        switch (cols[col]){
            case SERIE: return sucursal.getCodigo();
          case DESCRIPCION: return sucursal.getNombre();
            case TOLERANCIA: return sucursal.getUnidad();
            default: return "";
        }
    }

    public TipoInstrumento getRowAt(int row) {
        return rows.get(row);
    }

    public static final int SERIE =0;
    public static final int DESCRIPCION =1;
    public static final int TOLERANCIA =2;

    String[] colNames = new String[6];
    private void initColNames(){
        colNames[SERIE]= "Serie";
        colNames[DESCRIPCION]= "Descripción";
        colNames[TOLERANCIA]= "Tolerancia";
    }

}
