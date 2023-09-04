package instruments;

import instruments.logic.Service;
import instruments.presentation.tipos.View;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class Application {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");}
        catch (Exception ignored) {};

        window = new JFrame();

        instruments.presentation.tipos.Model tiposModel= new instruments.presentation.tipos.Model();
        View tiposView = new View();
        tiposController = new instruments.presentation.tipos.Controller(tiposView,tiposModel);

        window.getContentPane().add(tiposView.getTabbedPane1());

        window.setSize(900,450);
        window.setResizable(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(Application.class.getResource("presentation/icons/icon.png")));
        window.setIconImage(icon.getImage());
        window.setTitle("SILAB: Sistema de Laboratorio Industrial");
        window.setVisible(true);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                tiposController.guardarDatos("files/TiposInstrumentos.xml"); // Guardar datos antes de cerrar
            }
        });
    }



    public static instruments.presentation.tipos.Controller tiposController;

    public static JFrame window;
}
