

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;


import java.util.Random;

public class HealthyCellAgent extends Agent {
    private String myName;
    private boolean infected = false;
    private SimulationEnvironment env;

    @Override
    protected void setup() {
        myName = getArguments()[0].toString();
        env = SimulationEnvironment.getInstance();
        env.addAgent(myName, "HEALTHY");

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg == null) { block(); return; }
                if ("INFECT".equals(msg.getContent()) && !infected) becomeInfected();
                else if ("KILL".equals(msg.getContent())) doDelete();
            }
        });

        addBehaviour(new TickerBehaviour(this, 800) {
            protected void onTick() { env.moveRandom(myName, 1); }
        });
    }

    private void becomeInfected() {
        infected = true;
        env.setRole(myName, "INFECTED");
        addBehaviour(new WakerBehaviour(this, 6000 + new Random().nextInt(8000)) {
            protected void onWake() { doDelete(); }
        });
    }

    protected void takeDown() { env.removeAgent(myName); }
}
