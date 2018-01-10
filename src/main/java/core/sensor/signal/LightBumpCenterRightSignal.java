package core.sensor.signal;

import controllers.ModuleController;

public class LightBumpCenterRightSignal extends SignalSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().lightBumperSignalCenterRight());
    }
}
