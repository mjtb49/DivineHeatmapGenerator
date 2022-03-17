package panels;

import Conditions.Condition;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Arrays;


public class SettingsPanel extends JPanel implements Panel {

    private JTextField distance;
    private JTextField samples;
    private JCheckBox cross;

    public SettingsPanel(boolean crossRefCheckbox) {
        distance = new JTextField("Blocks");
        distance.setColumns(4);
        Panel.deleteTextOnSelection(distance);

        samples = new JTextField("Trials");
        samples.setColumns(10);
        Panel.deleteTextOnSelection(samples);

        if (crossRefCheckbox)
            this.setLayout(new GridLayout(3,1));
        else
            this.setLayout(new GridLayout(2,1));
        this.setBorder(BorderFactory.createTitledBorder("Settings"));
        this.add(distance);
        this.add(samples);

        if (crossRefCheckbox) {
            cross = new JCheckBox("Cross ref.");
            this.add(cross);
        }
    }

    @Override
    public void reset() {

    }

    public int getBlockThreshold() {
        int d = Panel.parseIntFromTextField(distance);
        if (Panel.parseIntFromTextField(distance) != Panel.FAIL) {
            if (Panel.parseIntFromTextField(distance) <= 2000) {
                return d;
            } else {
                return 2000;
            }
        }
        return 0;
    }

    public int getSampleSize() {
        int d = Panel.parseIntFromTextField(samples);
        if (Panel.parseIntFromTextField(samples) != Panel.FAIL)
            return d;
        return 100000;
    }

    public JTextField getDistanceField() {
        return distance;
    }

    public boolean shouldCrossReference() {
        return cross == null || cross.isSelected();
    }

    @Override //TODO this is stupid
    public Condition getCondition(boolean crossReference) {
        return null;
    }
}
