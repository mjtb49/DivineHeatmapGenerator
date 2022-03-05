package panels;

import Conditions.Condition;
import Conditions.FortressCondition;
import Conditions.RuinCondition;

import javax.swing.*;
import java.awt.*;

public class FortressPanel extends JPanel implements Panel {

    private final JTextField xT;
    private final JTextField zT;
    public FortressPanel() {
        xT = new JTextField("x");
        xT.setColumns(10);
        Panel.deleteTextOnSelection(xT);

        zT = new JTextField("z");
        zT.setColumns(10);
        Panel.deleteTextOnSelection(zT);

        this.setLayout(new GridLayout(2,1));
        this.setBorder(BorderFactory.createTitledBorder("Fortress"));
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
            if ((((x - 4) & 0xfL) < 8) && (((z - 4) & 0xfL) < 8))
                return new FortressCondition(x, z);
        }

        return null;
    }
}