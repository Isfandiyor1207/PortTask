package epam.task.thread;

import epam.task.thread.entity.Boat;
import epam.task.thread.entity.Port;
import epam.task.thread.generator.BoatGenerator;
import epam.task.thread.generator.PortGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger logger = LogManager.getLogger();
    private static final Port PORT = Port.getInstance();

    public static void main(String[] args) {

        BoatGenerator boatUtilGenerator = new BoatGenerator();
        PortGenerator portUtilGenerator = new PortGenerator();


        Timer timer=new Timer();

        timer.schedule(new Boat(boatUtilGenerator.generateNumberForBoat(),
                boatUtilGenerator.generateNumberOfContainerToBoat()), 100, 1000);

//        System.exit(0);
//        timer.cancel();

        PORT.setActualContainer(portUtilGenerator.generateNumberOfContainerForPort());

    }
}
