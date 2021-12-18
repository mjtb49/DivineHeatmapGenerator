package heatmaps;

import Conditions.Condition;
import kaptainwutax.mcutils.util.pos.CPos;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class DivineHeatmapCalculator {

    final static int SIZE = 168 + 7;
    final static int SIDE_LENGTH = SIZE * 2 + 1;


    public static double[][] computeHeatMap(int sampleSize, ArrayList<Condition> conditions) {
        double[][] heatMap = new double[SIDE_LENGTH][SIDE_LENGTH];
        for (int i = 0; i < sampleSize;) {
            long seed = new Random().nextLong();
            if (testConditions(conditions, seed)) {
                i++;
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
        double[][] heatMap = new double[SIDE_LENGTH][SIDE_LENGTH];
        int total = 0;
        int successes = 0;
        while (successes < sampleSize && total < maxNumSeeds && !(total > 1000000 && successes == 0)) {
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

    public static double[][] convolveWithArray(double[][] data, double[][] kernel) {
        double[][] result =  new double[data.length][data[0].length];
        if (kernel.length % 2 == 0 || kernel[0].length % 2 == 0)
            System.err.println("Kernel not of odd width");
        int xCenter = kernel.length / 2;
        int zCenter = kernel[0].length / 2;
        for (int dRow = 0; dRow < data.length; dRow++) {
            for (int dCol = 0; dCol < data[0].length; dCol++) {
                for (int kRow = 0; kRow < kernel.length; kRow++) {
                    for (int kCol = 0; kCol < kernel[0].length; kCol++) {
                        int x = dRow + kRow - xCenter;
                        int z = dCol + kCol - zCenter;
                        if (x >= 0 && x < data.length && z >= 0 && z < data[0].length)
                            result[x][z] += kernel[kRow][kCol] * data[dRow][dCol];
                    }
                }
            }
        }
        return result;
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

    public static double[][] getSuccessProbabilityOfLocations(int blockThreshold, ArrayList<Condition> conds, int sampleSize, int maxNumSeeds) {
        double[][] heatmap = computeHeatMapAllStrongholds(sampleSize, conds, maxNumSeeds);
        double[][] kernel = makeKernel(blockThreshold);
        //TODO biome pushing is directional, make sure heatmap is correct orientation to do this!!
        heatmap = convolveWithArray(heatmap, StrongholdHelper.getBiomePushDist());
        return convolveWithArray(heatmap, kernel);
    }

    private static Color computeGradient(double max, double pixel) {
        int numColors = 8;
        int divisor = 256 / numColors;
        int intensity = (int) (255 * pixel / max) / divisor * divisor + 15;
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
