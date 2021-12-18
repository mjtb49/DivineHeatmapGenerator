import Conditions.BuriedTreasureCondition;
import Conditions.Condition;
import Conditions.DecoratorCondition;
import heatmaps.DivineHeatmapCalculator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.html.Option;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class Main {

    final static int SIZE = 168 + 7;
    final static int SIDE_LENGTH = SIZE * 2 + 1;
    static final int SAMPLE_SIZE = 100000;
    static int treasureX = Integer.MIN_VALUE;
    static int treasureZ = Integer.MIN_VALUE;
    static int fossilValue = -1;
    static int portalValue = -1;
    static int blockThreshold = 16;
    static final BufferedImage defaultImage = DivineHeatmapCalculator.getHeatMapAsImage(DivineHeatmapCalculator.getSuccessProbabilityOfLocations(16, new ArrayList<>(), 100000));
    static JLabel heatMap = new JLabel(new ImageIcon(defaultImage));
    static JLabel output = new JLabel();


    public static void main(String[] args) {
        JFrame f =new JFrame();
        f.setSize(SIDE_LENGTH,SIDE_LENGTH + 200);

        FlowLayout flowLayout = new FlowLayout();
        f.setLayout(flowLayout);

        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JTextField distance = new JTextField("threshold");
        distance.setSize(125,25);
        distance.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                JTextField source = (JTextField)e.getComponent();
                source.setText("");
                source.removeFocusListener(this);
            }
        });

        distance.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }
            public void removeUpdate(DocumentEvent e) {
                warn();
            }
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                try {
                    blockThreshold = Integer.parseInt(distance.getText());
                } catch (NumberFormatException nfe) {
                    System.err.println(nfe.getMessage());
                    blockThreshold = 16;
                }
            }
        });

        JTextField xT = new JTextField("x");
        xT.setSize(125,25);

        xT.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }
            public void removeUpdate(DocumentEvent e) {
                warn();
            }
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                try {
                    treasureX = Integer.parseInt(xT.getText());
                } catch (NumberFormatException nfe) {
                    System.err.println(nfe.getMessage());
                    treasureX = Integer.MIN_VALUE;
                }
            }
        });

        xT.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                JTextField source = (JTextField)e.getComponent();
                source.setText("");
                source.removeFocusListener(this);
            }
        });

        JTextField zT = new JTextField("z");
        zT.setSize(125,25);
        zT.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                JTextField source = (JTextField)e.getComponent();
                source.setText("");
                source.removeFocusListener(this);
            }
        });
        zT.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }
            public void removeUpdate(DocumentEvent e) {
                warn();
            }
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                try {
                    treasureZ = Integer.parseInt(zT.getText());
                } catch (NumberFormatException nfe) {
                    System.err.println(nfe.getMessage());
                    treasureZ = Integer.MIN_VALUE;
                }
            }
        });

        JTextField fossil = new JTextField("fossil");
        fossil.setSize(225, 25);
        fossil.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                JTextField source = (JTextField)e.getComponent();
                source.setText("");
                source.removeFocusListener(this);
            }
        });

        fossil.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }
            public void removeUpdate(DocumentEvent e) {
                warn();
            }
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                try {
                    fossilValue = Integer.parseInt(fossil.getText());
                } catch (NumberFormatException nfe) {
                    System.err.println(nfe.getMessage());
                    fossilValue = -1;
                }
            }
        });

        JButton jButton = new JButton("Compute Heatmap");
        //jButton.setSize(75,25);
        jButton.addActionListener(e -> {
            ArrayList<Condition> conds = new ArrayList<>();
            if (treasureX != Integer.MIN_VALUE && treasureZ != Integer.MIN_VALUE) {
                BuriedTreasureCondition buriedTreasureCondition = new BuriedTreasureCondition(treasureX, treasureZ);
                conds.add(buriedTreasureCondition);
            }

            if (fossilValue != -1) {
                conds.add(new DecoratorCondition(0, fossilValue, fossilValue + 1, 0, 16));
            }

            if (portalValue != -1) {
                conds.add(new DecoratorCondition(0, portalValue, portalValue + 4, 0, 16));
            }

            //conds.add(firstPortal);
            double[][] locations = DivineHeatmapCalculator.getSuccessProbabilityOfLocations(blockThreshold, conds, SAMPLE_SIZE);
            heatMap.setIcon(new ImageIcon(DivineHeatmapCalculator.getHeatMapAsImage(locations)));

            double max = 0;
            for (int i = 0; i < locations.length; i++)
                for (int j = 0; j < locations[0].length; j++)
                    max = Math.max(max, locations[i][j]);
            output.setText("Best Probability " + max);
        });


        JButton north = new JButton("North");
        JButton south = new JButton("South");
        JButton east = new JButton("East");
        JButton west = new JButton("West");
        JButton unknown = new JButton("Unknown");
        JButton reset = new JButton("Reset");

        north.setSize(25,125);
        south.setSize(25,125);
        east.setSize(25,125);
        west.setSize(25,125);
        unknown.setSize(25,125);
        reset.setSize(25,125);

        /*
            east  = 00 -> ++
            north = 01 -> -+
            west  = 10 -> --
            south = 11 -> +-
         */
        east.addActionListener(e -> {
            portalValue = 0b0000;
        });
        north.addActionListener(e -> {
            portalValue = 0b0100;
        });
        west.addActionListener(e -> {
            portalValue = 0b1000;
        });
        south.addActionListener(e -> {
            portalValue = 0b1100;
        });
        unknown.addActionListener(e -> {
            portalValue = -1;
        });
        reset.addActionListener(e -> {
            portalValue = -1;
            fossilValue = -1;
            treasureX = Integer.MIN_VALUE;
            treasureZ = Integer.MIN_VALUE;
            fossil.setText("");
            xT.setText("");
            zT.setText("");
            heatMap.setIcon(new ImageIcon(defaultImage));
        });

        f.add(distance);
        f.add(heatMap);
        f.add(north);
        f.add(south);
        f.add(east);
        f.add(west);
        f.add(unknown);
        f.add(fossil);
        f.add(xT);
        f.add(zT);
        f.add(jButton);
        f.add(reset);
        f.add(output);
        f.setVisible(true);
    }
}
