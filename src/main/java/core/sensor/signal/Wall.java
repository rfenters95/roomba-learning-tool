package core.sensor.signal;

import controllers.ModuleController;

public class Wall extends SignalSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().wallSignal());
    }
}
