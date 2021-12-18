package Conditions;

import java.util.ArrayList;
import java.util.Random;

public class ChanceDecoratorCondition extends DecoratorCondition {

    protected float chance;

    public ChanceDecoratorCondition(long salt, float chance, ArrayList<Long> lowerSeedBounds, ArrayList<Long> upperSeedBounds) {
        super(salt, lowerSeedBounds, upperSeedBounds);
        this.chance = chance;
    }

    public ChanceDecoratorCondition(long salt, float chance, int xlb, int xub, int zlb, int zub) {
        this(salt, chance, buildBoundsFromCoords(xlb, zlb), buildBoundsFromCoords(xub, zub));
    }

    public ChanceDecoratorCondition(long salt, float chance, int x, int z) {
        this(salt, chance, buildBoundsFromCoords(x, z), buildBoundsFromCoords(x+1, z+1));
    }

    public ChanceDecoratorCondition(long salt, float chance, int z) {
        this(salt, chance, buildBoundsFromCoords(0, z), buildBoundsFromCoords(16, z+1));
    }

    public ChanceDecoratorCondition(long salt, float chance) {
        this(salt, chance, new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public boolean test(long seed) {
        long internalSeed = (seed + salt) ^ 0x5deece66dL;
        //simming a nextFloat
        internalSeed = lcg(internalSeed);
        if (new Random(seed + salt).nextFloat() > chance)
            return false;

        for (int i = 0; i < lbs.size(); i++) {
            internalSeed = lcg(internalSeed);

            if (!(lbs.get(i) <= internalSeed && internalSeed < ubs.get(i)))
                return false;
        }
        return true;
    }
}
