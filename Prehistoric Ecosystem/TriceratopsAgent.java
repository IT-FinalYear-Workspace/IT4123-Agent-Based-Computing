package ecosystem;

public class TriceratopsAgent extends AbstractAnimalAgent {
    @Override
    protected void setup() {
        int startX = 600 + (int)(Math.random() * 100 - 50);
        int startY = 400 + (int)(Math.random() * 100 - 50);
        setupAnimal("TRICERATOPS", startX, startY, 120, 5);
    }
}
