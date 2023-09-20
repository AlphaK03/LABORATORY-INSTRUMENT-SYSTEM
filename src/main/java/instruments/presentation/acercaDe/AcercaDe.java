package instruments.presentation.acercaDe;

import javax.swing.*;

public class AcercaDe {
    private JPanel acercaDePanel;
    private JPanel panel;
    private JTextArea textArea1;
    private JTextField textField1;
    private JTextField KEYLORJOSUÉCORTÉSCASCANTETextField;
    private JTextField PABLOROJASVILLALOBOSTextField;
    private JTextField ANDRELRAMIREZSOLISTextField;

    public AcercaDe() {
        textField1.setText(" JOSE SANCHEZ SALAZAR");
        textField1.setEnabled(false);
        KEYLORJOSUÉCORTÉSCASCANTETextField.setEnabled(false);
        PABLOROJASVILLALOBOSTextField.setEnabled(false);
        ANDRELRAMIREZSOLISTextField.setEnabled(false);
    }

    public JPanel getAcercaDePanel() {
        return acercaDePanel;
    }

    public JPanel getPanel() {
        return panel;
    }
}
