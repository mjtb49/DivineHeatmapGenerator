package panels;

import Conditions.Condition;
import Conditions.ConditionGroup;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelGroup extends JPanel implements Panel {

    private final Panel[] panels;

    public PanelGroup(LayoutManager layoutManager, Panel... panels) {
        this.panels = panels;
        this.setLayout(layoutManager);
        for (Panel panel : panels) {
            this.add((JPanel) panel);
        }
    }

    @Override
    public void reset() {
        for (Panel panel : panels) {
            panel.reset();
        }
    }

    @Override
    public Condition getCondition() {
        ArrayList<Condition> conds = new ArrayList<>();
        for (Panel panel : panels) {
            conds.add(panel.getCondition());
            this.add((JPanel) panel);
        }
        return new ConditionGroup(conds);
    }
}
