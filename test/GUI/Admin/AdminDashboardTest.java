package GUI.Admin;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import javax.swing.*;

/**
 * Test class for AdminDashboard
 * Verifies UI components and basic dashboard functionality.
 * 
 * @author Disath Damsutha
 */
public class AdminDashboardTest {

    private AdminDashboard instance;

    public AdminDashboardTest() {
    }

    @Before
    public void setUp() {
        instance = new AdminDashboard();
    }

    @After
    public void tearDown() {
        if (instance != null) {
            instance.dispose();
        }
    }

    /**
     * Test that AdminDashboard initializes correctly.
     */
    @Test
    public void testInitialization() {
        assertNotNull("AdminDashboard instance should be created", instance);
        assertEquals("Title should match", "Admin Dashboard", instance.getTitle());
        // By default it's set to maximized in constructor
        assertEquals("Should be maximized", javax.swing.JFrame.MAXIMIZED_BOTH, instance.getExtendedState());
    }

    /**
     * Test that essential UI components are initialized and accessible.
     */
    @Test
    public void testComponentsExist() throws Exception {
        JButton btnLogout = (JButton) getPrivateField("btnLogout");
        JButton btnUserManagement = (JButton) getPrivateField("btnUserManagement");
        JLabel lblUsername = (JLabel) getPrivateField("lbl_username");
        JTextArea lowStockArea = (JTextArea) getPrivateField("txt_lowStockArea");

        assertNotNull("Logout button should exist", btnLogout);
        assertNotNull("User Management button should exist", btnUserManagement);
        assertNotNull("Username label should exist", lblUsername);
        assertNotNull("Low stock area should exist", lowStockArea);
    }

    /**
     * Test data loading labels.
     */
    @Test
    public void testStatsLabels() throws Exception {
        JLabel lblProfit = (JLabel) getPrivateField("lblMonthlyProfit");
        JLabel lblTotalSales = (JLabel) getPrivateField("lblshowTotalSales");

        assertNotNull("Profit label exists", lblProfit);
        assertNotNull("Total sales label exists", lblTotalSales);

        // Initially they might show "Loarding..." or "$ 0.00" depending on DB
        // connection
        // We just verify they are not empty
        assertFalse("Profit label should not be empty", lblProfit.getText().isEmpty());
    }

    /**
     * Helper method to access private fields of AdminDashboard.
     */
    private Object getPrivateField(String fieldName) throws Exception {
        Field field = AdminDashboard.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    /**
     * Test the main method execution.
     */
    @Test
    public void testMain() {
        String[] args = {};
        try {
            AdminDashboard.main(args);
        } catch (Exception e) {
            fail("Main method threw exception: " + e.getMessage());
        }
    }
}
