package panels;

import Conditions.Condition;
import Conditions.RuinCondition;
import Conditions.ShipwreckCondition;

import javax.swing.*;
import java.awt.*;

public class RuinPanel extends JPanel implements Panel {

    private final JTextField xT;
    private final JTextField zT;
    public RuinPanel() {
        xT = new JTextField("x");
        xT.setColumns(10);
        Panel.deleteTextOnSelection(xT);

        zT = new JTextField("z");
        zT.setColumns(10);
        Panel.deleteTextOnSelection(zT);

        this.setLayout(new GridLayout(2,1));
        this.setBorder(BorderFactory.createTitledBorder("Ruins"));
        this.add(xT);
        this.add(zT);
    }

    @Override
    public void reset() {
        xT.setText("x");
        zT.setText("z");
        Panel.deleteTextOnSelection(xT);
        Panel.deleteTextOnSelection(zT);
    }

    @Override
    public Condition getCondition() {

        int x = Panel.parseIntFromTextField(xT);
        int z = Panel.parseIntFromTextField(zT);
        if (x != Panel.FAIL && z != Panel.FAIL) {
            if (((x & 0xfL) < 8) && ((z & 0xfL) < 8))
                return new RuinCondition(x, z);
        }

        return null;
    }
}