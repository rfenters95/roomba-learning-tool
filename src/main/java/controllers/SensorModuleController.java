package controllers;

import core.sensor.Sensor;
import core.sensor.bool.*;
import core.sensor.signal.*;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SensorModuleController extends ModuleController implements Initializable {

    private final static Logger LOGGER = Logger.getLogger(SensorModuleController.class);

    private final ArrayList<BooleanSensor> booleanSensors = new ArrayList<>();
    private final ArrayList<SignalSensor> signalSensors = new ArrayList<>();

    @FXML
    private MaterialDesignIconView iconView;

    @FXML
    private ComboBox<String> sensorTypeComboBox;

    @FXML
    private ComboBox<Sensor> sensorComboBox;

    @FXML
    private ListView<String> listView;

    @FXML
    private Label showGraphLabel;

    @FXML
    void handlePlayButtonActionEvent(ActionEvent event) {
        if (isReadingSensors()) {
            setReadingSensors(false);
        } else {
            listView.getItems().clear();
            Sensor sensor = sensorComboBox.getSelectionModel().getSelectedItem();
            SensorThread thread = new SensorThread(sensor);
            setReadingSensors(true);
            thread.start();
        }
    }

    @FXML
    void handleSensorTypeActionEvent(ActionEvent event) {
        sensorComboBox.getItems().clear();
        int index = sensorTypeComboBox.getSelectionModel().getSelectedIndex();
        switch (index) {
            case 0:
                sensorComboBox.getItems().addAll(booleanSensors);
                break;
            case 1:
                sensorComboBox.getItems().addAll(signalSensors);
                break;
            default:
                break;
        }
        sensorComboBox.getSelectionModel().selectFirst();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        sensorTypeComboBox.getItems().add("Boolean Sensor");
        sensorTypeComboBox.getItems().add("Signal Sensor");
        sensorTypeComboBox.getSelectionModel().selectFirst();

        showGraphLabel.setVisible(false);

        Callback<ListView<Sensor>, ListCell<Sensor>> callback = new Callback<ListView<Sensor>, ListCell<Sensor>>() {
            @Override
            public ListCell<Sensor> call(ListView<Sensor> param) {
                return new ListCell<Sensor>() {
                    @Override
                    protected void updateItem(Sensor item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            setText(item.getClass().getSimpleName());
                        } else {
                            setText(null);
                        }
                        setGraphic(null);
                    }
                };
            }
        };
        sensorComboBox.setCellFactory(callback);
        sensorComboBox.setButtonCell(callback.call(null));

        booleanSensors.add(new BumpCenter());
        booleanSensors.add(new BumpLeft());
        booleanSensors.add(new BumpLeft());
        booleanSensors.add(new BumpRight());
        booleanSensors.add(new CliffLeft());
        booleanSensors.add(new CliffFrontLeft());
        booleanSensors.add(new CliffFrontRight());
        booleanSensors.add(new CliffRight());
        booleanSensors.add(new LightBumperLeft());
        booleanSensors.add(new LightBumperFrontLeft());
        booleanSensors.add(new LightBumperCenterLeft());
        booleanSensors.add(new LightBumperCenterRight());
        booleanSensors.add(new LightBumperFrontRight());
        booleanSensors.add(new LightBumperRight());

        signalSensors.add(new Wall());
        signalSensors.add(new CliffLeftSignal());
        signalSensors.add(new CliffFrontLeftSignal());
        signalSensors.add(new CliffFrontRightSignal());
        signalSensors.add(new CliffRightSignal());
        signalSensors.add(new LightBumpLeftSignal());
        signalSensors.add(new LightBumpFrontLeftSignal());
        signalSensors.add(new LightBumpCenterLeftSignal());
        signalSensors.add(new LightBumpCenterRightSignal());
        signalSensors.add(new LightBumpFrontRightSignal());
        signalSensors.add(new LightBumpRightSignal());

        sensorComboBox.getItems().addAll(booleanSensors);
        sensorComboBox.getSelectionModel().selectFirst();

        Label placeHolder = new Label("Play Sensor Module to read sensor");
        placeHolder.getStyleClass().add("normal-font");
        listView.setPlaceholder(placeHolder);
    }

    private class SensorThread extends Thread {

        private Sensor sensor;

        SensorThread(Sensor sensor) {
            this.sensor = sensor;
        }

        @Override
        public void run() {
            toggleNodeStatus(iconView, "play", "stop");
            while (isReadingSensors()) {
                ModuleController.getRoomba().updateSensors();
                LOGGER.debug("SensorThread updated sensors");
                String value = sensor.read();
                Platform.runLater(() -> listView.getItems().add(0, value));
                LOGGER.debug("SensorThread added sensor value to listView");
                LOGGER.debug("SensorThread entering sleep for 100 millis");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage(), e);
                }
                LOGGER.debug("SensorThread awoken from sleep");
            }
            toggleNodeStatus(iconView, "stop", "play");
        }
    }

}
