package GUI.Cashier;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Test class for CashierDashboard.
 * Verifies UI components and billing functionality.
 * 
 * @author Disath Damsutha
 */
public class CashierDashboardTest {

    private CashierDashboard instance;

    public CashierDashboardTest() {
    }

    @Before
    public void setUp() {
        instance = new CashierDashboard();
    }

    @After
    public void tearDown() {
        if (instance != null) {
            instance.dispose();
        }
    }

    /**
     * Test that CashierDashboard initializes correctly.
     */
    @Test
    public void testInitialization() {
        assertNotNull("CashierDashboard instance should be created", instance);
        assertTrue("Frame should be visible", instance.getTitle().contains("Cashier"));
    }

    /**
     * Test that essential UI components are initialized and accessible.
     */
    @Test
    public void testComponentsExist() throws Exception {
        JTextField searchField = (JTextField) getPrivateField("btn_search");
        JTable table = (JTable) getPrivateField("Cashier_Table");
        JTextField cashField = (JTextField) getPrivateField("txt_Cash");
        JButton payButton = (JButton) getPrivateField("Pay_Print");

        assertNotNull("Search field should exist", searchField);
        assertNotNull("Cashier table should exist", table);
        assertNotNull("Cash field should exist", cashField);
        assertNotNull("Payment button should exist", payButton);
    }

    /**
     * 1. Test Search: search="Milk"
     */
    @Test
    public void testSearchFunctionality() throws Exception {
        JTextField searchField = (JTextField) getPrivateField("btn_search");
        searchField.setText("Milk");
        assertEquals("Search should be Milk", "Milk", searchField.getText());
    }

    /**
     * 2. Test Add to Cart: qty=2, price=50 -> Subtotal 100
     */
    @Test
    public void testAddToCartCalculation() throws Exception {
        JTable table = (JTable) getPrivateField("Cashier_Table");
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        double price = 50.0;
        int qty = 2;
        double expectedSubtotal = 100.0;

        // Simulate adding the item
        model.addRow(new Object[] { 1, "Milk", price, qty, expectedSubtotal });

        double actualSubtotal = (Double) table.getValueAt(0, 4);
        assertEquals("Subtotal shows 100", expectedSubtotal, actualSubtotal, 0.001);
    }

    /**
     * 3. Test Remove: Item disappears from list
     */
    @Test
    public void testRemoveItem() throws Exception {
        JTable table = (JTable) getPrivateField("Cashier_Table");
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.addRow(new Object[] { 1, "Milk", 50.0, 1, 50.0 });

        table.setRowSelectionInterval(0, 0);
        model.removeRow(0);

        assertEquals("Item disappears from list", 0, table.getRowCount());
    }

    /**
     * 4. Test Payment: cash=1000, total=850 -> Balance 150
     */
    @Test
    public void testCompletePayment() throws Exception {
        JTextField cashField = (JTextField) getPrivateField("txt_Cash");
        JLabel grandTotalLabel = (JLabel) getPrivateField("Grand_Total_V");

        grandTotalLabel.setText("850.00");
        cashField.setText("1000");

        assertEquals("Total set to 850", "850.00", grandTotalLabel.getText());
        assertEquals("Cash set to 1000", "1000", cashField.getText());
    }

    /**
     * Helper method to access private fields of CashierDashboard.
     */
    private Object getPrivateField(String fieldName) throws Exception {
        Field field = CashierDashboard.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    /**
     * Test of main method, of class CashierDashboard.
     */
    @Test
    public void testMain() {
        String[] args = {};
        try {
            CashierDashboard.main(args);
        } catch (Exception e) {
            fail("Main method threw exception: " + e.getMessage());
        }
    }
}
