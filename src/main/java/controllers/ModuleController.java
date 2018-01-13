package controllers;

import com.maschel.roomba.RoombaJSSC;
import com.maschel.roomba.RoombaJSSCSerial;
import core.ConnectionType;
import core.StartMode;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import org.apache.log4j.Logger;

public abstract class ModuleController {

    private final static Logger LOGGER = Logger.getLogger(ModuleController.class);

    private static SimpleObjectProperty<RoombaJSSC> roomba = new SimpleObjectProperty<>(null);
    private static SimpleObjectProperty<ConnectionType> connectionType = new SimpleObjectProperty<>(null);
    private static SimpleObjectProperty<StartMode> startMode = new SimpleObjectProperty<>(null);
    private static SimpleObjectProperty<String> port = new SimpleObjectProperty<>(null);
    private static SimpleBooleanProperty connected = new SimpleBooleanProperty(false);
    private static SimpleBooleanProperty readingSensors = new SimpleBooleanProperty(false);


    public static boolean isConnected() {
        return connected.get();
    }

    private static void setConnected(boolean connected) {
        ModuleController.connected.set(connected);
    }

    protected static SimpleBooleanProperty connectedProperty() {
        return connected;
    }

    static void startUp(StartMode startMode) {
        switch (startMode) {
            case Full:
                ModuleController.roomba.get().fullMode();
                break;
            case Safe:
                ModuleController.roomba.get().safeMode();
                break;
            default:
                break;
        }
        setStartMode(startMode);
    }

    protected static void connect(String port) {
        ModuleController.roomba.get().connect(port);
        ModuleController.roomba.get().start();
        ModuleController.connected.set(true);
    }

    protected static String[] getPorts() {
        return new RoombaJSSCSerial().portList();
    }

    public static void disconnect() {
        ModuleController.setReadingSensors(false);
        ModuleController.roomba.get().stop();
        LOGGER.debug("Roomba stopped");
        ModuleController.roomba.get().disconnect();
        LOGGER.debug("Roomba disconnected");
        ModuleController.setConnected(false);
    }

    public static RoombaJSSC getRoomba() {
        return roomba.get();
    }

    public static void setRoomba(RoombaJSSC roomba) {
        ModuleController.roomba.set(roomba);
    }

    public static SimpleObjectProperty<RoombaJSSC> roombaProperty() {
        return roomba;
    }

    protected static ConnectionType getConnectionType() {
        return connectionType.get();
    }

    protected static void setConnectionType(ConnectionType connectionType) {
        ModuleController.connectionType.set(connectionType);
    }

    public static SimpleObjectProperty<ConnectionType> connectionTypeProperty() {
        return connectionType;
    }

    public static StartMode getStartMode() {
        return startMode.get();
    }

    private static void setStartMode(StartMode startMode) {
        ModuleController.startMode.set(startMode);
    }

    public static SimpleObjectProperty<StartMode> startModeProperty() {
        return startMode;
    }

    protected static String getPort() {
        return port.get();
    }

    protected static void setPort(String port) {
        ModuleController.port.set(port);
    }

    public static SimpleObjectProperty<String> portProperty() {
        return port;
    }

    public static boolean isReadingSensors() {
        return readingSensors.get();
    }

    protected static void setReadingSensors(boolean readingSensors) {
        ModuleController.readingSensors.set(readingSensors);
    }

    public static SimpleBooleanProperty readingSensorsProperty() {
        return readingSensors;
    }

    public static void toggleNodeStatus(Node node, String oldGlyph, String newGlyph) {
        node.getStyleClass().remove(oldGlyph);
        node.getStyleClass().add(newGlyph);
    }
}
