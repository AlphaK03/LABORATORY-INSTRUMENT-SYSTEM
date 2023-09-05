package instruments.presentation.tipos;

import instruments.logic.ButtonUtils;
import instruments.logic.TipoInstrumento;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.event.ListSelectionEvent;


public class View implements Observer {
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
    private JPanel panel3;
    private JPanel panel4;
    private JPanel panel2;
    private JComboBox comboBoxTipo;
    private JScrollPane tableModel2;
    private JTable list2;
    private JButton saveInstrumentos;
    private JButton clearInstrumentos;
    private JTextField serie;
    private JTextField descripcion;
    private JTextField tolerancia;
    private JTextField minimo;
    private JTextField maximo;

    public View() {
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    ButtonUtils.fixColorTextFields(codigo,unidad,nombre);
                    TipoInstrumento filter= new TipoInstrumento();
                    filter.setNombre(searchNombre.getText());
                    controller.search(filter);
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

            }
        });

        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = list.getSelectedRow();
                if (selectedRow >= 0) {
                    controller.edit(selectedRow);
                }
            }
        });





        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = list.getSelectedRow();
                if (row >= 0) {
                    TipoInstrumento tipoInstrumento = model.getList().get(row);
                    try {
                        controller.delete(tipoInstrumento);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "No item selected.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
                codigo.setEnabled(true);
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
                        controller.create(tipoInstrumento);
                        model.update(tipoInstrumento);
                    }else {
                        controller.update(tipoInstrumento);
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

                    List<TipoInstrumento> tipos = model.getList();
                    controller.generatePDFReport(tipos);
                    JOptionPane.showMessageDialog(panel, "Reporte generado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error al generar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //VENTANA #2 -------------------------------------------------------------------------------------------------------
        saveInstrumentos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ButtonUtils.verifyTextFields(serie, descripcion, tolerancia, maximo, minimo);
                }catch (Exception ex){
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
    }

    public JPanel getPanel() {
        return panel;
    }

    Controller controller;
    Model model;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addObserver(this);
    }

    @Override
    public void update(Observable updatedModel, Object properties) {
        int changedProps = (int) properties;
        if ((changedProps & Model.LIST) == Model.LIST) {
            int[] cols = {TableModel.CODIGO, TableModel.NOMBRE, TableModel.UNIDAD};
            list.setModel(new TableModel(cols, model.getList()));
            list.setRowHeight(30);
            TableColumnModel columnModel = list.getColumnModel();
            columnModel.getColumn(2).setPreferredWidth(200);
        }

        if ((changedProps & Model.LIST) == Model.LIST) {
            int[] cols = {TableModel2.SERIE, TableModel2.DESCRIPCION, TableModel2.TOLERANCIA};
            list2.setModel(new TableModel2(cols, model.getList()));
            list2.setRowHeight(30);
            TableColumnModel columnModel = list2.getColumnModel();
            columnModel.getColumn(2).setPreferredWidth(200);
        }

        if ((changedProps & Model.CURRENT) == Model.CURRENT) {
            codigo.setText(model.getCurrent().getCodigo());
            nombre.setText(model.getCurrent().getNombre());
            unidad.setText(model.getCurrent().getUnidad());
        }
        this.panel.revalidate();
    }

    public void enableEditing() {
        codigo.setEnabled(false);
        delete.setEnabled(true);
        codigo.setText(model.getCurrent().getCodigo());
        nombre.setText(model.getCurrent().getNombre());
        unidad.setText(model.getCurrent().getUnidad());
    }









}
