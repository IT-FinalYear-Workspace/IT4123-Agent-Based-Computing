package ecosystem;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class EcosystemGUIAgent extends Agent {
    private JFrame frame;
    private DrawPanel panel;

    private WeatherType weather = WeatherType.SUNNY;
    private boolean isDay = true;
    private boolean drought = false;

    private int tickCounter = 0;

    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    private HashMap<String, AnimalInfo> animals = new HashMap<>();
    private java.util.List<Point> waterSources = new ArrayList<>();

    private Random random = new Random();

    @Override
    protected void setup() {
        
        initWaterSources();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame("Ecosystem Simulation");
                frame.setSize(WIDTH, HEIGHT);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                panel = new DrawPanel();
                frame.add(panel);
                frame.setVisible(true);
            }
        });

        addBehaviour(new TickerBehaviour(this, 100) {
            @Override
            protected void onTick() {
                tickCounter++;

                if (tickCounter % 300 == 0) { // every 30 seconds
                    changeWeatherRandomly();
                }

                if (tickCounter % 600 == 0) { // every 60 seconds toggle day/night
                    isDay = !isDay;
                }

                if (tickCounter % 1200 == 0) { // every 2 minutes maybe drought
                    drought = random.nextDouble() < 0.3;
                    if (drought) {
                        System.out.println("Drought started!");
                    } else {
                        System.out.println("Drought ended.");
                    }
                }

                ACLMessage msg;
                while ((msg = receive()) != null) {
                    processMessage(msg);
                }

                panel.repaint();
            }
        });
    }

    private void initWaterSources() {
        waterSources.clear();
        for (int i = 0; i < 3; i++) {
            int x = random.nextInt(WIDTH - 100) + 50;
            int y = random.nextInt(HEIGHT - 100) + 50;
            waterSources.add(new Point(x, y));
        }
    }

    private void changeWeatherRandomly() {
        int w = random.nextInt(4);
        switch (w) {
            case 0:
                weather = WeatherType.SUNNY;
                break;
            case 1:
                weather = WeatherType.RAIN;
                break;
            case 2:
                weather = WeatherType.STORM;
                break;
            case 3:
                weather = WeatherType.CLOUDY;
                break;
        }
        System.out.println("Weather changed to: " + weather);
    }

    private void processMessage(ACLMessage msg) {
        String sender = msg.getSender().getLocalName();
        String content = msg.getContent();

        if (content.startsWith("UPDATE:")) {
            // Format: UPDATE:x:y:type:health:maxHealth:status:gen:thirst
            String[] parts = content.split(":");
            if (parts.length >= 9) {
                try {
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    String type = parts[3];
                    int health = Integer.parseInt(parts[4]);
                    int maxHealth = Integer.parseInt(parts[5]);
                    String status = parts[6];
                    int gen = Integer.parseInt(parts[7]);
                    int thirst = Integer.parseInt(parts[8]);

                    animals.put(sender, new AnimalInfo(x, y, type, health, maxHealth, status, gen, thirst));
                } catch (NumberFormatException e) {
                    // ignore malformed updates
                }
            }
        } else if (content.equals("REMOVE")) {
            animals.remove(sender);
        } else if (content.equals("WATER?")) {
            // Respond with water sources coordinates as a message
            ACLMessage reply = msg.createReply();
            StringBuilder sb = new StringBuilder();
            sb.append("WATERLIST");
            for (Point p : waterSources) {
                sb.append(":").append(p.x).append(",").append(p.y);
            }
            reply.setContent(sb.toString());
            send(reply);
        }
    }

    private class DrawPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Background
            if (drought) {
                g.setColor(new Color(139, 69, 19)); // brown background drought
            } else if (!isDay) {
                g.setColor(new Color(10, 10, 60)); // dark blue night
            } else {
                switch (weather) {
                    case SUNNY:
                        g.setColor(new Color(135, 206, 235));
                        break;
                    case RAIN:
                        g.setColor(new Color(100, 149, 237));
                        break;
                    case STORM:
                        g.setColor(Color.DARK_GRAY);
                        break;
                    case CLOUDY:
                        g.setColor(new Color(176, 196, 222));
                        break;
                }
            }
            g.fillRect(0, 0, WIDTH, HEIGHT);

            // Draw water sources
            g.setColor(Color.BLUE);
            for (Point p : waterSources) {
                g.fillOval(p.x - 15, p.y - 15, 30, 30);
            }

            // Draw herding lines
            drawHerdLines(g);

            // Draw animals
            for (Map.Entry<String, AnimalInfo> entry : animals.entrySet()) {
                AnimalInfo a = entry.getValue();

                Color baseColor = Color.BLACK;
                if (a.type.equals("TREX")) {
                    baseColor = Color.RED;
                } else if (a.type.equals("BRONTOSAURUS")) {
                    baseColor = Color.GREEN;
                } else if (a.type.equals("TRICERATOPS")) {
                    baseColor = Color.BLUE;
                }

                if ("SICK".equals(a.status)) {
                    baseColor = Color.GRAY;
                }

                if (a.gen == 2) {
                    baseColor = baseColor.brighter();
                }

                if (a.thirst > 50) {
                    baseColor = baseColor.darker();
                }

                g.setColor(baseColor);

                int size = 20;
                if (a.gen == 2) {
                    size = 30;
                }

                g.fillOval(a.x - size / 2, a.y - size / 2, size, size);

                // Health bar
                int barWidth = 40;
                int barHeight = 5;
                int healthBarWidth = (int) ((double) a.health / a.maxHealth * barWidth);

                g.setColor(Color.RED);
                g.fillRect(a.x - barWidth / 2, a.y - size / 2 - 10, barWidth, barHeight);
                g.setColor(Color.GREEN);
                g.fillRect(a.x - barWidth / 2, a.y - size / 2 - 10, healthBarWidth, barHeight);
                g.setColor(Color.BLACK);
                g.drawRect(a.x - barWidth / 2, a.y - size / 2 - 10, barWidth, barHeight);

                // Labels
                StringBuilder label = new StringBuilder();
                if ("SICK".equals(a.status)) {
                    label.append("[SICK] ");
                }
                if (a.gen == 2) {
                    label.append("[Gen2] ");
                }
                if (a.thirst > 50) {
                    label.append("[THIRSTY] ");
                }

                g.setColor(Color.BLACK);
                g.drawString(label.toString().trim(), a.x - size / 2, a.y - size / 2 - 15);

                // TREX territory circle
                if (a.type.equals("TREX")) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(new Color(255, 0, 0, 50));
                    g2d.fillOval(a.x - 100, a.y - 100, 200, 200);
                }
            }

            // UI Text
            g.setColor(Color.BLACK);
            g.drawString("Weather: " + weather.name(), 10, 20);
            g.drawString("Time: " + (isDay ? "Day" : "Night"), 10, 40);
            g.drawString("Animals: " + animals.size(), 10, 60);

            if (drought) {
                g.setColor(Color.RED);
                g.drawString("DROUGHT ACTIVE!", WIDTH - 120, 20);
            }
        }

        private void drawHerdLines(Graphics g) {
            java.util.List<AnimalInfo> brontos = new ArrayList<>();
            java.util.List<AnimalInfo> tricers = new ArrayList<>();

            for (AnimalInfo a : animals.values()) {
                if ("BRONTOSAURUS".equals(a.type)) {
                    brontos.add(a);
                } else if ("TRICERATOPS".equals(a.type)) {
                    tricers.add(a);
                }
            }

            g.setColor(Color.WHITE);

            for (int i = 0; i < brontos.size(); i++) {
                for (int j = i + 1; j < brontos.size(); j++) {
                    if (distance(brontos.get(i), brontos.get(j)) < 100) {
                        g.drawLine(brontos.get(i).x, brontos.get(i).y, brontos.get(j).x, brontos.get(j).y);
                    }
                }
            }

            for (int i = 0; i < tricers.size(); i++) {
                for (int j = i + 1; j < tricers.size(); j++) {
                    if (distance(tricers.get(i), tricers.get(j)) < 100) {
                        g.drawLine(tricers.get(i).x, tricers.get(i).y, tricers.get(j).x, tricers.get(j).y);
                    }
                }
            }
        }

        private double distance(AnimalInfo a1, AnimalInfo a2) {
            int dx = a1.x - a2.x;
            int dy = a1.y - a2.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
    }

    private static class AnimalInfo {
        int x;
        int y;
        String type;
        int health;
        int maxHealth;
        String status;
        int gen;
        int thirst;

        public AnimalInfo(int x, int y, String type, int health, int maxHealth, String status, int gen, int thirst) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.health = health;
            this.maxHealth = maxHealth;
            this.status = status;
            this.gen = gen;
            this.thirst = thirst;
        }
    }
}
