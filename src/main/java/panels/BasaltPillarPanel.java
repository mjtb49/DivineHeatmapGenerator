package panels;

import Conditions.Condition;
import Conditions.CountDecoratorCondition;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BasaltPillarPanel extends JPanel implements Panel {

    private final JTextField xB;
    private final JTextField yB;
    private final JTextField zB;

    public BasaltPillarPanel() {
        xB = new JTextField("x");
        xB.setColumns(10);
        Panel.deleteTextOnSelection(xB);

        yB = new JTextField("y");
        yB.setColumns(10);
        Panel.deleteTextOnSelection(yB);

        zB = new JTextField("z");
        zB.setColumns(10);
        zB.setBackground(Color.GRAY);
        Panel.deleteTextOnSelection(zB);

        this.setLayout(new GridLayout(3,1));
        this.setBorder(BorderFactory.createTitledBorder("Basalt Pillar"));
        this.add(xB);
        this.add(yB);
        this.add(zB);
    }

    @Override
    public void reset() {
        xB.setText("x");
        yB.setText("y");
        zB.setText("z");
    }

    @Override
    public Condition getCondition(boolean crossReference) {

        int basaltX = Panel.parseIntFromTextField(xB);
        int basaltY = Panel.parseIntFromTextField(yB);
        int basaltZ = Panel.parseIntFromTextField(zB);

        if (0 <= basaltX && basaltX < 16 && 0 <= basaltY && basaltY < 128) {

            long lowerZ = 0;
            long upperZ = 16L << 48;
            if (0 <= basaltZ && basaltZ < 16) {
                lowerZ = (long) basaltZ << (48 - 4);
                upperZ = (long) (basaltZ + 1) << (48 - 4);
            }

            ArrayList<Long> lowerBounds = new ArrayList<>();
            lowerBounds.add((long) basaltX << (48 - 4));
            lowerBounds.add(lowerZ);
            lowerBounds.add((long) basaltY << (48 - 7));

            ArrayList<Long> upperBounds = new ArrayList<>();
            upperBounds.add((long) (basaltX + 1) << (48 - 4));
            upperBounds.add(upperZ);
            upperBounds.add((long) (basaltY + 1) << (48 - 7));

            return new CountDecoratorCondition(20000, 10, lowerBounds, upperBounds);
        }
        return null;
    }
}
