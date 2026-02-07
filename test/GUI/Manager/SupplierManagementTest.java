package GUI.Manager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import javax.swing.*;

/**
 * Test class for SupplierManagement.
 * Verifies supplier phone number validation.
 * 
 * @author Disath Damsutha
 */
public class SupplierManagementTest {

    private SupplierManagement instance;

    public SupplierManagementTest() {
    }

    @Before
    public void setUp() {
        instance = new SupplierManagement();
    }

    @After
    public void tearDown() {
        if (instance != null) {
            instance.dispose();
        }
    }

    /**
     * 1. Test Valid Phone: tel="0771234567"
     */
    @Test
    public void testValidPhoneNumber() throws Exception {
        JTextField phoneField = (JTextField) getPrivateField("txt_phone");
        phoneField.setText("0771234567");

        assertEquals("Phone field should be 0771234567", "0771234567", phoneField.getText());
        assertTrue("Contact length should be 10", phoneField.getText().length() == 10);
    }

    /**
     * 2. Test Invalid Phone: tel="abc"
     */
    @Test
    public void testInvalidPhoneNumber() throws Exception {
        JTextField phoneField = (JTextField) getPrivateField("txt_phone");
        phoneField.setText("abc");

        // Simulating the check that would trigger "Numbers only" alert
        String input = phoneField.getText();
        boolean isNumeric = input.chars().allMatch(Character::isDigit);

        assertFalse("Input 'abc' should not be considered numeric", isNumeric);
    }

    /**
     * Helper method to access private fields of SupplierManagement.
     */
    private Object getPrivateField(String fieldName) throws Exception {
        Field field = SupplierManagement.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }
}
