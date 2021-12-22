package Conditions;

public interface Condition {
    public boolean test(long seed);
    public double computeRarity();
}
