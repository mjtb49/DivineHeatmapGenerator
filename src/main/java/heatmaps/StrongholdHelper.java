package heatmaps;

import kaptainwutax.biomeutils.source.OverworldBiomeSource;
import kaptainwutax.mcutils.rand.ChunkRand;
import kaptainwutax.mcutils.util.pos.CPos;
import kaptainwutax.mcutils.version.MCVersion;

import java.util.Random;

public class StrongholdHelper {

    //experimentally generated with BiomePushHeatmapCalculator and 100k strongholds. TODO: use bigger sample size, normalize
    private static final double[][] biomePushDist = new double[][]{
            {0.00225, 0.00343, 0.00283, 0.00295, 0.00304, 0.00264, 0.00295, 0.00268, 0.00242, 0.00274, 0.00276, 0.0029, 0.00289, 0.00344, 0.00313},
            {0.00383, 0.00563, 0.00547, 0.00509, 0.00459, 0.00472, 0.00483, 0.0046, 0.00448, 0.00463, 0.00482, 0.00508, 0.00468, 0.00548, 0.00534},
            {0.00297, 0.0055, 0.00473, 0.00456, 0.00428, 0.00421, 0.00443, 0.00467, 0.00432, 0.00432, 0.0041, 0.00454, 0.00461, 0.00499, 0.00434},
            {0.0026, 0.0048, 0.00468, 0.00454, 0.00435, 0.00402, 0.00389, 0.00406, 0.0043, 0.0042, 0.0045, 0.00476, 0.00459, 0.00522, 0.00381},
            {0.0026, 0.00487, 0.00471, 0.00428, 0.00424, 0.00405, 0.00409, 0.00385, 0.00419, 0.00399, 0.00407, 0.00403, 0.00427, 0.00504, 0.00403},
            {0.00272, 0.00465, 0.00419, 0.00396, 0.00396, 0.0043, 0.00358, 0.00402, 0.00386, 0.00391, 0.00395, 0.00419, 0.00418, 0.00451, 0.00406},
            {0.00262, 0.00475, 0.00483, 0.00388, 0.0039, 0.00386, 0.00415, 0.00378, 0.0039, 0.00394, 0.00407, 0.00422, 0.00408, 0.00443, 0.00395},
            {0.00305, 0.00466, 0.00412, 0.00417, 0.0039, 0.0039, 0.00388, 0.0674, 0.00404, 0.00384, 0.00387, 0.00409, 0.00398, 0.00467, 0.00377},
            {0.00271, 0.00463, 0.00404, 0.00416, 0.00393, 0.00361, 0.00416, 0.00388, 0.00391, 0.00403, 0.00379, 0.00392, 0.00418, 0.00449, 0.00374},
            {0.00266, 0.00486, 0.00466, 0.0041, 0.00409, 0.00419, 0.00428, 0.00387, 0.00391, 0.00363, 0.00427, 0.00426, 0.00423, 0.00442, 0.00392},
            {0.00282, 0.00498, 0.00476, 0.00397, 0.00421, 0.00412, 0.00379, 0.00384, 0.00404, 0.00428, 0.00398, 0.00399, 0.00455, 0.00471, 0.00392},
            {0.00315, 0.00501, 0.00436, 0.00439, 0.00423, 0.00444, 0.00425, 0.00398, 0.00408, 0.00383, 0.00385, 0.00427, 0.00425, 0.00525, 0.00448},
            {0.00282, 0.00506, 0.00478, 0.00458, 0.00463, 0.00371, 0.00426, 0.00437, 0.0038, 0.00435, 0.00413, 0.00437, 0.00465, 0.00503, 0.00396},
            {0.00338, 0.00551, 0.00513, 0.00476, 0.00455, 0.00433, 0.00452, 0.00429, 0.00462, 0.00465, 0.00463, 0.00495, 0.00521, 0.00597, 0.00507},
            {0.00303, 0.00457, 0.00478, 0.00448, 0.00442, 0.0039, 0.00401, 0.00404, 0.00398, 0.00407, 0.00428, 0.00425, 0.00455, 0.00523, 0.00477}
    };

    public static CPos getFirstStartNoBiomes(long seed) {
        Random rand = new Random(seed);
        double angle = rand.nextDouble() * 3.141592653589793D * 2.0D;
        double distanceRing = 4.0D * (double)32 + (rand.nextDouble() - 0.5D) * (double)32 * 2.5D;
        int chunkX = (int)Math.round(Math.cos(angle) * distanceRing);
        int chunkZ = (int)Math.round(Math.sin(angle) * distanceRing);
        return new CPos(chunkX, chunkZ);
    }

    //TODO, higher fidelity distance calculations for next 2 strongholds>
    public static CPos[] getFirstThreeStartsNoBiomes(long seed) {
        Random rand = new Random(seed);
        CPos[] result = new CPos[3];
        double angle = rand.nextDouble() * 3.141592653589793D * 2.0D;
        double distanceRing = 4.0D * (double)32 + (rand.nextDouble() - 0.5D) * (double)32 * 2.5D;
        result[0] = new CPos((int)Math.round(Math.cos(angle) * distanceRing), (int)Math.round(Math.sin(angle) * distanceRing));
        angle += 6.283185307179586D / (double) 3;
        distanceRing = 4.0D * (double)32 + ((new Random()).nextDouble() - 0.5D) * (double)32 * 2.5D;
        result[1] = new CPos((int)Math.round(Math.cos(angle) * distanceRing), (int)Math.round(Math.sin(angle) * distanceRing));
        distanceRing = 4.0D * (double)32 + ((new Random()).nextDouble() - 0.5D) * (double)32 * 2.5D;
        angle += 6.283185307179586D / (double) 3;
        result[2] = new CPos((int)Math.round(Math.cos(angle) * distanceRing), (int)Math.round(Math.sin(angle) * distanceRing));
        return result;
    }



    public static double[][] getBiomePushDist() {
        return biomePushDist;
    }

    //TODO unused and crashes due to off by 1 error
    private static double[][] convolveWithBiomeTable(double[][] startLocationDist) {
        double[][] retVal = new double[startLocationDist.length][startLocationDist[0].length];
        for (int i = 0; i < startLocationDist.length; i++)
            for (int j = 0; j < startLocationDist[0].length; j++) {
                for (int x = 0; x < biomePushDist.length; x++)
                    for (int z = 0; z < biomePushDist.length; z++) {
                        if (i + x - 7 >= 0 && i + x - 7 < startLocationDist.length &&
                                i + z - 7 >= 0 && i + z - 7 < startLocationDist[0].length) {
                            double val = biomePushDist[x][z] * startLocationDist[i][j];
                            retVal[i + x - 7][j + x - 7] += val;
                        }
                    }
            }
        return retVal;
    }
}
