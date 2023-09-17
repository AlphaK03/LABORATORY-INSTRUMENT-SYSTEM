package instruments.presentation.calibraciones;


import instruments.logic.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JTable;


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
    private JPanel medicionesButtonPanel;
    private JPanel calibracionesButtonPanel;


    public CalibracionView() {


        calibracionesPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                medicionesPanel.setVisible(false);
                deleteCalibraciones.setEnabled(false);
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
                int row = list.getSelectedRow();
                if (row >= 0) {
                    Calibracion calibracion = calibracionModel.getList().get(row);
                    try {
                        calibracionController.delete(calibracion);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(calibracionesPanel, "No item selected.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
                calibracionController.saveData();
            }
        });

        saveCalibraciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calibracion calibracion = new Calibracion();

                String numeroText = numero.getText();
                String medicionesText = mediciones.getText();
                ButtonUtils.verifyTextFields(numero, mediciones, fecha);
                if (isNumeric(numeroText) || isNumeric(medicionesText)) {
                    if(isNumeric(numeroText)){
                        numero.setBackground(new Color(255, 163, 142));
                    }
                    if(isNumeric(medicionesText)){
                        mediciones.setBackground(new Color(255, 163, 142));
                    }
                    JOptionPane.showMessageDialog(calibracionesPanel, "Coloque únicamente números en los campos de 'Número' y 'Mediciones'.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                calibracion.setFecha(fecha.getText());
                calibracion.setNumero(Integer.parseInt(numeroText));
                calibracion.setInstrumentoCalibrado(calibracionController.lastSelectedInstrumento);
                calibracion.setCantidadMediciones(Integer.parseInt(medicionesText));

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
        clearCalibraciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ButtonUtils.clearFields(numero, mediciones, fecha, searchDescription);
                ButtonUtils.fixColorTextFields(numero, mediciones, fecha);
                deleteCalibraciones.setEnabled(false);
                numero.setEnabled(true);
            }
        });

        //Se hace visible la lista de mediciones que contiene cada Onjeto Calibracion
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                medicionesPanel.setVisible(true);
                deleteCalibraciones.setEnabled(true);
                numero.setEnabled(false);
                enableEditing();
            }
        });

        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                calibracionesButtonPanel.setVisible(true);
                int selectedRow = list.getSelectedRow();
                if (selectedRow >= 0) {
                    Calibracion selectedCalibracion = calibracionModel.getList().get(selectedRow);
                    calibracionModel.setCurrent(selectedCalibracion); // Establece la calibración actual en el modelo

                    // Actualiza la tabla de mediciones
                    int[] colsMediciones = {MedicionTableModel.VALOR_REFERENCIA, MedicionTableModel.VALOR_LECTURA};
                    listMediciones.setModel(new MedicionTableModel(colsMediciones, selectedCalibracion.getMediciones(), calibracionModel));
                    listMediciones.setRowHeight(30);

                    // Notifica a la tabla que los datos han cambiado
                    ((AbstractTableModel) listMediciones.getModel()).fireTableDataChanged();

                    enableEditing();
                    if(selectedCalibracion.getInstrumentoCalibrado() != null){
                        panelText.setText(selectedCalibracion.getInstrumentoCalibrado().toString());
                        panelText.setForeground(Color.RED);
                    }else {
                        panelText.setText("");
                    }
                }
            }
        });



        reportCalibraciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ButtonUtils.fixColorTextFields(numero, fecha, mediciones);

                    List<Calibracion> calibracions = calibracionModel.getList();
                    calibracionController.generatePDFReport(calibracions);
                    JOptionPane.showMessageDialog(calibracionesPanel, "Reporte generado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(calibracionesPanel, "Error al generar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        listMediciones.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedColumn = listMediciones.getSelectedColumn();
                    int selectedRow = listMediciones.getSelectedRow();

                    // Verificar si se ha hecho doble clic en la columna "Valor de Lectura"
                    if (selectedColumn == MedicionTableModel.VALOR_LECTURA) {
                        listMediciones.editCellAt(selectedRow, selectedColumn);
                        Component editorComponent = listMediciones.getEditorComponent();
                        if (editorComponent != null) {
                            editorComponent.requestFocus();
                        }
                    }
                }
                calibracionController.saveData();
            }
        });

        listMediciones.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int col = e.getColumn();
                    listMediciones.getModel().setValueAt(listMediciones.getValueAt(row, col), row, col);

                }

            }
        });

        listMediciones.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int col = e.getColumn();
                    if (col == CalibracionTableModel.NUMERO) {
                        listMediciones.getModel().setValueAt(listMediciones.getValueAt(row, col), row, col);
                    }
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

        if ((changedProps & CalibracionModel.LIST) == CalibracionModel.LIST) {
            int[] cols = {CalibracionTableModel.NUMERO, CalibracionTableModel.FECHA, CalibracionTableModel.MEDICIONES};
            list.setModel(new CalibracionTableModel(cols, calibracionModel.getList()));
            list.setRowHeight(30);
            TableColumnModel columnModel = list.getColumnModel();
            columnModel.getColumn(1).setPreferredWidth(200); // Ajustar la anchura de la columna de descripción
        }

        // Verificar si el instrumento actual ha cambiado
        if ((changedProps & CalibracionModel.CURRENT) == CalibracionModel.CURRENT) {
            int[] colsMediciones = {MedicionTableModel.VALOR_REFERENCIA, MedicionTableModel.VALOR_LECTURA};
            listMediciones.setModel(new MedicionTableModel(colsMediciones, CalibracionModel.getCurrent().getMediciones(), calibracionModel));
            listMediciones.setRowHeight(30);
            TableColumnModel columnModelMediciones = listMediciones.getColumnModel();

        }

        TableCellEditor editor = new DefaultCellEditor(new JTextField());
        listMediciones.getColumnModel().getColumn(MedicionTableModel.VALOR_LECTURA).setCellEditor(editor);
        listMediciones.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);


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
        mediciones.setText(String.valueOf(CalibracionModel.getCurrent().getCantidadMediciones()));

    }

    public List<TipoInstrumento> tipoInstrumentoList(){
        return TipoInstrumentoXMLManager.cargarTiposInstrumento("files/XMLData/TiposInstrumentos.xml");
    }



    // Crear un editor de celdas personalizado para la columna "Valor de Lectura"




}
