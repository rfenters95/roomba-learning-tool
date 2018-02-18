package alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class DriveDirectInfo extends Alert {

    public DriveDirectInfo() {
        super(AlertType.INFORMATION);

        setTitle("DriveDirect Information");

        getDialogPane().getStylesheets().add("css/Main.css");

        getDialogPane().setPrefWidth(500);

        setHeaderText("DriveDirect Information");

        String message = "This command lets you control the forward and backward motion of Roomba’s drive wheels independently. "
                + "The first parameter specifies the average velocity of the right wheel in millimeters per second (mm/s). "
                + "While the second parameter specifies the average velocity of the left wheel in millimeters per second (mm/s).";

        TextArea textArea = new TextArea(message);
        textArea.setEditable(false);
        textArea.setPrefSize(400, 100);
        textArea.setWrapText(true);

        BorderPane messageRow = new BorderPane(null, null, textArea, null, new Label("Info:"));

        BorderPane velocityRow = new BorderPane(null, null, valueLabel(-500, 500), null, new Label("Right Velocity Input Range:"));

        BorderPane radiusRow = new BorderPane(null, null, valueLabel(-500, 500), null, new Label("Left Velocity Input Range:"));

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
