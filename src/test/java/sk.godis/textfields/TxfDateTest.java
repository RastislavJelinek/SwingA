package sk.godis.textfields;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class TxfDateTest {
    private TxfDate txfDate;

    @BeforeEach
    void setUp() {
        txfDate = new TxfDate();
    }
        @Test
        void testDateValidation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

            // Use reflection to access the private method dateValidation
            Method dateValidationMethod = TxfDate.class.getDeclaredMethod("dateValidation", String.class);
            dateValidationMethod.setAccessible(true);

            // Test a valid date
            boolean validDate = (boolean) dateValidationMethod.invoke(txfDate, "01.01.2023");
            Assertions.assertTrue(validDate);

            // Test an invalid date
            boolean invalidDate = (boolean) dateValidationMethod.invoke(txfDate, "32.13.2023");
            Assertions.assertFalse(invalidDate);

            // Test an invalid date that can be automatically set to last day in month
            validDate = (boolean) dateValidationMethod.invoke(txfDate, "32.12.2023");
            Assertions.assertTrue(validDate);
            Assertions.assertEquals("31.12.2023",txfDate.getText());
        }

        @Test
        void testCheckDate() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {

            // Use reflection to access the private method checkDate
            Method checkDateMethod = TxfDate.class.getDeclaredMethod("checkDate", String.class);
            checkDateMethod.setAccessible(true);

            // Test a valid date
            boolean validDate = (boolean) checkDateMethod.invoke(txfDate, "01.01.2023");
            Assertions.assertTrue(validDate);

            // Test an invalid date
            boolean invalidDate = (boolean) checkDateMethod.invoke(txfDate, "32.13.2023");
            Assertions.assertFalse(invalidDate);
        }

        @Test
        void testKeyPressed() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {

            // Use reflection to access the private method keyPressed
            Method keyPressedMethod = TxfDate.class.getDeclaredMethod("keyPressed", KeyEvent.class);
            keyPressedMethod.setAccessible(true);

            // Create a mock KeyEvent
            KeyEvent mockKeyEvent = new KeyEvent(txfDate, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE);

            // Set the caret position using reflection
            Field caretPositionField = TxfDate.class.getDeclaredField("caretPosition");
            caretPositionField.setAccessible(true);
            caretPositionField.set(txfDate, 1);

            // Invoke the keyPressed method
            keyPressedMethod.invoke(txfDate, mockKeyEvent);

            String textValue = txfDate.getText();
            Assertions.assertEquals("00.00.2023", textValue);

            Field caretPositionValueField = TxfDate.class.getDeclaredField("caretPosition");
            caretPositionValueField.setAccessible(true);
            int caretPositionValue = (int) caretPositionValueField.get(txfDate);
            Assertions.assertEquals(0, caretPositionValue);
        }
}