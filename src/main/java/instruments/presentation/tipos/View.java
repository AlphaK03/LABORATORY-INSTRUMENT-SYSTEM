package instruments.presentation.tipos;

import instruments.logic.Service;
import instruments.logic.TipoInstrumento;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.event.ListSelectionEvent;


public class View implements Observer {
    private JPanel panel;
    private JTextField searchNombre;
    private JButton search;
    private JButton save;
    private JTable list;
    private JButton delete;
    private JTextField codigo;
    private JTextField nombre;
    private JTextField unidad;
    private JButton clear;

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
    private JComboBox comboBox1;
    private JScrollPane tableModel2;
    private JTable list2;

    public View() {
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
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
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Highlighter highlighter = new Highlighter(Color.RED);
                TipoInstrumento tipoInstrumento = new TipoInstrumento();
                tipoInstrumento.setCodigo(codigo.getText());
                tipoInstrumento.setNombre(nombre.getText());
                tipoInstrumento.setUnidad(unidad.getText());

                try {
                    if(codigo.isEnabled() && !codigo.getText().isBlank()){
                        controller.create(tipoInstrumento);
                        model.update(tipoInstrumento);

                    }else {
                        controller.update(tipoInstrumento);
                    }
                    codigo.setEnabled(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });




        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    codigo.setText("");
                    nombre.setText("");
                    unidad.setText("");
                    searchNombre.setText("");
                    delete.setEnabled(false);
                    codigo.setEnabled(true);
            }
        });
        report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<TipoInstrumento> tipos = model.getList();
                    controller.generatePDFReport(tipos);
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
