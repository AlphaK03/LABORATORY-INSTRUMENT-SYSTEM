package instruments;

import instruments.presentation.acercaDe.AcercaDe;
import instruments.presentation.calibraciones.CalibracionController;
import instruments.presentation.calibraciones.CalibracionModel;
import instruments.presentation.calibraciones.CalibracionView;
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

        // Panel para "Tipos de Instrumento"
        TiposModel tiposModel = new TiposModel();
        TiposView tiposView = new TiposView();
        TiposController tiposController = new TiposController(tiposView, tiposModel);
        tabbedPane.addTab("Tipos de Instrumento", tiposView.getPanel());

        // Panel para "Instrumentos"
        InstrumentosModel instrumentosModel = new InstrumentosModel();
        InstrumentosView instrumentosView = new InstrumentosView();
        InstrumentosController instrumentosController = new InstrumentosController(instrumentosView, instrumentosModel);


        // Panel para "Calibraciones"
        CalibracionModel calibracionModel = new CalibracionModel();
        CalibracionView calibracionView = new CalibracionView();
        CalibracionController calibracionController = new CalibracionController(calibracionView, calibracionModel);

        AcercaDe acercaDe = new AcercaDe();


        tabbedPane.addTab("Instrumentos", instrumentosView.getPanel());
        tabbedPane.addTab("Calibraciones", calibracionView.getCalibracionesPanel());
        tabbedPane.addTab("Acerca de", acercaDe.getAcercaDePanel());

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
                try {
                    tiposController.saveData();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    instrumentosController.saveData();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                // calibracionController.saveData();
            }
        });
    }



    public static TiposController tiposController;

    public static JFrame window;
}
