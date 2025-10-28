package agents;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.*;

public class StatsAgent extends Agent {
    protected void setup() {
        System.out.println("StatsAgent started.");

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String[] marks = msg.getContent().split(",");
                    int max = Integer.MIN_VALUE;
                    int min = Integer.MAX_VALUE;

                    for (String mark : marks) {
                        int val = Integer.parseInt(mark.trim());
                        if (val > max) max = val;
                        if (val < min) min = val;
                    }

                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent("Highest: " + max + ", Lowest: " + min);
                    send(reply);
                } else {
                    block();
                }
            }
        });
    }
}
