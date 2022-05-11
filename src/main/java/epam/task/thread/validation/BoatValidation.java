package epam.task.thread.validation;

import epam.task.thread.entity.Boat;

public class BoatValidation {

    public int checkActionIsTrue(int actualContainers) {
        return actualContainers < Boat.MAXIMUM_CAPACITY_OF_BOAT ? 0 : 1;
    }

    public boolean checkBoatToEmpty(int actualContainerInBoat) {
        return actualContainerInBoat < Boat.MAXIMUM_CAPACITY_OF_BOAT;
    }

}
