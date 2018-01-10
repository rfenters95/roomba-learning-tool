package core.sensor.bool;

import controllers.ModuleController;

public class BumpCenter extends BooleanSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().bumpLeft() && ModuleController.getRoomba().bumpRight());
    }
}
