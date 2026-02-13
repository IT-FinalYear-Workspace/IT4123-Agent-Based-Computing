import java.util.Map;
import java.util.HashMap;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;




public class LedgerAgent extends Agent {
    private Map<String, Boolean> tokenLedger;
  
    protected void setup() {
        tokenLedger = new HashMap<String, Boolean>();
        System.out.println(getLocalName() + " initialized token ledger");

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setName("ledger-service");
        sd.setType("ledger-service");
        dfd.addServices(sd);

        try{
            DFService.register(this, dfd);
        }catch (FIPAException fe) {
            fe.printStackTrace();
        }
          
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                
                if(msg != null){
                    if(msg.getPerformative() == ACLMessage.REQUEST){
                        String content = msg.getContent();

                        if(content.equals("generate-token")){
                            String token = generateToken();
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.INFORM);
                            reply.setContent(token);
                            send(reply);
                            System.out.println("LedgerAgent: Token " + token + " generated for " + msg.getSender().getLocalName());
                            
                        } else if(msg.getPerformative() == ACLMessage.REQUEST && msg.getContent().startsWith("validate-token:")){
                            String token = msg.getContent().split(":")[1];
                            ACLMessage reply = msg.createReply();

                            if(tokenLedger.containsKey(token) && tokenLedger.get(token)){
                                reply.setPerformative(ACLMessage.CONFIRM);
                                reply.setContent("token-valid");
                                System.out.println(getLocalName() + ": Token " + token + " validated successfully");
                            } else {
                                reply.setPerformative(ACLMessage.REFUSE);
                                reply.setContent("token-invalid");
                                System.out.println(getLocalName() + ": Token " + token + " validated Failed");
                            }
                            send(reply);
                        } else if(msg.getPerformative() == ACLMessage.INFORM && msg.getContent().startsWith("transaction-complete:")){
                            String token = msg.getContent().split(":")[1];
                            if (tokenLedger.containsKey(token)) {
                                tokenLedger.put(token, false);
                                System.out.println("LedgerAgent: Received transaction Successful message from " + msg.getSender().getLocalName());
                                System.out.println("LedgerAgent: Token " + token + " invalidated");
                            }
                        }
                    }
                } else {
                    block();
                }
            }
        });
    }

    private String generateToken() {
        String token = "TXN" + System.currentTimeMillis();
        tokenLedger.put(token, true);
        return token;
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println(getLocalName() + " terminating.");
    }
}

