package instruments;

import instruments.presentation.instrumentos.InstrumentosController;
import instruments.presentation.instrumentos.InstrumentosModel;
import instruments.presentation.instrumentos.InstrumentosView;
import instruments.presentation.tipos.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class Application {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");}
        catch (Exception ignored) {};

        JFrame window = new JFrame();
        JTabbedPane tabbedPane = new JTabbedPane();

        InstrumentosModel instrumentosModel = new InstrumentosModel();
        InstrumentosView instrumentosView = new InstrumentosView();
        InstrumentosController instrumentosController = new InstrumentosController(instrumentosView, instrumentosModel);

        // Panel para "Tipos de Instrumento"
        TiposModel tiposModel = new TiposModel();
        TiposView tiposView = new TiposView();
        TiposController tiposController = new TiposController(tiposView, tiposModel);
        tabbedPane.addTab("Tipos de Instrumento", tiposView.getPanel());

        // Panel para "Instrumentos"
        JPanel calibracionesPanel = new JPanel();
        JPanel acercaDePanel = new JPanel();



        tabbedPane.addTab("Instrumentos", instrumentosView.getPanel());
        tabbedPane.addTab("Calibraciones", calibracionesPanel);
        tabbedPane.addTab("Acerca de", acercaDePanel);

        window.getContentPane().add(tabbedPane);

        window.setSize(900,450);
        window.setResizable(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(Application.class.getResource("presentation/icons/icon.png")));
        window.setIconImage(icon.getImage());
        window.setTitle("SILAB: Sistema de Laboratorio Industrial");
        window.setVisible(true);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                tiposController.saveData();
                instrumentosController.saveData();
            }
        });
    }



    public static TiposController tiposController;

    public static JFrame window;
}
