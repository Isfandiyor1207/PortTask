package epam.task.thread.entity;

import epam.task.thread.enums.Action;
import epam.task.thread.service.PortService;
import epam.task.thread.validation.BoatValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.StringJoiner;
import java.util.concurrent.Semaphore;

public class Boat implements Runnable {

    private static final Logger LOGGER= LogManager.getLogger();
    private static final Semaphore SEMAPHORE = new Semaphore(Port.MAXIMUM_CAPACITY_FOR_BOAT, true);
    public static final int MAXIMUM_CAPACITY_OF_BOAT = 100;
    private int boatNumber;
    private Action action;
    private int actualContainerInBoat;

    public Boat() {
    }

    public Boat(int boatNumber, int actualContainerInBoat) {
        BoatValidation boatValidation=new BoatValidation();

        this.boatNumber = boatNumber;
        this.actualContainerInBoat = actualContainerInBoat;
        this.action = Action.values()[boatValidation.checkActionIsTrue(this.actualContainerInBoat)];
    }

    public int getBoatNumber() {
        return boatNumber;
    }

    public void setBoatNumber(int boatNumber) {
        this.boatNumber = boatNumber;
    }

    public int getActualContainerInBoat() {
        return actualContainerInBoat;
    }

    public void setActualContainerInBoat(int actualContainerInBoat) {
        this.actualContainerInBoat = actualContainerInBoat;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public void run() {
        PortService portService = new PortService();

        try {
            SEMAPHORE.acquire();
            portService.taskManager(this);

            SEMAPHORE.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boat boat = (Boat) o;
        return boatNumber == boat.boatNumber && actualContainerInBoat == boat.actualContainerInBoat && action == boat.action;
    }

    @Override
    public int hashCode() {
        if (boatNumber == 0 && actualContainerInBoat == 0) {
            return 0;
        }

        int result = 1;

        result = 31 * result + boatNumber;
        result = 31 * result + actualContainerInBoat;

        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Boat.class.getSimpleName() + "[", "]")
                .add("boatNumber=" + boatNumber)
                .add("actualContainerInBoat=" + actualContainerInBoat)
                .add("action=" + action)
                .toString();
    }
}
