package core.sensor;

public abstract class Sensor {
    public abstract String read();

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
