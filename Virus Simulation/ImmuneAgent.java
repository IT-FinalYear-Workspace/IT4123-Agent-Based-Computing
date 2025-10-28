
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import java.awt.Point;
import java.util.Random;

public class ImmuneAgent extends Agent {
    private String myName;
    private SimulationEnvironment env;
    private Random rnd = new Random();

    protected void setup() {
        myName = getArguments()[0].toString();
        env = SimulationEnvironment.getInstance();
        env.addAgent(myName, "IMMUNE");

        addBehaviour(new TickerBehaviour(this, 1000) {
            protected void onTick() {
                env.moveRandom(myName, 2);
                String infected = env.pickRandomByRole("INFECTED");
                if (infected != null) {
                    Point me = env.getPosition(myName);
                    Point t = env.getPosition(infected);
                    double dist = me.distance(t);
                    double prob = Math.max(0.2, 1.0 - dist/30.0);
                    if (rnd.nextDouble() < prob) {
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.addReceiver(new AID(infected, AID.ISLOCALNAME));
                        msg.setContent("KILL");
                        send(msg);
                    }
                }
            }
        });
    }

    protected void takeDown() { env.removeAgent(myName); }
}
