/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI.Manager;

import controller.GRNController;
import model.Product;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 *
 * @author Disath Damsutha
 */
public class GRN extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GRN.class.getName());

    private GRNController grnController;
    private int selectedGrnId = -1;
    private int selectedProductId = -1;

    /**
     * Creates new form GRN
     */
    public GRN() {
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        Date.setText("");

        grnController = new GRNController();
        loadGRNTable();
        loadSuppliers();
        setupListeners();
        updateDateLabel();
    }

    /**
     * Setup event listeners
     */
    private void setupListeners() {
        // Item search listener
        Item_Search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                searchProducts();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                searchProducts();
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                searchProducts();
            }
        });

        // Searched item table click listener
        Searched_item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = Searched_item.getSelectedRow();
                if (row >= 0) {
                    selectProduct(row);
                }
            }
        });

        // GRN table click listener
        GRN_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = GRN_Table.getSelectedRow();
                if (row >= 0) {
                    selectGRN(row);
                }
            }
        });
    }

    /**
     * Update the date label to show current date
     */
    private void updateDateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");
        lbl_6.setText(sdf.format(new java.util.Date()));
    }

    /**
     * Load all GRN records into the table
     */
    private void loadGRNTable() {
        DefaultTableModel model = (DefaultTableModel) GRN_Table.getModel();
        model.setRowCount(0);

        List<model.GRN> grnList = grnController.getAllGRN();
        for (model.GRN grn : grnList) {
            model.addRow(new Object[] {
                    grn.getGrnId(),
                    grn.getProductName(),
                    grn.getBuyingPrice(),
                    grn.getSupplierName(),
                    grn.getOrderedQuantity(),
                    grn.getDeliveredQuantity(),
                    grn.getDateCreated(),
                    String.format("%.2f", grn.getTotalPrice())
            });
        }
    }

    /**
     * Load GRN records filtered by date
     */
    private void loadGRNByDate(String dateStr) {
        DefaultTableModel model = (DefaultTableModel) GRN_Table.getModel();
        model.setRowCount(0);

        try {
            String queryDate;
            // Check if date is already in yyyy-MM-dd format
            if (dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                queryDate = dateStr;
            } else {
                // Try parsing as dd-MMMM-yyyy (Old format)
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMMM-yyyy");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = inputFormat.parse(dateStr);
                queryDate = outputFormat.format(date);
            }

            List<model.GRN> grnList = grnController.getGRNByDate(queryDate);
            for (model.GRN grn : grnList) {
                model.addRow(new Object[] {
                        grn.getGrnId(),
                        grn.getProductName(),
                        grn.getBuyingPrice(),
                        grn.getSupplierName(),
                        grn.getOrderedQuantity(),
                        grn.getDeliveredQuantity(),
                        grn.getDateCreated(),
                        String.format("%.2f", grn.getTotalPrice())
                });
            }
        } catch (ParseException e) {
            // Try one last fallback - the date chooser might return yyyy-MM-dd with time or
            // other variations
            try {
                SimpleDateFormat simpleInput = new SimpleDateFormat("yyyy-MM-dd");
                simpleInput.setLenient(false);
                java.util.Date date = simpleInput.parse(dateStr);
                String queryDate = simpleInput.format(date);

                List<model.GRN> grnList = grnController.getGRNByDate(queryDate);
                for (model.GRN grn : grnList) {
                    model.addRow(new Object[] {
                            grn.getGrnId(),
                            grn.getProductName(),
                            grn.getBuyingPrice(),
                            grn.getSupplierName(),
                            grn.getOrderedQuantity(),
                            grn.getDeliveredQuantity(),
                            grn.getDateCreated(),
                            String.format("%.2f", grn.getTotalPrice())
                    });
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format! Please use yyyy-MM-dd");
            }
        }
    }

    /**
     * Load suppliers into combo box
     */
    private void loadSuppliers() {
        Supplier.removeAllItems();
        Supplier.addItem("-- Select Supplier --");

        List<model.Supplier> suppliers = grnController.getAllSuppliers();
        for (model.Supplier s : suppliers) {
            Supplier.addItem(s.getSupplierId() + " - " + s.getSupplierName() + " (" + s.getCompanyName() + ")");
        }
    }

    /**
     * Load suppliers for a specific product
     */
    private void loadSuppliersForProduct(int productId) {
        Supplier.removeAllItems();
        Supplier.addItem("-- Select Supplier --");

        List<model.Supplier> suppliers = grnController.getSuppliersForProduct(productId);
        if (suppliers.isEmpty()) {
            // If no specific suppliers, load all
            suppliers = grnController.getAllSuppliers();
        }
        for (model.Supplier s : suppliers) {
            Supplier.addItem(s.getSupplierId() + " - " + s.getSupplierName() + " (" + s.getCompanyName() + ")");
        }
    }

    /**
     * Search products based on search field
     */
    private void searchProducts() {
        String searchTerm = Item_Search.getText().trim();
        DefaultTableModel model = (DefaultTableModel) Searched_item.getModel();
        model.setRowCount(0);

        if (!searchTerm.isEmpty()) {
            List<Product> products = grnController.searchProducts(searchTerm);
            for (Product p : products) {
                model.addRow(
                        new Object[] { p.getProductId() + " - " + p.getProductName() + " (Buying Price: Rs."
                                + p.getBuyingPrice() + ")" });
            }
        }
    }

    /**
     * Select a product from search results
     */
    private void selectProduct(int row) {
        String value = Searched_item.getValueAt(row, 0).toString();
        selectedProductId = Integer.parseInt(value.split(" - ")[0]);
        Item_Search.setText(value.split(" - ")[1].split(" \\(")[0]);
        loadSuppliersForProduct(selectedProductId);

        // Clear search results
        DefaultTableModel model = (DefaultTableModel) Searched_item.getModel();
        model.setRowCount(0);
    }

    /**
     * Select a GRN from the table for editing
     */
    private void selectGRN(int row) {
        selectedGrnId = Integer.parseInt(GRN_Table.getValueAt(row, 0).toString());
        String productName = GRN_Table.getValueAt(row, 1).toString();
        String supplierName = GRN_Table.getValueAt(row, 3).toString();
        int orderedQty = Integer.parseInt(GRN_Table.getValueAt(row, 4).toString());
        int deliveredQty = Integer.parseInt(GRN_Table.getValueAt(row, 5).toString());

        Item_Search.setText(productName);
        Ordered_Qty.setText(String.valueOf(orderedQty));
        Delivered_Quantity.setText(String.valueOf(deliveredQty));

        // Find and select supplier in combo
        for (int i = 0; i < Supplier.getItemCount(); i++) {
            if (Supplier.getItemAt(i).contains(supplierName)) {
                Supplier.setSelectedIndex(i);
                break;
            }
        }

        // Get product ID from search
        List<Product> products = grnController.searchProducts(productName);
        if (!products.isEmpty()) {
            selectedProductId = products.get(0).getProductId();
        }
    }

    /**
     * Get selected supplier ID from combo box
     */
    private int getSelectedSupplierId() {
        if (Supplier.getSelectedIndex() <= 0) {
            return -1;
        }
        String selected = Supplier.getSelectedItem().toString();
        return Integer.parseInt(selected.split(" - ")[0]);
    }

    /**
     * Clear all input fields
     */
    private void clearFields() {
        Item_Search.setText("");
        Ordered_Qty.setText("");
        Delivered_Quantity.setText("");
        Supplier.setSelectedIndex(0);
        selectedGrnId = -1;
        selectedProductId = -1;

        DefaultTableModel model = (DefaultTableModel) Searched_item.getModel();
        model.setRowCount(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Main_Panel = new javax.swing.JPanel();
        Top_Panel = new javax.swing.JPanel();
        GRN_lbl = new javax.swing.JLabel();
        Left_Panel = new javax.swing.JPanel();
        item_name_lbl = new javax.swing.JLabel();
        btn_clear1 = new javax.swing.JButton();
        btn_save1 = new javax.swing.JButton();
        btn_delete1 = new javax.swing.JButton();
        btn_update1 = new javax.swing.JButton();
        Item_Search = new javax.swing.JTextField();
        btn_back = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Searched_item = new javax.swing.JTable();
        Ordered_Qty = new javax.swing.JTextField();
        Ordered_Qty_lbl = new javax.swing.JLabel();
        Delivered_Quantity = new javax.swing.JTextField();
        Delivered_Quantity_lbl = new javax.swing.JLabel();
        Supplier_lbl = new javax.swing.JLabel();
        Supplier = new javax.swing.JComboBox<>();
        Right_Panel = new javax.swing.JPanel();
        lbl_date = new javax.swing.JLabel();
        lbl_6 = new javax.swing.JLabel();
        GRN_Scroll = new javax.swing.JScrollPane();
        GRN_Table = new javax.swing.JTable();
        Date = new javax.swing.JTextField();
        Popup_date = new javax.swing.JButton();
        clear = new javax.swing.JButton();
        Search = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Top_Panel.setBackground(new java.awt.Color(73, 128, 37));

        GRN_lbl.setBackground(new java.awt.Color(255, 255, 255));
        GRN_lbl.setFont(new java.awt.Font("Unispace", 0, 20)); // NOI18N
        GRN_lbl.setText("Gross Received Note");

        javax.swing.GroupLayout Top_PanelLayout = new javax.swing.GroupLayout(Top_Panel);
        Top_Panel.setLayout(Top_PanelLayout);
        Top_PanelLayout.setHorizontalGroup(
            Top_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Top_PanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(GRN_lbl)
                .addGap(803, 803, 803))
        );
        Top_PanelLayout.setVerticalGroup(
            Top_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Top_PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(GRN_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Left_Panel.setBackground(new java.awt.Color(73, 149, 51));

        item_name_lbl.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        item_name_lbl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        item_name_lbl.setText("Item Name   :");

        btn_clear1.setBackground(new java.awt.Color(153, 153, 153));
        btn_clear1.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        btn_clear1.setText("Clear");
        btn_clear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clear1ActionPerformed(evt);
            }
        });

        btn_save1.setBackground(new java.awt.Color(147, 202, 55));
        btn_save1.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        btn_save1.setText("Add");
        btn_save1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_save1ActionPerformed(evt);
            }
        });

        btn_delete1.setBackground(new java.awt.Color(255, 102, 102));
        btn_delete1.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        btn_delete1.setText("Delete");
        btn_delete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_delete1ActionPerformed(evt);
            }
        });

        btn_update1.setBackground(new java.awt.Color(147, 186, 40));
        btn_update1.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        btn_update1.setText("Update");
        btn_update1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_update1ActionPerformed(evt);
            }
        });

        Item_Search.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N

        btn_back.setBackground(new java.awt.Color(153, 153, 153));
        btn_back.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        btn_back.setText("Back");
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });

        Searched_item.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Items"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Searched_item.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Searched_item.setName(""); // NOI18N
        jScrollPane1.setViewportView(Searched_item);
        if (Searched_item.getColumnModel().getColumnCount() > 0) {
            Searched_item.getColumnModel().getColumn(0).setResizable(false);
        }

        Ordered_Qty.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N

        Ordered_Qty_lbl.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        Ordered_Qty_lbl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Ordered_Qty_lbl.setText("Ordered Qty   :");

        Delivered_Quantity.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N

        Delivered_Quantity_lbl.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        Delivered_Quantity_lbl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Delivered_Quantity_lbl.setText("Delivered Qty   :");

        Supplier_lbl.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        Supplier_lbl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Supplier_lbl.setText("Supplier   :");

        Supplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SupplierActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Left_PanelLayout = new javax.swing.GroupLayout(Left_Panel);
        Left_Panel.setLayout(Left_PanelLayout);
        Left_PanelLayout.setHorizontalGroup(
            Left_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Left_PanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(Left_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Supplier_lbl)
                    .addComponent(Delivered_Quantity_lbl)
                    .addComponent(Delivered_Quantity, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                    .addComponent(Ordered_Qty, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                    .addGroup(Left_PanelLayout.createSequentialGroup()
                        .addComponent(btn_save1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(btn_update1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Left_PanelLayout.createSequentialGroup()
                        .addComponent(btn_delete1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(btn_clear1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(item_name_lbl)
                    .addComponent(Item_Search, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                    .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(Ordered_Qty_lbl)
                    .addComponent(Supplier, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        Left_PanelLayout.setVerticalGroup(
            Left_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Left_PanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(item_name_lbl)
                .addGap(7, 7, 7)
                .addComponent(Item_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Ordered_Qty_lbl)
                .addGap(7, 7, 7)
                .addComponent(Ordered_Qty, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Delivered_Quantity_lbl)
                .addGap(7, 7, 7)
                .addComponent(Delivered_Quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Supplier_lbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Supplier, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                .addGap(76, 76, 76)
                .addGroup(Left_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_update1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_save1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(Left_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_delete1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_clear1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(128, 128, 128)
                .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        lbl_date.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        lbl_date.setText("Date          :");

        lbl_6.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        lbl_6.setText("Loading...");

        GRN_Table.setAutoCreateRowSorter(true);
        GRN_Table.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        GRN_Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "GRN ID", "Item_Name", "Price ", "Supplier", "Ordered Quantity", "Delivered Quantity", "Date Created", "Total Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        GRN_Scroll.setViewportView(GRN_Table);
        if (GRN_Table.getColumnModel().getColumnCount() > 0) {
            GRN_Table.getColumnModel().getColumn(0).setResizable(false);
            GRN_Table.getColumnModel().getColumn(0).setPreferredWidth(0);
            GRN_Table.getColumnModel().getColumn(1).setResizable(false);
            GRN_Table.getColumnModel().getColumn(1).setPreferredWidth(150);
            GRN_Table.getColumnModel().getColumn(2).setResizable(false);
            GRN_Table.getColumnModel().getColumn(3).setResizable(false);
            GRN_Table.getColumnModel().getColumn(3).setPreferredWidth(150);
            GRN_Table.getColumnModel().getColumn(4).setResizable(false);
            GRN_Table.getColumnModel().getColumn(5).setResizable(false);
            GRN_Table.getColumnModel().getColumn(6).setResizable(false);
            GRN_Table.getColumnModel().getColumn(6).setPreferredWidth(150);
            GRN_Table.getColumnModel().getColumn(7).setResizable(false);
        }

        Date.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N

        Popup_date.setText("...");

        clear.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N
        clear.setText("Clear");
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });

        Search.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N
        Search.setText("Search");
        Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Right_PanelLayout = new javax.swing.GroupLayout(Right_Panel);
        Right_Panel.setLayout(Right_PanelLayout);
        Right_PanelLayout.setHorizontalGroup(
            Right_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Right_PanelLayout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addGroup(Right_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Right_PanelLayout.createSequentialGroup()
                        .addComponent(Date, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Popup_date)
                        .addGap(8, 8, 8)
                        .addComponent(Search)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(clear))
                    .addGroup(Right_PanelLayout.createSequentialGroup()
                        .addComponent(lbl_date)
                        .addGap(26, 26, 26)
                        .addComponent(lbl_6))
                    .addComponent(GRN_Scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 1415, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
        );
        Right_PanelLayout.setVerticalGroup(
            Right_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Right_PanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(Right_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_date)
                    .addComponent(lbl_6))
                .addGap(27, 27, 27)
                .addComponent(GRN_Scroll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(Right_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Date, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Popup_date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clear, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(363, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout Main_PanelLayout = new javax.swing.GroupLayout(Main_Panel);
        Main_Panel.setLayout(Main_PanelLayout);
        Main_PanelLayout.setHorizontalGroup(
            Main_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Top_Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(Main_PanelLayout.createSequentialGroup()
                .addComponent(Left_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(Right_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        Main_PanelLayout.setVerticalGroup(
            Main_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Main_PanelLayout.createSequentialGroup()
                .addComponent(Top_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(Main_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Right_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Left_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Main_Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Main_Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1935, 1008));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_clear1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_clear1ActionPerformed
        clearFields();
    }// GEN-LAST:event_btn_clear1ActionPerformed

    private void btn_save1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_save1ActionPerformed
        int supplierId = getSelectedSupplierId();
        String result = grnController.addGRN(selectedProductId, supplierId, Ordered_Qty.getText(),
                Delivered_Quantity.getText());

        if (result.startsWith("Success")) {
            JOptionPane.showMessageDialog(this, result);
            loadGRNTable();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_btn_save1ActionPerformed

    private void btn_delete1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_delete1ActionPerformed
        if (selectedGrnId <= 0) {
            JOptionPane.showMessageDialog(this, "Please select a GRN to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this GRN?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String result = grnController.deleteGRN(selectedGrnId);
            if (result.startsWith("Success")) {
                JOptionPane.showMessageDialog(this, result);
                loadGRNTable();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }// GEN-LAST:event_btn_delete1ActionPerformed

    private void btn_update1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_update1ActionPerformed
        if (selectedGrnId <= 0) {
            JOptionPane.showMessageDialog(this, "Please select a GRN to update!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int supplierId = getSelectedSupplierId();
        String result = grnController.updateGRN(selectedGrnId, selectedProductId, supplierId, Ordered_Qty.getText(),
                Delivered_Quantity.getText());

        if (result.startsWith("Success")) {
            JOptionPane.showMessageDialog(this, result);
            loadGRNTable();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_btn_update1ActionPerformed

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_backActionPerformed
        this.dispose();
        new ManagerDashboard().setVisible(true);
    }// GEN-LAST:event_btn_backActionPerformed

    private void SupplierActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_SupplierActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_SupplierActionPerformed


    private void clearActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_clearActionPerformed
        Date.setText("");
        loadGRNTable();
    }// GEN-LAST:event_clearActionPerformed

    private void SearchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_SearchActionPerformed
        String dateStr = Date.getText().trim();
        if (dateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a date first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        loadGRNByDate(dateStr);
    }// GEN-LAST:event_SearchActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new GRN().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Date;
    private javax.swing.JTextField Delivered_Quantity;
    private javax.swing.JLabel Delivered_Quantity_lbl;
    private javax.swing.JScrollPane GRN_Scroll;
    private javax.swing.JTable GRN_Table;
    private javax.swing.JLabel GRN_lbl;
    private javax.swing.JTextField Item_Search;
    private javax.swing.JPanel Left_Panel;
    private javax.swing.JPanel Main_Panel;
    private javax.swing.JTextField Ordered_Qty;
    private javax.swing.JLabel Ordered_Qty_lbl;
    private javax.swing.JButton Popup_date;
    private javax.swing.JPanel Right_Panel;
    private javax.swing.JButton Search;
    private javax.swing.JTable Searched_item;
    private javax.swing.JComboBox<String> Supplier;
    private javax.swing.JLabel Supplier_lbl;
    private javax.swing.JPanel Top_Panel;
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_clear1;
    private javax.swing.JButton btn_delete1;
    private javax.swing.JButton btn_save1;
    private javax.swing.JButton btn_update1;
    private javax.swing.JButton clear;
    private javax.swing.JLabel item_name_lbl;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_6;
    private javax.swing.JLabel lbl_date;
    // End of variables declaration//GEN-END:variables
}
