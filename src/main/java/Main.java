import controllers.ModuleController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends Application {

    private final static Logger LOGGER = Logger.getLogger(Main.class);

    /* Adds current date to properties, for use in naming log files */
    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ssa");
        System.setProperty("current.date", dateFormat.format(new Date()));
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);

        // Load FXML
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/modules/main_module.fxml"));
        Parent root = loader.load();
        LOGGER.trace("FXMLLoader has loaded main_module.fxml");

        // Configure & display stage
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(new CloseAction());
        primaryStage.show();
        LOGGER.trace("Stage is showing");
    }

    private class CloseAction implements EventHandler<WindowEvent> {
        @Override
        public void handle(WindowEvent event) {
            if (ModuleController.isConnected()) {
                ModuleController.disconnect();
                ModuleController.shutdown();
            }
            Platform.runLater(Platform::exit);
        }
    }
}
