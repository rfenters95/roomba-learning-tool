package alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class DriveInfo extends Alert {

    public DriveInfo() {
        super(AlertType.INFORMATION);

        setTitle("Drive Information");

        getDialogPane().getStylesheets().add("css/Main.css");

        getDialogPane().setPrefWidth(500);

        setHeaderText("Drive Information");

        String message = "This module controls Roomba’s drive wheels. "
                + "The first parameter specifies the average velocity of the drive wheels in millimeters per second (mm/s). "
                + "While the second parameter specifies the radius is measured from the center of the turning circle to"
                + "the center of Roomba in millimeters (mm).";

        Label label = new Label(message);
        label.getStyleClass().clear();
        label.getStyleClass().add("normal-font");
        label.setWrapText(true);
        label.setPrefWidth(275);

        BorderPane messageRow = new BorderPane(null, null, label, null, new Label("Info:"));

        BorderPane velocityRow = new BorderPane(null, null, valueLabel(-500, 500), null, new Label("Velocity Input Range:"));

        BorderPane radiusRow = new BorderPane(null, null, valueLabel(-2000, 2000), null, new Label("Radius Input Range:"));

        VBox content = new VBox(new Label(), messageRow, new Label(), velocityRow, radiusRow, new Label());

        content.getStyleClass().add("dialog-pane-content-vBox");

        getDialogPane().setContent(content);
    }

    private Label valueLabel(int lowerLimit, int upperLimit) {

        String lower = String.valueOf(lowerLimit);

        String upper = String.valueOf(upperLimit);

        String text = String.format("%s <= User input <= %s", lower, upper);

        Label label = new Label(String.valueOf(text));

        label.getStyleClass().clear();

        label.getStyleClass().add("normal-font");

        return label;
    }

}
