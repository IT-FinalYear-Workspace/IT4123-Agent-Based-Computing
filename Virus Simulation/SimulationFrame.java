

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SimulationFrame extends JFrame {
    private final SimulationEnvironment env;
    private final GridPanel gridPanel;
    private final Timer repaintTimer;

    public SimulationFrame(SimulationEnvironment env) {
        super("Virus–Host Simulation");
        this.env = env;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout());

        gridPanel = new GridPanel(env);
        add(gridPanel, BorderLayout.CENTER);

        JLabel counts = new JLabel();
        add(counts, BorderLayout.SOUTH);

        repaintTimer = new Timer(100, e -> {
            gridPanel.repaint();
            counts.setText(String.format("Healthy: %d | Infected: %d | Virus: %d | Immune: %d",
                    env.countByRole("HEALTHY"),
                    env.countByRole("INFECTED"),
                    env.countByRole("VIRUS"),
                    env.countByRole("IMMUNE")));
        });
        repaintTimer.start();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
    }
}
