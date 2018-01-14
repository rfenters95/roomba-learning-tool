package controllers.main;

import controllers.Module;
import controllers.ModuleController;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class MainModuleController extends ModuleController implements Initializable {

    private final static Logger LOGGER = Logger.getLogger(MainModuleController.class);

    @FXML
    private MaterialDesignIconView iconView;

    @FXML
    private Label connectionStatusLabel;

    @FXML
    private Label deviceNameLabel;

    @FXML
    private Label connectionTypeLabel;

    @FXML
    private Label batteryStatusLabel;

    @FXML
    private Label startModeLabel;

    private BatteryUpdaterThread batteryUpdaterThread;

    /* Opens a new window for the Drive Module. */
    @FXML
    void openDriveModule(ActionEvent event) {
        Module module = Module.Drive;
        openModule(module);
    }

    /* Opens a new window for the LED Module. */
    @FXML
    void openLEDModule(ActionEvent event) {
        Module module = Module.LED;
        openModule(module);
    }

    /* Opens a new window for the Sensor Module. */
    @FXML
    void openSensorModule(ActionEvent event) {
        Module module = Module.Sensor;
        openModule(module);
    }

    /* Opens a new window for the Song Module. */
    @FXML
    void openSongModule(ActionEvent event) {
        Module module = Module.Song;
        openModule(module);
    }

    /* Opens a new window for the Information Module. */
    @FXML
    void openInformationModule(ActionEvent event) {
        Module module = Module.Information;
        openModule(module);
    }

    /* Opens a new window for the Connection Module. */
    @FXML
    void openConnectionModule(ActionEvent event) {
        Module module = Module.Connection;
        module.open();
    }

    private void openModule(Module module) {
        if (isConnected()) {
            module.open();
        } else {
            LOGGER.trace(String.format("Roomba not connected! %s Module prevented from opening!", module));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                iconView.getStyleClass().clear();
                iconView.getStyleClass().add("power");
                iconView.getStyleClass().add("green-status");
                connectionStatusLabel.setText("Connected!");
                connectionStatusLabel.getStyleClass().clear();
                connectionStatusLabel.getStyleClass().add("okay-status-font");
                deviceNameLabel.setText(getPort());
                connectionTypeLabel.setText(getConnectionType().toString());
                batteryUpdaterThread = new BatteryUpdaterThread();
                batteryUpdaterThread.start();
            } else {
                iconView.getStyleClass().clear();
                iconView.getStyleClass().add("power");
                iconView.getStyleClass().add("red-status");
                connectionStatusLabel.setText("Not Connected!");
                connectionStatusLabel.getStyleClass().clear();
                connectionStatusLabel.getStyleClass().add("error-status-font");
                deviceNameLabel.setText("N/A");
                connectionTypeLabel.setText("N/A");
                startModeLabel.setText("N/A");
                batteryStatusLabel.setText("N/A");
            }
        });
    }

    private class BatteryUpdaterThread extends Thread {

        @Override
        public void run() {
            final int minutesToSleep = 10;
            final int minutesToMillisConversionFactor = 60 * 1000;
            final long millisToSleep = minutesToSleep * minutesToMillisConversionFactor;
            while (isConnected()) {
                int measuredBatteryCharge = ModuleController.getRoomba().batteryCharge();
                int maxBatteryCharge = 65535;
                double batteryPercentage = (measuredBatteryCharge / maxBatteryCharge) * 100;
                Platform.runLater(() -> batteryStatusLabel.setText(String.format("%3.2f%%", batteryPercentage)));
                try {
                    final long sleepStartTime = System.currentTimeMillis();
                    final long timeSlept = System.currentTimeMillis() - sleepStartTime;
                    while (isConnected() && (millisToSleep > timeSlept)) {
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
            LOGGER.debug("BatteryUpdaterThread ended successfully!");
        }

    }
}
