package core.sensor.bool;

import controllers.ModuleController;

public class CliffFrontRight extends BooleanSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().cliffFrontRight());
    }
}
