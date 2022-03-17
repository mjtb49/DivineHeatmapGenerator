package panels;

import Conditions.Condition;
import Conditions.DecoratorCondition;

import javax.swing.*;
import java.awt.*;

public class FossilPanel extends JPanel implements Panel {

    private final JTextField fossil;
    private final JTextField fossilZ;

    public FossilPanel() {
        JTextField fossil = new JTextField("fossil X");
        fossil.setColumns(10);
        Panel.deleteTextOnSelection(fossil);

        JTextField fossilZ = new JTextField("fossil Z");
        fossilZ.setColumns(10);
        fossilZ.setBackground(Color.GRAY);
        Panel.deleteTextOnSelection(fossilZ);

        this.setLayout(new GridLayout(2,1));
        this.setBorder(BorderFactory.createTitledBorder("Fossils"));
        this.add(fossil);
        this.add(fossilZ);

        this.fossil = fossil;
        this.fossilZ = fossilZ;
    }

    @Override
    public void reset() {
        fossil.setText("fossil x");
        fossilZ.setText("fossil z");
        Panel.deleteTextOnSelection(fossil);
        Panel.deleteTextOnSelection(fossilZ);
    }

    @Override
    public Condition getCondition(boolean crossReference) {
        int fossilValue = Panel.parseIntFromTextField(fossil, 0, 16);
        int fossilZValue = Panel.parseIntFromTextField(fossilZ, 0, 16);

        if (fossilValue != Panel.FAIL) {
            if (fossilZValue != Panel.FAIL) {
               return new DecoratorCondition(0, fossilValue, fossilValue + 1, fossilZValue, fossilZValue+1);
            } else {
                return new DecoratorCondition(0, fossilValue, fossilValue + 1, 0, 16);
            }
        }
        return null;
    }
}
