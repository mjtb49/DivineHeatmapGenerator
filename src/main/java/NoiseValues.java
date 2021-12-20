import kaptainwutax.biomeutils.source.StaticNoiseSource;

public class NoiseValues {
    public static void main(String[] args) {
        int noiseToCountRatio = 160;
        double noiseFactor = 400.0D;
        double noiseOffset = 0.3D;

        System.out.println((int)Math.ceil((StaticNoiseSource.BIOME_INFO_NOISE.sample(0,0,false) + noiseOffset) * (double) noiseToCountRatio));
    }
}
