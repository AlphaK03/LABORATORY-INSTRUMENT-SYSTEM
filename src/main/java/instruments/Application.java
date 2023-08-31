package instruments;

import javax.swing.*;
import java.util.Objects;

public class Application {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");}
        catch (Exception ex) {};

        window = new JFrame();
        window.setContentPane(new JTabbedPane());

        instruments.presentation.tipos.Model tiposModel= new instruments.presentation.tipos.Model();
        instruments.presentation.tipos.View tiposView = new instruments.presentation.tipos.View();
        tiposController = new instruments.presentation.tipos.Controller(tiposView,tiposModel);

        window.getContentPane().add("Tipos de Instrumento",tiposView.getPanel());

        window.setSize(900,450);
        window.setResizable(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(Application.class.getResource("presentation/icons/icon.png")));
        window.setIconImage(icon.getImage());
        window.setTitle("SILAB: Sistema de Laboratorio Industrial");
        window.setVisible(true);
    }

    public static instruments.presentation.tipos.Controller tiposController;

    public static JFrame window;
}
