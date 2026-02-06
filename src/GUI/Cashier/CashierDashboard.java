/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI.Cashier;

import GUIComponents.CalculatorPanel;
import controller.StockController;
import model.Stock;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import model.User;
import model.Sale;
import model.SaleItem;
import controller.SalesController;
import java.util.ArrayList;

/**
 *
 * @author Disath Damsutha
 */
public class CashierDashboard extends javax.swing.JFrame {

        private static final java.util.logging.Logger logger = java.util.logging.Logger
                        .getLogger(CashierDashboard.class.getName());

        private final StockController stockController;
        private final SalesController salesController;
        private JPopupMenu searchSuggestions;
        private java.util.List<Object[]> heldTransaction;
        private User currentUser;

        /**
         * Creates new form Cashier_Dashboard
         */
        public CashierDashboard() {
                this(null);
        }

        public CashierDashboard(User user) {
                initComponents();
                if (user != null) {
                        this.currentUser = user;
                        lbl_NameOfUser.setText(user.getFullName());
                } else {
                        lbl_NameOfUser.setText("Unknown Cashier");
                }
                this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
                stockController = new StockController();
                salesController = new SalesController();
                Remove.setEnabled(false);
                setupSearchPlaceholder();
                setupCalculator();
                initSearchSuggestions();
                resetSummary();
                setupDiscountListener();

                // Set table selection mode to single
                Cashier_Table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                setupTableStyle();
                setupKeyboardShortcuts();
                startDateTimeTimer();
                setupRealTimeBalance();
                setupTableEditing();
        }

        private void setupTableEditing() {
                DefaultTableModel model = new DefaultTableModel(
                                new Object[][] {},
                                new String[] { "ID", "Item_Name", "Price", "Qty", "Total" }) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return column == 3; // Only Qty is editable
                        }

                        @Override
                        public Class<?> getColumnClass(int columnIndex) {
                                if (columnIndex == 0 || columnIndex == 3)
                                        return Integer.class;
                                if (columnIndex == 2 || columnIndex == 4)
                                        return Double.class;
                                return String.class;
                        }
                };
                Cashier_Table.setModel(model);
                setupTableStyle(); // Re-apply styles after setting model

                model.addTableModelListener(e -> {
                        if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                                int row = e.getFirstRow();
                                int col = e.getColumn();
                                if (col == 3) {
                                        Integer newQty = (Integer) model.getValueAt(row, 3);
                                        Double price = (Double) model.getValueAt(row, 2);
                                        int productId = (Integer) model.getValueAt(row, 0);

                                        // Validate stock
                                        int availableStock = stockController.getStockQuantity(productId);
                                        if (newQty == null || newQty <= 0) {
                                                JOptionPane.showMessageDialog(this, "Please enter a valid quantity",
                                                                "Input Error", JOptionPane.WARNING_MESSAGE);
                                                // Revert to 1 if invalid
                                                javax.swing.SwingUtilities
                                                                .invokeLater(() -> model.setValueAt(1, row, 3));
                                                return;
                                        }

                                        if (newQty > availableStock) {
                                                JOptionPane.showMessageDialog(this,
                                                                "Insufficient stock! Available: " + availableStock,
                                                                "Stock Warning", JOptionPane.WARNING_MESSAGE);
                                                // Revert to available stock
                                                javax.swing.SwingUtilities.invokeLater(
                                                                () -> model.setValueAt(availableStock, row, 3));
                                                return;
                                        }

                                        // Update total for this row
                                        model.setValueAt(newQty * price, row, 4);
                                        calculateTotals();
                                }
                        }
                });
        }

        private void setupRealTimeBalance() {
                txt_Cash.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                        public void changedUpdate(javax.swing.event.DocumentEvent e) {
                                updateBalance();
                        }

                        public void removeUpdate(javax.swing.event.DocumentEvent e) {
                                updateBalance();
                        }

                        public void insertUpdate(javax.swing.event.DocumentEvent e) {
                                updateBalance();
                        }

                        private void updateBalance() {
                                javax.swing.SwingUtilities.invokeLater(() -> calculateTotals());
                        }
                });
        }

        private void startDateTimeTimer() {
                java.util.Timer timer = new java.util.Timer(true);
                timer.scheduleAtFixedRate(new java.util.TimerTask() {
                        @Override
                        public void run() {
                                String dateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                                .format(new java.util.Date());
                                javax.swing.SwingUtilities.invokeLater(() -> lbl_DateTime.setText(dateTime));
                        }
                }, 0, 1000);
        }

        private void setupTableStyle() {
                Cashier_Table.setRowHeight(35);
                Cashier_Table.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
                Cashier_Table.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
                Cashier_Table.setGridColor(java.awt.Color.LIGHT_GRAY);
                Cashier_Table.setShowGrid(true);
        }

        private void setupKeyboardShortcuts() {
                // F1: Pay & Print
                Main_Panel.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW)
                                .put(javax.swing.KeyStroke.getKeyStroke("F1"), "pay");
                Main_Panel.getActionMap().put("pay", new javax.swing.AbstractAction() {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                                Pay_PrintActionPerformed(e);
                        }
                });

                // Table Navigation (Arrows)
                Cashier_Table.addKeyListener(new java.awt.event.KeyAdapter() {
                        @Override
                        public void keyPressed(java.awt.event.KeyEvent e) {
                                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE) {
                                        RemoveActionPerformed(null);
                                }
                        }
                });

                // F5: Reset/Cancel
                Main_Panel.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW)
                                .put(javax.swing.KeyStroke.getKeyStroke("F5"), "cancel");
                Main_Panel.getActionMap().put("cancel", new javax.swing.AbstractAction() {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                                CancelActionPerformed(e);
                        }
                });

                // ESC: Clear Search
                btn_search.getInputMap().put(javax.swing.KeyStroke.getKeyStroke("ESCAPE"), "clearSearch");
                btn_search.getActionMap().put("clearSearch", new javax.swing.AbstractAction() {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                                btn_search.setText("");
                                searchSuggestions.setVisible(false);
                        }
                });

                // F2: Focus Search
                Main_Panel.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW)
                                .put(javax.swing.KeyStroke.getKeyStroke("F2"), "focusSearch");
                Main_Panel.getActionMap().put("focusSearch", new javax.swing.AbstractAction() {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                                btn_search.requestFocus();
                                btn_search.selectAll();
                        }
                });
        }

        private void initSearchSuggestions() {
                searchSuggestions = new JPopupMenu();
                searchSuggestions.setFocusable(true);

                // Add keyboard listener to the popup menu
                searchSuggestions.addKeyListener(new java.awt.event.KeyAdapter() {
                        @Override
                        public void keyPressed(java.awt.event.KeyEvent e) {
                                // Determine if 'Enter' key was pressed to select an item
                                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                                        javax.swing.MenuElement[] path = javax.swing.MenuSelectionManager
                                                        .defaultManager().getSelectedPath();

                                        if (path.length > 1 && path[1] instanceof JMenuItem) {
                                                ((JMenuItem) path[1]).doClick(); // Select the item
                                        }
                                }
                        }
                });

                searchSuggestions.setLightWeightPopupEnabled(true);
        }

        private void setupDiscountListener() {
                txt_Discount.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                        @Override
                        public void insertUpdate(javax.swing.event.DocumentEvent e) {
                                calculateTotals();
                        }

                        @Override
                        public void removeUpdate(javax.swing.event.DocumentEvent e) {
                                calculateTotals();
                        }

                        @Override
                        public void changedUpdate(javax.swing.event.DocumentEvent e) {
                                calculateTotals();
                        }
                });
        }

        private void setupSearchPlaceholder() {
                String placeholder = "Search items here...";
                btn_search.setText(placeholder);
                btn_search.setForeground(java.awt.Color.GRAY);

                btn_search.addFocusListener(new java.awt.event.FocusAdapter() {
                        @Override
                        public void focusGained(java.awt.event.FocusEvent evt) {
                                if (btn_search.getText().equals(placeholder)) {
                                        btn_search.setText("");
                                        btn_search.setForeground(java.awt.Color.BLACK);
                                }
                        }

                        @Override
                        public void focusLost(java.awt.event.FocusEvent evt) {
                                if (btn_search.getText().isEmpty()) {
                                        btn_search.setForeground(java.awt.Color.GRAY);
                                        btn_search.setText(placeholder);
                                }
                        }
                });
        }

        private void setupCalculator() {
                // Create a new CalculatorPanel instance
                CalculatorPanel calculatorPanel = new CalculatorPanel();

                // Set the layout and add it to the Calculater_Panel
                Calculater_Panel.setLayout(new java.awt.BorderLayout());
                Calculater_Panel.add(calculatorPanel, java.awt.BorderLayout.CENTER);
        }

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                Main_Panel = new javax.swing.JPanel();
                Top_Panel = new javax.swing.JPanel();
                lbl_NameOfUser = new javax.swing.JLabel();
                lbl_DateTime = new javax.swing.JLabel();
                IMG_Logo = new rojerusan.RSPanelImage();
                Logout_btn = new javax.swing.JButton();
                Bottom_Panel = new javax.swing.JPanel();
                Right_Panel = new javax.swing.JPanel();
                btn_search = new javax.swing.JTextField();
                Table_Scroll = new javax.swing.JScrollPane();
                Cashier_Table = new javax.swing.JTable();
                Transaction_Summary = new javax.swing.JPanel();
                top_panel_TS = new javax.swing.JPanel();
                Transaction_Summary_lbl = new javax.swing.JLabel();
                Total_lbl = new javax.swing.JLabel();
                colon_01 = new javax.swing.JLabel();
                Discount_lbl = new javax.swing.JLabel();
                colon_02 = new javax.swing.JLabel();
                Item_Count_lbl = new javax.swing.JLabel();
                colon_03 = new javax.swing.JLabel();
                Total_V = new javax.swing.JLabel();
                Discount_V = new javax.swing.JLabel();
                Item_Count_V = new javax.swing.JLabel();
                Grand_Total_lbl = new javax.swing.JLabel();
                colon_5 = new javax.swing.JLabel();
                Grand_Total_V = new javax.swing.JLabel();
                Grand_Total_lbl1 = new javax.swing.JLabel();
                colon_6 = new javax.swing.JLabel();
                lbl_balance = new javax.swing.JLabel();
                Pay_Print = new javax.swing.JButton();
                Cancel = new javax.swing.JButton();
                Transaction_Detail = new javax.swing.JPanel();
                top_panel_TD = new javax.swing.JPanel();
                Transaction_Detail_lbl = new javax.swing.JLabel();
                Cash_lbl = new javax.swing.JLabel();
                colon_2 = new javax.swing.JLabel();
                Discount_lbl1 = new javax.swing.JLabel();
                colon_3 = new javax.swing.JLabel();
                Total_V1 = new javax.swing.JLabel();
                txt_Cash = new javax.swing.JTextField();
                txt_Discount = new javax.swing.JTextField();
                Qty = new javax.swing.JLabel();
                Qty1 = new javax.swing.JLabel();
                qty = new javax.swing.JSpinner();
                Remove = new javax.swing.JButton();
                Left_Panel = new javax.swing.JPanel();
                jPanel1 = new javax.swing.JPanel();
                Calculater_Panel = new javax.swing.JPanel();
                top_panel_TD1 = new javax.swing.JPanel();
                Transaction_Detail_lbl1 = new javax.swing.JLabel();
                Quick_Action = new javax.swing.JPanel();
                top_panel_QA = new javax.swing.JPanel();
                Quick_Action_lbl = new javax.swing.JLabel();
                btn_Void_Item = new javax.swing.JButton();
                btn_Price_Check = new javax.swing.JButton();
                btn_Open_Drawer = new javax.swing.JButton();
                btn_Hold_Trassaction = new javax.swing.JButton();
                btn_Recall_Order = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setTitle("Cashier ");

                Top_Panel.setBackground(new java.awt.Color(4, 63, 23));
                Top_Panel.setPreferredSize(new java.awt.Dimension(1910, 70));

                lbl_NameOfUser.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                lbl_NameOfUser.setForeground(new java.awt.Color(255, 255, 255));

                lbl_DateTime.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                lbl_DateTime.setForeground(new java.awt.Color(255, 255, 255));

                IMG_Logo.setImagen(new javax.swing.ImageIcon(getClass().getResource("/IMG/Logo.png"))); // NOI18N

                javax.swing.GroupLayout IMG_LogoLayout = new javax.swing.GroupLayout(IMG_Logo);
                IMG_Logo.setLayout(IMG_LogoLayout);
                IMG_LogoLayout.setHorizontalGroup(
                                IMG_LogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 275, Short.MAX_VALUE));
                IMG_LogoLayout.setVerticalGroup(
                                IMG_LogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 0, Short.MAX_VALUE));

                Logout_btn.setBackground(new java.awt.Color(255, 102, 102));
                Logout_btn.setFont(new java.awt.Font("Unispace", 1, 12)); // NOI18N
                Logout_btn.setText("Logout");
                Logout_btn.setAlignmentX(0.5F);
                Logout_btn.setBorder(null);
                Logout_btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                Logout_btn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                Logout_btn.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                Logout_btnActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout Top_PanelLayout = new javax.swing.GroupLayout(Top_Panel);
                Top_Panel.setLayout(Top_PanelLayout);
                Top_PanelLayout.setHorizontalGroup(
                                Top_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Top_PanelLayout
                                                                .createSequentialGroup()
                                                                .addComponent(IMG_Logo,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(lbl_DateTime,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                286,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(lbl_NameOfUser,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                247,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(33, 33, 33)
                                                                .addComponent(Logout_btn,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                128,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap()));
                Top_PanelLayout.setVerticalGroup(
                                Top_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(Top_PanelLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(Top_PanelLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(IMG_Logo,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addGroup(Top_PanelLayout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                .addComponent(lbl_NameOfUser,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                58,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addComponent(lbl_DateTime,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                58,
                                                                                                                Short.MAX_VALUE))
                                                                                .addComponent(Logout_btn,
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addContainerGap()));

                Bottom_Panel.setBackground(new java.awt.Color(243, 247, 244));
                Bottom_Panel.setPreferredSize(new java.awt.Dimension(1919, 935));

                Right_Panel.setBackground(new java.awt.Color(243, 247, 244));
                Right_Panel.setPreferredSize(new java.awt.Dimension(960, 940));

                btn_search.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                                btn_searchKeyReleased(evt);
                        }
                });

                Cashier_Table.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {

                                },
                                new String[] {
                                                "ID", "Item_Name", "Price", "Qty", "Total"
                                }) {
                        boolean[] canEdit = new boolean[] {
                                        false, false, false, false, false
                        };

                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                                return canEdit[columnIndex];
                        }
                });
                Cashier_Table.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                Cashier_TableMouseClicked(evt);
                        }
                });
                Table_Scroll.setViewportView(Cashier_Table);
                if (Cashier_Table.getColumnModel().getColumnCount() > 0) {
                        Cashier_Table.getColumnModel().getColumn(0).setResizable(false);
                        Cashier_Table.getColumnModel().getColumn(0).setPreferredWidth(0);
                        Cashier_Table.getColumnModel().getColumn(1).setResizable(false);
                        Cashier_Table.getColumnModel().getColumn(1).setPreferredWidth(350);
                        Cashier_Table.getColumnModel().getColumn(2).setResizable(false);
                        Cashier_Table.getColumnModel().getColumn(2).setPreferredWidth(0);
                        Cashier_Table.getColumnModel().getColumn(3).setResizable(false);
                        Cashier_Table.getColumnModel().getColumn(3).setPreferredWidth(0);
                        Cashier_Table.getColumnModel().getColumn(4).setResizable(false);
                        Cashier_Table.getColumnModel().getColumn(4).setPreferredWidth(0);
                }

                Transaction_Summary.setBackground(new java.awt.Color(243, 247, 244));
                Transaction_Summary.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

                top_panel_TS.setBackground(new java.awt.Color(4, 63, 23));

                Transaction_Summary_lbl.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                Transaction_Summary_lbl.setForeground(new java.awt.Color(255, 255, 255));
                Transaction_Summary_lbl.setText("Transaction Summary");

                javax.swing.GroupLayout top_panel_TSLayout = new javax.swing.GroupLayout(top_panel_TS);
                top_panel_TS.setLayout(top_panel_TSLayout);
                top_panel_TSLayout.setHorizontalGroup(
                                top_panel_TSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                top_panel_TSLayout.createSequentialGroup()
                                                                                .addContainerGap(232, Short.MAX_VALUE)
                                                                                .addComponent(Transaction_Summary_lbl)
                                                                                .addGap(189, 189, 189)));
                top_panel_TSLayout.setVerticalGroup(
                                top_panel_TSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, top_panel_TSLayout
                                                                .createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(Transaction_Summary_lbl,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                36,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap()));

                Total_lbl.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                Total_lbl.setText("Total");

                colon_01.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                colon_01.setText(":");

                Discount_lbl.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                Discount_lbl.setText("Discount");

                colon_02.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                colon_02.setText(":");

                Item_Count_lbl.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                Item_Count_lbl.setText("Item Count");

                colon_03.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                colon_03.setText(":");

                Total_V.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N

                Discount_V.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N

                Item_Count_V.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N

                Grand_Total_lbl.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                Grand_Total_lbl.setText("Balance");

                colon_5.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                colon_5.setText(":");

                Grand_Total_V.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N

                Grand_Total_lbl1.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                Grand_Total_lbl1.setText("Grand Total");

                colon_6.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                colon_6.setText(":");

                lbl_balance.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N

                javax.swing.GroupLayout Transaction_SummaryLayout = new javax.swing.GroupLayout(Transaction_Summary);
                Transaction_Summary.setLayout(Transaction_SummaryLayout);
                Transaction_SummaryLayout.setHorizontalGroup(
                                Transaction_SummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(top_panel_TS, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(Transaction_SummaryLayout.createSequentialGroup()
                                                                .addGap(23, 23, 23)
                                                                .addGroup(Transaction_SummaryLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(Item_Count_lbl)
                                                                                .addComponent(Discount_lbl)
                                                                                .addComponent(Total_lbl)
                                                                                .addComponent(Grand_Total_lbl1)
                                                                                .addComponent(Grand_Total_lbl,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                155,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(Transaction_SummaryLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(colon_6)
                                                                                .addComponent(colon_5)
                                                                                .addComponent(colon_01)
                                                                                .addComponent(colon_02)
                                                                                .addComponent(colon_03))
                                                                .addGap(49, 49, 49)
                                                                .addGroup(Transaction_SummaryLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                false)
                                                                                .addComponent(Item_Count_V,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                280, Short.MAX_VALUE)
                                                                                .addComponent(Total_V,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(Discount_V,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(Grand_Total_V,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(lbl_balance,
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                Transaction_SummaryLayout.setVerticalGroup(
                                Transaction_SummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(Transaction_SummaryLayout.createSequentialGroup()
                                                                .addComponent(top_panel_TS,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(Transaction_SummaryLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(Total_lbl,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                36,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(colon_01,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                36,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(Total_V,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                36,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(Transaction_SummaryLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(Discount_lbl,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                36,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(colon_02,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                36,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(Discount_V,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                36,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(Transaction_SummaryLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(Item_Count_lbl,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                36,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(colon_03,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                36,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(Item_Count_V,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                36,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(Transaction_SummaryLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(colon_5,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                36,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(Grand_Total_V,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                36,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(Grand_Total_lbl1,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                36,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(Transaction_SummaryLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(Transaction_SummaryLayout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                .addComponent(Grand_Total_lbl,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                36,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addComponent(colon_6,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                36,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(lbl_balance,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                36,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addContainerGap(15, Short.MAX_VALUE)));

                Pay_Print.setBackground(new java.awt.Color(147, 202, 55));
                Pay_Print.setFont(new java.awt.Font("Unispace", 1, 18)); // NOI18N
                Pay_Print.setText("Pay & Print");
                Pay_Print.setToolTipText("");
                Pay_Print.setAlignmentX(0.5F);
                Pay_Print.setBorder(null);
                Pay_Print.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                Pay_Print.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                Pay_Print.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                Pay_PrintActionPerformed(evt);
                        }
                });

                Cancel.setBackground(new java.awt.Color(255, 102, 102));
                Cancel.setFont(new java.awt.Font("Unispace", 1, 18)); // NOI18N
                Cancel.setText("Cancel");
                Cancel.setToolTipText("");
                Cancel.setAlignmentX(0.5F);
                Cancel.setBorder(null);
                Cancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                Cancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                Cancel.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                CancelActionPerformed(evt);
                        }
                });

                Transaction_Detail.setBackground(new java.awt.Color(243, 247, 244));
                Transaction_Detail.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

                top_panel_TD.setBackground(new java.awt.Color(4, 63, 23));

                Transaction_Detail_lbl.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                Transaction_Detail_lbl.setForeground(new java.awt.Color(255, 255, 255));
                Transaction_Detail_lbl.setText("Transaction Detail");

                javax.swing.GroupLayout top_panel_TDLayout = new javax.swing.GroupLayout(top_panel_TD);
                top_panel_TD.setLayout(top_panel_TDLayout);
                top_panel_TDLayout.setHorizontalGroup(
                                top_panel_TDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, top_panel_TDLayout
                                                                .createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(Transaction_Detail_lbl)
                                                                .addGap(218, 218, 218)));
                top_panel_TDLayout.setVerticalGroup(
                                top_panel_TDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(top_panel_TDLayout.createSequentialGroup()
                                                                .addComponent(Transaction_Detail_lbl,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                36,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 12, Short.MAX_VALUE)));

                Cash_lbl.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                Cash_lbl.setText("Cash");

                colon_2.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                colon_2.setText(":");

                Discount_lbl1.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                Discount_lbl1.setText("Discount (%)");

                colon_3.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                colon_3.setText(":");

                Total_V1.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N

                txt_Cash.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                txt_CashActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout Transaction_DetailLayout = new javax.swing.GroupLayout(Transaction_Detail);
                Transaction_Detail.setLayout(Transaction_DetailLayout);
                Transaction_DetailLayout.setHorizontalGroup(
                                Transaction_DetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(top_panel_TD, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(Transaction_DetailLayout.createSequentialGroup()
                                                                .addGap(23, 23, 23)
                                                                .addGroup(Transaction_DetailLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(Discount_lbl1)
                                                                                .addComponent(Cash_lbl))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(Transaction_DetailLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(Transaction_DetailLayout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(colon_2)
                                                                                                .addGap(12, 12, 12)
                                                                                                .addComponent(txt_Cash,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                314,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(Total_V1))
                                                                                .addGroup(Transaction_DetailLayout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(colon_3)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                .addComponent(txt_Discount,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                314,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addContainerGap(135, Short.MAX_VALUE)));
                Transaction_DetailLayout.setVerticalGroup(
                                Transaction_DetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(Transaction_DetailLayout.createSequentialGroup()
                                                                .addComponent(top_panel_TD,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(Transaction_DetailLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                false)
                                                                                .addGroup(Transaction_DetailLayout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                .addComponent(Cash_lbl,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                36,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addComponent(colon_2,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                36,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addComponent(Total_V1,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                36,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(txt_Cash))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(Transaction_DetailLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(Transaction_DetailLayout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                .addComponent(colon_3,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                36,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addComponent(txt_Discount,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                36,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(Discount_lbl1,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                36,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addContainerGap(41, Short.MAX_VALUE)));

                Qty.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                Qty.setText("Qty");

                Qty1.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                Qty1.setText(":");

                Remove.setBackground(new java.awt.Color(255, 102, 102));
                Remove.setFont(new java.awt.Font("Unispace", 1, 18)); // NOI18N
                Remove.setText("Remove");
                Remove.setToolTipText("");
                Remove.setAlignmentX(0.5F);
                Remove.setBorder(null);
                Remove.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                Remove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                Remove.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                RemoveActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout Right_PanelLayout = new javax.swing.GroupLayout(Right_Panel);
                Right_Panel.setLayout(Right_PanelLayout);
                Right_PanelLayout.setHorizontalGroup(
                                Right_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(Right_PanelLayout.createSequentialGroup()
                                                                .addGap(21, 21, 21)
                                                                .addGroup(Right_PanelLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                false)
                                                                                .addGroup(Right_PanelLayout
                                                                                                .createSequentialGroup()
                                                                                                .addGroup(Right_PanelLayout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addGroup(Right_PanelLayout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addGap(6, 6, 6)
                                                                                                                                .addComponent(Pay_Print,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                241,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addGap(18, 18, 18)
                                                                                                                                .addComponent(Cancel,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                241,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                .addComponent(Transaction_Detail,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(Transaction_Summary,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE))
                                                                                .addComponent(btn_search)
                                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                Right_PanelLayout
                                                                                                                .createSequentialGroup()
                                                                                                                .addComponent(Qty)
                                                                                                                .addPreferredGap(
                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                .addComponent(Qty1)
                                                                                                                .addPreferredGap(
                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                .addComponent(qty,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                149,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addPreferredGap(
                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                Short.MAX_VALUE)
                                                                                                                .addComponent(Remove,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                207,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(Table_Scroll))
                                                                .addContainerGap(19, Short.MAX_VALUE)));
                Right_PanelLayout.setVerticalGroup(
                                Right_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(Right_PanelLayout.createSequentialGroup()
                                                                .addGap(21, 21, 21)
                                                                .addComponent(btn_search,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                46,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(23, 23, 23)
                                                                .addGroup(Right_PanelLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(Qty,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                36,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(Qty1,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                36,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(qty,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                36,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(Remove,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                46,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                43, Short.MAX_VALUE)
                                                                .addComponent(Table_Scroll,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                417,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(28, 28, 28)
                                                                .addGroup(Right_PanelLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(Transaction_Summary,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGroup(Right_PanelLayout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(Transaction_Detail,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(44, 44, 44)
                                                                                                .addGroup(Right_PanelLayout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                                .addComponent(Cancel,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                59,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(Pay_Print,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                59,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                                .addGap(16, 16, 16)));

                Left_Panel.setBackground(new java.awt.Color(243, 247, 244));
                Left_Panel.setBorder(
                                javax.swing.BorderFactory.createMatteBorder(0, 3, 0, 0, new java.awt.Color(0, 0, 0)));

                jPanel1.setBackground(new java.awt.Color(102, 102, 102));

                Calculater_Panel.setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 5, 5,
                                new java.awt.Color(255, 255, 255)));
                Calculater_Panel.setPreferredSize(new java.awt.Dimension(420, 520));

                javax.swing.GroupLayout Calculater_PanelLayout = new javax.swing.GroupLayout(Calculater_Panel);
                Calculater_Panel.setLayout(Calculater_PanelLayout);
                Calculater_PanelLayout.setHorizontalGroup(
                                Calculater_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 410, Short.MAX_VALUE));
                Calculater_PanelLayout.setVerticalGroup(
                                Calculater_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 510, Short.MAX_VALUE));

                top_panel_TD1.setBackground(new java.awt.Color(4, 63, 23));

                Transaction_Detail_lbl1.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                Transaction_Detail_lbl1.setForeground(new java.awt.Color(255, 255, 255));
                Transaction_Detail_lbl1.setText("Calculator");

                javax.swing.GroupLayout top_panel_TD1Layout = new javax.swing.GroupLayout(top_panel_TD1);
                top_panel_TD1.setLayout(top_panel_TD1Layout);
                top_panel_TD1Layout.setHorizontalGroup(
                                top_panel_TD1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                top_panel_TD1Layout.createSequentialGroup()
                                                                                .addContainerGap(
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(Transaction_Detail_lbl1)
                                                                                .addGap(189, 189, 189)));
                top_panel_TD1Layout.setVerticalGroup(
                                top_panel_TD1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                top_panel_TD1Layout.createSequentialGroup()
                                                                                .addContainerGap(
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(Transaction_Detail_lbl1,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                36,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addContainerGap()));

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(top_panel_TD1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(44, Short.MAX_VALUE)
                                                                .addComponent(Calculater_Panel,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(36, 36, 36)));
                jPanel1Layout.setVerticalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout
                                                                .createSequentialGroup()
                                                                .addComponent(top_panel_TD1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(Calculater_Panel,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(24, Short.MAX_VALUE)));

                Quick_Action.setBackground(new java.awt.Color(243, 247, 244));
                Quick_Action.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

                top_panel_QA.setBackground(new java.awt.Color(4, 63, 23));

                Quick_Action_lbl.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
                Quick_Action_lbl.setForeground(new java.awt.Color(255, 255, 255));
                Quick_Action_lbl.setText("Quick Action");

                javax.swing.GroupLayout top_panel_QALayout = new javax.swing.GroupLayout(top_panel_QA);
                top_panel_QA.setLayout(top_panel_QALayout);
                top_panel_QALayout.setHorizontalGroup(
                                top_panel_QALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, top_panel_QALayout
                                                                .createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(Quick_Action_lbl)
                                                                .addGap(178, 178, 178)));
                top_panel_QALayout.setVerticalGroup(
                                top_panel_QALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, top_panel_QALayout
                                                                .createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(Quick_Action_lbl,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                36,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap()));

                btn_Void_Item.setBackground(new java.awt.Color(147, 202, 55));
                btn_Void_Item.setFont(new java.awt.Font("Unispace", 1, 12)); // NOI18N
                btn_Void_Item.setText("Void_Item");
                btn_Void_Item.setAlignmentX(0.5F);
                btn_Void_Item.setBorder(null);
                btn_Void_Item.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btn_Void_Item.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                btn_Void_Item.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btn_Void_ItemActionPerformed(evt);
                        }
                });

                btn_Price_Check.setBackground(new java.awt.Color(147, 202, 55));
                btn_Price_Check.setFont(new java.awt.Font("Unispace", 1, 12)); // NOI18N
                btn_Price_Check.setText("Price_Check");
                btn_Price_Check.setAlignmentX(0.5F);
                btn_Price_Check.setBorder(null);
                btn_Price_Check.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btn_Price_Check.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                btn_Price_Check.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btn_Price_CheckActionPerformed(evt);
                        }
                });

                btn_Open_Drawer.setBackground(new java.awt.Color(147, 202, 55));
                btn_Open_Drawer.setFont(new java.awt.Font("Unispace", 1, 12)); // NOI18N
                btn_Open_Drawer.setText("Open_Drawer");
                btn_Open_Drawer.setToolTipText("");
                btn_Open_Drawer.setAlignmentX(0.5F);
                btn_Open_Drawer.setBorder(null);
                btn_Open_Drawer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btn_Open_Drawer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                btn_Open_Drawer.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btn_Open_DrawerActionPerformed(evt);
                        }
                });

                btn_Hold_Trassaction.setBackground(new java.awt.Color(147, 202, 55));
                btn_Hold_Trassaction.setFont(new java.awt.Font("Unispace", 1, 12)); // NOI18N
                btn_Hold_Trassaction.setText("Hold_Trassaction");
                btn_Hold_Trassaction.setAlignmentX(0.5F);
                btn_Hold_Trassaction.setBorder(null);
                btn_Hold_Trassaction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btn_Hold_Trassaction.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                btn_Hold_Trassaction.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btn_Hold_TrassactionActionPerformed(evt);
                        }
                });

                btn_Recall_Order.setBackground(new java.awt.Color(147, 202, 55));
                btn_Recall_Order.setFont(new java.awt.Font("Unispace", 1, 12)); // NOI18N
                btn_Recall_Order.setText("Recall_Order");
                btn_Recall_Order.setAlignmentX(0.5F);
                btn_Recall_Order.setBorder(null);
                btn_Recall_Order.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btn_Recall_Order.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                btn_Recall_Order.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btn_Recall_OrderActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout Quick_ActionLayout = new javax.swing.GroupLayout(Quick_Action);
                Quick_Action.setLayout(Quick_ActionLayout);
                Quick_ActionLayout.setHorizontalGroup(
                                Quick_ActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(top_panel_QA, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(Quick_ActionLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(Quick_ActionLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                false)
                                                                                .addComponent(btn_Recall_Order,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                238, Short.MAX_VALUE)
                                                                                .addComponent(btn_Price_Check,
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(btn_Void_Item,
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(Quick_ActionLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(btn_Hold_Trassaction,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(btn_Open_Drawer,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addContainerGap()));
                Quick_ActionLayout.setVerticalGroup(
                                Quick_ActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(Quick_ActionLayout.createSequentialGroup()
                                                                .addComponent(top_panel_QA,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(Quick_ActionLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(btn_Void_Item,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                51,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btn_Hold_Trassaction,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                51,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(Quick_ActionLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(btn_Price_Check,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                51,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btn_Open_Drawer,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                51,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(btn_Recall_Order,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                51,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 19, Short.MAX_VALUE)));

                javax.swing.GroupLayout Left_PanelLayout = new javax.swing.GroupLayout(Left_Panel);
                Left_Panel.setLayout(Left_PanelLayout);
                Left_PanelLayout.setHorizontalGroup(
                                Left_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Left_PanelLayout
                                                                .createSequentialGroup()
                                                                .addContainerGap(47, Short.MAX_VALUE)
                                                                .addGroup(Left_PanelLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                false)
                                                                                .addComponent(jPanel1,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(Quick_Action,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addGap(39, 39, 39)));
                Left_PanelLayout.setVerticalGroup(
                                Left_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(Left_PanelLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(jPanel1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(Quick_Action,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(399, Short.MAX_VALUE)));

                javax.swing.GroupLayout Bottom_PanelLayout = new javax.swing.GroupLayout(Bottom_Panel);
                Bottom_Panel.setLayout(Bottom_PanelLayout);
                Bottom_PanelLayout.setHorizontalGroup(
                                Bottom_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(Bottom_PanelLayout.createSequentialGroup()
                                                                .addComponent(Right_Panel,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                1332,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(Left_Panel,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)));
                Bottom_PanelLayout.setVerticalGroup(
                                Bottom_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(Right_Panel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(Left_Panel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE));

                javax.swing.GroupLayout Main_PanelLayout = new javax.swing.GroupLayout(Main_Panel);
                Main_Panel.setLayout(Main_PanelLayout);
                Main_PanelLayout.setHorizontalGroup(
                                Main_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(Main_PanelLayout.createSequentialGroup()
                                                                .addGap(0, 0, 0)
                                                                .addGroup(Main_PanelLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(Top_Panel,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                1919, Short.MAX_VALUE)
                                                                                .addGroup(Main_PanelLayout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(Bottom_Panel,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(0, 0, Short.MAX_VALUE)))
                                                                .addContainerGap()));
                Main_PanelLayout.setVerticalGroup(
                                Main_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(Main_PanelLayout.createSequentialGroup()
                                                                .addComponent(Top_Panel,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, 0)
                                                                .addComponent(Bottom_Panel,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                940, Short.MAX_VALUE)));

                getContentPane().add(Main_Panel, java.awt.BorderLayout.CENTER);

                setSize(new java.awt.Dimension(1935, 1019));
                setLocationRelativeTo(null);
        }// </editor-fold>//GEN-END:initComponents

        private void btn_searchKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_btn_searchKeyReleased
                String query = btn_search.getText().trim();
                int keyCode = evt.getKeyCode();

                // Handle keyboard navigation for suggestions
                if (searchSuggestions.isVisible()) {
                        // Down Arrow checks focus
                        if (keyCode == java.awt.event.KeyEvent.VK_DOWN) {
                                if (searchSuggestions.getComponentCount() > 0) {
                                        searchSuggestions.requestFocus();
                                        javax.swing.MenuSelectionManager.defaultManager()
                                                        .setSelectedPath(new javax.swing.MenuElement[] {
                                                                        searchSuggestions,
                                                                        (javax.swing.JMenuItem) searchSuggestions
                                                                                        .getComponent(0)
                                                        });
                                }
                                return; // Prevent further processing
                        }

                        // Enter Key selects the highlighted item
                        if (keyCode == java.awt.event.KeyEvent.VK_ENTER) {
                                javax.swing.MenuElement[] path = javax.swing.MenuSelectionManager.defaultManager()
                                                .getSelectedPath();
                                if (path.length > 1 && path[1] instanceof JMenuItem) {
                                        ((JMenuItem) path[1]).doClick();
                                }
                                return;
                        }
                }
                if (evt.isActionKey() || keyCode == java.awt.event.KeyEvent.VK_ENTER
                                || keyCode == java.awt.event.KeyEvent.VK_ESCAPE) {
                        return;
                }
                if (query.isEmpty() || query.equals("Search items here...")) {
                        searchSuggestions.setVisible(false);
                        return;
                }

                // Fetch matching items
                List<Stock> results = stockController.searchStock(query);
                searchSuggestions.removeAll();

                if (results.isEmpty()) {
                        searchSuggestions.add(new JMenuItem("No items found"));
                } else {
                        for (Stock item : results) {
                                String label = String.format("%s - Rs.%.2f (Stock: %d)",
                                                item.getProductName(), item.getPrice(), item.getQuantity());
                                JMenuItem menuItem = new JMenuItem(label);
                                // Ensure it works with both mouse and keyboard
                                menuItem.addActionListener(e -> {
                                        qty.requestFocus();
                                        addItemToCart(item);
                                        searchSuggestions.setVisible(false);
                                });
                                searchSuggestions.add(menuItem);
                        }
                }

                searchSuggestions.pack();
                // Show suggestions below search field
                if (searchSuggestions.getComponentCount() > 0) {
                        searchSuggestions.show(btn_search, 0, btn_search.getHeight());
                        btn_search.requestFocus(); // Keep focus for typing
                }
        }// GEN-LAST:event_btn_searchKeyReleased

        private void addItemToCart(Stock item) {
                int qtyRequested = (Integer) qty.getValue();
                if (qtyRequested <= 0) {
                        JOptionPane.showMessageDialog(this, "Please select a valid quantity", "Invalid Quantity",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                if (item.getQuantity() < qtyRequested) {
                        JOptionPane.showMessageDialog(this, "Insufficient stock! Available: " + item.getQuantity(),
                                        "Stock Warning",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                DefaultTableModel model = (DefaultTableModel) Cashier_Table.getModel();
                boolean exists = false;

                // Check if item already in table
                for (int i = 0; i < model.getRowCount(); i++) {
                        if ((Integer) model.getValueAt(i, 0) == item.getProductId()) {
                                int currentQty = (Integer) model.getValueAt(i, 3);
                                int newQty = currentQty + qtyRequested;

                                if (newQty > item.getQuantity()) {
                                        JOptionPane.showMessageDialog(this, "Total quantity exceeds stock!",
                                                        "Stock Warning",
                                                        JOptionPane.WARNING_MESSAGE);
                                        return;
                                }

                                model.setValueAt(newQty, i, 3);
                                model.setValueAt(newQty * item.getPrice(), i, 4);
                                exists = true;
                                break;
                        }
                }

                if (!exists) {
                        model.addRow(new Object[] {
                                        item.getProductId(),
                                        item.getProductName(),
                                        item.getPrice(),
                                        qtyRequested,
                                        qtyRequested * item.getPrice()
                        });
                }

                calculateTotals();
                btn_search.setText("");
                btn_search.requestFocus();
        }

        private void calculateTotals() {
                DefaultTableModel model = (DefaultTableModel) Cashier_Table.getModel();
                double subtotal = 0;
                int itemCount = 0;

                for (int i = 0; i < model.getRowCount(); i++) {
                        subtotal += (Double) model.getValueAt(i, 4);
                        itemCount += (Integer) model.getValueAt(i, 3);
                }

                Total_V.setText(String.format("%.2f", subtotal));
                Item_Count_V.setText(String.valueOf(itemCount));

                // Get discount percentage
                double discountPercent = 0;
                try {
                        String discText = txt_Discount.getText().trim();
                        if (!discText.isEmpty()) {
                                discountPercent = Double.parseDouble(discText);
                        }
                } catch (NumberFormatException e) {
                        // Ignore invalid discount format
                }

                double discountAmount = subtotal * (discountPercent / 100.0);
                Discount_V.setText(String.format("%.2f", discountAmount));

                double grandTotal = subtotal - discountAmount;
                Grand_Total_V.setText(String.format("%.2f", grandTotal));

                // Update Balance
                try {
                        String cashText = txt_Cash.getText().trim();
                        if (!cashText.isEmpty()) {
                                double cash = Double.parseDouble(cashText);
                                double balance = cash - grandTotal;
                                if (balance < 0) {
                                        lbl_balance.setText("Insufficient Balance");
                                        lbl_balance.setForeground(java.awt.Color.RED);
                                } else {
                                        lbl_balance.setText(String.format("%.2f", balance));
                                        lbl_balance.setForeground(java.awt.Color.BLACK);
                                }
                        } else {
                                lbl_balance.setText("0.00");
                                lbl_balance.setForeground(java.awt.Color.BLACK);
                        }
                } catch (NumberFormatException e) {
                        lbl_balance.setText("Invalid Cash");
                        lbl_balance.setForeground(java.awt.Color.RED);
                }
        }

        private void resetSummary() {
                Total_V.setText("0.00");
                Discount_V.setText("0.00");
                Item_Count_V.setText("0");
                Grand_Total_V.setText("0.00");
                txt_Cash.setText("");
                lbl_balance.setText("0.00");
                lbl_balance.setForeground(java.awt.Color.BLACK);
                txt_Discount.setText("");
                qty.setValue(1);
                ((DefaultTableModel) Cashier_Table.getModel()).setRowCount(0);
        }

        private void Logout_btnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_Logout_btnActionPerformed
                int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout",
                                JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                        this.dispose();
                        new GUI.LoginFrame().setVisible(true);
                }
        }// GEN-LAST:event_Logout_btnActionPerformed

        private void txt_CashActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txt_CashActionPerformed
                Pay_PrintActionPerformed(evt);
        }// GEN-LAST:event_txt_CashActionPerformed

        private void Cashier_TableMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_Cashier_TableMouseClicked
                Remove.setEnabled(true);
        }// GEN-LAST:event_Cashier_TableMouseClicked

        private void RemoveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_RemoveActionPerformed
                int selectedRow = Cashier_Table.getSelectedRow();
                if (selectedRow != -1) {

                        ((DefaultTableModel) Cashier_Table.getModel()).removeRow(selectedRow);
                        calculateTotals();
                } else {
                        JOptionPane.showMessageDialog(this, "Please select an item to remove", "Selection Error",
                                        JOptionPane.WARNING_MESSAGE);
                }
        }// GEN-LAST:event_RemoveActionPerformed

        private void btn_Void_ItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_Void_ItemActionPerformed
                RemoveActionPerformed(evt);
        }// GEN-LAST:event_btn_Void_ItemActionPerformed

        private void btn_Price_CheckActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_Price_CheckActionPerformed
                String query = JOptionPane.showInputDialog(this, "Enter product name for Price Check:");
                if (query != null && !query.trim().isEmpty()) {
                        List<Stock> results = stockController.searchStock(query.trim());
                        if (!results.isEmpty()) {
                                Stock item = results.get(0);
                                JOptionPane.showMessageDialog(this,
                                                String.format("Product: %s\nCategory: %s\nPrice: Rs.%.2f\nAvailable Stock: %d",
                                                                item.getProductName(), item.getCategoryName(),
                                                                item.getPrice(), item.getQuantity()),
                                                "Price Check", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                                JOptionPane.showMessageDialog(this, "Product not found!", "Error",
                                                JOptionPane.ERROR_MESSAGE);
                        }
                }
        }// GEN-LAST:event_btn_Price_CheckActionPerformed

        private void btn_Open_DrawerActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_Open_DrawerActionPerformed
                JOptionPane.showMessageDialog(this, "Cash Drawer Opened", "Drawer", JOptionPane.INFORMATION_MESSAGE);
        }// GEN-LAST:event_btn_Open_DrawerActionPerformed

        private void btn_Hold_TrassactionActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_Hold_TrassactionActionPerformed
                DefaultTableModel model = (DefaultTableModel) Cashier_Table.getModel();
                if (model.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(this, "Nothing to hold!", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                }

                heldTransaction = new java.util.ArrayList<>();
                for (int i = 0; i < model.getRowCount(); i++) {
                        Object[] row = new Object[model.getColumnCount()];
                        for (int j = 0; j < model.getColumnCount(); j++) {
                                row[j] = model.getValueAt(i, j);
                        }
                        heldTransaction.add(row);
                }

                resetSummary();
                JOptionPane.showMessageDialog(this, "Current transaction held.", "Transaction Held",
                                JOptionPane.INFORMATION_MESSAGE);
        }// GEN-LAST:event_btn_Hold_TrassactionActionPerformed

        private void btn_Recall_OrderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_Recall_OrderActionPerformed
                if (heldTransaction == null || heldTransaction.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "No held transaction found!", "Note",
                                        JOptionPane.INFORMATION_MESSAGE);
                        return;
                }

                DefaultTableModel model = (DefaultTableModel) Cashier_Table.getModel();
                if (model.getRowCount() > 0) {
                        int result = JOptionPane.showConfirmDialog(this, "Discard current cart and recall held order?",
                                        "Confirm Recall", JOptionPane.YES_NO_OPTION);
                        if (result != JOptionPane.YES_OPTION)
                                return;
                }

                model.setRowCount(0);
                for (Object[] row : heldTransaction) {
                        model.addRow(row);
                }
                heldTransaction = null; // Clear after recall
                calculateTotals();
                JOptionPane.showMessageDialog(this, "Held transaction recalled.", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
        }// GEN-LAST:event_btn_Recall_OrderActionPerformed

        private void Pay_PrintActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_Pay_PrintActionPerformed
                DefaultTableModel model = (DefaultTableModel) Cashier_Table.getModel();
                if (model.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(this, "Empty cart!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                }

                double grandTotal;
                try {
                        grandTotal = Double.parseDouble(Grand_Total_V.getText());
                } catch (NumberFormatException e) {
                        grandTotal = 0;
                }

                double cashReceived = 0;
                try {
                        cashReceived = Double.parseDouble(txt_Cash.getText().trim());
                        if (cashReceived < grandTotal) {
                                JOptionPane.showMessageDialog(this, "Insufficient cash!", "Error",
                                                JOptionPane.ERROR_MESSAGE);
                                return;
                        }
                } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Invalid cash amount!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                }

                int result = JOptionPane.showConfirmDialog(this, "Confirm transaction and print receipt?",
                                "Confirm Purchase", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                        // Prepare Sale object
                        Sale sale = new Sale();
                        sale.setCashierName(currentUser != null ? currentUser.getFullName() : lbl_NameOfUser.getText());
                        sale.setSubtotal(Double.parseDouble(Total_V.getText()));
                        sale.setDiscount(Double.parseDouble(Discount_V.getText()));
                        sale.setGrandTotal(grandTotal);
                        sale.setCashReceived(cashReceived);
                        sale.setBalance(cashReceived - grandTotal);

                        // Prepare SaleItems list
                        List<SaleItem> saleItems = new ArrayList<>();
                        for (int i = 0; i < model.getRowCount(); i++) {
                                SaleItem si = new SaleItem();
                                si.setProductId((Integer) model.getValueAt(i, 0));
                                si.setProductName((String) model.getValueAt(i, 1));
                                si.setQuantity((Integer) model.getValueAt(i, 3));
                                si.setUnitPrice((Double) model.getValueAt(i, 2));
                                si.setTotalPrice((Double) model.getValueAt(i, 4));
                                saleItems.add(si);
                        }

                        // Save to database
                        String status = salesController.saveSale(sale, saleItems);

                        if (status.equals("SUCCESS")) {
                                double subtotal = Double.parseDouble(Total_V.getText());
                                double discountAmount = Double.parseDouble(Discount_V.getText());
                                double change = cashReceived - grandTotal;
                                showReceiptDialog(model, subtotal, discountAmount, grandTotal, cashReceived, change);
                                resetSummary();
                        } else {
                                JOptionPane.showMessageDialog(this, "Error saving transaction: " + status,
                                                "Database Error", JOptionPane.ERROR_MESSAGE);
                        }
                }
        }// GEN-LAST:event_Pay_PrintActionPerformed

        private void CancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_CancelActionPerformed
                int result = JOptionPane.showConfirmDialog(this,
                                "Are you sure you want to cancel the current transaction?", "Cancel Transaction",
                                JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                        resetSummary();
                }
        }// GEN-LAST:event_CancelActionPerformed

        private void showReceiptDialog(DefaultTableModel model, double subtotal, double discount, double grandTotal,
                        double cash, double change) {
                StringBuilder receipt = new StringBuilder();
                String sep = "------------------------------------------\n";
                receipt.append("          FOODCITY GROCERY SYSTEM          \n");
                receipt.append("          123 Main Street, City          \n");
                receipt.append("            Tel: +94 11 234 5678          \n");
                receipt.append(sep);
                receipt.append("Date: ")
                                .append(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                                .format(new java.util.Date()))
                                .append("\n");
                receipt.append("Cashier: ").append(lbl_NameOfUser.getText()).append("\n");
                receipt.append(sep);
                receipt.append(String.format("%-20s %4s %10s\n", "Item", "Qty", "Amount"));
                receipt.append(sep);

                for (int i = 0; i < model.getRowCount(); i++) {
                        String name = (String) model.getValueAt(i, 1);
                        if (name.length() > 20)
                                name = name.substring(0, 17) + "...";
                        receipt.append(String.format("%-20s %4d %10.2f\n",
                                        name,
                                        (Integer) model.getValueAt(i, 3),
                                        (Double) model.getValueAt(i, 4)));
                }

                receipt.append(sep);
                receipt.append(String.format("Subtotal:            Rs.%10.2f\n", subtotal));
                receipt.append(String.format("Discount:            Rs.%10.2f\n", discount));
                receipt.append(String.format("Grand Total:         Rs.%10.2f\n", grandTotal));
                receipt.append(sep);
                receipt.append(String.format("Cash Received:       Rs.%10.2f\n", cash));
                receipt.append(String.format("Change:              Rs.%10.2f\n", change));
                receipt.append(sep);
                receipt.append("        THANK YOU! COME AGAIN!        \n");

                // Save to Desktop
                saveReceiptToDesktop(receipt.toString());

                javax.swing.JTextArea textArea = new javax.swing.JTextArea(receipt.toString());
                textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 14));
                textArea.setEditable(false);
                textArea.setMargin(new java.awt.Insets(10, 10, 10, 10));

                javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(textArea);
                scrollPane.setPreferredSize(new java.awt.Dimension(400, 500));

                JOptionPane.showMessageDialog(this, scrollPane, "Sales Receipt", JOptionPane.PLAIN_MESSAGE);

                // Also print to console for logging
                System.out.println(receipt.toString());
        }

        private void saveReceiptToDesktop(String receiptContent) {
                try {
                        String userHome = System.getProperty("user.home");
                        String fileName = "Bill_"
                                        + new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date())
                                        + ".txt";
                        java.io.File file = new java.io.File(userHome + "/Desktop/" + fileName);

                        try (java.io.FileWriter writer = new java.io.FileWriter(file)) {
                                writer.write(receiptContent);
                                System.out.println("Receipt saved to Desktop: " + file.getAbsolutePath());
                        }
                } catch (java.io.IOException e) {
                        System.err.println("Error saving receipt to desktop: " + e.getMessage());
                        // Optional: Show a subtle error message to the user, but maybe not interrupt
                        // flow
                        // JOptionPane.showMessageDialog(this, "Could not save bill to Desktop", "Save
                        // Error", JOptionPane.WARNING_MESSAGE);
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
                java.awt.EventQueue.invokeLater(() -> new CashierDashboard().setVisible(true));
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JPanel Bottom_Panel;
        private javax.swing.JPanel Calculater_Panel;
        private javax.swing.JButton Cancel;
        private javax.swing.JLabel Cash_lbl;
        private javax.swing.JTable Cashier_Table;
        private javax.swing.JLabel Discount_V;
        private javax.swing.JLabel Discount_lbl;
        private javax.swing.JLabel Discount_lbl1;
        private javax.swing.JLabel Grand_Total_V;
        private javax.swing.JLabel Grand_Total_lbl;
        private javax.swing.JLabel Grand_Total_lbl1;
        private rojerusan.RSPanelImage IMG_Logo;
        private javax.swing.JLabel Item_Count_V;
        private javax.swing.JLabel Item_Count_lbl;
        private javax.swing.JPanel Left_Panel;
        private javax.swing.JButton Logout_btn;
        private javax.swing.JPanel Main_Panel;
        private javax.swing.JButton Pay_Print;
        private javax.swing.JLabel Qty;
        private javax.swing.JLabel Qty1;
        private javax.swing.JPanel Quick_Action;
        private javax.swing.JLabel Quick_Action_lbl;
        private javax.swing.JButton Remove;
        private javax.swing.JPanel Right_Panel;
        private javax.swing.JScrollPane Table_Scroll;
        private javax.swing.JPanel Top_Panel;
        private javax.swing.JLabel Total_V;
        private javax.swing.JLabel Total_V1;
        private javax.swing.JLabel Total_lbl;
        private javax.swing.JPanel Transaction_Detail;
        private javax.swing.JLabel Transaction_Detail_lbl;
        private javax.swing.JLabel Transaction_Detail_lbl1;
        private javax.swing.JPanel Transaction_Summary;
        private javax.swing.JLabel Transaction_Summary_lbl;
        private javax.swing.JButton btn_Hold_Trassaction;
        private javax.swing.JButton btn_Open_Drawer;
        private javax.swing.JButton btn_Price_Check;
        private javax.swing.JButton btn_Recall_Order;
        private javax.swing.JButton btn_Void_Item;
        private javax.swing.JTextField btn_search;
        private javax.swing.JLabel colon_01;
        private javax.swing.JLabel colon_02;
        private javax.swing.JLabel colon_03;
        private javax.swing.JLabel colon_2;
        private javax.swing.JLabel colon_3;
        private javax.swing.JLabel colon_5;
        private javax.swing.JLabel colon_6;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JLabel lbl_DateTime;
        private javax.swing.JLabel lbl_NameOfUser;
        private javax.swing.JLabel lbl_balance;
        private javax.swing.JSpinner qty;
        private javax.swing.JPanel top_panel_QA;
        private javax.swing.JPanel top_panel_TD;
        private javax.swing.JPanel top_panel_TD1;
        private javax.swing.JPanel top_panel_TS;
        private javax.swing.JTextField txt_Cash;
        private javax.swing.JTextField txt_Discount;
        // End of variables declaration//GEN-END:variables
}
