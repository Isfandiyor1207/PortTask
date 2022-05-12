package epam.task.thread.entity;

import epam.task.thread.generator.PortGenerator;

import java.util.StringJoiner;

public class Port {

    public static final int MAXIMUM_CAPACITY_FOR_BOAT = 1;
    public static final int MAXIMUM_CAPACITY = 1000;
    private int actualContainer;

    private Port() {
        PortGenerator generator=new PortGenerator();
        actualContainer = generator.generateNumberOfContainerForPort();
    }

    private static final class InstanceHolder {
        private static final Port instance = new Port();
    }

    public static Port getInstance() {
        return InstanceHolder.instance;
    }

    public int getActualContainer() {
        return actualContainer;
    }

    public void setActualContainer(int actualContainer) {
        this.actualContainer = actualContainer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Port port = (Port) o;
        return actualContainer == port.actualContainer;
    }

    @Override
    public int hashCode() {
        if (actualContainer == 0) {
            return 0;
        }
        int result;
        result = 31 * actualContainer;
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Port.class.getSimpleName() + "[", "]")
                .add("actualContainer=" + actualContainer)
                .toString();
    }
}
