package panels;

import Conditions.ChanceDecoratorCondition;
import Conditions.Condition;
import Conditions.CountDecoratorCondition;
import Conditions.DecoratorCondition;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GeneralDecoratorPanel extends JPanel implements Panel {

    private final JTextField saltField;
    private final JTextField chanceField;
    private final JTextField countField;
    private final JTextField observableOneField;
    private final JTextField observableTwoField;
    private final JTextField observableThreeField;
    private final JTextField observableOneRange;
    private final JTextField observableTwoRange;
    private final JTextField observableThreeRange;

    public GeneralDecoratorPanel(int id) {
        saltField = new JTextField("salt");
        chanceField = new JTextField("chance");
        countField = new JTextField("count");
        observableOneField = new JTextField("a");
        observableTwoField = new JTextField("b");
        observableThreeField = new JTextField("c");
        observableOneRange = new JTextField("pa");
        observableTwoRange = new JTextField("pb");
        observableThreeRange = new JTextField("pc");

        Panel.deleteTextOnSelection(saltField);
        Panel.deleteTextOnSelection(chanceField);
        Panel.deleteTextOnSelection(countField);
        Panel.deleteTextOnSelection(observableOneField);
        Panel.deleteTextOnSelection(observableTwoField);
        Panel.deleteTextOnSelection(observableThreeField);
        Panel.deleteTextOnSelection(observableOneRange);
        Panel.deleteTextOnSelection(observableTwoRange);
        Panel.deleteTextOnSelection(observableThreeRange);

        saltField.setColumns(10);
        chanceField.setColumns(10);
        countField.setColumns(10);
        observableOneField.setColumns(4);
        observableTwoField.setColumns(4);
        observableThreeField.setColumns(4);
        observableOneRange.setColumns(4);
        observableTwoRange.setColumns(4);
        observableThreeRange.setColumns(4);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,1));
        panel.add(saltField);
        panel.add(chanceField);
        panel.add(countField);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(3,2));
        panel2.add(observableOneField);
        panel2.add(observableOneRange);
        panel2.add(observableTwoField);
        panel2.add(observableTwoRange);
        panel2.add(observableThreeField);
        panel2.add(observableThreeRange);
        this.setBorder(BorderFactory.createTitledBorder("Decorator " + id));
        this.add(panel);
        this.add(panel2);
    }

    @Override
    public void reset() {
        saltField.setText("salt");
        chanceField.setText("chance");
        countField.setText("count");
        observableOneField.setText("a");
        observableTwoField.setText("b");
        observableThreeField.setText("c");

        observableOneRange.setText("pa");
        observableTwoRange.setText("pb");
        observableThreeRange.setText("pc");

        Panel.deleteTextOnSelection(saltField);
        Panel.deleteTextOnSelection(chanceField);
        Panel.deleteTextOnSelection(countField);
        Panel.deleteTextOnSelection(observableOneField);
        Panel.deleteTextOnSelection(observableTwoField);
        Panel.deleteTextOnSelection(observableThreeField);
        Panel.deleteTextOnSelection(observableOneRange);
        Panel.deleteTextOnSelection(observableTwoRange);
        Panel.deleteTextOnSelection(observableThreeRange);
    }

    @Override
    public Condition getCondition(boolean crossReference) {
        int salt = Panel.parseIntFromTextField(saltField);
        if (salt == Panel.FAIL)
            return null;
        float chance = Panel.parseFloatFromTextField(chanceField);
        int count = Panel.parseIntFromTextField(countField);

        int o1 = Panel.parseIntFromTextField(observableOneField);
        int o2 = Panel.parseIntFromTextField(observableTwoField);
        int o3 = Panel.parseIntFromTextField(observableThreeField);
        int r1 = Panel.parseIntFromTextField(observableOneRange);
        int r2 = Panel.parseIntFromTextField(observableTwoRange);
        int r3 = Panel.parseIntFromTextField(observableThreeRange);

        long step1 = r1 != Panel.FAIL ? (1L << 48) / r1 : 1L << 44;
        long step2 = r2 != Panel.FAIL ? (1L << 48) / r2 : 1L << 44;
        long step3 = r3 != Panel.FAIL ? (1L << 48) / r3 : 1L << 44;

        long l1 = o1 != Panel.FAIL ? o1 * step1 : 0;
        long l2 = o2 != Panel.FAIL ? o2 * step2 : 0;
        long l3 = o3 != Panel.FAIL ? o3 * step3 : 0;

        long u1 = o1 != Panel.FAIL ? (o1 + 1) * step1 : 1L << 48;
        long u2 = o2 != Panel.FAIL ? (o2 + 1) * step2 : 1L << 48;
        long u3 = o3 != Panel.FAIL ? (o3 + 1) * step3 : 1L << 48;

        ArrayList<Long> lowerBounds = new ArrayList<>();
        ArrayList<Long> upperBounds = new ArrayList<>();
        lowerBounds.add(l1);
        lowerBounds.add(l2);
        lowerBounds.add(l3);
        upperBounds.add(u1);
        upperBounds.add(u2);
        upperBounds.add(u3);

        if (count != Panel.FAIL) {
            return new CountDecoratorCondition(salt, count, lowerBounds, upperBounds);
        } else if (!Float.isNaN(chance)) {
            return new ChanceDecoratorCondition(salt, chance, lowerBounds, upperBounds);
        } else  {
            return new DecoratorCondition(salt, lowerBounds, upperBounds);
        }
    }
}
