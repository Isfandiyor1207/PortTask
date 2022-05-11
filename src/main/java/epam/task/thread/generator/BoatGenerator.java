package epam.task.thread.generator;

import epam.task.thread.entity.Boat;

import java.util.Random;

public class BoatGenerator {

    private final Random random = new Random();

    public int generateNumberOfContainerToBoat(){
        return random.nextInt(Boat.MAXIMUM_CAPACITY_OF_BOAT)+1;
    }

    public int generateNumberForBoat(){
        return random.nextInt(9999-1000) + 1000;
    }

    public int getRandomAction(){
        return random.nextInt(2);
    }
}
