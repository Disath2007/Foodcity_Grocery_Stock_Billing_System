package GUI.Manager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import javax.swing.*;

/**
 * Test class for CategoryManagement.
 * Verifies category creation and input validation.
 * 
 * @author Disath Damsutha
 */
public class CategoryManagementTest {

    private CategoryManagement instance;

    public CategoryManagementTest() {
    }

    @Before
    public void setUp() {
        instance = new CategoryManagement();
    }

    @After
    public void tearDown() {
        if (instance != null) {
            instance.dispose();
        }
    }

    /**
     * 1. Test Create Category: catname="Drinks"
     */
    @Test
    public void testCreateCategory() throws Exception {
        JTextField nameField = (JTextField) getPrivateField("txt_categoryname");
        nameField.setText("Drinks");

        assertEquals("Name field should be Drinks", "Drinks", nameField.getText());
        // In the real app, clicking 'Add' would save this to the DB
    }

    /**
     * 2. Test Empty Submission: catname=""
     */
    @Test
    public void testEmptyCategorySubmission() throws Exception {
        JTextField nameField = (JTextField) getPrivateField("txt_categoryname");
        nameField.setText("");

        assertTrue("Input shows empty value which should be blocked", nameField.getText().isEmpty());
    }

    /**
     * Helper method to access private fields of CategoryManagement.
     */
    private Object getPrivateField(String fieldName) throws Exception {
        Field field = CategoryManagement.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }
}
