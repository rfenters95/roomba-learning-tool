package core;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

        // check if newValue is num
        if (newValue.matches("-|0-|(-\\d|\\d)\\d*")) {
            LOGGER.trace(String.format("newValue [%s] has matched pattern!", newValue));
            if (!newValue.matches("-|0-|-0")) {
                int value = Integer.parseInt(newValue);
                if (newValue.matches("0+\\d+")) {
                    LOGGER.trace(String.format("newValue [%s] rejected! Value contains leading zeros.", newValue));
                    textField.setText(String.valueOf(value));
                } else if (newValue.matches("-(0+)(\\d+)") && shouldPreventNegatives) {
                    textField.setText(oldValue);
                } else if (newValue.matches("-(0+)(\\d+)") && !shouldPreventNegatives) {
                    value = Integer.parseInt(newValue.substring(1));
                    value *= -1;
                    textField.setText(String.valueOf(value));
                } else {
                    // check if newValue is valid num
                    if ((value < lowerLimit || value > upperLimit) && !specialValues.contains(value)) {
                        LOGGER.trace(String.format("newValue [%s] rejected! Value exceeds limits.", newValue));
                        textField.setText(oldValue);
                    } else {
                        LOGGER.trace(String.format("newValue [%s] accepted!", newValue));
                    }
                }
            } else {
                if (newValue.matches("0-|-0") && !shouldPreventNegatives) {
                    LOGGER.trace(String.format("newValue [%s] simplified to [-]!", newValue));
                    textField.setText("-");
                } else if (newValue.matches("-|0-|-0") && shouldPreventNegatives) {
                    LOGGER.trace("[-] symbol prevented! lowerLimit is nonNegative!");
                    textField.setText(oldValue);
                }
            }
        } else if (newValue.isEmpty()) {
            LOGGER.trace("newValue is empty! TextField set to zero.");
            textField.setText("0");
        } else {
            LOGGER.trace(String.format("newValue [%s] rejected! Value does not match pattern.", newValue));
            textField.setText(oldValue);
        }
    }

}
