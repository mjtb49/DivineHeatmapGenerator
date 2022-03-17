package panels;

import Conditions.ChanceDecoratorCondition;
import Conditions.Condition;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AnimalPanel extends JPanel implements Panel {

    private final JTextField numAnimals;
    private final JRadioButton animal_none;
    private final JRadioButton biome_other;
    private Animal animal;
    private Biome biome;

    private enum Animal {
        STRIDER(1,2),
        RABBIT(2,3),
        POLAR_BEAR(1,2),
        JUNGLE_PARROT(1,2), //this is 1 1 in jungle hills
        PARROT(1,1),
        TURTLES(2,5),
        TUNDRA_OTHER,
        TUNDRA_RABBIT(2,3),
        TUNDRA_BEAR(1,2),
        OTHER,
        NONE(0,0);

        int range;
        int min;
        int max;
        Animal(int min, int max) {
            this.range = max - min + 1;
            this.min = min;
            this.max = max;
        }

        Animal() {
            this(1,1);
        }
    }

    private enum Biome {
        TUNDRA(0.07f),
        OTHER(0.1f),
        JUNGLE_NO_HILLS(0.1f);

        float chance;
        Biome(float chance) {
            this.chance = chance;
        }
    }

    public AnimalPanel() {
        animal = Animal.NONE;
        biome = Biome.OTHER;
        ButtonGroup animals = new ButtonGroup();
        ButtonGroup biomes = new ButtonGroup();

        JRadioButton none = new JRadioButton("None");
        JRadioButton strider = new JRadioButton("Strider");
        JRadioButton rabbit = new JRadioButton("Rabbit");
        JRadioButton bears = new JRadioButton("Bear");
        JRadioButton parrots = new JRadioButton("Parrot");
        JRadioButton turtles = new JRadioButton("Turtle");
        JRadioButton other = new JRadioButton("Other");

        animals.add(none);
        animals.add(strider);
        animals.add(rabbit);
        animals.add(bears);
        animals.add(parrots);
        animals.add(turtles);
        animals.add(other);

        JPanel animalSelector = new JPanel(new GridLayout(7,1));
        animalSelector.add(none);
        animalSelector.add(strider);
        animalSelector.add(rabbit);
        animalSelector.add(bears);
        animalSelector.add(parrots);
        animalSelector.add(turtles);
        animalSelector.add(other);

        other.addActionListener(e -> {
            if (biome == Biome.TUNDRA)
                animal = Animal.TUNDRA_OTHER;
            else animal = Animal.OTHER;
        });

        turtles.addActionListener(e -> {
            animal = Animal.TURTLES;
        });

        parrots.addActionListener(e -> {
            if (biome == Biome.JUNGLE_NO_HILLS) animal = Animal.JUNGLE_PARROT;
            else animal = Animal.PARROT;
        });

        rabbit.addActionListener(e -> {
            if (biome == Biome.TUNDRA) animal = Animal.TUNDRA_RABBIT;
            else animal = Animal.RABBIT;
        });

        bears.addActionListener(e -> {
            if (biome == Biome.TUNDRA) animal = Animal.TUNDRA_BEAR;
            else animal = Animal.POLAR_BEAR;
        });

        none.addActionListener(e -> {
            animal = Animal.NONE;
        });

        strider.addActionListener(e -> {
            animal = Animal.STRIDER;
        });

        JRadioButton tundra = new JRadioButton("Tundra");
        JRadioButton other_biome = new JRadioButton("Other");
        JRadioButton jungle = new JRadioButton("Jungle No Hills");

        biomes.add(tundra);
        biomes.add(other_biome);
        biomes.add(jungle);

        tundra.addActionListener(e -> {
            this.biome = Biome.TUNDRA;

            if (animal == Animal.RABBIT)
                animal = Animal.TUNDRA_RABBIT;
            else if (animal == Animal.POLAR_BEAR)
                animal = Animal.TUNDRA_BEAR;
            else if (animal == Animal.OTHER)
                animal = Animal.TUNDRA_OTHER;
            else if (animal == Animal.JUNGLE_PARROT)
                animal = Animal.PARROT;
        });

        other_biome.addActionListener(e -> {
            this.biome = Biome.OTHER;

            if (animal == Animal.TUNDRA_RABBIT)
                animal = Animal.RABBIT;
            else if (animal == Animal.TUNDRA_BEAR)
                animal = Animal.POLAR_BEAR;
            else if (animal == Animal.TUNDRA_OTHER)
                animal = Animal.OTHER;
            else if (animal == Animal.JUNGLE_PARROT)
                animal = Animal.PARROT;
        });

        jungle.addActionListener(e -> {
            this.biome = Biome.JUNGLE_NO_HILLS;

            if (animal == Animal.TUNDRA_RABBIT)
                animal = Animal.RABBIT;
            else if (animal == Animal.TUNDRA_BEAR)
                animal = Animal.POLAR_BEAR;
            else if (animal == Animal.TUNDRA_OTHER)
                animal = Animal.OTHER;
            else if (animal == Animal.PARROT)
                animal = Animal.JUNGLE_PARROT;
        });

        JPanel biomePanel = new JPanel(new GridLayout(4,1));
        biomePanel.add(tundra);
        biomePanel.add(other_biome);
        biomePanel.add(jungle);

        numAnimals = new JTextField("# animals");
        Panel.deleteTextOnSelection(numAnimals);
        biomePanel.add(numAnimals);
        this.add(animalSelector);
        this.add(biomePanel);

        biome_other = other_biome;
        animal_none = none;
    }

    @Override
    public void reset() {
        biome = Biome.OTHER;
        animal = Animal.NONE;

        animal_none.setSelected(true);
        biome_other.setSelected(true);
        numAnimals.setText("# animals");
        Panel.deleteTextOnSelection(numAnimals);
    }

    @Override
    public Condition getCondition(boolean crossReference) {
        if (animal == Animal.NONE)
            return null;

        float chance = biome.chance;

        ArrayList<Long> lbs = new ArrayList<>();
        ArrayList<Long> ubs = new ArrayList<>();

        int val = Panel.parseIntFromTextField(numAnimals, animal.min, animal.max + 1);
        if (val != Panel.FAIL && animal.range > 1) {
            long step = (1L << 48) / animal.range;
            lbs.add(0L);
            ubs.add(1L << 48);
            lbs.add(step * (val - animal.min));
            ubs.add(step * (val - animal.min + 1));
        }

        return new ChanceDecoratorCondition(0,chance, lbs, ubs);
    }
}
