package GUI.Manager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import javax.swing.*;

/**
 * Test class for ManagerDashboard.
 * Verifies UI components and dashboard data loading.
 * 
 * @author Disath Damsutha
 */
public class ManagerDashboardTest {

    private ManagerDashboard instance;

    public ManagerDashboardTest() {
    }

    @Before
    public void setUp() {
        instance = new ManagerDashboard();
    }

    @After
    public void tearDown() {
        if (instance != null) {
            instance.dispose();
        }
    }

    /**
     * Test that ManagerDashboard initializes correctly.
     */
    @Test
    public void testInitialization() {
        assertNotNull("ManagerDashboard instance should be created", instance);
        // Title from source is "Admin Dashboard" (likely a copy-paste error in source
        // but we test current state)
        assertTrue("Title should contain Dashboard", instance.getTitle().contains("Dashboard"));
    }

    /**
     * Test that essential UI components are initialized and accessible.
     */
    @Test
    public void testComponentsExist() throws Exception {
        JButton btnProduct = (JButton) getPrivateField("btnproductmanagement");
        JButton btnStock = (JButton) getPrivateField("btnStockmanagement");
        JLabel lblProfit = (JLabel) getPrivateField("lblMonthlyProfit");
        JTextArea lowStockArea = (JTextArea) getPrivateField("txt_lowStockArea");

        assertNotNull("Product Management button should exist", btnProduct);
        assertNotNull("Stock Management button should exist", btnStock);
        assertNotNull("Profit label should exist", lblProfit);
        assertNotNull("Low stock area should exist", lowStockArea);
    }

    /**
     * Test data loading - verification that stats labels are not empty.
     */
    @Test
    public void testStatsLoading() throws Exception {
        JLabel lblTotalSales = (JLabel) getPrivateField("lblshowTotalSales");
        assertNotNull("Total sales label exists", lblTotalSales);
        assertFalse("Total sales label should have content", lblTotalSales.getText().isEmpty());
    }

    /**
     * Helper method to access private fields of ManagerDashboard.
     */
    private Object getPrivateField(String fieldName) throws Exception {
        Field field = ManagerDashboard.class.getDeclaredField(fieldName);
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
            ManagerDashboard.main(args);
        } catch (Exception e) {
            fail("Main method threw exception: " + e.getMessage());
        }
    }
}
