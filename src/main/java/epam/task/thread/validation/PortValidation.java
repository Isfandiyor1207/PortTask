package epam.task.thread.validation;

import epam.task.thread.entity.Port;

public class PortValidation {

    public boolean checkPortToEmpty(int actualContainerInPort) {
        return actualContainerInPort < Port.MAXIMUM_CAPACITY;
    }

    public boolean checkNumberOfContainerAvailable(int containers){
        return Port.getInstance().getActualContainer() >= containers;
    }


}
