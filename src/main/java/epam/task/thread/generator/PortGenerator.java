package epam.task.thread.generator;

import epam.task.thread.entity.Port;

import java.util.Random;

public class PortGenerator {

    public int generateNumberOfContainerForPort(){
        Random random=new Random();
        return random.nextInt(Port.MAXIMUM_CAPACITY);
    }
}
