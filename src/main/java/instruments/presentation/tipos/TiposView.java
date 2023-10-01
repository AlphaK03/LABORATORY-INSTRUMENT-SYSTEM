package instruments.presentation.tipos;

import instruments.logic.ButtonUtils;
import instruments.logic.Instrumento;
import instruments.logic.TipoInstrumento;
import instruments.presentation.instrumentos.InstrumentosView;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.event.ListSelectionEvent;


public class TiposView implements Observer {
    private JPanel panel;
    private JTextField searchNombre;
    private JButton search;
    private JButton saveTiposInstrumentos;
    private JTable list;
    private JButton delete;
    private JTextField codigo;
    private JTextField nombre;
    private JTextField unidad;
    private JButton clearTiposInstrumentos;

    public JTabbedPane getTabbedPane1() {
        return tabbedPane1;
    }

    private JTabbedPane tabbedPane1;
    private JLabel searchNombreLbl;
    private JButton report;
    private JScrollPane tableModel;
    private JLabel codigoLbl;
    private JLabel nombreLbl;
    private JLabel unidadLbl;


    public TiposView() {
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    ButtonUtils.fixColorTextFields(codigo,unidad,nombre);
                    TipoInstrumento filter= new TipoInstrumento();
                    filter.setNombre(searchNombre.getText());
                    tiposController.search(filter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        delete.setEnabled(false);

        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRowCount = list.getSelectedRowCount();
                delete.setEnabled(selectedRowCount > 0);
                ButtonUtils.fixColorTextFields(codigo,unidad,nombre);
                enableEditing();
            }
        });

        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = list.getSelectedRow();
                if (selectedRow >= 0) {
                    tiposController.edit(selectedRow);
                }
            }
        });





        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = list.getSelectedRow();
                if (row >= 0) {
                    TipoInstrumento tipoInstrumento = tiposModel.getList().get(row);
                    try {
                        tiposController.delete(tipoInstrumento);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "No item selected.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
                codigo.setEnabled(true);
                try {
                    tiposController.saveData();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        saveTiposInstrumentos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TipoInstrumento tipoInstrumento = new TipoInstrumento();


                tipoInstrumento.setCodigo(codigo.getText());
                tipoInstrumento.setNombre(nombre.getText());
                tipoInstrumento.setUnidad(unidad.getText());

                try {
                    ButtonUtils.verifyTextFields(codigo, unidad, nombre);

                    if(codigo.isEnabled()){
                        tiposController.create(tipoInstrumento);
                        tiposModel.update(tipoInstrumento);
                    }else {
                        tiposController.update(tipoInstrumento);
                    }
                    codigo.setEnabled(true);
                    delete.setEnabled(false);
                    ButtonUtils.clearFields(codigo, unidad, nombre, searchNombre);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        clearTiposInstrumentos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ButtonUtils.clearFields(codigo, unidad, nombre, searchNombre);
                ButtonUtils.fixColorTextFields(codigo, unidad, nombre, searchNombre);
                codigo.setEnabled(true);
            }
        });
        report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ButtonUtils.fixColorTextFields(codigo,unidad,nombre);

                    List<TipoInstrumento> tipos = tiposModel.getList();
                    tiposController.generatePDFReport(tipos);
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

    TiposController tiposController;
    TiposModel tiposModel;

    public void setController(TiposController tiposController) {
        this.tiposController = tiposController;
    }

    public void setModel(TiposModel tiposModel) {
        this.tiposModel = tiposModel;
        tiposModel.addObserver(this);
    }

    @Override
    public void update(Observable updatedModel, Object properties) {
        int changedProps = (int) properties;
        if ((changedProps & TiposModel.LIST) == TiposModel.LIST) {
            int[] cols = {TableModel.CODIGO, TableModel.NOMBRE, TableModel.UNIDAD};
            list.setModel(new TableModel(cols, tiposModel.getList()));
            list.setRowHeight(30);
            TableColumnModel columnModel = list.getColumnModel();
            columnModel.getColumn(2).setPreferredWidth(200);
        }

        if ((changedProps & TiposModel.CURRENT) == TiposModel.CURRENT) {
            codigo.setText(tiposModel.getCurrent().getCodigo());
            nombre.setText(tiposModel.getCurrent().getNombre());
            unidad.setText(tiposModel.getCurrent().getUnidad());
        }
        this.panel.revalidate();
    }

    public void enableEditing() {
        codigo.setEnabled(false);
        delete.setEnabled(true);
        codigo.setText(tiposModel.getCurrent().getCodigo());
        nombre.setText(tiposModel.getCurrent().getNombre());
        unidad.setText(tiposModel.getCurrent().getUnidad());
    }









}
