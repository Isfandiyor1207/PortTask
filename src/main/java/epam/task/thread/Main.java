package epam.task.thread;

import epam.task.thread.entity.Boat;
import epam.task.thread.entity.Port;
import epam.task.thread.generator.BoatGenerator;
import epam.task.thread.generator.PortGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger logger = LogManager.getLogger();
    private static final Port PORT = Port.getInstance();

    public static void main(String[] args) {

        BoatGenerator boatUtilGenerator = new BoatGenerator();
        PortGenerator portUtilGenerator = new PortGenerator();


        ExecutorService executorService = Executors.newFixedThreadPool(15);

        for (int i = 0; i < 25; i++) {

            executorService.submit(new Boat(boatUtilGenerator.generateNumberForBoat(),
                    boatUtilGenerator.generateNumberOfContainerToBoat()));

            try {
                executorService.awaitTermination((long) (Math.random() * 100 + 300), TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                logger.error(String.format("Thread by name %s cannot executed!", Thread.currentThread().getName()));
                Thread.currentThread().interrupt();
            }
        }

        executorService.shutdown();

        PORT.setActualContainer(portUtilGenerator.generateNumberOfContainerForPort());

    }
}
