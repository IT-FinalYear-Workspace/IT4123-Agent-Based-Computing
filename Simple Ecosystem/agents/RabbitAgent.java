package ecosystem.agents;

import ecosystem.Animal;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.awt.Color;
import java.util.List;
import java.util.Random;

public class RabbitAgent extends Agent {

    private Animal rabbit;
    private Random rand = new Random();

    @Override
    protected void setup() {
        System.out.println(getLocalName() + " started.");

        rabbit = new Animal(
            "Rabbit",
            rand.nextInt(800),
            rand.nextInt(600),
            Color.PINK,
            "Rabbit"
        ) {
            @Override
            public void behave() {
                GuiAgent.GuiPanel panel = GuiAgent.getPanel();
                if (panel == null) return;

                List<Animal> animals = panel.getAnimals();

                // Flee from predators
                for (Animal a : animals) {
                    if (a == this || a.getType().equals("Lion")) continue;

                    double distance = Math.hypot(
                        a.getX() - getX(),
                        a.getY() - getY()
                    );
                    if (a.getType().equals("Lion") && distance < 120) {
                        int dx = Integer.compare(getX(), a.getX());
                        int dy = Integer.compare(getY(), a.getY());
                        move(dx * 6, dy * 6);
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
        if (panel != null) panel.addAnimal(rabbit);

        addBehaviour(
            new TickerBehaviour(this, 200) {
                @Override
                protected void onTick() {
                    if (rabbit.isAlive()) {
                        rabbit.behave();
                        if (panel != null) panel.repaint();
                    }
                }
            }
        );
    }
}
