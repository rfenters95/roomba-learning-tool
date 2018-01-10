package core.sensor.signal;

import controllers.ModuleController;

public class CliffRightSignal extends SignalSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().cliffRight());
    }
}
