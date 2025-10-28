
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class SimulationEnvironment {
    private static SimulationEnvironment INSTANCE;

    private final Map<String, Point> positions = new ConcurrentHashMap<>();
    private final Map<String, String> roles = new ConcurrentHashMap<>();
    private int width = 30, height = 30;
    private final Random rnd = ThreadLocalRandom.current();

    private SimulationEnvironment() {}
    public static synchronized SimulationEnvironment getInstance() {
        if (INSTANCE == null) INSTANCE = new SimulationEnvironment();
        return INSTANCE;
    }

    public void initGrid(int w, int h) { width = w; height = h; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void addAgent(String name, String role) {
        roles.put(name, role);
        positions.put(name, new Point(rnd.nextInt(width), rnd.nextInt(height)));
    }
    public void removeAgent(String name) { roles.remove(name); positions.remove(name); }
    public void setRole(String name, String role) { roles.put(name, role); }
    public String getRole(String name) { return roles.get(name); }
    public Point getPosition(String name) { return positions.get(name); }
    public Map<String, Point> snapshotPositions() { return new HashMap<>(positions); }
    public Map<String, String> snapshotRoles() { return new HashMap<>(roles); }

    public void moveRandom(String name, int step) {
        Point p = positions.get(name);
        if (p == null) return;
        int nx = Math.max(0, Math.min(width-1, p.x + rnd.nextInt(2*step+1)-step));
        int ny = Math.max(0, Math.min(height-1, p.y + rnd.nextInt(2*step+1)-step));
        positions.put(name, new Point(nx, ny));
    }

    public String pickRandomByRole(String role) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String,String> e : roles.entrySet())
            if (role.equals(e.getValue())) list.add(e.getKey());
        if (list.isEmpty()) return null;
        return list.get(rnd.nextInt(list.size()));
    }

    public int countByRole(String role) {
        int c = 0; for (String r : roles.values()) if (role.equals(r)) c++;
        return c;
    }
}
