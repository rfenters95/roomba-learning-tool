package alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class InvalidInputAlert extends Alert {

    public InvalidInputAlert(int lowerLimit, int value, int upperLimit) {
        super(AlertType.INFORMATION);

        getDialogPane().getStylesheets().add("css/Main.css");

        getDialogPane().setPrefWidth(450);

        setHeaderText("Invalid input");

        BorderPane messageRow = new BorderPane(null, null, messageLabel("Input value exceeded acceptable bounds!"), null, new Label("Message:"));

        BorderPane lowerBoundRow = new BorderPane(null, null, limitLabel(lowerLimit), null, new Label("Lower Bound:"));

        BorderPane valueRow = new BorderPane(null, null, valueLabel(value), null, new Label("Value:"));

        BorderPane upperBoundRow = new BorderPane(null, null, limitLabel(upperLimit), null, new Label("Upper Bound:"));

        VBox content = new VBox(new Label(), messageRow, new Label(), lowerBoundRow, valueRow, upperBoundRow, new Label());

        content.getStyleClass().add("dialog-pane-content-vBox");

        getDialogPane().setContent(content);
    }

    private Label messageLabel(String message) {

        Label label = new Label(message);

        label.getStyleClass().clear();

        label.getStyleClass().add("normal-font");

        return label;
    }

    private Label limitLabel(int value) {

        Label label = new Label(String.valueOf(value));

        label.getStyleClass().clear();

        label.getStyleClass().add("normal-font");

        return label;
    }

    private Label valueLabel(int value) {

        Label label = new Label(String.valueOf(value));

        label.getStyleClass().clear();

        label.getStyleClass().add("error-status-font");

        return label;
    }

}
