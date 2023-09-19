package instruments.logic;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.*;

public class ButtonUtils {
    public static void verifyTextFields(JTextField... fields) {
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

    public static void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    public static void enableButtons(JButton... buttons) {
        for (JButton button : buttons) {
            button.setEnabled(true);
        }
    }

    public static void disableButtons(JButton... buttons) {
        for (JButton button : buttons) {
            button.setEnabled(false);
        }
    }

    public static void fixColorTextFields(JTextField... fields){
        for (JTextField field : fields) {
            field.setBackground(Color.WHITE);
            field.setText("");
        }
    }

    public static void generateReport() {
        // Agrega aquí la lógica para generar un informe (reporte)
        // Por ejemplo, abrir un diálogo de selección de archivo y guardar el informe en un archivo específico.
    }

    public static void isNumeric(JTextField... fields) {
        for (JTextField field : fields) {
            if (field == null || field.getText().isEmpty()) {
                throw new IllegalArgumentException("El campo no puede ser nulo o tener texto vacío.");
            } else if (!isValidNumericField(field.getText())) {
                throw new IllegalArgumentException("El valor '" + field.getText() + "' no es un número válido.");
            }
        }
    }

    private static boolean isValidNumericField(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }


}
