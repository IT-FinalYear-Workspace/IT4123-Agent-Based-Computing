package ecosystem.agents;

import ecosystem.Animal;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.awt.Color;
import java.util.List;
import java.util.Random;

public class LionAgent extends Agent {

    private Animal lion;
    private Random rand = new Random();

    @Override
    protected void setup() {
        System.out.println(getLocalName() + " started.");

        lion = new Animal(
            "Lion",
            rand.nextInt(800),
            rand.nextInt(600),
            Color.ORANGE,
            "Lion"
        ) {
            @Override
            public void behave() {
                GuiAgent.GuiPanel panel = GuiAgent.getPanel();
                if (panel == null) return;

                List<Animal> animals = panel.getAnimals();
                Animal nearestPrey = null;
                double minDist = Double.MAX_VALUE;

                // Chase nearest prey
                for (Animal a : animals) {
                    if (
                        a == this || a.getType().equals("Lion") || !a.isAlive()
                    ) continue;

                    double d = Math.hypot(a.getX() - getX(), a.getY() - getY());
                    if (d < minDist) {
                        nearestPrey = a;
                        minDist = d;
                    }
                }

                if (nearestPrey != null) {
                    // Move toward prey
                    int dx = Integer.compare(nearestPrey.getX(), getX());
                    int dy = Integer.compare(nearestPrey.getY(), getY());
                    move(dx * 7, dy * 7);

                    // Eat if close
                    if (minDist < 20) {
                        nearestPrey.setAlive(false);
                        eatPrey();
                    }
                } else {
                    // Random wandering
                    move(rand.nextInt(5) - 2, rand.nextInt(5) - 2);
                }

                // Drink water if nearby
                panel
                    .getWaters()
                    .forEach(w -> {
                        double d = Math.hypot(
                            w.getX() - getX(),
                            w.getY() - getY()
                        );
                        if (d < 40) drinkWater();
                    });

                rest();

                if (getEnergy() <= 0) setAlive(false);
            }
        };

        GuiAgent.GuiPanel panel = GuiAgent.getPanel();
        if (panel != null) panel.addAnimal(lion);

        addBehaviour(
            new TickerBehaviour(this, 200) {
                @Override
                protected void onTick() {
                    if (lion.isAlive()) {
                        lion.behave();
                        if (panel != null) panel.repaint();
                    }
                }
            }
        );
    }
}
