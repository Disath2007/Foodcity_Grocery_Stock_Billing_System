/*
 * Stock Management System
 * This class handles inventory tracking and stock updates.
 */
package GUI.Manager;

import controller.StockController;
import model.Stock;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Screen for tracking item inventory levels.
 */
public class StockManagement extends javax.swing.JFrame {

        private final StockController stockController;
        private int selectedProductId = -1;
        private javax.swing.Timer refreshTimer;
        private static final int REFRESH_INTERVAL = 5000; // 5 seconds

        public StockManagement() {
                stockController = new StockController();
                initComponents();
                this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

                loadStockData(); // Load all items into the main table
                loadLowStockAlerts(); // Load current low stock items into the alert panel
                startAutoRefresh(); // Begin auto-refresh cycle for alerts
                lbl_name.setText("");
        }

        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jPanel1 = new javax.swing.JPanel();
                jPanel4 = new javax.swing.JPanel();
                jScrollPane1 = new javax.swing.JScrollPane();
                jTable1 = new javax.swing.JTable();
                lbl_5 = new javax.swing.JLabel();
                txt_item = new javax.swing.JTextField();
                btn_back = new javax.swing.JButton();
                txt_qupdate = new javax.swing.JTextField();
                lbl_6 = new javax.swing.JLabel();
                btn_quentityupdate = new javax.swing.JButton();
                lbl_name = new javax.swing.JLabel();
                btn_clear = new javax.swing.JButton();
                jPanel2 = new javax.swing.JPanel();
                lbl_stock = new javax.swing.JLabel();
                jPanel3 = new javax.swing.JPanel();
                jPanel_lowStock = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                jScrollPane_lowStock = new javax.swing.JScrollPane();
                txt_lowStockArea = new javax.swing.JTextArea();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setSize(new java.awt.Dimension(1538, 974));

                jPanel1.setMinimumSize(new java.awt.Dimension(1538, 974));

                jPanel4.setBackground(new java.awt.Color(243, 247, 244));

                jTable1.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N
                jTable1.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {

                                },
                                new String[] {
                                                "Item_ID", "Item_Name", "Category", "Quantity"
                                }) {
                        boolean[] canEdit = new boolean[] {
                                        false, false, false, false
                        };

                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                                return canEdit[columnIndex];
                        }
                });
                jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jTable1MouseClicked(evt);
                        }
                });
                jScrollPane1.setViewportView(jTable1);
                if (jTable1.getColumnModel().getColumnCount() > 0) {
                        jTable1.getColumnModel().getColumn(0).setResizable(false);
                        jTable1.getColumnModel().getColumn(0).setPreferredWidth(10);
                        jTable1.getColumnModel().getColumn(1).setResizable(false);
                        jTable1.getColumnModel().getColumn(1).setPreferredWidth(300);
                        jTable1.getColumnModel().getColumn(2).setResizable(false);
                        jTable1.getColumnModel().getColumn(2).setPreferredWidth(100);
                        jTable1.getColumnModel().getColumn(3).setResizable(false);
                }

                lbl_5.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                lbl_5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                lbl_5.setText("Search:");

                txt_item.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                                txt_itemKeyReleased(evt);
                        }
                });

                btn_back.setBackground(new java.awt.Color(153, 153, 153));
                btn_back.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                btn_back.setText("Back");
                btn_back.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btn_backActionPerformed(evt);
                        }
                });

                lbl_6.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                lbl_6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                lbl_6.setText("Update Quantity:");

                btn_quentityupdate.setBackground(new java.awt.Color(147, 202, 55));
                btn_quentityupdate.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N
                btn_quentityupdate.setText("Update");
                btn_quentityupdate.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btn_quentityupdateActionPerformed(evt);
                        }
                });

                lbl_name.setFont(new java.awt.Font("Unispace", 0, 10)); // NOI18N
                lbl_name.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                lbl_name.setText("name ...");

                btn_clear.setBackground(new java.awt.Color(153, 153, 153));
                btn_clear.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N
                btn_clear.setText("Clear");
                btn_clear.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btn_clearActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
                jPanel4.setLayout(jPanel4Layout);
                jPanel4Layout.setHorizontalGroup(
                                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addGap(36, 36, 36)
                                                                .addGroup(jPanel4Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(lbl_5)
                                                                                .addComponent(txt_item,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                330,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGroup(jPanel4Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                false)
                                                                                                .addComponent(jScrollPane1,
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                972,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGroup(jPanel4Layout
                                                                                                                .createSequentialGroup()
                                                                                                                .addComponent(btn_back,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                156,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addPreferredGap(
                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                Short.MAX_VALUE)
                                                                                                                .addComponent(lbl_6)
                                                                                                                .addPreferredGap(
                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                .addGroup(jPanel4Layout
                                                                                                                                .createParallelGroup(
                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                .addComponent(txt_qupdate,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                225,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addComponent(lbl_name,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                164,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                .addPreferredGap(
                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                .addGroup(jPanel4Layout
                                                                                                                                .createParallelGroup(
                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                                false)
                                                                                                                                .addComponent(btn_quentityupdate,
                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                144,
                                                                                                                                                Short.MAX_VALUE)
                                                                                                                                .addComponent(btn_clear,
                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                Short.MAX_VALUE)))))
                                                                .addContainerGap(40, Short.MAX_VALUE)));
                jPanel4Layout.setVerticalGroup(
                                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addGap(18, 18, 18)
                                                                .addComponent(lbl_5)
                                                                .addGap(6, 6, 6)
                                                                .addComponent(txt_item,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                37,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(35, 35, 35)
                                                                .addComponent(jScrollPane1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                698,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGroup(jPanel4Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel4Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(35, 35, 35)
                                                                                                .addComponent(btn_back,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                58,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(jPanel4Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(27, 27, 27)
                                                                                                .addComponent(btn_quentityupdate,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                37,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(btn_clear,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                35,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(jPanel4Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(18, 18, 18)
                                                                                                .addComponent(lbl_name,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                23,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addGroup(jPanel4Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                                .addComponent(txt_qupdate,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                37,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(lbl_6))))
                                                                .addContainerGap(142, Short.MAX_VALUE)));

                jPanel2.setBackground(new java.awt.Color(73, 128, 37));
                jPanel2.setForeground(new java.awt.Color(204, 204, 255));
                jPanel2.setPreferredSize(new java.awt.Dimension(869, 60));
                jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                lbl_stock.setBackground(new java.awt.Color(255, 255, 255));
                lbl_stock.setFont(new java.awt.Font("Unispace", 0, 20)); // NOI18N
                lbl_stock.setForeground(new java.awt.Color(255, 255, 255));
                lbl_stock.setText("Stock Management");
                jPanel2.add(lbl_stock, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 20, -1, 30));

                jPanel3.setBackground(new java.awt.Color(243, 247, 244));

                jPanel_lowStock.setBackground(new java.awt.Color(255, 204, 204));

                jLabel1.setFont(new java.awt.Font("Unispace", 1, 14)); // NOI18N
                jLabel1.setText(" Low Stock Alert");

                txt_lowStockArea.setEditable(false);
                txt_lowStockArea.setColumns(20);
                txt_lowStockArea.setRows(5);
                jScrollPane_lowStock.setViewportView(txt_lowStockArea);

                javax.swing.GroupLayout jPanel_lowStockLayout = new javax.swing.GroupLayout(jPanel_lowStock);
                jPanel_lowStock.setLayout(jPanel_lowStockLayout);
                jPanel_lowStockLayout.setHorizontalGroup(
                                jPanel_lowStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel_lowStockLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(jScrollPane_lowStock,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                833,
                                                                                Short.MAX_VALUE)
                                                                .addContainerGap())
                                                .addGroup(jPanel_lowStockLayout.createSequentialGroup()
                                                                .addGap(14, 14, 14)
                                                                .addComponent(jLabel1)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                jPanel_lowStockLayout.setVerticalGroup(
                                jPanel_lowStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel_lowStockLayout.createSequentialGroup()
                                                                .addGap(12, 12, 12)
                                                                .addComponent(jLabel1)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jScrollPane_lowStock,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                507,
                                                                                Short.MAX_VALUE)
                                                                .addContainerGap()));

                javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
                jPanel3.setLayout(jPanel3Layout);
                jPanel3Layout.setHorizontalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addGap(14, 14, 14)
                                                                .addComponent(jPanel_lowStock,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(124, Short.MAX_VALUE)));
                jPanel3Layout.setVerticalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addGap(16, 16, 16)
                                                                .addComponent(jPanel_lowStock,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGroup(jPanel1Layout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                false)
                                                                                .addComponent(jPanel2,
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jPanel4,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(0, 0, 0)
                                                                                                .addComponent(jPanel3,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addGap(0, 0, Short.MAX_VALUE)));
                jPanel1Layout.setVerticalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jPanel2,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                72,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jPanel4,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(0, 0, 0)
                                                                                                .addComponent(jPanel3,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE)))));

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE));

                setSize(new java.awt.Dimension(1920, 1027));
                setLocationRelativeTo(null);
        }// </editor-fold>//GEN-END:initComponents

        private void txt_itemKeyReleased(java.awt.event.KeyEvent evt) {
                String searchTerm = txt_item.getText().trim();
                if (searchTerm.isEmpty()) {
                        loadStockData();
                } else {
                        searchStock(searchTerm);
                }
        }

        private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {
                clearFields();
        }

        private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = jTable1.getSelectedRow();
                if (selectedRow != -1) {
                        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                        selectedProductId = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
                        String currentQuantity = model.getValueAt(selectedRow, 3).toString();
                        txt_qupdate.setText(currentQuantity);
                        String productName = model.getValueAt(selectedRow, 1).toString();
                        lbl_name.setText(productName);
                }
        }

        private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {
                new ManagerDashboard().setVisible(true);
                this.dispose();
        }

        /**
         * Loads and displays low stock items in the alert panel.
         */
        private void loadLowStockAlerts() {
                int threshold = 50;
                List<Stock> lowStockList = stockController.getLowStockItems(threshold);

                txt_lowStockArea.setText("");

                if (lowStockList.isEmpty()) {
                        txt_lowStockArea.setText(
                                        "✅ All stock levels are good!\n\nNo items below " + threshold + " units.");
                } else {
                        StringBuilder alertText = new StringBuilder();
                        alertText.append("⚠️ Items running low:\n");
                        alertText.append("   ─────────────────────────\n");

                        for (Stock stock : lowStockList) {
                                String urgency = getUrgencyLevel(stock.getQuantity());
                                alertText.append(String.format("%s %s\n", urgency, stock.getProductName()));
                                alertText.append(
                                                String.format("   Qty: %d | Category: %s\n", stock.getQuantity(),
                                                                stock.getCategoryName()));
                                alertText.append("   ─────────────────────────\n");
                        }

                        alertText.append("\nTotal low stock items: " + lowStockList.size());
                        txt_lowStockArea.setText(alertText.toString());
                        txt_lowStockArea.setCaretPosition(0);
                }
        }

        /**
         * Sets up the auto-refresh timer for low stock alerts.
         */
        private void startAutoRefresh() {
                refreshTimer = new javax.swing.Timer(REFRESH_INTERVAL, (e) -> loadLowStockAlerts());
                refreshTimer.setRepeats(true);
                refreshTimer.start();
        }

        /**
         * Stops the auto-refresh timer.
         */
        private void stopAutoRefresh() {
                if (refreshTimer != null && refreshTimer.isRunning()) {
                        refreshTimer.stop();
                }
        }

        /**
         * Returns an urgency keyword based on stock quantity.
         */
        private String getUrgencyLevel(int quantity) {
                if (quantity < 20)
                        return "[CRITICAL]";
                if (quantity < 40)
                        return "[WARNING] ";
                return "[LOW]     ";
        }

        /**
         * Updates the quantity of the selected item.
         */
        private void btn_quentityupdateActionPerformed(java.awt.event.ActionEvent evt) {
                if (selectedProductId == -1) {
                        JOptionPane.showMessageDialog(this, "Please select an item from the table first",
                                        "No Selection",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                String quantityStr = txt_qupdate.getText().trim();
                if (quantityStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please enter a quantity", "Empty Field",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                // 1. Create and populate Model object
                Stock stock = new Stock();
                stock.setProductId(selectedProductId);
                try {
                        stock.setQuantity(Integer.parseInt(quantityStr));
                } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Invalid quantity format!", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return;
                }

                // 2. Pass to Controller
                String result = stockController.updateStockQuantity(stock);

                if ("SUCCESS".equals(result)) {
                        JOptionPane.showMessageDialog(this, "Stock quantity updated successfully!", "Success",
                                        JOptionPane.INFORMATION_MESSAGE);
                        loadStockData();
                        txt_qupdate.setText("");
                        lbl_name.setText("");
                        selectedProductId = -1;
                } else {
                        JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
        }

        /**
         * Fetches all stock data and populates the table.
         */
        private void loadStockData() {
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);

                List<Stock> stockList = stockController.getAllStock();
                for (Stock stock : stockList) {
                        model.addRow(new Object[] {
                                        stock.getProductId(),
                                        stock.getProductName(),
                                        stock.getCategoryName(),
                                        stock.getQuantity()
                        });
                }
        }

        /**
         * Searches for stock items based on a name or ID.
         */
        private void searchStock(String searchTerm) {
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);

                List<Stock> stockList = stockController.searchStock(searchTerm);

                if (stockList.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "No items found matching: " + searchTerm, "No Results",
                                        JOptionPane.INFORMATION_MESSAGE);
                        loadStockData();
                } else {
                        for (Stock stock : stockList) {
                                model.addRow(new Object[] {
                                                stock.getProductId(),
                                                stock.getProductName(),
                                                stock.getCategoryName(),
                                                stock.getQuantity()
                                });
                        }
                }
        }

        @Override
        public void dispose() {
                stopAutoRefresh();
                super.dispose();
        }

        private void clearFields() {
                lbl_name.setText("");
                txt_qupdate.setText("");
                jTable1.clearSelection();
        }

        public static void main(String args[]) {
                try {
                        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
                                        .getInstalledLookAndFeels()) {
                                if ("Nimbus".equals(info.getName())) {
                                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                        break;
                                }
                        }
                } catch (Exception ex) {
                        java.util.logging.Logger.getLogger(StockManagement.class.getName()).log(
                                        java.util.logging.Level.SEVERE,
                                        null, ex);
                }

                java.awt.EventQueue.invokeLater(() -> new StockManagement().setVisible(true));
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton btn_back;
        private javax.swing.JButton btn_clear;
        private javax.swing.JButton btn_quentityupdate;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JPanel jPanel4;
        private javax.swing.JPanel jPanel_lowStock;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane_lowStock;
        private javax.swing.JTable jTable1;
        private javax.swing.JLabel lbl_5;
        private javax.swing.JLabel lbl_6;
        private javax.swing.JLabel lbl_name;
        private javax.swing.JLabel lbl_stock;
        private javax.swing.JTextField txt_item;
        private javax.swing.JTextArea txt_lowStockArea;
        private javax.swing.JTextField txt_qupdate;
        // End of variables declaration//GEN-END:variables
}
