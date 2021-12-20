import Conditions.*;
import heatmaps.DivineHeatmapCalculator;
import panels.*;
import panels.Panel;

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

    static double[][] probabilities = DivineHeatmapCalculator.getSuccessProbabilityOfLocations(0, new ArrayList<>(), sampleSize, maxNumSeeds);
    static final BufferedImage defaultImage = DivineHeatmapCalculator.getHeatMapAsImage(probabilities);
    static JLabel heatMap = new JLabel(new ImageIcon(defaultImage));
    static JLabel output = new JLabel();
    static ArrayList<Panel> inputs = new ArrayList<>();
    static SettingsPanel settingsPanel;

    private static double[][] makeTheMap() {
        ArrayList<Condition> conds = new ArrayList<>();

        for (Panel input : inputs) {
            if (input.getCondition() != null) {
                conds.add(input.getCondition());
                System.out.println(input);
            }
        }

        return DivineHeatmapCalculator.getSuccessProbabilityOfLocations(settingsPanel.getBlockThreshold(), conds, settingsPanel.getSampleSize(), maxNumSeeds);
    }


    public static void main(String[] args) {

        settingsPanel = new SettingsPanel();
        inputs.add(new PortalPanel(1));
        inputs.add(new PortalPanel(2));
        inputs.add(new PortalPanel(3));
        inputs.add(new TreasurePanel());
        inputs.add(new TreasurePanel());
        inputs.add(new FossilPanel());
        inputs.add(new BasaltPillarPanel());
        inputs.add(new TreePanel());

        JFrame f =new JFrame();
        f.setSize(SIDE_LENGTH + 400,SIDE_LENGTH + 400);
        FlowLayout flowLayout = new FlowLayout();
        f.setLayout(flowLayout);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JButton jButton = new JButton("Compute Heatmap");
        jButton.addActionListener(e -> {
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
            output.setText(String.format("%.3g", max * 100) +"% chance of stronghold within " + settingsPanel.getBlockThreshold() + " blocks attained at " + maxX + " " + maxZ);
        });

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


        f.add(settingsPanel);
        for (Panel input : inputs) {
            f.add((JPanel) input);
        }
        f.add(heatMap);
        f.add(jButton);
        f.add(reset);
        f.add(output);

        f.setVisible(true);
    }
}
