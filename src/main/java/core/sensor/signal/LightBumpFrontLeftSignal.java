package core.sensor.signal;

import controllers.ModuleController;

public class LightBumpFrontLeftSignal extends SignalSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().lightBumperSignalFrontLeft());
    }
}
