package core.sensor.signal;

import controllers.ModuleController;

public class LightBumpRightSignal extends SignalSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().lightBumperSignalRight());
    }
}

