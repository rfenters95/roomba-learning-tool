package core.sensor.bool;

import controllers.ModuleController;

public class LightBumperFrontRight extends BooleanSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().lightBumperFrontRight());
    }
}
