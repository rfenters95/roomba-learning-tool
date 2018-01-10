package core.sensor.signal;

import controllers.ModuleController;

public class LightBumpCenterLeftSignal extends SignalSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().lightBumperSignalCenterLeft());
    }
}
