/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI.Manager;

// MVC Imports - Using Controller instead of direct database access
import controller.SupplierController;
import model.Supplier;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * VIEW LAYER - Supplier Management Screen
 * 
 * This View uses SupplierController for all operations.
 * No direct database access - follows MVC pattern.
 *
 * @author Disath Damsutha
 */
public class SupplierManagement extends javax.swing.JFrame {

        private static final java.util.logging.Logger logger = java.util.logging.Logger
                        .getLogger(SupplierManagement.class.getName());

        // MVC Controller - handles all business logic and database operations
        private SupplierController controller;

        // Variable to store selected supplier ID for update/delete operations
        private int selectedSupplierId = -1;

        public SupplierManagement() {
                initComponents();
                controller = new SupplierController(); // Initialize MVC controller
                btn_viewdetail.setEnabled(false);
                this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
                loadSupplierData();
        }

        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jPanel1 = new javax.swing.JPanel();
                jPanel4 = new javax.swing.JPanel();
                jScrollPane1 = new javax.swing.JScrollPane();
                jTable1 = new javax.swing.JTable();
                lbl_5 = new javax.swing.JLabel();
                txt_search = new javax.swing.JTextField();
                btn_viewdetail = new javax.swing.JButton();
                jPanel2 = new javax.swing.JPanel();
                lbl_supplier = new javax.swing.JLabel();
                jPanel3 = new javax.swing.JPanel();
                lbl_2 = new javax.swing.JLabel();
                btn_clear = new javax.swing.JButton();
                btn_save = new javax.swing.JButton();
                btn_delete = new javax.swing.JButton();
                btn_update = new javax.swing.JButton();
                txt_suppliername = new javax.swing.JTextField();
                btn_back = new javax.swing.JButton();
                btn_Product = new javax.swing.JButton();
                txt_companyname = new javax.swing.JTextField();
                lbl_6 = new javax.swing.JLabel();
                txt_phone = new javax.swing.JTextField();
                lbl_7 = new javax.swing.JLabel();
                btn_GRN = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setTitle("Supplier Management");
                setSize(new java.awt.Dimension(1538, 974));

                jPanel1.setMinimumSize(new java.awt.Dimension(1538, 974));

                jPanel4.setBackground(new java.awt.Color(243, 247, 244));

                jTable1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
                jTable1.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {

                                },
                                new String[] {
                                                "Supplier_ID", "Supplier_Name", "Company_Name", "Phone"
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

                lbl_5.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                lbl_5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                lbl_5.setText("Search:");

                txt_search.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N
                txt_search.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                                txt_searchKeyReleased(evt);
                        }
                });

                btn_viewdetail.setBackground(new java.awt.Color(147, 202, 55));
                btn_viewdetail.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                btn_viewdetail.setText("View Detail");
                btn_viewdetail.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btn_viewdetailActionPerformed(evt);
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
                                                                                .addComponent(txt_search,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                330,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jScrollPane1,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                1412,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btn_viewdetail,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                271,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                jPanel4Layout.setVerticalGroup(
                                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addGap(18, 18, 18)
                                                                .addComponent(lbl_5)
                                                                .addGap(6, 6, 6)
                                                                .addComponent(txt_search,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                37,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(35, 35, 35)
                                                                .addComponent(jScrollPane1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                549,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(103, 103, 103)
                                                                .addComponent(btn_viewdetail,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                58,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));

                jPanel2.setBackground(new java.awt.Color(73, 128, 37));
                jPanel2.setForeground(new java.awt.Color(204, 204, 255));
                jPanel2.setPreferredSize(new java.awt.Dimension(869, 60));

                lbl_supplier.setBackground(new java.awt.Color(255, 255, 255));
                lbl_supplier.setFont(new java.awt.Font("Unispace", 0, 20)); // NOI18N
                lbl_supplier.setForeground(new java.awt.Color(255, 255, 255));
                lbl_supplier.setText("Supplier Management");

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                jPanel2Layout.createSequentialGroup()
                                                                                .addContainerGap(871, Short.MAX_VALUE)
                                                                                .addComponent(lbl_supplier)
                                                                                .addGap(821, 821, 821)));
                jPanel2Layout.setVerticalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(lbl_supplier,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                48,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));

                jPanel3.setBackground(new java.awt.Color(73, 149, 51));

                lbl_2.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                lbl_2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                lbl_2.setText("Supplier Name   :");

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

                txt_suppliername.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N

                btn_back.setBackground(new java.awt.Color(153, 153, 153));
                btn_back.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                btn_back.setText("Back");
                btn_back.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btn_backActionPerformed(evt);
                        }
                });

                btn_Product.setBackground(new java.awt.Color(147, 186, 40));
                btn_Product.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                btn_Product.setText("Product Management");
                btn_Product.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btn_ProductActionPerformed(evt);
                        }
                });

                txt_companyname.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N

                lbl_6.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                lbl_6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                lbl_6.setText("Complany Name   :");

                txt_phone.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N

                lbl_7.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                lbl_7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                lbl_7.setText("Phone           :");

                btn_GRN.setBackground(new java.awt.Color(147, 186, 40));
                btn_GRN.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                btn_GRN.setText("GRN");
                btn_GRN.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btn_GRNActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
                jPanel3.setLayout(jPanel3Layout);
                jPanel3Layout.setHorizontalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addGap(40, 40, 40)
                                                                .addGroup(jPanel3Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel3Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(btn_save,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                156,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(54, 54, 54)
                                                                                                .addComponent(btn_update,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                156,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(jPanel3Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                false)
                                                                                                .addComponent(lbl_7)
                                                                                                .addComponent(txt_phone,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                369,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addComponent(lbl_6)
                                                                                                .addComponent(txt_companyname,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                369,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addComponent(lbl_2)
                                                                                                .addComponent(txt_suppliername,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                369,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addComponent(btn_back,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                156,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(jPanel3Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                .addComponent(btn_Product,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                366,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGroup(jPanel3Layout
                                                                                                                .createSequentialGroup()
                                                                                                                .addComponent(btn_delete,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                156,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addGap(54, 54, 54)
                                                                                                                .addComponent(btn_clear,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                156,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addComponent(btn_GRN,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                366,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addContainerGap(31, Short.MAX_VALUE)));
                jPanel3Layout.setVerticalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addGap(30, 30, 30)
                                                                .addComponent(lbl_2)
                                                                .addGap(7, 7, 7)
                                                                .addComponent(txt_suppliername,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                36,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(lbl_6)
                                                                .addGap(7, 7, 7)
                                                                .addComponent(txt_companyname,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                36,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(lbl_7)
                                                                .addGap(7, 7, 7)
                                                                .addComponent(txt_phone,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                36,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(31, 31, 31)
                                                                .addGroup(jPanel3Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(btn_save,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                58,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btn_update,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                58,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(22, 22, 22)
                                                                .addGroup(jPanel3Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(btn_delete,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                58,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btn_clear,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                58,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(129, 129, 129)
                                                                .addComponent(btn_Product,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                58,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(btn_GRN,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                58,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                244,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(btn_back,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                58,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(37, 37, 37)));

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jPanel2,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                1920,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jPanel3,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, 0)
                                                                .addComponent(jPanel4,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                jPanel1Layout.setVerticalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jPanel2,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jPanel3,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(jPanel4,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))));

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

        private void btn_viewdetailActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_viewdetailActionPerformed
                if (selectedSupplierId != -1) {
                        new Supplier_Product(selectedSupplierId).setVisible(true);
                        this.dispose();
                } else {
                        JOptionPane.showMessageDialog(this, "Please select a supplier first!",
                                        "Selection Error", JOptionPane.WARNING_MESSAGE);
                }
        }// GEN-LAST:event_btn_viewdetailActionPerformed

        private void txt_searchKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txt_searchKeyReleased
                searchSupplier();
        }// GEN-LAST:event_txt_searchKeyReleased

        private void btn_GRNActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_GRNActionPerformed
                new GRN().setVisible(true);
                this.dispose();
        }// GEN-LAST:event_btn_GRNActionPerformed

        private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable1MouseClicked
                int selectedRow = jTable1.getSelectedRow();
                if (selectedRow >= 0) {
                        selectedSupplierId = Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString());
                        String supplierName = jTable1.getValueAt(selectedRow, 1).toString();
                        String companyName = jTable1.getValueAt(selectedRow, 2).toString();
                        String phone = jTable1.getValueAt(selectedRow, 3).toString();
                        txt_suppliername.setText(supplierName);
                        txt_companyname.setText(companyName);
                        txt_phone.setText(phone);
                        btn_viewdetail.setEnabled(true);
                }
        }// GEN-LAST:event_jTable1MouseClicked

        private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_clearActionPerformed
                clearFields();
        }// GEN-LAST:event_btn_clearActionPerformed

        private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_saveActionPerformed
                addSupplier();
        }// GEN-LAST:event_btn_saveActionPerformed

        private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_updateActionPerformed
                updateSupplier();
        }// GEN-LAST:event_btn_updateActionPerformed

        private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_deleteActionPerformed
                deleteSupplier();
        }// GEN-LAST:event_btn_deleteActionPerformed

        private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_backActionPerformed
                new ManagerDashboard().setVisible(true);
                this.dispose();
        }// GEN-LAST:event_btn_backActionPerformed

        private void btn_ProductActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_CategoryActionPerformed
                new ProductManagement().setVisible(true);
                this.dispose();
        }

        // ==================== SUPPLIER CRUD METHODS (MVC Pattern) ====================

        /**
         * Load all suppliers from Controller into the table
         * Uses Controller instead of direct SQL
         */
        private void loadSupplierData() {
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0); // Clear existing rows

                // Get suppliers from Controller
                List<Supplier> suppliers = controller.getAllSuppliers();

                for (Supplier supplier : suppliers) {
                        model.addRow(new Object[] {
                                        supplier.getSupplierId(),
                                        supplier.getSupplierName(),
                                        supplier.getCompanyName(),
                                        supplier.getPhone()
                        });
                }
        }

        /**
         * Add a new supplier using Controller
         * Controller handles validation and database operations
         */
        private void addSupplier() {
                // Call Controller - it handles validation and database
                String result = controller.addSupplier(
                                txt_suppliername.getText(),
                                txt_companyname.getText(),
                                txt_phone.getText());

                if (result.startsWith("Success")) {
                        JOptionPane.showMessageDialog(this, result,
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                        loadSupplierData();
                } else {
                        JOptionPane.showMessageDialog(this, result,
                                        "Error", JOptionPane.ERROR_MESSAGE);
                }
        }

        /**
         * Update the selected supplier using Controller
         */
        private void updateSupplier() {
                // Confirm with user first
                int confirm = JOptionPane.showConfirmDialog(this,
                                "Are you sure you want to update this supplier?",
                                "Confirm Update", JOptionPane.YES_NO_OPTION);

                if (confirm != JOptionPane.YES_OPTION) {
                        return;
                }

                // Call Controller
                String result = controller.updateSupplier(
                                selectedSupplierId,
                                txt_suppliername.getText(),
                                txt_companyname.getText(),
                                txt_phone.getText());

                if (result.startsWith("Success")) {
                        JOptionPane.showMessageDialog(this, result,
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                        loadSupplierData();
                } else {
                        JOptionPane.showMessageDialog(this, result,
                                        "Error", JOptionPane.ERROR_MESSAGE);
                }
        }

        /**
         * Delete the selected supplier using Controller
         */
        private void deleteSupplier() {
                if (selectedSupplierId == -1) {
                        JOptionPane.showMessageDialog(this, "Please select a supplier from the table to delete!",
                                        "Selection Error", JOptionPane.WARNING_MESSAGE);
                        return;
                }

                // Confirm with user first
                int confirm = JOptionPane.showConfirmDialog(this,
                                "Are you sure you want to delete this supplier?",
                                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (confirm != JOptionPane.YES_OPTION) {
                        return;
                }

                // Call Controller
                String result = controller.deleteSupplier(selectedSupplierId);

                if (result.startsWith("Success")) {
                        JOptionPane.showMessageDialog(this, result,
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                        loadSupplierData();
                } else {
                        JOptionPane.showMessageDialog(this, result,
                                        "Error", JOptionPane.ERROR_MESSAGE);
                }
        }

        /**
         * Search suppliers using Controller
         */
        private void searchSupplier() {
                String searchTerm = txt_search.getText().trim();

                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);

                // Get search results from Controller
                List<Supplier> suppliers = controller.searchSuppliers(searchTerm);

                for (Supplier supplier : suppliers) {
                        model.addRow(new Object[] {
                                        supplier.getSupplierId(),
                                        supplier.getSupplierName(),
                                        supplier.getCompanyName(),
                                        supplier.getPhone()
                        });
                }

                if (suppliers.isEmpty() && !searchTerm.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "No suppliers found matching '" + searchTerm + "'",
                                        "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
        }

        /**
         * Clear all input fields and reset selection
         */
        private void clearFields() {
                txt_search.setText("");
                txt_suppliername.setText("");
                txt_companyname.setText("");
                txt_phone.setText("");
                selectedSupplierId = -1;
                jTable1.clearSelection();
                loadSupplierData();
                btn_viewdetail.setEnabled(false);
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
                java.awt.EventQueue.invokeLater(() -> new SupplierManagement().setVisible(true));
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton btn_GRN;
        private javax.swing.JButton btn_Product;
        private javax.swing.JButton btn_back;
        private javax.swing.JButton btn_clear;
        private javax.swing.JButton btn_delete;
        private javax.swing.JButton btn_save;
        private javax.swing.JButton btn_update;
        private javax.swing.JButton btn_viewdetail;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JPanel jPanel4;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JTable jTable1;
        private javax.swing.JLabel lbl_2;
        private javax.swing.JLabel lbl_5;
        private javax.swing.JLabel lbl_6;
        private javax.swing.JLabel lbl_7;
        private javax.swing.JLabel lbl_supplier;
        private javax.swing.JTextField txt_companyname;
        private javax.swing.JTextField txt_phone;
        private javax.swing.JTextField txt_search;
        private javax.swing.JTextField txt_suppliername;
        // End of variables declaration//GEN-END:variables
}
