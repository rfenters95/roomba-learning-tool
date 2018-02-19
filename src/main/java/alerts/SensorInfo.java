package alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class SensorInfo extends Alert {

    public SensorInfo() {
        super(AlertType.INFORMATION);

        setTitle("Sensor Information");

        getDialogPane().getStylesheets().add("css/Main.css");

        getDialogPane().setPrefWidth(500);

        setHeaderText("Sensor Information");

        String message = "This module lets read the Roomba's sensors. ";

        Label label = new Label(message);
        label.getStyleClass().clear();
        label.getStyleClass().add("normal-font");
        label.setWrapText(true);
        label.setPrefWidth(275);

        BorderPane messageRow = new BorderPane(null, null, label, null, new Label("Info:"));

        VBox content = new VBox(new Label(), messageRow, new Label());

        content.getStyleClass().add("dialog-pane-content-vBox");

        getDialogPane().setContent(content);
    }

}
