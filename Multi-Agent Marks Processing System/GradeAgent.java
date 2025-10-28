package agents;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.*;

public class GradeAgent extends Agent {
    protected void setup() {
        System.out.println("GradeAgent started.");

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    double avg = Double.parseDouble(msg.getContent());
                    String grade, remark;

                    if (avg >= 75) grade = "A";
                    else if (avg >= 60) grade = "B";
                    else if (avg >= 50) grade = "C";
                    else if (avg >= 40) grade = "D";
                    else grade = "F";

                    remark = (avg >= 50) ? "Pass" : "Fail";
                    String justification = "Average " + avg + " falls into " + grade + " range.";

                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent("Grade: " + grade + " | " + remark + " | " + justification);
                    send(reply);
                } else {
                    block();
                }
            }
        });
    }
}
