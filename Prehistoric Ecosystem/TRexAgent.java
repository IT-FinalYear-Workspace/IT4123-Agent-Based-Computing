package ecosystem;

import jade.core.AID;

public class TRexAgent extends AbstractAnimalAgent {

    @Override
    protected void setup() {
        int startX = 400 + (int)(Math.random() * 100 - 50);
        int startY = 300 + (int)(Math.random() * 100 - 50);
        setupAnimal("TREX", startX, startY, 200, 7);
    }
}
