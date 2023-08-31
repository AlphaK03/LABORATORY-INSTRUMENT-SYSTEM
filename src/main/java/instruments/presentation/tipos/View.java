package instruments.presentation.tipos;

import instruments.logic.Service;
import instruments.logic.TipoInstrumento;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
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
    private JLabel searchNombreLbl;
    private JButton report;
    private JTextField codigo;
    private JTextField nombre;
    private JTextField unidad;
    private JLabel codigoLbl;
    private JLabel nombreLbl;
    private JLabel unidadLbl;
    private JButton clear;
    private JScrollPane tableModel;

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
        delete.setEnabled(false); // Inhabilita el botón al inicio

        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRowCount = list.getSelectedRowCount();
                delete.setEnabled(selectedRowCount > 0); // Habilita o inhabilita según la selección
            }
        });




        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = list.getSelectedRow();
                if (row >= 0) {
                    TipoInstrumento tipoInstrumento = model.current;
                    try {
                        Service.instance().delete(tipoInstrumento);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "No item selected.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TipoInstrumento tipoInstrumento = new TipoInstrumento();
                tipoInstrumento.setCodigo(codigo.getText());
                tipoInstrumento.setNombre(nombre.getText());
                tipoInstrumento.setUnidad(unidad.getText());

                Service service = Service.instance();
                try {
                    service.create(tipoInstrumento);

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
}
