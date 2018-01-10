package core.sensor.bool;

import controllers.ModuleController;

public class LightBumperCenterRight extends BooleanSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().lightBumperCenterRight());
    }
}
