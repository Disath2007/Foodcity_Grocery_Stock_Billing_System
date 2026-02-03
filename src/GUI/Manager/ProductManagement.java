/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI.Manager;

// MVC Imports - Using Controller instead of direct database access
import controller.ProductController;
import model.Product;
import model.Category;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * VIEW LAYER - Product Management Screen
 * 
 * This View uses ProductController for all operations.
 * No direct database access - follows MVC pattern.
 *
 * @author Disath Damsutha
 */
public class ProductManagement extends javax.swing.JFrame {

        private static final java.util.logging.Logger logger = java.util.logging.Logger
                        .getLogger(ProductManagement.class.getName());

        // MVC Controller - handles all business logic and database operations
        private ProductController controller;

        // Variable to store selected product ID for update/delete operations
        private int selectedProductId = -1;

        // Map to store category name -> category ID mapping
        private Map<String, Integer> categoryMap = new HashMap<>();

        public ProductManagement() {
                initComponents();
                controller = new ProductController(); // Initialize MVC controller
                this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
                loadCategories(); // Load categories into dropdown
                loadProductData(); // Load products into table
        }

        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        lbl_5 = new javax.swing.JLabel();
        txt_itemsearch = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        lbl_Product = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lbl_2 = new javax.swing.JLabel();
        lbl_3 = new javax.swing.JLabel();
        lbl_4 = new javax.swing.JLabel();
        btn_clear = new javax.swing.JButton();
        btn_save = new javax.swing.JButton();
        btn_delete = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();
        txt_itemname = new javax.swing.JTextField();
        txt_itemprice = new javax.swing.JTextField();
        btn_back = new javax.swing.JButton();
        cmb_Category = new javax.swing.JComboBox<>();
        btn_Category = new javax.swing.JButton();
        btn_supplier = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(1538, 974));

        jPanel1.setMinimumSize(new java.awt.Dimension(1538, 974));

        jPanel4.setBackground(new java.awt.Color(243, 247, 244));

        jTable1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item_ID", "Item_Name", "Category", "Item_Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        lbl_5.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        lbl_5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_5.setText("Search:");

        txt_itemsearch.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N
        txt_itemsearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_itemsearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_5)
                    .addComponent(txt_itemsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1412, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lbl_5)
                .addGap(6, 6, 6)
                .addComponent(txt_itemsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 765, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(127, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(73, 128, 37));
        jPanel2.setForeground(new java.awt.Color(204, 204, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(869, 60));

        lbl_Product.setBackground(new java.awt.Color(255, 255, 255));
        lbl_Product.setFont(new java.awt.Font("Unispace", 0, 20)); // NOI18N
        lbl_Product.setForeground(new java.awt.Color(255, 255, 255));
        lbl_Product.setText("Product Management");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(870, Short.MAX_VALUE)
                .addComponent(lbl_Product)
                .addGap(821, 821, 821))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_Product, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(73, 149, 51));

        lbl_2.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        lbl_2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_2.setText("Item Name   :");

        lbl_3.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        lbl_3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_3.setText("Category    :");

        lbl_4.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        lbl_4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_4.setText("Item Price  :");

        btn_clear.setBackground(new java.awt.Color(153, 153, 153));
        btn_clear.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        btn_clear.setText("Clear");
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });

        btn_save.setBackground(new java.awt.Color(147, 202, 55));
        btn_save.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        btn_save.setText("Add");
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });

        btn_delete.setBackground(new java.awt.Color(255, 102, 102));
        btn_delete.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        btn_delete.setText("Delete");
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });

        btn_update.setBackground(new java.awt.Color(147, 186, 40));
        btn_update.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        btn_update.setText("Update");
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });

        txt_itemname.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N

        txt_itemprice.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N

        btn_back.setBackground(new java.awt.Color(153, 153, 153));
        btn_back.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        btn_back.setText("Back");
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });

        cmb_Category.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N

        btn_Category.setBackground(new java.awt.Color(147, 186, 40));
        btn_Category.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        btn_Category.setText("Category Management");
        btn_Category.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CategoryActionPerformed(evt);
            }
        });

        btn_supplier.setBackground(new java.awt.Color(147, 186, 40));
        btn_supplier.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        btn_supplier.setText("Supplier Management");
        btn_supplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_supplierActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(cmb_Category, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(54, 54, 54)
                            .addComponent(btn_update, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(btn_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(54, 54, 54)
                            .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(lbl_2)
                        .addComponent(txt_itemname, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                        .addComponent(lbl_3)
                        .addComponent(lbl_4)
                        .addComponent(txt_itemprice, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_Category, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(lbl_2)
                .addGap(7, 7, 7)
                .addComponent(txt_itemname, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(lbl_3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmb_Category, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_4)
                .addGap(7, 7, 7)
                .addComponent(txt_itemprice, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_update, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addComponent(btn_Category, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1920, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1920, 1080));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

        private void txt_itemsearchKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txt_itemsearchKeyReleased
                searchProduct();
        }// GEN-LAST:event_txt_itemsearchKeyReleased

        private void btn_supplierActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_supplierActionPerformed
                new SupplierManagement().setVisible(true);
                this.dispose();

        }// GEN-LAST:event_btn_supplierActionPerformed

        private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable1MouseClicked
                int selectedRow = jTable1.getSelectedRow();
                if (selectedRow >= 0) {
                        selectedProductId = Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString());
                        String productName = jTable1.getValueAt(selectedRow, 1).toString();
                        String categoryName = jTable1.getValueAt(selectedRow, 2).toString();
                        String price = jTable1.getValueAt(selectedRow, 3).toString();

                        txt_itemname.setText(productName);
                        cmb_Category.setSelectedItem(categoryName);
                        txt_itemprice.setText(price);
                }
        }// GEN-LAST:event_jTable1MouseClicked

        private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_clearActionPerformed
                clearFields();
        }// GEN-LAST:event_btn_clearActionPerformed

        private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_saveActionPerformed
                addProduct();
        }// GEN-LAST:event_btn_saveActionPerformed

        private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_updateActionPerformed
                updateProduct();
        }// GEN-LAST:event_btn_updateActionPerformed

        private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_deleteActionPerformed
                deleteProduct();
        }// GEN-LAST:event_btn_deleteActionPerformed

        private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_backActionPerformed
                new ManagerDashboard().setVisible(true);
                this.dispose();
        }// GEN-LAST:event_btn_backActionPerformed

        private void btn_CategoryActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_CategoryActionPerformed
                new CategoryManagement().setVisible(true);
                this.dispose();
        }// GEN-LAST:event_btn_CategoryActionPerformed

        /**
         * @param args the command line arguments
         */

        // ==================== PRODUCT CRUD METHODS (MVC Pattern) ====================

        /**
         * Load all categories into the dropdown (combo box)
         * Uses Controller instead of direct SQL
         */
        private void loadCategories() {
                cmb_Category.removeAllItems();
                categoryMap.clear();

                // Get categories from Controller
                List<Category> categories = controller.getAllCategories();

                for (Category category : categories) {
                        cmb_Category.addItem(category.getCategoryName());
                        categoryMap.put(category.getCategoryName(), category.getCategoryId());
                }

                cmb_Category.setSelectedIndex(-1); // No selection by default
        }

        /**
         * Load all products from Controller into the table
         * Uses Controller instead of direct SQL
         */
        private void loadProductData() {
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0); // Clear existing rows

                // Get products from Controller (which uses DAO)
                List<Product> products = controller.getAllProducts();

                for (Product product : products) {
                        model.addRow(new Object[] {
                                        product.getProductId(),
                                        product.getProductName(),
                                        product.getCategoryName(),
                                        String.format("%.2f", product.getPrice())
                        });
                }
        }

        /**
         * Add a new product using Controller
         * Controller handles validation and database operations
         */
        private void addProduct() {
                // Get the selected category as a Category object
                Category selectedCategory = null;
                if (cmb_Category.getSelectedIndex() >= 0) {
                        String categoryName = cmb_Category.getSelectedItem().toString();
                        int categoryId = categoryMap.get(categoryName);
                        selectedCategory = new Category(categoryId, categoryName);
                }

                // Call Controller - it handles validation and database
                String result = controller.addProduct(
                                txt_itemname.getText(),
                                selectedCategory,
                                txt_itemprice.getText());

                // Check if successful
                if (result.startsWith("Success")) {
                        JOptionPane.showMessageDialog(this, result,
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                        loadProductData();
                } else {
                        JOptionPane.showMessageDialog(this, result,
                                        "Error", JOptionPane.ERROR_MESSAGE);
                }
        }

        /**
         * Update the selected product using Controller
         */
        private void updateProduct() {
                // Confirm with user first
                int confirm = JOptionPane.showConfirmDialog(this,
                                "Are you sure you want to update this product?",
                                "Confirm Update", JOptionPane.YES_NO_OPTION);

                if (confirm != JOptionPane.YES_OPTION) {
                        return;
                }

                // Get the selected category
                Category selectedCategory = null;
                if (cmb_Category.getSelectedIndex() >= 0) {
                        String categoryName = cmb_Category.getSelectedItem().toString();
                        int categoryId = categoryMap.get(categoryName);
                        selectedCategory = new Category(categoryId, categoryName);
                }

                // Call Controller
                String result = controller.updateProduct(
                                selectedProductId,
                                txt_itemname.getText(),
                                selectedCategory,
                                txt_itemprice.getText());

                if (result.startsWith("Success")) {
                        JOptionPane.showMessageDialog(this, result,
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                        loadProductData();
                } else {
                        JOptionPane.showMessageDialog(this, result,
                                        "Error", JOptionPane.ERROR_MESSAGE);
                }
        }

        /**
         * Delete the selected product using Controller
         */
        private void deleteProduct() {
                if (selectedProductId == -1) {
                        JOptionPane.showMessageDialog(this, "Please select a product from the table to delete!",
                                        "Selection Error", JOptionPane.WARNING_MESSAGE);
                        return;
                }

                // Confirm with user first
                int confirm = JOptionPane.showConfirmDialog(this,
                                "Are you sure you want to delete this product?",
                                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (confirm != JOptionPane.YES_OPTION) {
                        return;
                }

                // Call Controller
                String result = controller.deleteProduct(selectedProductId);

                if (result.startsWith("Success")) {
                        JOptionPane.showMessageDialog(this, result,
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                        loadProductData();
                } else {
                        JOptionPane.showMessageDialog(this, result,
                                        "Error", JOptionPane.ERROR_MESSAGE);
                }
        }

        /**
         * Search products using Controller
         */
        private void searchProduct() {
                String searchTerm = txt_itemsearch.getText().trim();

                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);

                // Get search results from Controller
                List<Product> products = controller.searchProducts(searchTerm);

                for (Product product : products) {
                        model.addRow(new Object[] {
                                        product.getProductId(),
                                        product.getProductName(),
                                        product.getCategoryName(),
                                        String.format("%.2f", product.getPrice())
                        });
                }

                if (products.isEmpty() && !searchTerm.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "No products found matching '" + searchTerm + "'",
                                        "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
        }

        /**
         * Clear all input fields and reset selection
         */
        private void clearFields() {
                txt_itemsearch.setText("");
                txt_itemname.setText("");
                cmb_Category.setSelectedIndex(-1);
                txt_itemprice.setText("");
                selectedProductId = -1;
                jTable1.clearSelection();
                loadProductData(); // Refresh the table
        }

        public static void main(String args[]) {
                /* Set the Nimbus look and feel */
                // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
                // (optional) ">
                /*
                 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
                 * look and feel.
                 * For details see
                 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
                 */
                try {
                        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
                                        .getInstalledLookAndFeels()) {
                                if ("Nimbus".equals(info.getName())) {
                                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                        break;
                                }
                        }
                } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
                        logger.log(java.util.logging.Level.SEVERE, null, ex);
                }
                // </editor-fold>

                /* Create and display the form */
                java.awt.EventQueue.invokeLater(() -> new ProductManagement().setVisible(true));
        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Category;
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_clear;
    private javax.swing.JButton btn_delete;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_supplier;
    private javax.swing.JButton btn_update;
    private javax.swing.JComboBox<String> cmb_Category;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbl_2;
    private javax.swing.JLabel lbl_3;
    private javax.swing.JLabel lbl_4;
    private javax.swing.JLabel lbl_5;
    private javax.swing.JLabel lbl_Product;
    private javax.swing.JTextField txt_itemname;
    private javax.swing.JTextField txt_itemprice;
    private javax.swing.JTextField txt_itemsearch;
    // End of variables declaration//GEN-END:variables
}
