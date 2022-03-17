package panels;

import Conditions.BuriedTreasureCondition;
import Conditions.Condition;
import Conditions.ShipwreckCondition;

import javax.swing.*;
import java.awt.*;

public class ShipwreckPanel extends JPanel implements Panel {

    private final JTextField xT;
    private final JTextField zT;
    public ShipwreckPanel() {
        xT = new JTextField("x");
        xT.setColumns(10);
        Panel.deleteTextOnSelection(xT);

        zT = new JTextField("z");
        zT.setColumns(10);
        Panel.deleteTextOnSelection(zT);

        this.setLayout(new GridLayout(2,1));
        this.setBorder(BorderFactory.createTitledBorder("Shipwreck"));
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
    public Condition getCondition(boolean crossReference) {

        int x = Panel.parseIntFromTextField(xT);
        int z = Panel.parseIntFromTextField(zT);
        if (x != Panel.FAIL && z != Panel.FAIL) {
            if (((x & 0xfL) < 8) && ((z & 0xfL) < 8))
                return new ShipwreckCondition(x, z, crossReference);
        }

        return null;
    }
}
