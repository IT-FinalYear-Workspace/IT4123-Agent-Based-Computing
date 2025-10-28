package ecosystem;

import java.awt.Color;
import java.awt.Graphics;

public class Grass {

    private int x, y;

    public Grass(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x - 5, y - 5, 10, 10);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
