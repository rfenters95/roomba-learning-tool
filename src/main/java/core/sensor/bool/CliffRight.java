package core.sensor.bool;

import controllers.ModuleController;

public class CliffRight extends BooleanSensor {
    @Override
    public String read() {
        return String.valueOf(ModuleController.getRoomba().cliffRight());
    }
}
