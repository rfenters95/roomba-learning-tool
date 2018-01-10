package core.sensor.bool;

import controllers.ModuleController;

public class LightBumperLeft extends BooleanSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().lightBumperLeft());
    }
}
