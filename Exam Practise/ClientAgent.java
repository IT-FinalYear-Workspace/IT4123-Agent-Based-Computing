import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class ClientAgent extends Agent {

    private AID ledgerAID;
    private String targetAgent;
    private boolean isInitiator;
    private String receivedToken = "";
    private boolean tokenRecieved = false;

    protected void setup() {
        System.out.println(getLocalName() + " started.");

        int retries = 0;
        while (ledgerAID == null && retries < 10) {
            try {
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription templateSd = new ServiceDescription();
                templateSd.setType("ledger-service");
                template.addServices(templateSd);

                DFAgentDescription[] results = DFService.search(this, template);
                if (results.length > 0) {
                    ledgerAID = results[0].getName();
                    System.out.println(getLocalName() + " : Found ledger service at " + ledgerAID.getLocalName());
                } else {
                    System.out.println(getLocalName() + " : Ledger service not found yet, retrying...");
                    retries++;
                    Thread.sleep(500);
                }
            } catch (FIPAException fe) {
                fe.printStackTrace();
                // retries++;
            //     try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            // } catch (InterruptedException ie) {
            //     ie.printStackTrace();
            // }
        }

        if (ledgerAID == null) {
            System.out.println("Agent " + getLocalName() + " could not find ledger service after retries");
            doDelete();
            return;
        }

        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            targetAgent = (String) args[0];
            isInitiator = true;

            addBehaviour(new OneShotBehaviour(this) {
                public void action() {
                    ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                    msg.addReceiver(ledgerAID);
                    msg.setContent("generate-token");
                    send(msg);
                }
            });
        } else {
            isInitiator = false;
        }

       
        MessageTemplate tokenTemplate = MessageTemplate.and(
            MessageTemplate.MatchPerformative(ACLMessage.INFORM),
            MessageTemplate.MatchSender(ledgerAID));
        
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive(tokenTemplate);
                if (msg != null) {
                    receivedToken = msg.getContent();
                    tokenRecieved = true;
                }
                else {
                    block();
                }
            }
        });

        
        if(isInitiator) {
            addBehaviour(new OneShotBehaviour(this) {
                public void action() {
                    while(receivedToken.isEmpty()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.addReceiver(new AID(targetAgent, AID.ISLOCALNAME));
                    msg.setContent("token: " + receivedToken);
                    send(msg);
                    System.out.println(getLocalName() + ": Token " + receivedToken + " sent to " + targetAgent);
                }
            });
        }

        
        if(!isInitiator) {
            addBehaviour(new CyclicBehaviour(this) {
                private boolean validationHandled = false;
                
                public void action() {
                    if(!validationHandled) {
                        MessageTemplate validationTemplate = MessageTemplate.or(
                            MessageTemplate.MatchPerformative(ACLMessage.CONFIRM), 
                            MessageTemplate.MatchPerformative(ACLMessage.REFUSE));  
                        
                        ACLMessage msg = receive(validationTemplate);
                        if(msg != null) {
                            validationHandled = true;
                            
                            if(msg.getPerformative() == ACLMessage.CONFIRM) { 
                                String token = msg.getContent().split(":")[1]; 
                                System.out.println(getLocalName() + ": Token " + token + " verified successfully");
                                
                                ACLMessage completion = new ACLMessage(ACLMessage.INFORM);
                                completion.addReceiver(ledgerAID);
                                completion.setContent("transaction-complete:" + token);
                                send(completion);
                                System.out.println(getLocalName() + ": Informed the Successful transaction using Token " + token + " to LedgerAgent");
                            } else {
                                System.out.println(getLocalName() + ": Token validation FAILED");
                            }
                        } else {
                            block();
                        }
                    }
                }
            });
        }
    }
}