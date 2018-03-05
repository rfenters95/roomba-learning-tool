package controllers;

import core.NumericTextFieldValidator;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
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
    private MaterialDesignIconView playIcon;

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

    private boolean playing;

    @FXML
    public void handlePlayButtonAction(ActionEvent event) {
        if (!playing) {
            boolean debris = debrisCheckBox.isSelected();
            boolean spot = spotCheckBox.isSelected();
            boolean dock = dockCheckBox.isSelected();
            boolean checkRobot = checkRobotCheckBox.isSelected();
            int powerColor = Integer.parseInt(powerColorTextField.getText());
            int powerIntensity = Integer.parseInt(powerIntensityTextField.getText());
            getRoomba().leds(debris, spot, dock, checkRobot, powerColor, powerIntensity);
            toggleNodeStatus(playIcon, "play", "stop");
            LOGGER.info("Executed leds() command");
        } else {
            getRoomba().leds(false, false, false, false, 0, 0);
            toggleNodeStatus(playIcon, "stop", "play");
        }
        playing = !playing;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NumericTextFieldValidator powerColorValidator = new NumericTextFieldValidator(powerColorTextField, 0, 255);
        powerColorTextField.textProperty().addListener(powerColorValidator);

        NumericTextFieldValidator powerIntensityValidator = new NumericTextFieldValidator(powerIntensityTextField, 0, 255);
        powerIntensityTextField.textProperty().addListener(powerIntensityValidator);
    }
}
