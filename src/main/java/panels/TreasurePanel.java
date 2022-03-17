package panels;

import Conditions.BuriedTreasureCondition;
import Conditions.Condition;

import javax.swing.*;
import java.awt.*;

public class TreasurePanel extends JPanel implements Panel {

    private final JTextField xT;
    private final JTextField zT;
    public TreasurePanel() {
        xT = new JTextField("x");
        xT.setColumns(10);
        Panel.deleteTextOnSelection(xT);

        zT = new JTextField("z");
        zT.setColumns(10);
        Panel.deleteTextOnSelection(zT);

        this.setLayout(new GridLayout(2,1));
        this.setBorder(BorderFactory.createTitledBorder("Treasure"));
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
            return new BuriedTreasureCondition(x, z);
        }

        return null;
    }
}
