package controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/* Enum containing all app modules. */
public enum Module {
    Drive(new File("/fxml/modules/drive/drive_module.fxml"), "Drive Module", true),
    LED(new File("/fxml/modules/led/led_module.fxml"), "LED Module", true),
    Sensor(new File("/fxml/modules/sensor/sensor_module.fxml"), "Sensor Module", true),
    Song(new File("/fxml/modules/song/song_module.fxml"), "Song Module", true),
    Information(new File("/fxml/modules/information/information_module.fxml"), "Information Module", true),
    Connection(new File("/fxml/modules/connection/connection_module.fxml"), "Connection Module", false);

    private final static Logger LOGGER = Logger.getLogger(Module.class);

    private File file;
    private String title;
    private boolean isShowing;
    private boolean closable;
    private Stage stage;

    /*
     * Constructor for Enum Module
     * @param file Path to the modules FXML file.
     * @param title Title of the modules window.
     * */
    Module(File file, String title, boolean closable) {
        this.file = file;
        this.title = title;
        this.closable = closable;
    }

    /*
     * Moves Stage to the front and center of the screen.
     * @param stage The Stage that will be moved.
     * */
    private void frontAndCenter(Stage stage) {
        stage.toFront();
        stage.centerOnScreen();
    }

    /*
     * Loads FXML file and constructs a parent node.
     * @return Parent Parent node of FXML file.
     * */
    private Parent getParent(File file) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(file.toString()));
            Parent root = loader.load();
            LOGGER.trace("FXMLLoader has loaded " + file.getName());
            return root;
        } catch (IOException e) {
            LOGGER.fatal(e.getMessage(), e);
            Platform.exit();
            return null;
        }
    }

    /*
     * Constructs a Stage for each module with a consistent configuration.
     * @return Stage for a module.
     * */
    private Stage createStage(Parent root) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(e -> {
            if (closable) {
                isShowing = false;
            } else {
                e.consume();
                stage.setIconified(true);
            }
        });
        return stage;
    }

    /* Opens a new window for the calling module. */
    public void open() {
        if (isShowing()) {
            frontAndCenter(stage);
        } else {
            try {
                // Configure & show stage
                stage = createStage(getParent(file));
                stage.show();
                setShowing(true);
                LOGGER.trace(title + " is showing");
            } catch (Exception e) {
                // Missing FXML file
                LOGGER.fatal(e.getMessage(), e);
                Platform.exit();
            }
        }
    }

    /* Returns if module isShowing. */
    public boolean isShowing() {
        return isShowing;
    }

    /* Set if module isShowing */
    public void setShowing(boolean showing) {
        isShowing = showing;
    }

    /* Returns a reference to the stage */
    public Stage getStage() {
        return stage;
    }
}
