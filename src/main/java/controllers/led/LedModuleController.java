package controllers.led;

import controllers.ModuleController;
import core.NumericTextFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class LedModuleController extends ModuleController implements Initializable {

    private final static Logger LOGGER = Logger.getLogger(LedModuleController.class);

    @FXML
    private CheckBox debrisCheckBox;

    @FXML
    private CheckBox spotCheckBox;

    @FXML
    private CheckBox dockCheckBox;

    @FXML
    private CheckBox checkRobotCheckBox;

    @FXML
    private TextField powerColorTextField;

    @FXML
    private TextField powerIntensityTextField;

    @FXML
    public void handlePlayButtonAction(ActionEvent event) {
        boolean debris = debrisCheckBox.isSelected();
        boolean spot = spotCheckBox.isSelected();
        boolean dock = dockCheckBox.isSelected();
        boolean checkRobot = checkRobotCheckBox.isSelected();
        int powerColor = Integer.parseInt(powerColorTextField.getText());
        int powerIntensity = Integer.parseInt(powerIntensityTextField.getText());
        ModuleController.getRoomba().leds(debris, spot, dock, checkRobot, powerColor, powerIntensity);
        LOGGER.info("Executed leds() command");
    }

    @FXML
    public void handleInfoButtonAction(ActionEvent event) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NumericTextFieldValidator powerColorValidator = new NumericTextFieldValidator(powerColorTextField, 0, 255);
        powerColorTextField.textProperty().addListener(powerColorValidator);

        NumericTextFieldValidator powerIntensityValidator = new NumericTextFieldValidator(powerIntensityTextField, 0, 255);
        powerIntensityTextField.textProperty().addListener(powerIntensityValidator);
    }
}
