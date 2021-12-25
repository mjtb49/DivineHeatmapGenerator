import Conditions.*;
import heatmaps.DivineHeatmapCalculator;
import panels.*;
import panels.Panel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    final static int SIZE = 168 + 7;
    final static int SIDE_LENGTH = SIZE * 2 + 1;

    static int maxNumSeeds = 100_000_000;
    static int sampleSize = 100000;

    static double[][] startProbabilities = DivineHeatmapCalculator.getStartDistributions(new ArrayList<>(), sampleSize, maxNumSeeds, true);
    static double[][] probabilities = startProbabilities;
    static final BufferedImage defaultImage = DivineHeatmapCalculator.getHeatMapAsImage(probabilities);
    static JLabel heatMap = new JLabel(new ImageIcon(defaultImage));
    static JLabel output = new JLabel();
    static JLabel debug = new JLabel();
    static ArrayList<Panel> inputs = new ArrayList<>();
    static SettingsPanel settingsPanel;


    public static void updateHeatmapToNewStart() {
        probabilities = DivineHeatmapCalculator.getPlayerPreferences(startProbabilities, settingsPanel.getBlockThreshold());
        heatMap.setIcon(new ImageIcon(DivineHeatmapCalculator.getHeatMapAsImage(probabilities)));
    }

    private static double[][] makeTheMap(boolean useAllThreeStrongholds) {
        ArrayList<Condition> conds = new ArrayList<>();

        for (Panel input : inputs) {
            if (input.getCondition() != null) {
                conds.add(input.getCondition());
                System.out.println(input);
            }
        }
        startProbabilities = DivineHeatmapCalculator.getStartDistributions(conds, settingsPanel.getSampleSize(), maxNumSeeds, useAllThreeStrongholds);
        probabilities = DivineHeatmapCalculator.getPlayerPreferences(startProbabilities, settingsPanel.getBlockThreshold());
        return probabilities;
    }

    private static JButton makeHeatmapButton(boolean threeStrongholds) {
        JButton jButton = new JButton("Compute Heatmap");
        if (!threeStrongholds)
            jButton.setText("First Only");

        jButton.addActionListener(e -> {
            probabilities = makeTheMap(threeStrongholds);
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
            output.setText(String.format("%.3g", max * 100) +"% chance of stronghold within " + settingsPanel.getBlockThreshold() + " blocks attained at " + maxX + " " + maxZ);
            debug.setText(DivineHeatmapCalculator.getDebugString());
        });

        return jButton;
    }


    public static void main(String[] args) {
        settingsPanel = new SettingsPanel();

        settingsPanel.getDistanceField().getDocument().addDocumentListener(new DocumentListener() {
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
                    if (Integer.parseInt(settingsPanel.getDistanceField().getText())>=0) {
                        updateHeatmapToNewStart();
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
                        output.setText(String.format("%.3g", max * 100) +"% chance of stronghold within " + settingsPanel.getBlockThreshold() + " blocks attained at " + maxX + " " + maxZ);
                        debug.setText(DivineHeatmapCalculator.getDebugString());
                    }
                } catch (NumberFormatException nfe) {
                    System.err.println(Arrays.toString(nfe.getStackTrace()));
                }
            }
        });


        inputs.add(new PortalPanel(1));
        inputs.add(new PortalPanel(2));
        inputs.add(new PortalPanel(3));
        inputs.add(new TreasurePanel());
        inputs.add(new TreasurePanel());
        inputs.add(new FossilPanel());
        inputs.add(new BasaltPillarPanel());
        inputs.add(new TreePanel());
        inputs.add(new AnimalPanel());
        inputs.add(new DecoratorPanel("Pumpkin", 80007).chance(1/32.0f).call("x",true).call("z"));
        inputs.add(new DecoratorPanel("Iceberg", 20000).chance(1/16.0f).call("x",true).call("z"));
        inputs.add(new DecoratorPanel("Blue Iceberg", 20001).chance(1/200.0f).call("x",true).call("z"));
        inputs.add(new DecoratorPanel("Desert Well", 40000).chance(1/1000.0f).call("x",16,true).call("z",16));
        inputs.add(new DecoratorPanel("Sea Pickle", 80011).chance(1/16.0f).call("x",true).call("z"));
        inputs.add(new DecoratorPanel("Brown Mushroom", 80001).chance(1/4.0f).call("x",true).call("z"));
        inputs.add(new DecoratorPanel("Red Mushroom", 80002).chance(1/8.0f).call("x",true).call("z"));
        inputs.add(new DecoratorPanel("Gravel Patch", 60013).call("x",16).call("z",16,true).call("rad",4));
        inputs.add(new DecoratorPanel("Clay (Swamp)", 60011).call("x",16).call("z",16,true).call("rad",2));
        inputs.add(new DecoratorPanel("Clay (Other)", 60012).call("x",16).call("z",16,true).call("rad",2));

        PanelGroup panelGroup = new PanelGroup(new GridLayout(3,1),new GeneralDecoratorPanel(1),new GeneralDecoratorPanel(2),new GeneralDecoratorPanel(3));
        inputs.add(panelGroup);

        JFrame f =new JFrame();
        f.setSize(SIDE_LENGTH + 400,SIDE_LENGTH + 400);
        FlowLayout flowLayout = new FlowLayout();
        f.setLayout(flowLayout);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JButton reset = new JButton("Reset");
        reset.addActionListener(e -> {
            for (Panel input : inputs) {
                input.reset();
            }
            heatMap.setIcon(new ImageIcon(defaultImage));
        });
        reset.setSize(25,125);

        heatMap.addMouseMotionListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point location = e.getLocationOnScreen();
                Point offset = e.getComponent().getLocationOnScreen();
                int x = (location.x - offset.x);
                int z = (location.y - offset.y);
                heatMap.setToolTipText((x - SIZE)*2 + " " + (z - SIZE)*2 + " " + String.format("%.3g", probabilities[x][z] * 100)+"%");
            }
        });
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        ToolTipManager.sharedInstance().setInitialDelay(0);

        for (Panel input : inputs) {
            f.add((JPanel) input);
        }
        f.add(heatMap);
        JPanel controls = new JPanel(new GridLayout(4,1));
        controls.add(settingsPanel);
        controls.add(makeHeatmapButton(true));
        controls.add(makeHeatmapButton(false));
        controls.add(reset);
        f.add(controls);

        JPanel textOuputs = new JPanel(new GridLayout(2,1));
        textOuputs.add(output);
        textOuputs.add(debug);

        f.add(textOuputs);

        f.setVisible(true);
    }
}
