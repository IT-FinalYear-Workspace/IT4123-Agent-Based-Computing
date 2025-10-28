package agents;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.*;

public class AverageAgent extends Agent {
    protected void setup() {
        System.out.println("AverageAgent started.");

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String[] marks = msg.getContent().split(",");
                    int[] weights = {3, 2, 1}; // Math, English, Science
                    double weightedSum = 0;
                    int totalWeight = 0;

                    for (int i = 0; i < marks.length; i++) {
                        int mark = Integer.parseInt(marks[i].trim());
                        weightedSum += mark * weights[i];
                        totalWeight += weights[i];
                    }

                    double average = weightedSum / totalWeight;
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent(String.format("%.2f", average));
                    send(reply);
                } else {
                    block();
                }
            }
        });
    }
}
