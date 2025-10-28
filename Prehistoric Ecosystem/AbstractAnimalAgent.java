package ecosystem;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractAnimalAgent extends Agent {
    protected int x, y;
    protected int maxHealth;
    protected int health;
    protected int thirst;
    protected final int maxThirst = 100;
    protected int gen = 1;
    protected String status = "NORMAL"; // or "SICK"
    protected int speed;
    protected Random random = new Random();

    // World bounds
    protected final int MIN_X = 25;
    protected final int MAX_X = 775;
    protected final int MIN_Y = 25;
    protected final int MAX_Y = 575;

    protected String speciesName;

    protected boolean thirsty = false;

    protected List<Point> waterSources = new ArrayList<>();

    @Override
    protected void setup() {
        // subclasses must call setupAnimal() in their setup()
    }

    protected void setupAnimal(String species, int startX, int startY, int maxHealth, int speed) {
        this.speciesName = species;
        this.x = startX;
        this.y = startY;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.speed = speed;
        this.thirst = 0;

        // Listen for WATERLIST replies
        addBehaviour(new jade.core.behaviours.CyclicBehaviour(this) {
            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage msg = receive(mt);
                if (msg != null) {
                    String content = msg.getContent();
                    if (content.startsWith("WATERLIST")) {
                        // Parse water sources
                        waterSources.clear();
                        String[] parts = content.split(":");
                        for (int i = 1; i < parts.length; i++) {
                            String[] coords = parts[i].split(",");
                            try {
                                int wx = Integer.parseInt(coords[0]);
                                int wy = Integer.parseInt(coords[1]);
                                waterSources.add(new Point(wx, wy));
                            } catch (NumberFormatException e) {
                                // ignore
                            }
                        }
                    }
                } else {
                    block();
                }
            }
        });

        addBehaviour(new TickerBehaviour(this, 500) {
            @Override
            protected void onTick() {
                updateThirst();
                move();
                sendStatus();
                checkDeath();
            }
        });

        // Periodically request water sources from GUI
        addBehaviour(new TickerBehaviour(this, 10000) {
            @Override
            protected void onTick() {
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                msg.addReceiver(new jade.core.AID("ecosystemgui", jade.core.AID.ISLOCALNAME));
                msg.setContent("WATER?");
                send(msg);
            }
        });
    }

    private void updateThirst() {
        thirst += 1;
        if (thirst > maxThirst) {
            thirst = maxThirst;
        }
        thirsty = thirst > 50;
        if (thirst > 80) {
            health -= 1;
            if (health < 0) health = 0;
        }
    }

    private void move() {
        // If thirsty, move towards nearest water
        if (thirsty && !waterSources.isEmpty()) {
            Point nearest = getNearestWater();
            moveTowards(nearest.x, nearest.y);
            if (distanceTo(nearest.x, nearest.y) < 10) {
                drink();
            }
        } else {
            randomMove();
        }
    }

    private void moveTowards(int tx, int ty) {
        int dx = tx - x;
        int dy = ty - y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist == 0) return;

        int nx = x + (int) ((dx / dist) * speed);
        int ny = y + (int) ((dy / dist) * speed);

        x = clamp(nx, MIN_X, MAX_X);
        y = clamp(ny, MIN_Y, MAX_Y);
    }

    private void randomMove() {
        int nx = x + random.nextInt(speed * 2 + 1) - speed;
        int ny = y + random.nextInt(speed * 2 + 1) - speed;
        x = clamp(nx, MIN_X, MAX_X);
        y = clamp(ny, MIN_Y, MAX_Y);
    }

    private Point getNearestWater() {
        Point nearest = waterSources.get(0);
        double minDist = distanceTo(nearest.x, nearest.y);
        for (Point p : waterSources) {
            double dist = distanceTo(p.x, p.y);
            if (dist < minDist) {
                nearest = p;
                minDist = dist;
            }
        }
        return nearest;
    }

    protected double distanceTo(int ox, int oy) {
        int dx = ox - x;
        int dy = oy - y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    protected int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    protected void drink() {
        thirst -= 20;
        if (thirst < 0) thirst = 0;
        health += 5;
        if (health > maxHealth) health = maxHealth;
    }

protected void sendStatus() {
    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
    msg.addReceiver(new jade.core.AID("ecosystemgui", jade.core.AID.ISLOCALNAME));
    msg.setContent(String.format("UPDATE:%d:%d:%s:%d:%d:%s:%d:%d",
            x, y, speciesName, health, maxHealth, status, gen, thirst));
    send(msg);
}

    protected void checkDeath() {
        if (health <= 0) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new jade.core.AID("ecosystemgui", jade.core.AID.ISLOCALNAME));
            msg.setContent("REMOVE");
            send(msg);
            doDelete();
        }
    }
}
