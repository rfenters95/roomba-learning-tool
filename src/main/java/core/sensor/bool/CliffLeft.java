package core.sensor.bool;

import controllers.ModuleController;

public class CliffLeft extends BooleanSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().cliffLeft());
    }
}
