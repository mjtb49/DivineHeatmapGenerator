package panels;

import Conditions.ChanceDecoratorCondition;
import Conditions.Condition;
import Conditions.DecoratorCondition;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

//TODO support count decorators
public class DecoratorPanel extends JPanel implements Panel {

    private final int salt;
    private float chance;
    private JCheckBox button = null;
    private final ArrayList<JTextField> measurements = new ArrayList<>();
    private final ArrayList<JTextField> precisions = new ArrayList<>();
    private final ArrayList<String> measurementNames = new ArrayList<>();

    public DecoratorPanel(String name, int salt) {
        this.setLayout(new GridLayout(3,1));
        this.setBorder(BorderFactory.createTitledBorder(name));
        this.salt = salt;
    }

    public DecoratorPanel chance(float f) {
        this.chance = f;
        this.button = new JCheckBox("Exists");
        this.add(button);
        return this;
    }

    public DecoratorPanel call(String name) {
        return call(name, false);
    }

    public DecoratorPanel call(String name, int precision) {
        return call(name, precision, false);
    }

    public DecoratorPanel call(String name, boolean greyOut) {
        JTextField call = new JTextField(name);
        JTextField precision = new JTextField();

        call.setColumns(4);
        precision.setColumns(4);

        measurements.add(call);
        precisions.add(precision);
        measurementNames.add(name);

        if (greyOut) {
            call.setBackground(Color.GRAY);
            precision.setBackground(Color.GRAY);
        }

        Panel.deleteTextOnSelection(call);
        Panel.deleteTextOnSelection(precision);

        JPanel panel = new JPanel(new GridLayout(1,2));
        panel.add(call);
        panel.add(precision);
        this.add(panel);

        return this;
    }

    public DecoratorPanel call(String name, int numChoices, boolean greyOut) {

        JTextField call = new JTextField(name);
        JTextField precision = new JTextField("" + numChoices);
        precision.setEnabled(false);

        call.setColumns(4);
        precision.setColumns(4);

        measurements.add(call);
        precisions.add(precision);
        measurementNames.add(name);

        if (greyOut) {
            call.setBackground(Color.GRAY);
            precision.setBackground(Color.GRAY);
        }

        Panel.deleteTextOnSelection(call);
        Panel.deleteTextOnSelection(precision);

        JPanel panel = new JPanel(new GridLayout(1,2));
        panel.add(call);
        panel.add(precision);
        this.add(panel);

        return this;
    }

    @Override
    public void reset() {
        if (button != null)
            button.setSelected(false);
        for (int i = 0; i < measurements.size(); i++) {
            measurements.get(i).setText(measurementNames.get(i));
            Panel.deleteTextOnSelection(measurements.get(i));
        }
    }

    @Override
    public Condition getCondition() {
        //If its a chance decorator, return null if it doesn't exist
        if (button != null && !button.isSelected())
            return null;

        ArrayList<Long> lbs = new ArrayList<>();
        ArrayList<Long> ubs = new ArrayList<>();

        boolean nonTrivial = false;

        for (int i = 0; i < measurements.size(); i++) {
            int precision = Panel.parseIntFromTextField(precisions.get(i));
            int measurement = Panel.parseIntFromTextField(measurements.get(i), 0, precision);
            if (precision != Panel.FAIL && measurement != Panel.FAIL) {
                    lbs.add(measurement * (1L << 48) / precision);
                    ubs.add((measurement + 1) * (1L << 48) / precision);
                    nonTrivial = true;
            } else {
                lbs.add(0L);
                ubs.add(1L << 48);
            }
        }

        if (button != null) {
            if (nonTrivial)
                return new ChanceDecoratorCondition(salt, chance, lbs, ubs);
            else return new ChanceDecoratorCondition(salt, chance);
        } else if (nonTrivial) {
            return new DecoratorCondition(salt, lbs, ubs);
        } else {
            return null;
        }
    }
}
