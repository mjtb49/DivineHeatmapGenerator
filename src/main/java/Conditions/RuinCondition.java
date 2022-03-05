package Conditions;

import java.util.ArrayList;

//165745295
public class RuinCondition extends DecoratorCondition {
    final static long ruinSalt = 14357621;
    final static long seedOffset = 1L << (48 - 3);

    public RuinCondition(int x, int z) {
        super(getRegionSalt(x,z),getLowerBounds(x,z), getUpperBounds(x,z));
    }

    private static ArrayList<Long> getLowerBounds(int x, int z) {
        //TODO add in a condition for z and add error handling
        ArrayList<Long> retval = new ArrayList<>();
        retval.add(seedOffset * (x & 0x7));
        return retval;
    }

    private static ArrayList<Long> getUpperBounds(int x, int z) {
        //TODO add in a condition for z and add error handling
        ArrayList<Long> retval = new ArrayList<>();
        retval.add((seedOffset) * (1 + (x & 0x7)) - 1);
        return retval;
    }

    private static long getRegionSalt(int x, int z) {
        x >>= 4;
        z >>= 4;
        return  (long)x * 341873128712L + (long)z * 132897987541L + ruinSalt;
    }
}
