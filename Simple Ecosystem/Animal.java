package ecosystem;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Animal {

    protected String name;
    protected int x, y;
    protected Color color;
    protected boolean alive = true;
    protected int energy = 100;
    protected int hunger = 0;
    protected String type;

    public Animal(String name, int x, int y, Color color, String type) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.color = color;
        this.type = type;
    }

    public abstract void behave();

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > 800) x = 800;
        if (y > 600) y = 600;
        energy -= 1;
        hunger += 1;
    }

    public void eatGrass() {
        energy += 5;
        hunger -= 5;
        if (hunger < 0) hunger = 0;
    }

    public void drinkWater() {
        energy += 5;
    }

    public void eatPrey() {
        energy += 20;
        hunger -= 20;
        if (hunger < 0) hunger = 0;
    }

    public void rest() {
        energy += 2;
        hunger += 1;
        if (energy > 100) energy = 100;
    }

    public void draw(Graphics g) {
        if (!alive) return;
        g.setColor(color);
        g.fillOval(x - 5, y - 5, 10, 10);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getEnergy() {
        return energy;
    }

    public int getHunger() {
        return hunger;
    }

    public String getType() {
        return type;
    }
}
