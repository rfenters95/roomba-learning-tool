package core.sensor.bool;

import controllers.ModuleController;

public class LightBumperRight extends BooleanSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().lightBumperRight());
    }
}
