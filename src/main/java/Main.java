import Conditions.BuriedTreasureCondition;
import Conditions.Condition;
import Conditions.DecoratorCondition;
import heatmaps.DivineHeatmapCalculator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Main {

    final static int SIZE = 168 + 7;
    final static int SIDE_LENGTH = SIZE * 2 + 1;
    static int sampleSize = 100000;
    static int maxNumSeeds = 1_000_000_000;
    static int treasureX = Integer.MIN_VALUE;
    static int treasureZ = Integer.MIN_VALUE;
    static int fossilValue = -1;
    static int portalValue = -1;
    static int blockThreshold = 0;
    static double[][] probabilities = DivineHeatmapCalculator.getSuccessProbabilityOfLocations(16, new ArrayList<>(), sampleSize, maxNumSeeds);
    static final BufferedImage defaultImage = DivineHeatmapCalculator.getHeatMapAsImage(probabilities);
    static JLabel heatMap = new JLabel(new ImageIcon(defaultImage));
    static JLabel output = new JLabel();
    static ArrayList<JTextField> textToReset = new ArrayList<>();
    static JRadioButton unknownButton;

    private static JPanel makeSettingsPanel() {
        JTextField distance = new JTextField("blocks");
        distance.setColumns(4);
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
                    blockThreshold = 0;
                }
            }
        });

        JTextField samples = new JTextField("num trials");
        samples.setColumns(4);
        samples.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                JTextField source = (JTextField)e.getComponent();
                source.setText("");
                source.removeFocusListener(this);
            }
        });

        samples.getDocument().addDocumentListener(new DocumentListener() {
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
                    sampleSize = Integer.parseInt(samples.getText());
                } catch (NumberFormatException nfe) {
                    System.err.println(nfe.getMessage());
                    sampleSize = 100000;
                }
            }
        });

        JPanel settings = new JPanel();
        settings.setBorder(BorderFactory.createTitledBorder("Settings"));
        settings.add(distance);
        settings.add(samples);
        return settings;
    }

    private static JPanel makeTreasurePanel() {
        JTextField xT = new JTextField("x");
        xT.setColumns(3);

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
        zT.setColumns(3);
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

        JPanel treasure = new JPanel();
        treasure.setBorder(BorderFactory.createTitledBorder("Treasure"));
        treasure.add(xT);
        treasure.add(zT);

        textToReset.add(xT);
        textToReset.add(zT);

        return treasure;
    }

    private static JPanel makeFossilPanel() {
        JTextField fossil = new JTextField("Fossil");
        fossil.setColumns(6);
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
                    if (fossilValue < 0 || fossilValue > 15) {
                        System.err.println("Tried and failed to set fossil to " + fossilValue);
                        fossilValue = -1;
                    }
                } catch (NumberFormatException nfe) {
                    System.err.println(nfe.getMessage());
                    fossilValue = -1;
                }
            }
        });
        JPanel fossils = new JPanel();
        fossils.setBorder(BorderFactory.createTitledBorder("Fossils"));
        fossils.add(fossil);

        textToReset.add(fossil);

        return  fossils;
    }

    private static JPanel makeFirstPortalButtons() {
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
        unknown.setSelected(true);
        unknown.addActionListener(e -> {
            portalValue = -1;
        });

        JPanel portalButtons = new JPanel();
        portalButtons.setLayout(new GridLayout(5,1));
        portalButtons.setBorder(BorderFactory.createTitledBorder("First Portal"));

        portalButtons.add(north);
        portalButtons.add(south);
        portalButtons.add(east);
        portalButtons.add(west);
        portalButtons.add(unknown);

        unknownButton = unknown;

        return portalButtons;
    }

    private static double[][] makeTheMap() {
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
        return DivineHeatmapCalculator.getSuccessProbabilityOfLocations(blockThreshold, conds, sampleSize, maxNumSeeds);
    }

    public static void main(String[] args) {
        JFrame f =new JFrame();
        f.setSize(SIDE_LENGTH + 400,SIDE_LENGTH + 300);

        FlowLayout flowLayout = new FlowLayout();
        f.setLayout(flowLayout);

        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JButton jButton = new JButton("Compute Heatmap");
        //jButton.setSize(75,25);
        jButton.addActionListener(e -> {

            //conds.add(firstPortal);
            probabilities = makeTheMap();
            heatMap.setIcon(new ImageIcon(DivineHeatmapCalculator.getHeatMapAsImage(probabilities)));

            double max = 0;
            int maxX = 0;
            int maxZ = 0;
            for (int i = 0; i < probabilities.length; i++) for (int j = 0; j < probabilities[0].length; j++) {
                    if (probabilities[i][j] > max) {
                        max = probabilities[i][j];
                        maxX = (i - SIZE) * 2;
                        maxZ = (j - SIZE) * 2;
                    }
                }
            output.setText(String.format("%.3g", max * 100) +"% chance of stronghold within " + blockThreshold + " blocks attained at " + maxX + " " + maxZ);
        });

        JButton reset = new JButton("Reset");
        reset.addActionListener(e -> {
            portalValue = -1;
            fossilValue = -1;
            treasureX = Integer.MIN_VALUE;
            treasureZ = Integer.MIN_VALUE;
            for (JTextField textField : textToReset) {
                textField.setText("");
            }
            heatMap.setIcon(new ImageIcon(defaultImage));
            unknownButton.setSelected(true);
        });
        reset.setSize(25,125);

        heatMap.addMouseMotionListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                //super.mouseMoved(e);
                Point location = e.getLocationOnScreen();
                Point offset = e.getComponent().getLocationOnScreen();

                int x = (location.x - offset.x);
                int z = (location.y - offset.y);
                //convert to nether coords
                heatMap.setToolTipText((x - SIZE)*2 + " " + (z - SIZE)*2 + " " + String.format("%.3g", probabilities[x][z] * 100)+"%");
            }
        });
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        ToolTipManager.sharedInstance().setInitialDelay(0);
        JPanel userInputs = new JPanel();
        userInputs.setLayout(new FlowLayout());
        f.add(heatMap);
        userInputs.add(makeFirstPortalButtons());
        userInputs.add(makeFossilPanel());
        userInputs.add(makeTreasurePanel());
        userInputs.add(makeSettingsPanel());
        userInputs.add(jButton);
        userInputs.add(reset);
        f.add(userInputs);
        f.add(output);
        f.setVisible(true);
    }
}
