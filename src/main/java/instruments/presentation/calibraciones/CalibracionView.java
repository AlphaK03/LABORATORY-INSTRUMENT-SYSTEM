package instruments.presentation.calibraciones;


import instruments.logic.*;

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

        saveCalibraciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calibracion calibracion = new Calibracion();

                String numeroText = numero.getText();
                String medicionesText = mediciones.getText();

                if (isNumeric(numeroText) || isNumeric(medicionesText)) {
                    JOptionPane.showMessageDialog(calibracionesPanel, "Coloque únicamente números en los campos de 'Número' y 'Mediciones'.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                calibracion.setFecha(fecha.getText());
                calibracion.setNumero(Integer.parseInt(numeroText));
                calibracion.setCantidadMediciones(Integer.parseInt(medicionesText));
                calibracion.setInstrumentoCalibrado(calibracionController.lastSelectedInstrumento);

                deleteCalibraciones.setEnabled(false);

                try {
                    if (numero.isEnabled()) {
                        calibracionController.create(calibracion);
                        calibracionController.update(calibracion);
                    } else {
                        calibracionController.update(calibracion);
                    }
                    numero.setEnabled(true);
                    ButtonUtils.clearFields(numero, fecha, mediciones);
                    numero.setText("");
                    fecha.setText("");
                    mediciones.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(calibracionesPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        searchCalibraciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    ButtonUtils.fixColorTextFields(numero, fecha, mediciones);
                    Calibracion filter= new Calibracion();
                    filter.setNumero(Integer.parseInt(searchCalibraciones.getText()));
                    calibracionController.search(filter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(calibracionesPanel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        return !str.chars().allMatch(Character::isDigit);
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
