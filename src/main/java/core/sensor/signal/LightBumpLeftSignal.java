package core.sensor.signal;

import controllers.ModuleController;

public class LightBumpLeftSignal extends SignalSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().lightBumperSignalLeft());
    }
}

