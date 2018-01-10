package core.sensor.bool;

import controllers.ModuleController;

public class CliffFrontLeft extends BooleanSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().cliffFrontLeft());
    }
}
