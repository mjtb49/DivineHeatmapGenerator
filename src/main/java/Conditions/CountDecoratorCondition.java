package Conditions;

import java.util.ArrayList;

public class CountDecoratorCondition extends DecoratorCondition {
    private final int count;

    public CountDecoratorCondition(long salt, int count, ArrayList<Long> lowerSeedBounds, ArrayList<Long> upperSeedBounds) {
        super(salt, lowerSeedBounds, upperSeedBounds);
        this.count = count;
    }

    @Override
    public boolean test(long seed) {
        long internalSeed = (seed + salt) ^ 0x5deece66dL;
        for (int n = 0; n < count; n++) {
            boolean passedNthCheck = true;
            for (int i = 0; i < lbs.size(); i++) {
                internalSeed = lcg(internalSeed);
                if (!(lbs.get(i) <= internalSeed && internalSeed < ubs.get(i)))
                    passedNthCheck = false;
            }
            if (passedNthCheck)
                return true;
        }
        return false;
    }
}
