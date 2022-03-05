package Conditions;


import java.util.ArrayList;
import java.util.Random;

public class DecoratorCondition implements Condition {

    protected final long salt;
    protected final ArrayList<Long> lbs;
    protected final ArrayList<Long> ubs;

    public DecoratorCondition(long salt, ArrayList<Long> lowerSeedBounds, ArrayList<Long> upperSeedBounds) {
        this.salt = salt;
        lbs = lowerSeedBounds;
        ubs = upperSeedBounds;
    }

    public DecoratorCondition(long salt, int xlb, int xub, int zlb, int zub) {
        this(salt, buildBoundsFromCoords(xlb, zlb), buildBoundsFromCoords(xub, zub));
    }

    public DecoratorCondition(long salt, int xlb, int xub, int ylb, int yub, int zlb, int zub) {
        this(salt, buildBoundsFrom3Coords(xlb,ylb, zlb), buildBoundsFrom3Coords(xub, yub, zub));
    }

    protected static long lcg(long seed) {
        seed *= 0x5deece66dL;
        seed += 11L;
        seed &= 0xffff_ffff_ffffL;
        return seed;
    }

    protected static ArrayList<Long> buildBoundsFromCoords(int xb, int zb) {
        ArrayList<Long> lowerBounds = new ArrayList<>();
        lowerBounds.add(((long)xb) << 44);
        //lowerBounds.add(((long)yb) << 44);
        lowerBounds.add(((long)zb) << 44);
        return lowerBounds;
    }

    protected static ArrayList<Long> buildBoundsFrom3Coords(int xb, int yb, int zb) {
        ArrayList<Long> lowerBounds = new ArrayList<>();
        lowerBounds.add(((long)xb) << 44);
        lowerBounds.add(((long)yb) << 44);
        lowerBounds.add(((long)zb) << 44);
        return lowerBounds;
    }

    @Override public double computeRarity() {
        double rarity = 1.0;
        for (int i = 0; i < lbs.size(); i++) {
            rarity *= (ubs.get(i) - lbs.get(i)) / (double) (1L << 48);
        }
        return rarity;
    }

    @Override
    public boolean test(long seed) {
        long internalSeed = (seed + salt) ^ 0x5deece66dL;
        for (int i = 0; i < lbs.size(); i++) {
            internalSeed = lcg(internalSeed);
            if (!(lbs.get(i) <= internalSeed && internalSeed < ubs.get(i)))
                return false;
        }
        return true;
    }

    public long getSalt() {
        return salt;
    }
}
