package ecosystem.agents;

import ecosystem.Animal;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.awt.Color;
import java.util.List;
import java.util.Random;

public class DeerAgent extends Agent {

    private Animal deer;
    private Random rand = new Random();

    @Override
    protected void setup() {
        System.out.println(getLocalName() + " started.");

        deer = new Animal(
            "Deer",
            rand.nextInt(800),
            rand.nextInt(600),
            Color.MAGENTA,
            "Deer"
        ) {
            @Override
            public void behave() {
                GuiAgent.GuiPanel panel = GuiAgent.getPanel();
                if (panel == null) return;

                List<Animal> animals = panel.getAnimals();

                // Flee from nearby predators (Lion)
                for (Animal a : animals) {
                    if (a == this || a.getType().equals("Lion")) continue;

                    double distance = Math.hypot(
                        a.getX() - getX(),
                        a.getY() - getY()
                    );
                    if (a.getType().equals("Lion") && distance < 100) {
                        int dx = Integer.compare(getX(), a.getX());
                        int dy = Integer.compare(getY(), a.getY());
                        move(dx * 5, dy * 5);
                        return;
                    }
                }

                // Random wandering
                move(rand.nextInt(5) - 2, rand.nextInt(5) - 2);

                // Eat grass if nearby
                panel
                    .getGrasses()
                    .forEach(gr -> {
                        double d = Math.hypot(
                            gr.getX() - getX(),
                            gr.getY() - getY()
                        );
                        if (d < 30) eatGrass();
                    });

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

                rest(); // regain energy

                if (getEnergy() <= 0 || getHunger() >= 100) setAlive(false);
            }
        };

        GuiAgent.GuiPanel panel = GuiAgent.getPanel();
        if (panel != null) panel.addAnimal(deer);

        addBehaviour(
            new TickerBehaviour(this, 200) {
                @Override
                protected void onTick() {
                    if (deer.isAlive()) {
                        deer.behave();
                        if (panel != null) panel.repaint();
                    }
                }
            }
        );
    }
}
