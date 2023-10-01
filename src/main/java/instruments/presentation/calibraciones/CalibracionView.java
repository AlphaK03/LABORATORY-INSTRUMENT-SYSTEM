package instruments.presentation.calibraciones;


import instruments.logic.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
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

    Date fechaActual;
    String fechaFormateada = "";

    boolean calibrationSelected = false;


    public CalibracionView() {


        calibracionesPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                deleteCalibraciones.setEnabled(false);
                calibracionController.setLastSelectedInstrumento();
                if (calibracionController.lastSelectedInstrumento != null) {
                    panelText.setText(calibracionController.lastSelectedInstrumento.toString());
                    panelText.setForeground(Color.RED);
                    // Carga las calibraciones del instrumento seleccionado
                    cargarCalibracionesInstrumento(calibracionController.lastSelectedInstrumento);
                } else {
                    panelText.setText("");
                }
                updateNumberAndDate();
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
                try {
                    calibracionController.saveData();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        saveCalibraciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calibracion calibracion = new Calibracion();


                String medicionesText = mediciones.getText();
                ButtonUtils.verifyTextFields(numero, mediciones, fecha);

                if (isNumeric(medicionesText)) {
                    mediciones.setBackground(new Color(255, 163, 142));
                    JOptionPane.showMessageDialog(calibracionesPanel, "Coloque únicamente números en el campo de 'Mediciones'.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                calibracion.setFecha(fechaFormateada);
                calibracion.setNumero(Integer.parseInt(numero.getText()));
                calibracion.setInstrumentoCalibrado(calibracionController.lastSelectedInstrumento);
                calibracion.setCantidadMediciones(Integer.parseInt(medicionesText));

                deleteCalibraciones.setEnabled(false);

                try {
                    if(!calibrationSelected){
                        calibracionController.create(calibracion);

                    }
                    else {
                        calibracionController.update(calibracion);
                        ButtonUtils.clearFields(mediciones);

                    }
                    updateNumberAndDate();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(calibracionesPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }}
        });

        searchCalibraciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    ButtonUtils.fixColorTextFields(mediciones);
                    Calibracion filter= new Calibracion();
                    filter.setNumero(Integer.parseInt(searchDescription.getText()));
                    calibracionController.search(filter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(calibracionesPanel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        clearCalibraciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ButtonUtils.clearFields(mediciones, searchDescription);
                ButtonUtils.fixColorTextFields(mediciones);
                deleteCalibraciones.setEnabled(false);
            }
        });

        //Se hace visible la lista de mediciones que contiene cada Onjeto Calibracion
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = list.getSelectedRow();
                if (selectedRow >= 0) {
                    Calibracion selectedCalibracion = calibracionModel.getList().get(selectedRow);

                    // Mostrar u ocultar el panel de mediciones si la calibración tiene datos
                    boolean hasMediciones = !selectedCalibracion.getMediciones().isEmpty();
                    medicionesPanel.setVisible(hasMediciones);

                    deleteCalibraciones.setEnabled(true);
                    numero.setEnabled(false);
                    calibracionesButtonPanel.setVisible(true);
                    calibracionModel.setCurrent(selectedCalibracion); // Establece la calibración actual en el modelo

                    // Actualiza la tabla de mediciones
                    int[] colsMediciones = {MedicionTableModel.VALOR_REFERENCIA, MedicionTableModel.VALOR_LECTURA};
                    listMediciones.setModel(new
                            MedicionTableModel(colsMediciones, selectedCalibracion.getMediciones(), calibracionModel));
                    listMediciones.setRowHeight(30);

                    // Notifica a la tabla que los datos han cambiado
                    ((AbstractTableModel) listMediciones.getModel()).fireTableDataChanged();

                    enableEditing();
                    calibrationSelected = true;
                    if (selectedCalibracion.getInstrumentoCalibrado() != null) {
                        panelText.setText(selectedCalibracion.getInstrumentoCalibrado().toString());
                        panelText.setForeground(Color.RED);
                    } else {
                        panelText.setText("");
                    }
                } else {
                    calibrationSelected = false;
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
                try {
                    calibracionController.saveData();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
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
            // Obtén el instrumento seleccionado
            Instrumento selectedInstrumento = calibracionController.lastSelectedInstrumento;

            if (selectedInstrumento != null) {
                // Filtra las calibraciones que pertenecen al instrumento seleccionado
                List<Calibracion> calibracionesInstrumento = calibracionModel.getList().stream()
                        .filter(calibracion -> calibracion.getInstrumentoCalibrado() == selectedInstrumento)
                        .collect(Collectors.toList());

                int[] cols = {CalibracionTableModel.NUMERO, CalibracionTableModel.FECHA, CalibracionTableModel.MEDICIONES};
                list.setModel(new CalibracionTableModel(cols, calibracionesInstrumento));
                list.setRowHeight(30);
                TableColumnModel columnModel = list.getColumnModel();
                columnModel.getColumn(1).setPreferredWidth(200); // Ajustar la anchura de la columna de descripción
            } else {
                // Si no hay instrumento seleccionado, borra la lista de calibraciones
                list.setModel(new DefaultTableModel());
            }
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
        mediciones.setText(String.valueOf(CalibracionModel.getCurrent().getCantidadMediciones()));

    }

    private int generateUniqueCalibrationNumber() {
        Random random = new Random();
        return random.nextInt(1000000) + 1;
    }
    private SimpleDateFormat dateFormat(){
        return new  SimpleDateFormat("dd MMM, yyyy | HH:mm a");
    }

    private void cargarCalibracionesInstrumento(Instrumento instrumento) {
        if (instrumento != null) {
            List<Calibracion> calibracionesInstrumento = calibracionModel.getList().stream()
                    .filter(calibracion -> calibracion.getInstrumentoCalibrado() == instrumento)
                    .collect(Collectors.toList());

            int[] cols = {CalibracionTableModel.NUMERO, CalibracionTableModel.FECHA, CalibracionTableModel.MEDICIONES};
            list.setModel(new CalibracionTableModel(cols, calibracionesInstrumento));
            list.setRowHeight(30);
            TableColumnModel columnModel = list.getColumnModel();
            columnModel.getColumn(1).setPreferredWidth(200);
        }
    }


//    public List<TipoInstrumento> tipoInstrumentoList(){
//        return XmlPersister.cargarTiposInstrumento("files/XMLData/TiposInstrumentos.xml");
//    }

    void updateNumberAndDate(){
        int newCalibrationNumber = generateUniqueCalibrationNumber();
        numero.setText(String.valueOf(newCalibrationNumber));
        numero.setEnabled(false);
        fecha.setEnabled(false);
        SimpleDateFormat formato = dateFormat();
        fechaActual = new Date();
        fechaFormateada = formato.format(fechaActual);
        fecha.setText(fechaFormateada);
        calibracionModel.commit();
    }

}
