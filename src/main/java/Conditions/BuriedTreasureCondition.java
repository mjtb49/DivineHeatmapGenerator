package Conditions;

public class BuriedTreasureCondition extends ChanceDecoratorCondition {
    private final static long treasureSalt = 10387320L;
    public BuriedTreasureCondition(int x, int z) {
        super(getRegionSalt(x,z), 0.01f);
    }

    private static long getRegionSalt(int x, int z) {
        return  (long)x * 341873128712L + (long)z * 132897987541L + treasureSalt;
    }
}
