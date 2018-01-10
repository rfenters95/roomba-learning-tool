package core.sensor.bool;

import controllers.ModuleController;

public class BumpRight extends BooleanSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().bumpRight());
    }
}
