package heatmaps;

import Conditions.Condition;
import kaptainwutax.mcutils.util.pos.CPos;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class DivineHeatmapCalculator {

    final static int SIZE = 168 + 7;
    final static int SIDE_LENGTH = SIZE * 2 + 1;


    public static double[][] computeHeatMap(int sampleSize, ArrayList<Condition> conditions, int maxNumSeeds) {
        conditions.sort(Comparator.comparingDouble(Condition::computeRarity));
        double[][] heatMap = new double[SIDE_LENGTH][SIDE_LENGTH];
        int successes = 0;
        int total = 0;

        while (successes < sampleSize && total < maxNumSeeds && !((total > maxNumSeeds / 100) && (successes == 0))) {
            long seed = new Random().nextLong();
            total += 1;
            if (testConditions(conditions, seed)) {
                successes++;
                CPos cpos = StrongholdHelper.getFirstStartNoBiomes(seed);
                heatMap[cpos.getX() + SIZE][cpos.getZ() + SIZE] += 1;
            }
        }

        for (int i = 0; i < SIDE_LENGTH; i ++)
            for (int j = 0; j < SIDE_LENGTH; j ++)
                heatMap[i][j] /= sampleSize;
        return heatMap;
    }

    public static double[][] computeHeatMapAllStrongholds(int sampleSize, ArrayList<Condition> conditions, int maxNumSeeds) {

        //Sort the list so harder conditions come first. may work poorly with Condition Group
        conditions.sort(Comparator.comparingDouble(Condition::computeRarity));
        double[][] heatMap = new double[SIDE_LENGTH][SIDE_LENGTH];
        int total = 0;
        int successes = 0;
        while (successes < sampleSize && total < maxNumSeeds && !(total > maxNumSeeds / 100 && successes == 0)) {
            long seed = new Random().nextLong();
            total++;
            if (testConditions(conditions, seed)) {
                successes++;
                CPos[] cposes = StrongholdHelper.getFirstThreeStartsNoBiomes(seed);
                for (CPos cpos : cposes)
                    heatMap[cpos.getX() + SIZE][cpos.getZ() + SIZE] += 1;
            }
        }

        if (successes != 0) {
            for (int i = 0; i < SIDE_LENGTH; i++)
                for (int j = 0; j < SIDE_LENGTH; j++)
                    heatMap[i][j] /= successes;
            System.err.println( (successes /(double) total) + " of seeds " + successes + " " + total);
        } else {
            System.err.println("No Seeds Found!");
        }
        return heatMap;
    }

    private static boolean testConditions(ArrayList<Condition> conditions, long seed) {
        for (Condition condition : conditions) {
            if (!condition.test(seed))
                return false;
        }
        return true;
    }

    static public double[][] makeKernel(int blockThreshold) {
        int threshold = blockThreshold / 16;
        int maxDSq = blockThreshold * blockThreshold / 256;
        double[][] kernel = new double[threshold * 2+ 1][ threshold * 2 + 1];
        for (int i = 0; i < threshold * 2 + 1; i++) for (int j = 0; j < threshold * 2 + 1; j++) {
            if ((i - threshold) * (i - threshold) + (j - threshold) * (j - threshold) <= maxDSq) {
                kernel[i][j] = 1;
            } else {
                kernel[i][j] = 0;
            }
        }
        return kernel;
    }

    public static double[][] getStartDistributions(ArrayList<Condition> conds, int sampleSize, int maxNumSeeds, boolean useAllThreeStrongholds) {
        double[][] heatmap = useAllThreeStrongholds ? computeHeatMapAllStrongholds(sampleSize, conds, maxNumSeeds) : computeHeatMap(sampleSize, conds, maxNumSeeds);
        //TODO biome pushing is directional, make sure heatmap is correct orientation to do this!!
        return FFTHelper.convolve(heatmap, StrongholdHelper.getBiomePushDist(), true);
    }

    public static double[][] getPlayerPreferences(double[][] heatmap, int threshold) {
        return FFTHelper.convolve(heatmap, makeKernel(threshold), true);
    }

    private static Color computeGradient(double max, double pixel) {
        int numColors = 256;
        int divisor = 256 / numColors;
        int intensity = (int) (255 * pixel / max) / divisor * divisor;
        return new Color(intensity,intensity,intensity);
    }

    public static BufferedImage getHeatMapAsImage(double[][] weights) {
        BufferedImage image = new BufferedImage(weights.length, weights[0].length, Image.SCALE_DEFAULT);
        double max = 0;
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[0].length; j++) {
                max = Math.max(weights[i][j], max);
            }
        }

        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[0].length; j++) {
                Color grayscale = computeGradient(max, weights[i][j]);
                image.setRGB(i, j, grayscale.getRGB());
            }
        }
        return image;
    }

}
