package panels;

import Conditions.Condition;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class SettingsPanel extends JPanel implements Panel {

    private JTextField distance;
    private JTextField samples;

    public SettingsPanel() {
        distance = new JTextField("Blocks");
        distance.setColumns(10);
        Panel.deleteTextOnSelection(distance);

        samples = new JTextField("Trials");
        samples.setColumns(10);
        Panel.deleteTextOnSelection(samples);

        this.setLayout(new GridLayout(2,1));
        this.setBorder(BorderFactory.createTitledBorder("Settings"));
        this.add(distance);
        this.add(samples);
    }

    @Override
    public void reset() {

    }

    public int getBlockThreshold() {
        int d = Panel.parseIntFromTextField(distance);
        if (Panel.parseIntFromTextField(distance) != Panel.FAIL)
            return d;
        return 0;
    }

    public int getSampleSize() {
        int d = Panel.parseIntFromTextField(samples);
        if (Panel.parseIntFromTextField(samples) != Panel.FAIL)
            return d;
        return 100000;
    }
    @Override //TODO this is stupid
    public Condition getCondition() {
        return null;
    }
}
