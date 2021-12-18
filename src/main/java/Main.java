import Conditions.*;
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

    static int maxNumSeeds = 100_000_000;
    static int sampleSize = 100000;
    static int blockThreshold = 0;

    static int treasureX = Integer.MIN_VALUE;
    static int treasureZ = Integer.MIN_VALUE;

    static int fossilValue = -1;
    static int fossilZValue = -1;

    static int portalValue = -1;

    static int treeSalt = 80000;
    static float treeChance = 0.1f;
    static int treeX = -1;
    static int treeZ = -1;

    static int basaltX = -1;
    static int basaltY = -1;
    static int basaltZ = -1;

    static double[][] probabilities = DivineHeatmapCalculator.getSuccessProbabilityOfLocations(16, new ArrayList<>(), sampleSize, maxNumSeeds);
    static final BufferedImage defaultImage = DivineHeatmapCalculator.getHeatMapAsImage(probabilities);
    static JLabel heatMap = new JLabel(new ImageIcon(defaultImage));
    static JLabel output = new JLabel();
    static ArrayList<JTextField> textToReset = new ArrayList<>();
    static JRadioButton unknownButton;

    private static void deleteTextOnSelection(JTextField jTextField) {
        jTextField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                JTextField source = (JTextField)e.getComponent();
                source.setText("");
                source.removeFocusListener(this);
            }
        });
    }

    private static JPanel makeSettingsPanel() {
        JTextField distance = new JTextField("blocks");
        distance.setColumns(10);
        deleteTextOnSelection(distance);
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
        samples.setColumns(10);
        deleteTextOnSelection(samples);
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
        settings.setLayout(new GridLayout(2,1));
        settings.setBorder(BorderFactory.createTitledBorder("Settings"));
        settings.add(distance);
        settings.add(samples);
        return settings;
    }

    private static JPanel makeTreasurePanel() {
        JTextField xT = new JTextField("x");
        xT.setColumns(10);

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

        deleteTextOnSelection(xT);
        JTextField zT = new JTextField("z");
        zT.setColumns(10);
        deleteTextOnSelection(zT);
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
        treasure.setLayout(new GridLayout(2,1));
        treasure.setBorder(BorderFactory.createTitledBorder("Treasure"));
        treasure.add(xT);
        treasure.add(zT);

        textToReset.add(xT);
        textToReset.add(zT);

        return treasure;
    }

    private static JPanel makeBasaltPillarPanel() {
        JTextField xB = new JTextField("x");
        xB.setColumns(10);
        deleteTextOnSelection(xB);
        xB.getDocument().addDocumentListener(new DocumentListener() {
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
                    basaltX = Integer.parseInt(xB.getText());
                } catch (NumberFormatException nfe) {
                    System.err.println(nfe.getMessage());
                    basaltX = -1;
                }
            }
        });

        JTextField yB = new JTextField("y");
        yB.setColumns(10);
        deleteTextOnSelection(yB);
        yB.getDocument().addDocumentListener(new DocumentListener() {
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
                    basaltY = Integer.parseInt(yB.getText());
                } catch (NumberFormatException nfe) {
                    System.err.println(nfe.getMessage());
                    basaltY = -1;
                }
            }
        });

        JTextField zB = new JTextField("z");
        zB.setColumns(10);
        zB.setBackground(Color.GRAY);
        deleteTextOnSelection(zB);
        zB.getDocument().addDocumentListener(new DocumentListener() {
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
                    basaltZ = Integer.parseInt(zB.getText());
                } catch (NumberFormatException nfe) {
                    System.err.println(nfe.getMessage());
                    basaltZ = -1;
                }
            }
        });

        JPanel pillar = new JPanel();
        pillar.setLayout(new GridLayout(3,1));
        pillar.setBorder(BorderFactory.createTitledBorder("Basalt Pillar"));
        pillar.add(xB);
        pillar.add(yB);
        pillar.add(zB);

        textToReset.add(xB);
        textToReset.add(yB);
        textToReset.add(zB);

        return pillar;
    }

    private static JPanel makeFossilPanel() {
        JTextField fossil = new JTextField("fossil X");
        fossil.setColumns(10);
        deleteTextOnSelection(fossil);
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

        JTextField fossilZ = new JTextField("fossil Z");
        fossilZ.setColumns(10);
        fossilZ.setBackground(Color.GRAY);
        deleteTextOnSelection(fossilZ);
        fossilZ.getDocument().addDocumentListener(new DocumentListener() {
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
                    fossilZValue = Integer.parseInt(fossilZ.getText());
                    if (fossilZValue < 0 || fossilZValue > 15) {
                        System.err.println("Tried and failed to set fossil Z to " + fossilZValue);
                        fossilZValue = -1;
                    }
                } catch (NumberFormatException nfe) {
                    System.err.println(nfe.getMessage());
                    fossilZValue = -1;
                }
            }
        });

        JPanel fossils = new JPanel();
        fossils.setLayout(new GridLayout(2,1));
        fossils.setBorder(BorderFactory.createTitledBorder("Fossils"));
        fossils.add(fossil);
        fossils.add(fossilZ);

        textToReset.add(fossil);
        textToReset.add(fossilZ);
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

    private static JPanel makeTreePanel() {
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

        /*
            east  = 00 -> ++
            north = 01 -> -+
            west  = 10 -> --
            south = 11 -> +-
         */
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

        JTextField treeXField = new JTextField("tree X");
        treeXField.setColumns(10);
        deleteTextOnSelection(treeXField);
        treeXField.setBackground(Color.GRAY);
        treeXField.getDocument().addDocumentListener(new DocumentListener() {
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
                    treeX = Integer.parseInt(treeXField.getText());
                    if (treeX < 0 || treeX > 15) {
                        System.err.println("Tried and failed to set fossil to " + treeX);
                        treeX = -1;
                    }
                } catch (NumberFormatException nfe) {
                    System.err.println(nfe.getMessage());
                    treeX = -1;
                }
            }
        });

        JTextField treeZField = new JTextField("tree Z");
        treeZField.setColumns(10);
        deleteTextOnSelection(treeZField);
        treeZField.getDocument().addDocumentListener(new DocumentListener() {
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
                    treeZ = Integer.parseInt(treeZField.getText());
                    if (treeZ < 0 || treeZ > 15) {
                        System.err.println("Tried and failed to set fossil Z to " + treeZ);
                        treeZ = -1;
                    }
                } catch (NumberFormatException nfe) {
                    System.err.println(nfe.getMessage());
                    treeZ = -1;
                }
            }
        });

        JPanel coords = new JPanel();
        coords.setLayout(new GridLayout(2,1));

        JPanel trees = new JPanel();
        trees.setLayout(new GridBagLayout());
        trees.setBorder(BorderFactory.createTitledBorder("Trees"));
        coords.add(treeXField);
        coords.add(treeZField);

        trees.add(treeTypeSelector);
        trees.add(coords);

        textToReset.add(treeXField);
        textToReset.add(treeZField);

        return trees;
    }

    private static double[][] makeTheMap() {
        ArrayList<Condition> conds = new ArrayList<>();
        if (treasureX != Integer.MIN_VALUE && treasureZ != Integer.MIN_VALUE) {
            BuriedTreasureCondition buriedTreasureCondition = new BuriedTreasureCondition(treasureX, treasureZ);
            conds.add(buriedTreasureCondition);
        }

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

            conds.add(new CountDecoratorCondition(20000, 10, lowerBounds, upperBounds));
        }

        if (fossilValue != -1) {
            if (fossilZValue != -1) {
                conds.add(new DecoratorCondition(0, fossilValue, fossilValue + 1, fossilZValue, fossilZValue+1));
            } else {
                conds.add(new DecoratorCondition(0, fossilValue, fossilValue + 1, 0, 16));
            }
        }

        if (treeZ != -1) {
            if (treeX != -1) {
                conds.add(new ChanceDecoratorCondition(treeSalt, treeChance, treeX, treeZ));
            } else {
                conds.add(new ChanceDecoratorCondition(treeSalt, treeChance, treeZ));
            }
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
            basaltX = -1;
            basaltY = -1;
            treeX = -1;
            treeZ = -1;
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

        f.add(makeFirstPortalButtons());
        f.add(makeFossilPanel());
        f.add(makeTreasurePanel());
        f.add(makeSettingsPanel());
        f.add(makeBasaltPillarPanel());
        f.add(makeTreePanel());

        f.add(heatMap);
        f.add(jButton);
        f.add(reset);
        f.add(output);

        f.setVisible(true);
    }
}
