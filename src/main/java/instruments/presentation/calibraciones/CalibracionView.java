package instruments.presentation.calibraciones;


import instruments.logic.Instrumento;
import instruments.logic.TipoInstrumento;
import instruments.logic.TipoInstrumentoXMLManager;
import instruments.presentation.instrumentos.InstrumentosModel;
import instruments.presentation.instrumentos.InstrumentosTableModel;
import instruments.presentation.instrumentos.InstrumentosView;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class CalibracionView implements Observer {

    private JPanel calibracionesPanel;
    private JTextField searchDescription;
    private JButton searchCalibraciones;
    private JButton reportCalibraciones;
    private JScrollPane tableModel;
    private JTable list;
    private JButton saveCalibraciones;
    private JButton deleteCalibraciones;
    private JButton clearCalibraciones;
    private JTextField tolerancia;
    private JTextField numero;
    private JTextField fecha;
    private JTextField mediciones;
    private JPanel instrumentoPanel;
    private JPanel medicionesPanel;
    private JTable listMediciones;
    private JLabel panelText;



    public CalibracionView() {
        calibracionesPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                medicionesPanel.setVisible(false);
                calibracionController.setLastSelectedInstrumento();
                if(calibracionController.lastSelectedInstrumento != null){
                    panelText.setText(calibracionController.lastSelectedInstrumento.toString());
                    panelText.setForeground(Color.RED);
                }else {
                    panelText.setText("");
                }

            }
        });
        deleteCalibraciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                medicionesPanel.setVisible(true);
            }
        });

    }

    public JPanel getCalibracionesPanel() {
        return calibracionesPanel;
    }



    CalibracionController calibracionController;
    CalibracionModel calibracionModel;
    @Override
    public void update(Observable updatedModel, Object properties) {
        int changedProps = (int) properties;

        // Verificar si la lista de calibraciones ha cambiado
        if ((changedProps & CalibracionModel.LIST) == CalibracionModel.LIST) {
            int[] cols = {CalibracionTableModel.NUMERO, CalibracionTableModel.FECHA};
            list.setModel(new CalibracionTableModel(cols, calibracionModel.getList()));
            list.setRowHeight(30);
            TableColumnModel columnModel = list.getColumnModel();
            columnModel.getColumn(1).setPreferredWidth(200); // Ajustar la anchura de la columna de descripción
        }

        // Verificar si el instrumento actual ha cambiado
        if ((changedProps & CalibracionModel.CURRENT) == CalibracionModel.CURRENT) {
            int[] colsMediciones = {MedicionTableModel.VALOR_REFERENCIA, MedicionTableModel.VALOR_LECTURA};
            listMediciones.setModel(new MedicionTableModel(colsMediciones, calibracionModel.getCurrent().getMediciones()));
            listMediciones.setRowHeight(30);
            TableColumnModel columnModelMediciones = listMediciones.getColumnModel();
            // Ajustar las anchuras de las columnas según sea necesario
        }

        this.calibracionesPanel.revalidate();
    }


    public void setCalibracionController(CalibracionController calibracionController) {
        this.calibracionController = calibracionController;
    }

    public void setCalibracionModel(CalibracionModel calibracionModel) {
        this.calibracionModel = calibracionModel;
        calibracionModel.addObserver(this);
    }

    public void enableEditing() {
        deleteCalibraciones.setEnabled(true);
        numero.setText(String.valueOf(CalibracionModel.getCurrent().getNumero()));
        fecha.setText(CalibracionModel.getCurrent().getFecha());
        mediciones.setText(String.valueOf(CalibracionModel.getCurrent().getMediciones()));
    }

    public List<TipoInstrumento> tipoInstrumentoList(){
        return TipoInstrumentoXMLManager.cargarTiposInstrumento("files/XMLData/TiposInstrumentos.xml");
    }


}
