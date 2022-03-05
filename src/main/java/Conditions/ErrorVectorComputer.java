package Conditions;

import java.util.*;

public class ErrorVectorComputer {

    final static long XOR = 0x5deece66dL;
    final static long MASK = (1L << 48) - 1;

    /**
     *
     * @param numCalls the number of calls to compute the error vectors out. Usually 3
     * @param salts the salts of the respective decorators we are looking for
     * @return The distribution of possible collections of error vectors based on the given salts
     * The keys in the map will be maps from salts to their error vector.
     * the values in the map will be the probability of that given collection of error vectors occurring
     */
    public static Map<Map<Long, Long[]>, Double> computeErrorVectors(int numCalls, ArrayList<Long> salts) {
        //(a + c)^m - a^m
        //salt is
        //10111011110111011001110011001101101
        return null;
    }

    public static void main(String[] args) {
        HashMap<Long, Integer> diffs = new HashMap<>();
        long salt = new BuriedTreasureCondition(123,23562).getSalt();
        long salt2 = new BuriedTreasureCondition(1723,71934).getSalt();
        int count = 0;
        for (int i = 0; i < 10000000; i++) {
            //long salt = new Random().nextLong() & MASK;
            long seed = new Random().nextLong() & (MASK);
            long key = (((seed + salt) ^ XOR) - (seed ^ XOR)) & MASK;
            long key2 = (((seed + salt2) ^ XOR) - (seed ^ XOR)) & MASK;
            diffs.put(key ^ (key2 * 213123211892331L), diffs.getOrDefault(key ^ (key2 * 213123211892331L), 0) + 1);
        }
        System.out.println(count);
        System.out.println(diffs.size());
        for (Long l : diffs.keySet()) {
            if (diffs.get(l)/1000000.0 > 0.05)
                System.out.println(l + " " + diffs.get(l)/1000000.0);
        }
    }
}
