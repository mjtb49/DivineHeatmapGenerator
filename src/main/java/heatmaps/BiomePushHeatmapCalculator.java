package heatmaps;

import kaptainwutax.biomeutils.source.OverworldBiomeSource;
import kaptainwutax.featureutils.structure.Stronghold;
import kaptainwutax.mcutils.rand.ChunkRand;
import kaptainwutax.mcutils.util.pos.CPos;
import kaptainwutax.mcutils.version.MCVersion;

import java.util.Random;

public class BiomePushHeatmapCalculator {
    public static final int SIZE = 15;

    public static double[][] getBiomePushHeatmap(int sampleSize, MCVersion mcVersion) {
        double[][] heatmap = new double[SIZE][SIZE];

        Stronghold stronghold = new Stronghold(mcVersion);
        for (int i = 0; i < sampleSize; i++) {
                long seed = new Random().nextLong();

                CPos pos = stronghold.getStarts(new OverworldBiomeSource(mcVersion, seed), 1, new ChunkRand())[0];
                CPos pos2 = StrongholdHelper.getFirstStartNoBiomes(seed);

                heatmap[pos.getX() - pos2.getX() + 7][pos.getZ() - pos2.getZ() + 7] += 1/(double) sampleSize;
        }

        return heatmap;
    }
}
