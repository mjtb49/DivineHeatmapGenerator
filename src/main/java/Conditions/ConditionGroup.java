package Conditions;

import java.util.ArrayList;

public class ConditionGroup implements Condition {

    private final ArrayList<Condition> conditions;

    public ConditionGroup(ArrayList<Condition> conditions) {
        this.conditions = new ArrayList<>();
        for (Condition condition : conditions)
            if (condition != null) this.conditions.add(condition);
    }

    @Override
    public boolean test(long seed) {
        for (Condition condition : conditions) {
            if (!condition.test(seed)) {
                return false;
            }
        }
        return true;
    }
}
