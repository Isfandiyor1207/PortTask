package epam.task.thread.service;

import epam.task.thread.entity.Boat;
import epam.task.thread.entity.Port;
import epam.task.thread.generator.BoatGenerator;
import epam.task.thread.generator.PortGenerator;
import epam.task.thread.validation.BoatValidation;
import epam.task.thread.validation.PortValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PortService {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Port PORT = Port.getInstance();
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition condition = lock.newCondition();

    public void entranceOfBoat() {

        BoatGenerator boatUtilGenerator = new BoatGenerator();
        PortGenerator portUtilGenerator = new PortGenerator();

        ExecutorService executorService= Executors.newFixedThreadPool(15);

        for (int i = 0; i < 15; i++) {

            executorService.submit(new Boat(boatUtilGenerator.generateNumberForBoat(),
                    boatUtilGenerator.generateNumberOfContainerToBoat()));

            try {
                executorService.awaitTermination(200, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                LOGGER.error(String.format("Thread by name %s cannot executed!", Thread.currentThread().getName()));
                Thread.currentThread().interrupt();
            }

        }
        executorService.shutdown();
        PORT.setActualContainer(portUtilGenerator.generateNumberOfContainerForPort());

    }

    public void taskManager(Boat boat) {
        BoatValidation validation = new BoatValidation();
        try {
            System.out.printf("Boat by number №_%s has entered to Port\n", boat.getBoatNumber());

            if (validation.checkBoatToEmpty(boat.getActualContainerInBoat())) {
                loadingContainerToBoat(boat.getActualContainerInBoat());
                boat.setActualContainerInBoat(Boat.MAXIMUM_CAPACITY_OF_BOAT);

                System.out.printf("Boat by number №_%s has unloaded containers\n", boat.getBoatNumber());
            } else {
                unloadingContainerToPort(boat.getActualContainerInBoat());
                boat.setActualContainerInBoat(0);
                System.out.printf("Boat by number №_%s has unloaded containers\n", boat.getBoatNumber());
            }
            TimeUnit.SECONDS.sleep(1);

            System.out.printf("Boat by number №_%s has left the Port\n", boat.getBoatNumber());

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

            if (!validation.checkPortToEmpty(actualContainerInPort) || loadingContainers > Port.MAXIMUM_CAPACITY) {
                condition.await();
                LOGGER.error("The port cannot accept all containers on board.");
            }

            PORT.setActualContainer(loadingContainers);

        } catch (InterruptedException e) {
            LOGGER.error("Unloading to Port from boat by name failed.");
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

            if (!validation.checkNumberOfContainerAvailable(numberOfContainersInShip)) {
                condition.await();
                LOGGER.error("There is not enough container to load boat.");
            }

            PORT.setActualContainer(actualContainerInPort - numberOfContainersInShip);

        } catch (InterruptedException e) {
            LOGGER.error("Unloading to Port from boat by name failed.");
        } finally {
            lock.unlock();
        }

    }
}
