package alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class LedInfo extends Alert {

    public LedInfo() {
        super(AlertType.INFORMATION);

        setTitle("Led Information");

        getDialogPane().getStylesheets().add("css/Main.css");

        getDialogPane().setPrefWidth(500);

        setHeaderText("Led Information");

        String message = "This module controls the Roomba's LEDs. " +
                "This includes the four on/off leds (debris, spot, dock, check robot). " +
                "As well as the power led which allows users to specify two values from 0 to 255 " +
                "which determine the power led's color and intensity. ";


        Label label = new Label(message);
        label.getStyleClass().clear();
        label.getStyleClass().add("normal-font");
        label.setWrapText(true);
        label.setPrefWidth(275);

        BorderPane messageRow = new BorderPane(null, null, label, null, new Label("Info:"));

        BorderPane powerColorRow = new BorderPane(null, null, valueLabel(0, 255), null, new Label("Power Color Input Range:"));

        BorderPane powerIntensityRow = new BorderPane(null, null, valueLabel(0, 255), null, new Label("Power Intensity Input Range:"));

        VBox content = new VBox(new Label(), messageRow, new Label(), powerColorRow, powerIntensityRow, new Label());

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
