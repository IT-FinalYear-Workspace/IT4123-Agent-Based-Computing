package ecosystem.agents;

import ecosystem.Animal;
import ecosystem.Grass;
import ecosystem.Water;
import jade.core.Agent;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.*;

public class GuiAgent extends Agent {

    private static GuiPanel panel;

    public static GuiPanel getPanel() {
        return panel;
    }

    @Override
    protected void setup() {
        System.out.println(getLocalName() + " started.");

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Ecosystem Simulation");
            panel = new GuiPanel();
            frame.add(panel, BorderLayout.CENTER);

            JPanel buttons = new JPanel();
            JButton addDeer = new JButton("Add Deer");
            JButton addRabbit = new JButton("Add Rabbit");
            JButton addLion = new JButton("Add Lion");

            addDeer.addActionListener(e ->
                panel.addAnimal(
                    new ecosystem.Animal(
                        "Deer",
                        100,
                        100,
                        Color.MAGENTA,
                        "Deer"
                    ) {
                        @Override
                        public void behave() {}
                    }
                )
            );
            addRabbit.addActionListener(e ->
                panel.addAnimal(
                    new ecosystem.Animal(
                        "Rabbit",
                        200,
                        100,
                        Color.PINK,
                        "Rabbit"
                    ) {
                        @Override
                        public void behave() {}
                    }
                )
            );
            addLion.addActionListener(e ->
                panel.addAnimal(
                    new ecosystem.Animal(
                        "Lion",
                        300,
                        100,
                        Color.ORANGE,
                        "Lion"
                    ) {
                        @Override
                        public void behave() {}
                    }
                )
            );

            buttons.add(addDeer);
            buttons.add(addRabbit);
            buttons.add(addLion);

            frame.add(buttons, BorderLayout.SOUTH);
            frame.setSize(900, 700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    public static class GuiPanel extends JPanel {

        private final List<Animal> animals = new CopyOnWriteArrayList<>();
        private final List<Grass> grasses = new CopyOnWriteArrayList<>();
        private final List<Water> waters = new CopyOnWriteArrayList<>();

        public GuiPanel() {
            grasses.add(new Grass(100, 400));
            grasses.add(new Grass(200, 450));
            waters.add(new Water(600, 400));
            waters.add(new Water(500, 300));
        }

        public void addAnimal(Animal a) {
            animals.add(a);
        }

        public List<Animal> getAnimals() {
            return animals;
        }

        public List<Grass> getGrasses() {
            return grasses;
        }

        public List<Water> getWaters() {
            return waters;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            grasses.forEach(gr -> gr.draw(g));
            waters.forEach(w -> w.draw(g));
            animals.forEach(a -> a.draw(g));
        }
    }
}
