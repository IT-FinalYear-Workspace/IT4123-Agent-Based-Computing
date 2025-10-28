

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import java.util.Random;

public class VirusAgent extends Agent {
    private String myName;
    private SimulationEnvironment env;
    private Random rnd = new Random();

    protected void setup() {
        myName = getArguments()[0].toString();
        env = SimulationEnvironment.getInstance();
        env.addAgent(myName, "VIRUS");

        addBehaviour(new TickerBehaviour(this, 800) {
            protected void onTick() {
                env.moveRandom(myName, 2);
                String target = env.pickRandomByRole("HEALTHY");
                if (target != null) {
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.addReceiver(new AID(target, AID.ISLOCALNAME));
                    msg.setContent("INFECT");
                    send(msg);
                }
            }
        });

        addBehaviour(new WakerBehaviour(this, 20000 + rnd.nextInt(20000)) {
            protected void onWake() { doDelete(); }
        });
    }

    protected void takeDown() { env.removeAgent(myName); }
}
