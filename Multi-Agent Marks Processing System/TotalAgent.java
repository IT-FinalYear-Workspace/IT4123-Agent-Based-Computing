package agents;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.*;

public class TotalAgent extends Agent {
    protected void setup() {
        System.out.println("TotalAgent started.");

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String[] marks = msg.getContent().split(",");
                    int total = 0;
                    for (String mark : marks) {
                        total += Integer.parseInt(mark.trim());
                    }

                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent("Total Marks: " + total);
                    send(reply);
                } else {
                    block();
                }
            }
        });
    }
}
 