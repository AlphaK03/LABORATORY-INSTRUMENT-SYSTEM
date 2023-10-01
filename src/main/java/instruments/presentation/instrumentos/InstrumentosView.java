package instruments.presentation.instrumentos;


import instruments.logic.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class InstrumentosView implements Observer {
    private JPanel panel;

    private JScrollPane tableModel;
    private JTable list;

    public JTable getList() {
        return list;
    }

    private JButton saveInstrumentos;
    private JButton clearInstrumentos;
    private JTextField serie;
    private JTextField tolerancia;
    private JTextField minimo;
    private JTextField descripcion;
    private JTextField maximo;
    private JComboBox<String> comboBoxTipo;
    private JButton searchInstrumentos;
    private JButton reportInstruments;
    private JButton deleteInstruments;
    private JTextField searchDescription;
    private JPanel panelInstrumento;


    public InstrumentosView() {

        deleteInstruments.setEnabled(false);

        saveInstrumentos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Instrumento instrumento = new Instrumento();
                ButtonUtils.verifyTextFields(serie, descripcion, tolerancia, maximo, minimo);

                instrumento.setSerie(serie.getText());
                instrumento.setDescripcion(descripcion.getText());

                instrumento.setTolerancia(Double.parseDouble(tolerancia.getText()));
                instrumento.setMaximo(Double.parseDouble(maximo.getText()));
                instrumento.setMinimo(Double.parseDouble(minimo.getText()));



                List<TipoInstrumento> tiposInstrumento = Service.instance().getData().getTipos();
                // Asignar el TipoInstrumento asociado a este instrumento
                TipoInstrumento tipoInstrumentoSeleccionado = null;
                for (TipoInstrumento tipo : tiposInstrumento) {
                    if (tipo.getNombre().equals(Objects.requireNonNull(comboBoxTipo.getSelectedItem()).toString())) {
                        tipoInstrumentoSeleccionado = tipo;
                        break;
                    }
                }
                instrumento.setTipoInstrumento(tipoInstrumentoSeleccionado);
                deleteInstruments.setEnabled(false);

                try {
                    ButtonUtils.isNumeric(serie, tolerancia, minimo, maximo);
                    ButtonUtils.verifyTextFields(serie, descripcion, tolerancia, maximo, minimo);

                    if (serie.isEnabled()) {
                        instrumentosController.create(instrumento);
                        instrumentosModel.update(instrumento);
                    } else {
                        instrumentosController.update(instrumento);
                    }
                    serie.setEnabled(true);
                    ButtonUtils.clearFields(serie, descripcion, tolerancia, maximo, minimo);
                    instrumentosController.onSelectInstrumento();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRowCount = list.getSelectedRowCount();
                deleteInstruments.setEnabled(selectedRowCount > 0);
                ButtonUtils.fixColorTextFields(serie, descripcion, tolerancia, maximo, minimo);
                instrumentosController.onSelectInstrumento();
                serie.setEnabled(false);
                enableEditing();
            }
        });

        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = list.getSelectedRow();
                if (selectedRow >= 0) {
                    instrumentosController.edit(selectedRow);
                    instrumentosController.onSelectInstrumento();
                    comboBoxTipo.setSelectedItem(instrumentosController.getSelectedInstrumento().getTipoInstrumento().getNombre());

                }
            }
        });


        clearInstrumentos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ButtonUtils.clearFields(serie, descripcion, searchDescription, tolerancia);
                ButtonUtils.fixColorTextFields(serie, descripcion);
                maximo.setText("0.0");
                minimo.setText("0.0");
                tolerancia.setText("0.0");
                deleteInstruments.setEnabled(false);
                serie.setEnabled(true);
            }
        });


        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                comboBoxTipo.removeAllItems();
                List<TipoInstrumento> tiposInstrumento = Service.instance().getData().getTipos();

                for (TipoInstrumento tipo : tiposInstrumento) {
                    comboBoxTipo.addItem(tipo.getNombre());
                }

            }
        });
        reportInstruments.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ButtonUtils.fixColorTextFields(serie,descripcion,tolerancia,maximo,minimo);

                    List<Instrumento> instrumentos = instrumentosModel.getList();
                    instrumentosController.generatePDFReport(instrumentos);
                    JOptionPane.showMessageDialog(panel, "Reporte generado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error al generar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        searchInstrumentos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    ButtonUtils.fixColorTextFields(serie,descripcion,tolerancia,maximo,minimo);
                    Instrumento filter= new Instrumento();
                    filter.setDescripcion(searchDescription.getText());
                    instrumentosController.search(filter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        deleteInstruments.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = list.getSelectedRow();
                if (row >= 0) {
                    Instrumento instrumento = instrumentosModel.getList().get(row);
                    try {
                        if(instrumento.getCalibracionList().isEmpty()){
                            instrumentosController.delete(instrumento);
                        }
                        else {
                            JOptionPane.showMessageDialog(panel, "El instrumento seleccionado contiene calibraciones.", "No Eliminado", JOptionPane.WARNING_MESSAGE);
                        }

                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "No item selected.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
                try {
                    instrumentosController.saveData();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                instrumentosController.onSelectInstrumento();
                serie.setEnabled(true);
            }
        });
    }




    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void update(Observable updatedModel, Object properties) {
        int changedProps = (int) properties;

        // Verificar si la lista de instrumentos ha cambiado
        if ((changedProps & InstrumentosModel.LIST) == InstrumentosModel.LIST) {
            int[] cols = {InstrumentosTableModel.SERIE, InstrumentosTableModel.DESCRIPCION, InstrumentosTableModel.TOLERANCIA, InstrumentosTableModel.MAXIMO, InstrumentosTableModel.MINIMO};
            list.setModel(new InstrumentosTableModel(cols, instrumentosModel.getList()));
            list.setRowHeight(30);
            TableColumnModel columnModel = list.getColumnModel();
            columnModel.getColumn(1).setPreferredWidth(200); // Ajustar la anchura de la columna de descripción
        }

        // Verificar si el instrumento actual ha cambiado
        if ((changedProps & InstrumentosModel.CURRENT) == InstrumentosModel.CURRENT) {
            serie.setText(instrumentosModel.getCurrent().getSerie());
            descripcion.setText(instrumentosModel.getCurrent().getDescripcion());
            tolerancia.setText(instrumentosModel.getCurrent().getToleranciaAsString());
            maximo.setText(String.valueOf(instrumentosModel.getCurrent().getMaximo()));
            minimo.setText(String.valueOf(instrumentosModel.getCurrent().getMinimo()));


        }

        this.panel.revalidate();
    }



    public void enableEditing() {
        deleteInstruments.setEnabled(true);
        serie.setText(instrumentosModel.getCurrent().getSerie());
        descripcion.setText(instrumentosModel.getCurrent().getDescripcion());
        tolerancia.setText(instrumentosModel.getCurrent().getToleranciaAsString());
        minimo.setText(String.valueOf(instrumentosModel.getCurrent().getMinimo()));
        maximo.setText(String.valueOf(instrumentosModel.getCurrent().getMaximo()));
    }

    public void cleaningTextFields(){
        fixColorTextFields();
        serie.setText("");
        descripcion.setText("");
        tolerancia.setText("");
        searchInstrumentos.setText("");
        deleteInstruments.setEnabled(false);
    }

    public void verifyTextFields() {
        JTextField[] fields = {serie, descripcion, tolerancia};
        boolean camposVacios = false;

        for (JTextField field : fields) {
            if (field.getText().isBlank()) {
                field.setBackground(new Color(255, 163, 142));
                camposVacios = true;
            } else {
                field.setBackground(Color.WHITE);
            }
        }

        if (camposVacios) {
            throw new RuntimeException("Los campos en rojo son obligatorios.");
        }
    }

    public void fixColorTextFields(){
        JTextField[] fields = {serie, descripcion, tolerancia};
        for (JTextField field : fields) {
            field.setBackground(Color.WHITE);
        }
    }

    InstrumentosController instrumentosController;
    InstrumentosModel instrumentosModel;

    public void setInstrumentosController(InstrumentosController instrumentosController) {
        this.instrumentosController = instrumentosController;
    }

    public void setInstrumentosModel(InstrumentosModel instrumentosModel) {
        this.instrumentosModel = instrumentosModel;
        instrumentosModel.addObserver(this);
    }


//    public List<TipoInstrumento> tipoInstrumentoList(){
//        return XmlPersister.cargarTiposInstrumento("files/XMLData/TiposInstrumentos.xml");
//    }




}

