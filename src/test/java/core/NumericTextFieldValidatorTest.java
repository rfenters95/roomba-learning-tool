package core;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumericTextFieldValidatorTest {

    private final int lowerLimit = -500;
    private final int upperLimit = 500;

    @Before
    public void setUp() {
        new JFXPanel();
    }

    private String toString(int value) {
        return String.valueOf(value);
    }

    @Test
    public void changed() {
        final int defaultValue = 0;

        // Test : only permitted non-numeric symbol -
        {
            TextField textField = new TextField(toString(defaultValue));

            NumericTextFieldValidator ledValidator = new NumericTextFieldValidator(textField, lowerLimit, upperLimit);

            textField.textProperty().addListener(ledValidator);

            textField.setText("-");

            assertEquals("-", textField.getText());
        }

        // Test : valid input is accepted
        {
            TextField textField = new TextField(toString(defaultValue));

            NumericTextFieldValidator ledValidator = new NumericTextFieldValidator(textField, lowerLimit, upperLimit);

            textField.textProperty().addListener(ledValidator);

            textField.setText(toString(100));

            assertEquals(toString(100), textField.getText());
        }

        // Test : lower boundary is accepted
        {
            TextField textField = new TextField(toString(defaultValue));

            NumericTextFieldValidator ledValidator = new NumericTextFieldValidator(textField, lowerLimit, upperLimit);

            textField.textProperty().addListener(ledValidator);

            textField.setText(toString(lowerLimit));

            assertEquals(toString(lowerLimit), textField.getText());
        }

        // Test : beyond lower boundary is rejected
        {
            TextField textField = new TextField(toString(defaultValue));

            NumericTextFieldValidator ledValidator = new NumericTextFieldValidator(textField, lowerLimit, upperLimit);

            textField.textProperty().addListener(ledValidator);

            textField.setText(toString(lowerLimit - 1));

            assertEquals(toString(0), textField.getText());
        }

        // Test : upper boundary is accepted
        {
            TextField textField = new TextField(toString(defaultValue));

            NumericTextFieldValidator ledValidator = new NumericTextFieldValidator(textField, lowerLimit, upperLimit);

            textField.textProperty().addListener(ledValidator);

            textField.setText(toString(upperLimit));

            assertEquals(toString(upperLimit), textField.getText());
        }

        // Test : beyond upper boundary is rejected
        {
            TextField textField = new TextField(toString(defaultValue));

            NumericTextFieldValidator ledValidator = new NumericTextFieldValidator(textField, lowerLimit, upperLimit);

            textField.textProperty().addListener(ledValidator);

            textField.setText(toString(upperLimit + 1));

            assertEquals(toString(defaultValue), textField.getText());
        }

        // Test : words are rejected
        {
            TextField textField = new TextField(toString(defaultValue));

            NumericTextFieldValidator ledValidator = new NumericTextFieldValidator(textField, lowerLimit, upperLimit);

            textField.textProperty().addListener(ledValidator);

            textField.setText("abc");

            assertEquals(toString(defaultValue), textField.getText());
        }

        // Test : units are rejected
        {
            TextField textField = new TextField(toString(defaultValue));

            NumericTextFieldValidator ledValidator = new NumericTextFieldValidator(textField, lowerLimit, upperLimit);

            textField.textProperty().addListener(ledValidator);

            textField.setText("100mm");

            assertEquals(toString(defaultValue), textField.getText());
        }

        // Test : leading zeros are trimmed
        {
            TextField textField = new TextField(toString(defaultValue));

            NumericTextFieldValidator ledValidator = new NumericTextFieldValidator(textField, lowerLimit, upperLimit);

            textField.textProperty().addListener(ledValidator);

            textField.setText("00255");

            assertEquals(toString(255), textField.getText());
        }

        // Test : zero layer is removed
        {
            TextField textField = new TextField(toString(defaultValue));

            NumericTextFieldValidator ledValidator = new NumericTextFieldValidator(textField, lowerLimit, upperLimit);

            textField.textProperty().addListener(ledValidator);

            textField.setText("-0255");

            assertEquals(toString(-255), textField.getText());
        }

    }
}