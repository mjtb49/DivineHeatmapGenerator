package panels;

import Conditions.Condition;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public interface Panel {
    void reset();
    Condition getCondition(boolean crossReference);
    static final int FAIL = Integer.MIN_VALUE;

    static void deleteTextOnSelection(JTextField jTextField) {
        jTextField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                JTextField source = (JTextField)e.getComponent();
                source.setText("");
                source.removeFocusListener(this);
            }
        });
    }

    static int parseIntFromTextField(JTextField textField, int lowerBound, int upperBound) {
        try {
            int i = Integer.parseInt(textField.getText());
            if (lowerBound <= i && i < upperBound)
                return i;
        } catch (NumberFormatException ignored) {}
        return FAIL;
    }

    static int parseIntFromTextField(JTextField textField) {
        try {
            return Integer.parseInt(textField.getText());
        }  catch (NumberFormatException ignored) {}
        return FAIL;
    }

    static float parseFloatFromTextField(JTextField textField) {
        try {
            return Float.parseFloat(textField.getText());
        }  catch (NumberFormatException ignored) {}
        return Float.NaN;
    }
}
