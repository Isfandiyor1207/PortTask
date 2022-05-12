package epam.task.thread.generator;

import java.util.Random;

public class BoatGenerator {

    private final Random random = new Random();

    public int generateNumberOfContainerToBoat() {
        return random.nextInt(150);
    }

    public int generateNumberForBoat() {
        return random.nextInt(9999 - 1000) + 1000;
    }

    public int getRandomAction() {
        return random.nextInt(2);
    }
}
