package instruments.presentation.instrumentos;


import instruments.logic.ButtonUtils;
import instruments.logic.Instrumento;
import instruments.logic.TipoInstrumento;
import instruments.logic.TipoInstrumentoXMLManager;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class InstrumentosView implements Observer {
    private JPanel panel;

    private JScrollPane tableModel;
    private JTable list;
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
    private JPanel panelInstrumento;


    public InstrumentosView() {

        List<TipoInstrumento> tiposInstrumento = tipoInstrumentoList();

        saveInstrumentos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Instrumento instrumento = new Instrumento();

                instrumento.setSerie(serie.getText());
                instrumento.setDescripcion(descripcion.getText());
                instrumento.setTolerancia(Double.parseDouble(tolerancia.getText()));
                instrumento.setMaximo(Double.parseDouble(maximo.getText()));
                instrumento.setMinimo(Double.parseDouble(minimo.getText()));

                // Asignar el TipoInstrumento asociado a este instrumento
                TipoInstrumento tipoInstrumentoSeleccionado = null;
                for (TipoInstrumento tipo : tiposInstrumento) {
                    if (tipo.getCodigo().equals(Objects.requireNonNull(comboBoxTipo.getSelectedItem()).toString())) {
                        tipoInstrumentoSeleccionado = tipo;
                        break;
                    }
                }
                instrumento.setTipoInstrumento(tipoInstrumentoSeleccionado);

                try {
                    ButtonUtils.verifyTextFields(serie, descripcion, tolerancia, maximo, minimo);

                    if (serie.isEnabled()) {
                        instrumentosController.create(instrumento);
                        instrumentosModel.update(instrumento);
                    } else {
                        instrumentosController.update(instrumento);
                    }
                    serie.setEnabled(true);
                    deleteInstruments.setEnabled(false);
                    ButtonUtils.clearFields(serie, descripcion, tolerancia, maximo, minimo);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        clearInstrumentos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ButtonUtils.clearFields(serie, descripcion, tolerancia, maximo, minimo);
                ButtonUtils.fixColorTextFields(serie, descripcion, tolerancia, maximo, minimo);
            }
        });


        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                comboBoxTipo.removeAllItems();
                List<TipoInstrumento> tiposInstrumento = tipoInstrumentoList();

                for (TipoInstrumento tipo : tiposInstrumento) {
                    comboBoxTipo.addItem(tipo.getCodigo());
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


    public List<TipoInstrumento> tipoInstrumentoList(){
        return TipoInstrumentoXMLManager.cargarTiposInstrumento("files/TiposInstrumentos.xml");
    }


}
