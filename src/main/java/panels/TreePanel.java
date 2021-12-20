package panels;

import Conditions.ChanceDecoratorCondition;
import Conditions.Condition;
import Conditions.DecoratorCondition;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class TreePanel extends JPanel implements Panel {

    private int treeSalt;
    private float treeChance;
    private final JTextField treeXField;
    private final JTextField treeZField;

    public TreePanel() {
        JRadioButton ocean = new JRadioButton("River/Ocean");
        JRadioButton savannah = new JRadioButton("Two Savannah Trees");
        JRadioButton mountains = new JRadioButton("Mountains");
        JRadioButton plains = new JRadioButton("Plains");
        JRadioButton tundra = new JRadioButton("Tundra");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(ocean);
        buttonGroup.add(savannah);
        buttonGroup.add(mountains);
        buttonGroup.add(plains);
        buttonGroup.add(tundra);

        ocean.setSize(25,125);
        savannah.setSize(25,125);
        mountains.setSize(25,125);
        plains.setSize(25,125);
        tundra.setSize(25,125);

        mountains.addActionListener(e -> {
            treeSalt = 80000;
            treeChance = 0.1f;
        });
        ocean.addActionListener(e -> {
            treeSalt = 80000;
            treeChance = 0.1f;
        });
        plains.addActionListener(e -> {
            treeSalt = 80001;
            treeChance = 0.05f;
        });
        savannah.addActionListener(e -> {
            treeSalt = 80001;
            treeChance = 0.1f;
        });
        tundra.addActionListener(e -> {
            treeSalt = 80000;
            treeChance = 0.1f;
        });

        JPanel treeTypeSelector = new JPanel();
        treeTypeSelector.setLayout(new GridLayout(5,1));

        treeTypeSelector.add(ocean);
        treeTypeSelector.add(savannah);
        treeTypeSelector.add(mountains);
        treeTypeSelector.add(plains);
        treeTypeSelector.add(tundra);

        treeXField = new JTextField("tree x");
        treeXField.setColumns(10);
        Panel.deleteTextOnSelection(treeXField);
        treeXField.setBackground(Color.GRAY);

        treeZField = new JTextField("tree z");
        treeZField.setColumns(10);
        Panel.deleteTextOnSelection(treeZField);

        JPanel coords = new JPanel();
        coords.setLayout(new GridLayout(2,1));

        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createTitledBorder("Trees"));
        coords.add(treeXField);
        coords.add(treeZField);

        this.add(treeTypeSelector);
        this.add(coords);
    }

    @Override
    public void reset() {
        treeXField.setText("tree x");
        treeZField.setText("tree z");
        Panel.deleteTextOnSelection(treeXField);
        Panel.deleteTextOnSelection(treeZField);
    }

    @Override
    public Condition getCondition() {
        int x = Panel.parseIntFromTextField(treeXField, 0, 16);
        int z = Panel.parseIntFromTextField(treeZField, 0, 16);

        if (z != Panel.FAIL) {
            if (x != Panel.FAIL) {
                return new ChanceDecoratorCondition(treeSalt, treeChance, x, z);
            } else {
               return new ChanceDecoratorCondition(treeSalt, treeChance, z);
            }
        }
        return null;
    }
}
