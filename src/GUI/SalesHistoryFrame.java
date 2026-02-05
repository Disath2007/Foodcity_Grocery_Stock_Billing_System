package GUI;

import controller.SalesController;
import model.Sale;
import model.SaleItem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.text.SimpleDateFormat;

public class SalesHistoryFrame extends javax.swing.JFrame {

    private final SalesController salesController;
    private JTable salesTable;
    private JTable itemsTable;
    private DefaultTableModel salesModel;
    private DefaultTableModel itemsModel;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SalesHistoryFrame() {
        salesController = new SalesController();
        initComponents();
        loadSalesData();
        setTitle("Transaction History - Green Leaf");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        // Layout setup
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(243, 247, 244));

        // Sales Table (Master)
        salesModel = new DefaultTableModel(
                new Object[] { "ID", "Cashier", "Subtotal", "Discount", "Grand Total", "Cash", "Balance", "Date" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        salesTable = new JTable(salesModel);
        salesTable.setShowGrid(true);
        salesTable.setGridColor(new Color(200, 220, 200));
        salesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        salesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = salesTable.getSelectedRow();
                if (selectedRow != -1) {
                    int saleId = (Integer) salesModel.getValueAt(selectedRow, 0);
                    loadItemsData(saleId);
                }
            }
        });

        // Items Table (Detail)
        itemsModel = new DefaultTableModel(
                new Object[] { "Item ID", "Product ID", "Quantity", "Unit Price", "Total Price" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        itemsTable = new JTable(itemsModel);
        itemsTable.setShowGrid(true);
        itemsTable.setGridColor(new Color(200, 220, 200));

        // Styling
        styleTable(salesTable);
        styleTable(itemsTable);

        // Panes
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(salesTable), new JScrollPane(itemsTable));
        splitPane.setDividerLocation(300);
        splitPane.setBackground(new Color(243, 247, 244));

        JLabel lblHeader = new JLabel("TRANSACTION HISTORY", JLabel.CENTER);
        lblHeader.setFont(new Font("Unispace", Font.BOLD, 24));
        lblHeader.setForeground(new Color(5, 63, 22));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        mainPanel.add(lblHeader, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        JButton btnRefresh = new JButton("REFRESH RECORDS");
        btnRefresh.setBackground(new Color(147, 202, 55));
        btnRefresh.setFont(new Font("Unispace", Font.BOLD, 14));
        btnRefresh.setForeground(new Color(5, 63, 22));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadSalesData());
        mainPanel.add(btnRefresh, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        setSize(1000, 700);
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(147, 202, 55));
        table.setSelectionForeground(new Color(5, 63, 22));

        // Header Styling
        table.getTableHeader().setPreferredSize(new Dimension(100, 40));
        table.getTableHeader().setFont(new Font("Unispace", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(5, 63, 22)); // Dark Green
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setOpaque(true);

        // Custom Header Renderer to ensure colors are applied in all Look and Feels
        table.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                label.setBackground(new Color(5, 63, 22)); // Dark Green
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Unispace", Font.BOLD, 13));
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(BorderFactory.createLineBorder(new Color(147, 202, 55), 1));
                return label;
            }
        });
    }

    private void loadSalesData() {
        salesModel.setRowCount(0);
        List<Sale> sales = salesController.getAllSales();
        for (Sale s : sales) {
            salesModel.addRow(new Object[] {
                    s.getSaleId(),
                    s.getCashierName(),
                    s.getSubtotal(),
                    s.getDiscount(),
                    s.getGrandTotal(),
                    s.getCashReceived(),
                    s.getBalance(),
                    dateFormat.format(s.getSaleDate())
            });
        }
    }

    private void loadItemsData(int saleId) {
        itemsModel.setRowCount(0);
        List<SaleItem> items = salesController.getItemsBySaleId(saleId);
        for (SaleItem i : items) {
            itemsModel.addRow(new Object[] {
                    i.getItemId(),
                    i.getProductId(),
                    i.getQuantity(),
                    i.getUnitPrice(),
                    i.getTotalPrice()
            });
        }
    }
}
