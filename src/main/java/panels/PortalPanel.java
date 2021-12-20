package panels;

import Conditions.Condition;
import Conditions.DecoratorCondition;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PortalPanel extends JPanel implements Panel{

    int callNumber;
    int portalValue;
    JRadioButton unknownButton;

    public PortalPanel(int portalNumber) {
        portalValue = Panel.FAIL;
        this.callNumber = portalNumber;
        JRadioButton north = new JRadioButton("North");
        JRadioButton south = new JRadioButton("South");
        JRadioButton east = new JRadioButton("East");
        JRadioButton west = new JRadioButton("West");
        JRadioButton unknown = new JRadioButton("Unknown");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(north);
        buttonGroup.add(south);
        buttonGroup.add(east);
        buttonGroup.add(west);
        buttonGroup.add(unknown);

        north.setSize(25,125);
        south.setSize(25,125);
        east.setSize(25,125);
        west.setSize(25,125);
        unknown.setSize(25,125);

        east.addActionListener(e -> {
            portalValue = 0b00;
        });
        north.addActionListener(e -> {
            portalValue = 0b01;
        });
        west.addActionListener(e -> {
            portalValue = 0b10;
        });
        south.addActionListener(e -> {
            portalValue = 0b11;
        });
        unknown.setSelected(true);
        unknown.addActionListener(e -> {
            portalValue = Panel.FAIL;
        });

        this.setLayout(new GridLayout(5,1));
        this.setBorder(BorderFactory.createTitledBorder("Portal " + callNumber));
        this.add(north);
        this.add(south);
        this.add(east);
        this.add(west);
        this.add(unknown);

        unknownButton = unknown;
    }

    @Override
    public void reset() {
        unknownButton.setSelected(true);
        portalValue = Panel.FAIL;
    }

    @Override
    public Condition getCondition() {
        if (portalValue != Panel.FAIL) {
            ArrayList<Long> lowerBounds = new ArrayList<>();
            ArrayList<Long> upperBounds = new ArrayList<>();
            for (int i = 0; i < callNumber - 1; i++) {
                lowerBounds.add(0L);
                upperBounds.add(1L << 48);
            }

            lowerBounds.add(((long) portalValue) << 46);
            upperBounds.add(((long) portalValue + 1) << 46);

            return new DecoratorCondition(0, lowerBounds, upperBounds);
        }
        return null;
    }
}
