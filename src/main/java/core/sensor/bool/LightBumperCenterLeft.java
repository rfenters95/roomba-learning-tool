package core.sensor.bool;

import controllers.ModuleController;

public class LightBumperCenterLeft extends BooleanSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().lightBumperCenterLeft());
    }
}
