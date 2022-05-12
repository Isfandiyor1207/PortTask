package epam.task.thread.validation;

import epam.task.thread.entity.Boat;

public class BoatValidation {

    public int putActionForBoat(int actualContainers) {
        return actualContainers < Boat.MAXIMUM_CAPACITY_OF_BOAT ? 1 : 0;
    }

    public boolean checkBoatToEmpty(int actualContainerInBoat) {
        return actualContainerInBoat < Boat.MAXIMUM_CAPACITY_OF_BOAT;
    }

}
