package core;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumericTextFieldValidatorTest {

    private TextField ledTextField;
    private TextField velocityTextField;


    @Before
    public void setUp() {
        new JFXPanel();
        ledTextField = new TextField("0");
        velocityTextField = new TextField("0");
        NumericTextFieldValidator ledValidator = new NumericTextFieldValidator(ledTextField, 0, 255);
        NumericTextFieldValidator velocityValidator = new NumericTextFieldValidator(velocityTextField, -500, 500);
        ledTextField.textProperty().addListener(ledValidator);
        velocityTextField.textProperty().addListener(velocityValidator);
    }

    @Test
    public void changed() {
        ledTextField.setText("-");
        assertEquals("0", ledTextField.getText());
        ledTextField.setText("-1");
        assertEquals("0", ledTextField.getText());
        ledTextField.setText("1");
        assertEquals("1", ledTextField.getText());
        ledTextField.setText("255");
        assertEquals("255", ledTextField.getText());
        ledTextField.setText("256");
        assertEquals("255", ledTextField.getText());
        ledTextField.setText("abc");
        assertEquals("255", ledTextField.getText());
        ledTextField.setText("00255");
        assertEquals("255", ledTextField.getText());
        ledTextField.setText("-0255");
        assertEquals("255", ledTextField.getText());

        velocityTextField.setText("-1000");
        assertEquals("0", velocityTextField.getText());
        velocityTextField.setText("-500");
        assertEquals("-500", velocityTextField.getText());
        velocityTextField.setText("0");
        assertEquals("0", velocityTextField.getText());
        velocityTextField.setText("300");
        assertEquals("300", velocityTextField.getText());
        velocityTextField.setText("500");
        assertEquals("500", velocityTextField.getText());
        velocityTextField.setText("-");
        assertEquals("-", velocityTextField.getText());
        velocityTextField.setText("0001");
        assertEquals("1", velocityTextField.getText());
        velocityTextField.setText("-0001");
        assertEquals("-1", velocityTextField.getText());
        velocityTextField.setText("0-12");
        assertEquals("-1", velocityTextField.getText());
        velocityTextField.setText("abc");
        assertEquals("-1", velocityTextField.getText());
    }
}