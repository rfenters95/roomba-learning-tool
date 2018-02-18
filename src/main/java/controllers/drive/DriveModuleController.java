package controllers.drive;

import alerts.DriveDirectInfo;
import alerts.DriveInfo;
import controllers.ModuleController;
import core.NumericTextFieldValidator;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class DriveModuleController extends ModuleController implements Initializable {

    private final static Logger LOGGER = Logger.getLogger(DriveModuleController.class);

    @FXML
    private MaterialDesignIconView playIcon;

    @FXML
    private ComboBox<DriveCommand> driveCommandComboBox;

    @FXML
    private Label distanceStatusLabel;

    @FXML
    private Label timeStatusLabel;

    @FXML
    private TabPane tabPane;

    @FXML
    private TextField velocityTextField;

    @FXML
    private TextField radiusTextField;

    @FXML
    private TextField leftVelocityTextField;

    @FXML
    private TextField rightVelocityTextField;
    private long startTime;
    private boolean playing = false;

    @FXML
    public void handleDriveCommandComboBoxActionEvent(ActionEvent event) {
        DriveCommand driveCommand = driveCommandComboBox.getValue();
        switch (driveCommand) {
            case Drive:
                tabPane.getSelectionModel().selectFirst();
                break;
            case DriveDirect:
                tabPane.getSelectionModel().selectLast();
                break;
            default:
                break;
        }
    }

    @FXML
    private void handleInfoButtonAction(ActionEvent event) {

        DriveCommand driveCommand = driveCommandComboBox.getValue();

        Alert alert;
        if (driveCommand == DriveCommand.Drive) {
            alert = new DriveInfo();
        } else {
            alert = new DriveDirectInfo();
        }

        alert.showAndWait();
    }

    private boolean isDriveValidInput() {
        return !velocityTextField.getText().equals("-") && !radiusTextField.getText().equals("-");
    }

    private boolean isDriveDirectValidInput() {
        return !leftVelocityTextField.getText().equals("-") && !rightVelocityTextField.getText().equals("-");
    }

    @FXML
    public void handlePlayButtonAction(ActionEvent event) {
        if (isConnected()) {
            if (!playing) {
                DriveCommand driveCommand = driveCommandComboBox.getValue();
                LOGGER.trace(String.format("%s tab selected!", driveCommand));
                switch (driveCommand) {
                    case Drive:
                        if (isDriveValidInput()) {
                            int velocity = Integer.parseInt(velocityTextField.getText());
                            int radius = Integer.parseInt(radiusTextField.getText());
                            LOGGER.trace(String.format("Passing velocity [%s] radius [%s] to drive method", velocity, radius));
                            getRoomba().updateSensors();
                            getRoomba().drive(velocity, radius);
                            startTime = System.currentTimeMillis();
                            playing = true;
                            toggleNodeStatus(playIcon, "play", "stop");
                        }
                        break;
                    case DriveDirect:
                        if (isDriveDirectValidInput()) {
                            int rightVelocity = Integer.parseInt(rightVelocityTextField.getText());
                            int leftVelocity = Integer.parseInt(leftVelocityTextField.getText());
                            LOGGER.trace(String.format("Passing rightVelocity [%s] leftVelocity [%s] to driveDirect method", rightVelocity, leftVelocity));
                            getRoomba().updateSensors();
                            getRoomba().driveDirect(rightVelocity, leftVelocity);
                            startTime = System.currentTimeMillis();
                            playing = true;
                            toggleNodeStatus(playIcon, "play", "stop");
                        }
                        break;
                    default:
                        break;
                }
            } else {
                getRoomba().driveDirect(0, 0);
                timeStatusLabel.setText(String.valueOf((System.currentTimeMillis() - startTime)) + " millis");
                getRoomba().updateSensors();
                distanceStatusLabel.setText(String.valueOf(getRoomba().distanceTraveled()));
                playing = false;
                toggleNodeStatus(playIcon, "stop", "play");
            }
        } else {
            LOGGER.trace("DriveModuleController play action prevented! Roomba is not connected!");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        NumericTextFieldValidator velocityValidator = new NumericTextFieldValidator(velocityTextField, -500, 500);
        velocityTextField.textProperty().addListener(velocityValidator);
        NumericTextFieldValidator radiusValidator = new NumericTextFieldValidator(radiusTextField, -2000, 2000);
        radiusValidator.addSpecialValue(32767);
        radiusValidator.addSpecialValue(32768);
        radiusTextField.textProperty().addListener(radiusValidator);
        NumericTextFieldValidator rightVelocityValidator = new NumericTextFieldValidator(rightVelocityTextField, -500, 500);
        rightVelocityTextField.textProperty().addListener(rightVelocityValidator);
        NumericTextFieldValidator leftVelocityValidator = new NumericTextFieldValidator(leftVelocityTextField, -500, 500);
        leftVelocityTextField.textProperty().addListener(leftVelocityValidator);

        driveCommandComboBox.getItems().addAll(DriveCommand.values());
        driveCommandComboBox.getSelectionModel().selectFirst();

    }

    private enum DriveCommand {
        Drive, DriveDirect
    }

}
