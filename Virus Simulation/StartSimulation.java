import jade.core.Runtime;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import javax.swing.*;

public class StartSimulation {
    public static final int GRID_W = 30;
    public static final int GRID_H = 20;

    public static void main(String[] args) throws Exception {
        SimulationEnvironment env = SimulationEnvironment.getInstance();
        env.initGrid(GRID_W, GRID_H);

        Runtime rt = Runtime.instance();
        ProfileImpl p = new ProfileImpl();
        p.setParameter(ProfileImpl.GUI, "true"); // show JADE RMA GUI
        ContainerController container = rt.createMainContainer(p);

        // Launch GUI
        SwingUtilities.invokeLater(() -> {
            SimulationFrame frame = new SimulationFrame(env);
            frame.setVisible(true);
        });

        // Spawn Healthy cells
        for (int i = 0; i < 80; i++) {
            AgentController ac = container.createNewAgent("Healthy-" + i,
                    HealthyCellAgent.class.getName(),
                    new Object[]{"Healthy-" + i});
            ac.start();
        }

        // Spawn Virus
        for (int i = 0; i < 6; i++) {
            AgentController ac = container.createNewAgent("Virus-" + i,
                    VirusAgent.class.getName(),
                    new Object[]{"Virus-" + i});
            ac.start();
        }

        // Spawn Immune cells
        for (int i = 0; i < 6; i++) {
            AgentController ac = container.createNewAgent("Immune-" + i,
                    ImmuneAgent.class.getName(),
                    new Object[]{"Immune-" + i});
            ac.start();
        }
    }
}
