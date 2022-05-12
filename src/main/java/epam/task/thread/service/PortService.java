package epam.task.thread.service;

import epam.task.thread.entity.Boat;
import epam.task.thread.entity.Port;
import epam.task.thread.validation.BoatValidation;
import epam.task.thread.validation.PortValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PortService {

    private static final Logger logger = LogManager.getLogger();
    private static final Port PORT = Port.getInstance();
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition condition = lock.newCondition();

    public void taskManager(Boat boat) {
        BoatValidation validation = new BoatValidation();
        try {
            if (validation.checkBoatToEmpty(boat.getActualContainerInBoat())) {
                loadingContainerToBoat(boat.getActualContainerInBoat());
                boat.setActualContainerInBoat(Boat.MAXIMUM_CAPACITY_OF_BOAT);

                logger.info("Boat by this thread name {} and by this number №_{} has loaded containers",
                        Thread.currentThread().getName(),
                        boat.getBoatNumber());
                System.out.printf("Boat by this thread name {%s} and by this number №_{%s} has loaded containers\n",
                        Thread.currentThread().getName(),
                        boat.getBoatNumber());
            } else {
                unloadingContainerToPort(boat.getActualContainerInBoat());
                boat.setActualContainerInBoat(0);
                logger.info("Boat by this thread name {} and by this number №_{} has unloaded containers",
                        Thread.currentThread().getName(),
                        boat.getBoatNumber());
                System.out.printf("Boat by this thread name {%s} and by this number №_{%s} has unloaded containers\n",
                        Thread.currentThread().getName(),
                        boat.getBoatNumber());
            }

            TimeUnit.MILLISECONDS.sleep((long) (Math.random()*200+300));

            logger.info("Boat by this thread name {} and by this number №_{} has left the Port",
                    Thread.currentThread().getName(),
                    boat.getBoatNumber());
            System.out.printf("Boat by this thread name {%s} and by this number №_{%S} has left the Port\n",
                    Thread.currentThread().getName(),
                    boat.getBoatNumber());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void unloadingContainerToPort(int numberOfContainersInShip) {
        int actualContainerInPort = PORT.getActualContainer();
        PortValidation validation = new PortValidation();
        try {

            lock.lock();

            int loadingContainers = actualContainerInPort + numberOfContainersInShip;

            while (!validation.checkPortToEmpty(actualContainerInPort) || loadingContainers > Port.MAXIMUM_CAPACITY) {
                condition.await();
                logger.error("The port cannot accept all containers on board.");
            }

            PORT.setActualContainer(loadingContainers);

        } catch (InterruptedException e) {
            logger.error("Unloading to Port from boat by name failed.");
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }

    }


    public void loadingContainerToBoat(int numberOfContainersInShip) {
        int actualContainerInPort = PORT.getActualContainer();
        PortValidation validation = new PortValidation();
        try {

            lock.lock();

            while (!validation.checkNumberOfContainerAvailable(numberOfContainersInShip)) {
                condition.await();
                logger.error("There is not enough container to load boat.");
            }

            PORT.setActualContainer(actualContainerInPort - numberOfContainersInShip);

        } catch (InterruptedException e) {
            logger.error("Unloading to Port from boat by name failed.");
        } finally {
            lock.unlock();
        }

    }
}
