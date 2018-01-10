package core.sensor.signal;

import controllers.ModuleController;

public class CliffFrontLeftSignal extends SignalSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().cliffSignalFrontLeft());
    }
}

