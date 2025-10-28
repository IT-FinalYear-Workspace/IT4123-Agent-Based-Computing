
import java.awt.*;
import javax.swing.*;
import java.util.Map;

public class GridPanel extends JPanel {
    private final SimulationEnvironment env;

    public GridPanel(SimulationEnvironment env) { this.env = env; }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int gw = env.getWidth(), gh = env.getHeight();
        int w = getWidth(), h = getHeight();
        double cellW = (double) w / gw, cellH = (double) h / gh;

        g2.setColor(Color.WHITE); g2.fillRect(0, 0, w, h);

        Map<String, Point> pos = env.snapshotPositions();
        Map<String, String> roles = env.snapshotRoles();

        for (Map.Entry<String, Point> e : pos.entrySet()) {
            Point p = e.getValue();
            String role = roles.get(e.getKey());
            int x = (int)((p.x + 0.5) * cellW);
            int y = (int)((p.y + 0.5) * cellH);

            switch(role) {
                case "HEALTHY": g2.setColor(new Color(50,150,50)); break;
                case "INFECTED": g2.setColor(new Color(200,40,40)); break;
                case "VIRUS": g2.setColor(new Color(50,100,200)); break;
                case "IMMUNE": g2.setColor(new Color(240,140,20)); break;
                default: g2.setColor(Color.GRAY);
            }
            g2.fillOval(x-5, y-5, 10, 10);
        }
    }
}
