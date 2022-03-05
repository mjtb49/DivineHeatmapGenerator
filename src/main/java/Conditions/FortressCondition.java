package Conditions;

import java.util.Random;

public class FortressCondition implements Condition {

    final int regionX;
    final int regionZ;
    final int residueX;
    final int residueZ;

    public FortressCondition(int x, int z) {
        this.regionX = x >> 4;
        this.regionZ = z >> 4;
        this.residueX = (x - 4) & 0x7;
        this.residueZ = (z - 4) & 0x7;
    }
    @Override
    public boolean test(long seed) {
        Random r = new Random((long)(regionX ^ regionZ << 4) ^ seed);
        r.nextInt();
        r.nextInt(3);
        return r.nextInt(8) == residueX;
    }

    @Override
    public double computeRarity() {
        return 0.125;
    }
}
