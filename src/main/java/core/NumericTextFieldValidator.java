package core;

import alerts.InvalidInputAlert;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class NumericTextFieldValidator implements ChangeListener<String> {

    private final static Logger LOGGER = Logger.getLogger(NumericTextFieldValidator.class);

    private TextField textField;
    private int lowerLimit;
    private int upperLimit;
    private ArrayList<Integer> specialValues;
    private boolean shouldPreventNegatives;

    public NumericTextFieldValidator(TextField textField, int lowerLimit, int upperLimit) {
        this.textField = textField;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        specialValues = new ArrayList<>();
        shouldPreventNegatives = lowerLimit >= 0;
    }

    public void addSpecialValue(int value) {
        specialValues.add(value);
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

        if (!isValueNumeric(newValue)) {
            textField.setText(oldValue);
            return;
        }

        if (newValue.isEmpty()) {
            textField.setText("0");
            return;
        }

        if (!hasNumericFragment(newValue)) {

            int value = Integer.parseInt(newValue);

            if (hasLeadingZeros(newValue)) {
                textField.setText(String.valueOf(value));
                return;
            }

            if (isNegative(value) && shouldPreventNegatives) {
                textField.setText(oldValue);
                return;
            }

            if (hasZeroLayer(newValue) && isNegative(value)) {
                textField.setText(String.valueOf(value));
                return;
            }

            if (isOutOfBounds(value) && !isSpecialValue(value)) {

                Alert alert = new InvalidInputAlert(lowerLimit, value, upperLimit);

                alert.showAndWait(); // Comment this out to test

                textField.setText(oldValue);

                return;
            }

        } else {
            if (shouldPreventNegatives) {
                textField.setText(oldValue);
                return;
            }

            if (newValue.matches("0-|-0") && !shouldPreventNegatives) {
                textField.setText("-");
                return;
            }
        }
    }

    private boolean isValueNumeric(String value) {
        return value.matches("-|0-|(-\\d|\\d)\\d*");
    }

    private boolean hasNumericFragment(String value) {
        return value.matches("-|0-|-0");
    }

    private boolean hasLeadingZeros(String value) {
        return value.matches("0+\\d+");
    }

    private boolean hasZeroLayer(String value) {
        return value.matches("-(0+)(\\d+)");
    }

    private boolean isNegative(int value) {
        return value < 0;
    }

    private boolean isOutOfBounds(int value) {
        return value < lowerLimit || value > upperLimit;
    }

    private boolean isSpecialValue(int value) {
        return specialValues.contains(value);
    }

}
