package core.sensor.bool;

import controllers.ModuleController;

public class LightBumperFrontLeft extends BooleanSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().lightBumperFrontLeft());
    }
}
