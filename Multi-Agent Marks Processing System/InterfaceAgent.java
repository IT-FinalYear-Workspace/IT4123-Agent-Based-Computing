package agents;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.*;

import java.util.Scanner;

public class InterfaceAgent extends Agent {
    protected void setup() {
        System.out.println("InterfaceAgent started.");

        addBehaviour(new OneShotBehaviour() {
            public void action() {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter marks for Math, English, Science (comma-separated):");
                String input = scanner.nextLine();

                sendMessage("TotalAgent", input);
                sendMessage("AverageAgent", input);
                sendMessage("StatsAgent", input);
            }

            private void sendMessage(String agentName, String content) {
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                msg.addReceiver(new AID(agentName, AID.ISLOCALNAME));
                msg.setContent(content);
                send(msg);
            }
        });

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String sender = msg.getSender().getLocalName();
                    String content = msg.getContent();
                    System.out.println("From " + sender + ": " + content);

                    if (sender.equals("AverageAgent")) {
                        ACLMessage gradeMsg = new ACLMessage(ACLMessage.REQUEST);
                        gradeMsg.addReceiver(new AID("GradeAgent", AID.ISLOCALNAME));
                        gradeMsg.setContent(content);
                        send(gradeMsg);
                    }
                } else {
                    block();
                }
            }
        });
    }
}




// C:\Users\User\Downloads\JADE-all-4.6.0\jade\jade>javac -cp lib\jade.jar src\agents\InterfaceAgent.java

// C:\Users\User\Downloads\JADE-all-4.6.0\jade\jade>java -cp lib\jade.jar;src jade.Boot -gui InterfaceAgent:agents.InterfaceAgent