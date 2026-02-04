/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI.Manager;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Disath Damsutha
 */
public class Supplier_Product extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(Supplier_Product.class.getName());

    // Store the supplier ID passed from SupplierManagement
    private int supplierId;
    private int selectedSpId = -1; // For update/delete operations

    // Default constructor
    public Supplier_Product() {
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
    }

    // Constructor with supplier ID
    public Supplier_Product(int supplierId) {
        initComponents();
        this.supplierId = supplierId;
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        loadSupplierDetails();
        loadSupplierProducts();
        loadProductsComboBox();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        lbl_1 = new javax.swing.JLabel();
        de_supplier_name = new javax.swing.JLabel();
        de_company_name = new javax.swing.JLabel();
        lbl_3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        supplier_product_Table = new javax.swing.JTable();
        txt_search = new javax.swing.JTextField();
        lbl_5 = new javax.swing.JLabel();
        lbl_4 = new javax.swing.JLabel();
        de_phone = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lbl_supplierproduct = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lbl_2 = new javax.swing.JLabel();
        btn_clear = new javax.swing.JButton();
        btn_save = new javax.swing.JButton();
        btn_delete = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();
        btn_back = new javax.swing.JButton();
        txt_buyingprice = new javax.swing.JTextField();
        lbl_7 = new javax.swing.JLabel();
        cmb_item = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Supplier Management");
        setSize(new java.awt.Dimension(1538, 974));

        jPanel1.setMinimumSize(new java.awt.Dimension(1538, 974));

        jPanel4.setBackground(new java.awt.Color(243, 247, 244));

        lbl_1.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        lbl_1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_1.setText("Supplier Name   :");

        de_supplier_name.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        de_supplier_name.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        de_supplier_name.setText("Loading..");

        de_company_name.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        de_company_name.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        de_company_name.setText("Loading..");

        lbl_3.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        lbl_3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_3.setText("Company Name    :");

        supplier_product_Table.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "ID", "Item_Name", "Category"
                }) {
            boolean[] canEdit = new boolean[] {
                    false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        supplier_product_Table.setToolTipText("Supplier_Product");
        // Hide the ID column (column 0)
        supplier_product_Table.getColumnModel().getColumn(0).setMinWidth(0);
        supplier_product_Table.getColumnModel().getColumn(0).setMaxWidth(0);
        supplier_product_Table.getColumnModel().getColumn(0).setWidth(0);
        supplier_product_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(supplier_product_Table);

        txt_search.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N
        txt_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_searchKeyReleased(evt);
            }
        });

        lbl_5.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        lbl_5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_5.setText("Search:");

        lbl_4.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        lbl_4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_4.setText("Phone           :");

        de_phone.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        de_phone.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        de_phone.setText("Loading..");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(lbl_4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(de_phone))
                                        .addGroup(jPanel4Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                        .addComponent(lbl_1)
                                                        .addPreferredGap(
                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(de_supplier_name)
                                                        .addPreferredGap(
                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(lbl_5)
                                                        .addPreferredGap(
                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(txt_search,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 330,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        1364, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(lbl_3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(de_company_name)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGap(40, 40, 40)
                                                .addGroup(jPanel4Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txt_search,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 37,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lbl_5)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout
                                                .createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(jPanel4Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lbl_1)
                                                        .addComponent(de_supplier_name))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(de_company_name)
                                        .addComponent(lbl_3))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(de_phone)
                                        .addComponent(lbl_4))
                                .addGap(58, 58, 58)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(367, Short.MAX_VALUE)));

        jPanel2.setBackground(new java.awt.Color(73, 128, 37));
        jPanel2.setForeground(new java.awt.Color(204, 204, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(869, 60));

        lbl_supplierproduct.setBackground(new java.awt.Color(255, 255, 255));
        lbl_supplierproduct.setFont(new java.awt.Font("Unispace", 0, 20)); // NOI18N
        lbl_supplierproduct.setForeground(new java.awt.Color(255, 255, 255));
        lbl_supplierproduct.setText("Supplier_Product");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap(907, Short.MAX_VALUE)
                                .addComponent(lbl_supplierproduct)
                                .addGap(821, 821, 821)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbl_supplierproduct, javax.swing.GroupLayout.PREFERRED_SIZE, 48,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        jPanel3.setBackground(new java.awt.Color(73, 149, 51));

        lbl_2.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        lbl_2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_2.setText("Item Name     :");

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

        btn_back.setBackground(new java.awt.Color(153, 153, 153));
        btn_back.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        btn_back.setText("Back");
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });

        txt_buyingprice.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N

        lbl_7.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        lbl_7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_7.setText("Buying_price  :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(jPanel3Layout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lbl_7)
                                        .addComponent(txt_buyingprice, javax.swing.GroupLayout.DEFAULT_SIZE, 369,
                                                Short.MAX_VALUE)
                                        .addComponent(lbl_2)
                                        .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 156,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmb_item, 0, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(jPanel3Layout.createSequentialGroup()
                                                        .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(54, 54, 54)
                                                        .addComponent(btn_update,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 156,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel3Layout.createSequentialGroup()
                                                        .addComponent(btn_delete,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 156,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(54, 54, 54)
                                                        .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                156, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(31, Short.MAX_VALUE)));
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(lbl_2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmb_item, javax.swing.GroupLayout.PREFERRED_SIZE, 37,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbl_7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_buyingprice, javax.swing.GroupLayout.PREFERRED_SIZE, 36,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 58,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btn_update, javax.swing.GroupLayout.PREFERRED_SIZE, 58,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btn_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 58,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 58,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 58,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1920,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        setSize(new java.awt.Dimension(1920, 1080));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_searchKeyReleased(java.awt.event.KeyEvent evt) {
        searchSupplierProducts();
    }

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
        int selectedRow = supplier_product_Table.getSelectedRow();
        if (selectedRow >= 0) {
            selectedSpId = Integer.parseInt(supplier_product_Table.getValueAt(selectedRow, 0).toString());
            String productName = supplier_product_Table.getValueAt(selectedRow, 1).toString();

            // Find and select product in combo box
            for (int i = 0; i < cmb_item.getItemCount(); i++) {
                if (cmb_item.getItemAt(i).contains(productName)) {
                    cmb_item.setSelectedIndex(i);
                    break;
                }
            }
            // txt_buyingprice.setText(buyingPrice);
        }
    }

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {
        clearFields();
    }

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {
        addSupplierProduct();
    }

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {
        updateSupplierProduct();
    }

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {
        deleteSupplierProduct();
    }

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {
        new SupplierManagement().setVisible(true);
        this.dispose();
    }

    /**
     * Clear all input fields and reset selection
     */
    private void clearFields() {
        cmb_item.setSelectedIndex(-1);
        txt_buyingprice.setText("");
        selectedSpId = -1;
        supplier_product_Table.clearSelection();
    }

    /**
     * Load supplier details into labels
     */
    private void loadSupplierDetails() {
        String sql = "SELECT supplier_name, company_name, phone FROM supplier WHERE supplier_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, supplierId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    de_supplier_name.setText(rs.getString("supplier_name"));
                    de_company_name.setText(rs.getString("company_name"));
                    de_phone.setText(rs.getString("phone"));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading supplier details: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Load products supplied by this supplier into the table
     */
    private void loadSupplierProducts() {
        DefaultTableModel model = (DefaultTableModel) supplier_product_Table.getModel();
        model.setRowCount(0);

        String sql = "SELECT sp.sp_id, p.product_name, c.category_name " +
                "FROM supplier_product sp " +
                "JOIN product p ON sp.product_id = p.product_id " +
                "JOIN category c ON p.category_id = c.category_id " +
                "WHERE sp.supplier_id = ? " +
                "ORDER BY p.product_name";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, supplierId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int spId = rs.getInt("sp_id");
                    String productName = rs.getString("product_name");
                    String categoryName = rs.getString("category_name");

                    model.addRow(new Object[] { spId, productName, categoryName });
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading supplier products: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Load all products into the combo box
     */
    private void loadProductsComboBox() {
        cmb_item.removeAllItems();
        String sql = "SELECT product_id, product_name FROM product ORDER BY product_id";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                cmb_item.addItem(productId + " - " + productName);
            }

            cmb_item.setSelectedIndex(-1);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Get product ID from combo box selection
     */
    private int getSelectedProductId() {
        String selected = (String) cmb_item.getSelectedItem();
        if (selected != null && selected.contains(" - ")) {
            return Integer.parseInt(selected.split(" - ")[0]);
        }
        return -1;
    }

    /**
     * Add a new supplier-product relationship
     */
    private void addSupplierProduct() {
        int productId = getSelectedProductId();

        if (productId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product!",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO supplier_product (supplier_id, product_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, supplierId);
            pstmt.setInt(2, productId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Product added to supplier successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadSupplierProducts();
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(this, "This product is already assigned to this supplier!",
                        "Duplicate Error", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error adding product: " + e.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Update the selected supplier-product relationship
     */
    private void updateSupplierProduct() {
        // Feature removed as Buying Price is now managed in Product Management
        JOptionPane.showMessageDialog(this, "Buying Price is now managed in Product Management.",
                "Feature Updated", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Delete the selected supplier-product relationship
     */
    private void deleteSupplierProduct() {
        if (selectedSpId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product from the table to delete!",
                    "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove this product from the supplier?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM supplier_product WHERE sp_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, selectedSpId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Product removed from supplier successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadSupplierProducts();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Search supplier products
     */
    private void searchSupplierProducts() {
        String searchTerm = txt_search.getText().trim();

        if (searchTerm.isEmpty()) {
            loadSupplierProducts();
            return;
        }

        DefaultTableModel model = (DefaultTableModel) supplier_product_Table.getModel();
        model.setRowCount(0);

        String sql = "SELECT sp.sp_id, p.product_name, c.category_name " +
                "FROM supplier_product sp " +
                "JOIN product p ON sp.product_id = p.product_id " +
                "JOIN category c ON p.category_id = c.category_id " +
                "WHERE sp.supplier_id = ? AND (p.product_name LIKE ? OR c.category_name LIKE ?) " +
                "ORDER BY p.product_name";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, supplierId);
            pstmt.setString(2, "%" + searchTerm + "%");
            pstmt.setString(3, "%" + searchTerm + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int spId = rs.getInt("sp_id");
                    String productName = rs.getString("product_name");
                    String categoryName = rs.getString("category_name");

                    model.addRow(new Object[] { spId, productName, categoryName });
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error searching: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param args the command line arguments
     */
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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
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
        java.awt.EventQueue.invokeLater(() -> new Supplier_Product().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_clear;
    private javax.swing.JButton btn_delete;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_update;
    private javax.swing.JComboBox<String> cmb_item;
    private javax.swing.JLabel de_company_name;
    private javax.swing.JLabel de_phone;
    private javax.swing.JLabel de_supplier_name;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_1;
    private javax.swing.JLabel lbl_2;
    private javax.swing.JLabel lbl_3;
    private javax.swing.JLabel lbl_4;
    private javax.swing.JLabel lbl_5;
    private javax.swing.JLabel lbl_7;
    private javax.swing.JLabel lbl_supplierproduct;
    private javax.swing.JTable supplier_product_Table;
    private javax.swing.JTextField txt_buyingprice;
    private javax.swing.JTextField txt_search;
    // End of variables declaration//GEN-END:variables
}
