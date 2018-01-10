package core.sensor.signal;

import controllers.ModuleController;

public class CliffLeftSignal extends SignalSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().cliffSignalLeft());
    }
}
