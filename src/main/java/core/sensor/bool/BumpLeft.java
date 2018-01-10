package core.sensor.bool;

import controllers.ModuleController;

public class BumpLeft extends BooleanSensor {

    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().bumpLeft());
    }
}
