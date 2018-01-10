package controllers.connection;

import com.maschel.roomba.RoombaJSSCSerial;
import controllers.ModuleController;
import core.ConnectionType;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import net.RoombaJSSCClient;
import net.RoombaServerScanner;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ConnectionModuleController extends ModuleController implements Initializable {

    private final static Logger LOGGER = Logger.getLogger(ConnectionModuleController.class);

    @FXML
    private MaterialDesignIconView connectIcon;

    @FXML
    private ComboBox<ConnectionType> connectionTypeComboBox;

    @FXML
    private Label selectedPortStatusLabel;

    @FXML
    private ListView<String> listView;

    @FXML
    public void handleTestConnectionButtonAction(ActionEvent event) {
        if (isConnected()) {
            Thread thread = new Thread(() -> {
                getRoomba().leds(true, true, true, true, 100, 100);
                getRoomba().digitLedsAscii('H', 'E', 'Y', '!');
                getRoomba().sleep(2500);
                getRoomba().leds(false, false, false, false, 0, 0);
                getRoomba().digitLedsAscii(' ', ' ', ' ', ' ');
            });
            thread.start();
        }
    }

    private String extractHostAddress(String item) {
        String[] splitItem = item.split("@");
        RoombaJSSCClient roomba = (RoombaJSSCClient) getRoomba();
        try {
            roomba.setSocket(new Socket(splitItem[1], 13950));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return splitItem[0];
    }

    @FXML
    void handleConnectButtonAction(ActionEvent event) {
        if (!isConnected()) {
            if (!listView.getSelectionModel().isEmpty()) {
                String item = listView.getSelectionModel().getSelectedItem();
                setPort(item);
                if (getRoomba() instanceof RoombaJSSCClient) {
                    item = extractHostAddress(item);
                }
                connect(item);
            }
        } else {
            disconnect();
        }
    }

    private void loadUsbDevices() {
        listView.getItems().clear();
        listView.getItems().addAll(getPorts());
        listView.getItems().add("DEBUG");
    }

    private void loadWifiDevices() {
        listView.getItems().clear();
        NetworkScannerService service = new NetworkScannerService();
        service.start();
    }

    private void populateListView() {
        ConnectionType connectionType = connectionTypeComboBox.getValue();
        if (connectionType == ConnectionType.USB) {
            loadUsbDevices();
            LOGGER.debug("USB devices loaded");
        } else if (connectionType == ConnectionType.Wifi) {
            loadWifiDevices();
            LOGGER.debug("Wifi devices loaded");
        }
    }

    @FXML
    void handleConnectionTypeButtonAction(ActionEvent event) {
        selectedPortStatusLabel.setText("Not Selected!");
        ConnectionType connectionType = connectionTypeComboBox.getValue();
        setConnectionType(connectionType);
        if (connectionType == ConnectionType.USB) {
            setRoomba(new RoombaJSSCSerial());
        } else if (connectionType == ConnectionType.Wifi) {
            setRoomba(new RoombaJSSCClient());
        }
        populateListView();
    }

    @FXML
    void handleRefreshButtonAction(ActionEvent event) {
        populateListView();
        selectedPortStatusLabel.setText("Not Selected!");
        updateSelectedDeviceStatusLabel();
        LOGGER.debug("listView refreshed");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connectionTypeComboBox.getItems().addAll(ConnectionType.values());
        connectionTypeComboBox.getSelectionModel().selectFirst();
        LOGGER.trace("Initialized connectionTypeComboBox");
        Event.fireEvent(connectionTypeComboBox, new ActionEvent());
        LOGGER.trace("Triggering connectionTypeComboBox actionEventHandler");

        listView.setOnMouseClicked(e -> {
            if (!listView.getSelectionModel().isEmpty()) {
                selectedPortStatusLabel.setText(listView.getSelectionModel().getSelectedItem());
            }
        });

        connectedProperty().addListener((observable, oldValue, newValue) -> {
            updateSelectedDeviceStatusLabel();
            setDisabledNodes(newValue);
        });

        if (isConnected()) {
            resumeState();
        }
    }

    private void setDisabledNodes(boolean connected) {
        connectionTypeComboBox.setDisable(connected);
        listView.setDisable(connected);
        setConnectIconDisable();
    }

    private void resumeState() {
        connectionTypeComboBox.getSelectionModel().select(getConnectionType());
        listView.getItems().clear();
        listView.getItems().add(getPort());
        setDisabledNodes(isConnected());
        updateSelectedDeviceStatusLabel();
    }

    private void updateSelectedDeviceStatusLabel() {
        if (isConnected()) {
            selectedPortStatusLabel.setText(getPort());
            selectedPortStatusLabel.getStyleClass().clear();
            selectedPortStatusLabel.getStyleClass().add("okay-status-font");
        } else {
//            selectedPortStatusLabel.setText("Not Selected!");
            selectedPortStatusLabel.getStyleClass().clear();
            selectedPortStatusLabel.getStyleClass().add("error-status-font");
        }
    }

    /*
     * Sets icon for connect button
     * @param disable If true, sets icon to close.
     * */
    private void setConnectIconDisable() {
        if (isConnected()) {
            connectIcon.getStyleClass().clear();
            connectIcon.getStyleClass().add("close");
        } else {
            connectIcon.getStyleClass().clear();
            connectIcon.getStyleClass().add("check");
        }
    }

    /*
     * Searches local area network (LAN) for hosts running the
     * roomba protocol server using a thread running in the background.
     * */
    private class NetworkScannerService extends Service<Void> {

        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() {
                    ArrayList<String> arrayList = RoombaServerScanner.scan();
                    LOGGER.debug("Scanning network for app servers");
                    Platform.runLater(() -> {
                        listView.getItems().addAll(arrayList);
                        LOGGER.debug("Updating listView with results");
                        //updateStatusLabel();
                    });
                    return null;
                }
            };
        }

    }

}
