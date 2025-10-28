package ecosystem;

import java.awt.Color;
import java.awt.Graphics;

public class Water {

    private int x, y;

    public Water(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x - 5, y - 5, 10, 10);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
