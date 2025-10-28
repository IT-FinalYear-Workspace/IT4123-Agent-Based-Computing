package ecosystem;

public class BrontosaurusAgent extends AbstractAnimalAgent {
    @Override
    protected void setup() {
        int startX = 200 + (int)(Math.random() * 100 - 50);
        int startY = 200 + (int)(Math.random() * 100 - 50);
        setupAnimal("BRONTOSAURUS", startX, startY, 150, 4);
    }
}
