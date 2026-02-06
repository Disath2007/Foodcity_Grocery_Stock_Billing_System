package GUI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import javax.swing.JButton;
import javax.swing.JTextField;

/**
 * Test class for LoginFrame
 * 
 * @author Disath Damsutha
 */
public class LoginFrameTest {

    private LoginFrame instance;

    public LoginFrameTest() {
    }

    @Before
    public void setUp() {
        instance = new LoginFrame();
    }

    @After
    public void tearDown() {
        if (instance != null) {
            instance.dispose();
        }
    }

    /**
     * Test that LoginFrame initializes correctly.
     */
    @Test
    public void testInitialization() {
        assertNotNull("LoginFrame instance should be created", instance);
        assertEquals("Title should match", "Foodcity Grocery System – Login", instance.getTitle());
        assertFalse("Frame should not be resizable", instance.isResizable());
    }

    /**
     * Test that essential UI components are initialized and accessible.
     */
    @Test
    public void testComponentsExist() throws Exception {
        JTextField txtUsername = (JTextField) getPrivateField("txtusername");
        JTextField txtPassword = (JTextField) getPrivateField("txtpassword");
        JButton btnLogin = (JButton) getPrivateField("btnlogin");

        assertNotNull("Username field should exist", txtUsername);
        assertNotNull("Password field should exist", txtPassword);
        assertNotNull("Login button should exist", btnLogin);

        assertEquals("Initial username should be empty", "", txtUsername.getText());
        assertEquals("Initial password should be empty", "", txtPassword.getText());
    }

    /**
     * Helper method to access private fields of LoginFrame.
     */
    private Object getPrivateField(String fieldName) throws Exception {
        Field field = LoginFrame.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    /**
     * Test the main method execution.
     */
    @Test
    public void testMain() {
        // Just verify it doesn't throw exception when called
        String[] args = {};
        try {
            // We don't want to actually show the frame for long in automated tests
            // but we can test if it starts up.
            LoginFrame.main(args);
        } catch (Exception e) {
            fail("Main method threw exception: " + e.getMessage());
        }
    }
}
