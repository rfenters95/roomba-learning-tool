package core.sensor.signal;

import controllers.ModuleController;

public class LightBumpFrontRightSignal extends SignalSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().lightBumperSignalFrontRight());
    }
}
