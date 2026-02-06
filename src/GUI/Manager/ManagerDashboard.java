/*
 * Manager Dashboard
 * This is the central control panel for the Manager.
 */
package GUI.Manager;

import GUI.LoginFrame;
import GUI.SalesHistoryFrame;
import controller.StockController;
import controller.SalesController;
import model.Stock;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.*;
import javax.swing.*;

/**
 * Main dashboard for application managers.
 * Shows sales charts, stock alerts, and provides navigation to other management
 * screens.
 */
public class ManagerDashboard extends javax.swing.JFrame {

        private StockController stockController;
        private SalesController salesController;
        private javax.swing.Timer refreshTimer;
        private static final int REFRESH_INTERVAL = 10000; // 10 seconds

        public ManagerDashboard() {
                stockController = new StockController();
                salesController = new SalesController();
                initComponents();
                setLocationRelativeTo(null);
                this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

                createChart(); // Initialize the sales chart
                loadLowStockAlerts(); // Initial load of stock alerts
                loadDashboardStats(); // Initial load of sales stats
                startAutoRefresh(); // Start the background refresh timer
        }

        /**
         * Loads sales and profit statistics from the database.
         */
        private void loadDashboardStats() {
                try {
                        double monthlyProfit = salesController.getMonthlyProfit();
                        double monthlySales = salesController.getMonthlySales();
                        double todaySales = salesController.getTodaySales();

                        lblMonthlyProfit.setText(String.format("$ %.2f", monthlyProfit));
                        lblshowTotalSales.setText(String.format("$ %.2f", monthlySales));
                        lblMonthlyProfit5.setText(String.format("$ %.2f", todaySales));

                } catch (Exception e) {
                        System.err.println("Error loading dashboard stats: " + e.getMessage());
                        lblMonthlyProfit.setText("Error");
                        lblshowTotalSales.setText("Error");
                        lblMonthlyProfit5.setText("Error");
                }
        }

        /**
         * Creates and displays a bar chart for monthly sales.
         */
        private void createChart() {
                try {
                        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                        // Fetch real data from controller
                        java.util.LinkedHashMap<String, Double> salesData = salesController.getLastSixMonthsSales();

                        if (salesData.isEmpty()) {
                                dataset.addValue(0, "Sales", "No Data");
                        } else {
                                for (java.util.Map.Entry<String, Double> entry : salesData.entrySet()) {
                                        dataset.addValue(entry.getValue(), "Sales", entry.getKey());
                                }
                        }

                        JFreeChart chart = ChartFactory.createBarChart(
                                        "Monthly Sales Overview",
                                        "Month",
                                        "Sales ($)",
                                        dataset,
                                        PlotOrientation.VERTICAL,
                                        true, true, false);

                        chart.setBackgroundPaint(Color.WHITE);
                        CategoryPlot plot = (CategoryPlot) chart.getPlot();
                        plot.setBackgroundPaint(new Color(243, 247, 244));
                        plot.setRangeGridlinePaint(Color.GRAY);

                        ChartPanel chartPanel = new ChartPanel(chart);
                        chartPanel.setPreferredSize(new Dimension(380, 370));
                        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                        jpanelchart.removeAll();
                        jpanelchart.setLayout(new BorderLayout());
                        jpanelchart.add(chartPanel, BorderLayout.CENTER);
                        jpanelchart.revalidate();
                        jpanelchart.repaint();

                } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Error creating chart: " + e.getMessage());
                }
        }

        /**
         * Fetches items with low stock and displays alerts in the text area.
         */
        private void loadLowStockAlerts() {
                int threshold = 50;

                try {
                        List<Stock> lowStockList = stockController.getLowStockItems(threshold);
                        txt_lowStockArea.setText("");

                        if (lowStockList.isEmpty()) {
                                txt_lowStockArea.setText("\n  All stock levels are good!\n\n  No items below "
                                                + threshold + " units.");
                        } else {
                                StringBuilder alertText = new StringBuilder();
                                alertText.append(" Items Running Low\n");
                                alertText.append(" ════════════════════════\n\n");

                                for (Stock stock : lowStockList) {
                                        String urgency = getUrgencyLevel(stock.getQuantity());
                                        alertText.append(String.format(" %s %s\n", urgency, stock.getProductName()));
                                        alertText.append(String.format("    Qty: %d | %s\n", stock.getQuantity(),
                                                        stock.getCategoryName()));
                                        alertText.append("    ─────────────────\n");
                                }

                                alertText.append("\n Total: " + lowStockList.size() + " items need attention");
                                txt_lowStockArea.setText(alertText.toString());
                                txt_lowStockArea.setCaretPosition(0);
                        }
                } catch (Exception e) {
                        txt_lowStockArea.setText("\n  Error loading stock alerts:\n  " + e.getMessage());
                }
        }

        /**
         * Determines the urgency prefix based on quantity.
         */
        private String getUrgencyLevel(int quantity) {
                if (quantity < 20)
                        return "[CRITICAL]";
                if (quantity < 40)
                        return "[WARNING] ";
                return "[LOW]     ";
        }

        /**
         * Starts the timer to refresh stock alerts periodically.
         */
        private void startAutoRefresh() {
                refreshTimer = new javax.swing.Timer(REFRESH_INTERVAL, (e) -> {
                        loadLowStockAlerts();
                        loadDashboardStats();
                });
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

        @Override
        public void dispose() {
                stopAutoRefresh(); // Stop the timer when the window is closed
                super.dispose();
        }

        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                menuBar1 = new java.awt.MenuBar();
                menu1 = new java.awt.Menu();
                menu2 = new java.awt.Menu();
                jPanel1 = new javax.swing.JPanel();
                jPanel2 = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                btnLogout = new javax.swing.JButton();
                btnStockmanagement = new javax.swing.JButton();
                btnproductmanagement = new javax.swing.JButton();
                btnReport = new javax.swing.JButton();
                btnSuppliermanagement = new javax.swing.JButton();
                GRNbtn1 = new javax.swing.JButton();
                jPanel3 = new javax.swing.JPanel();
                Welcome = new javax.swing.JLabel();
                MonthlyProfit = new javax.swing.JPanel();
                lblMonthlyProfit1 = new javax.swing.JLabel();
                lblMonthlyProfit = new javax.swing.JLabel();
                TotalSales = new javax.swing.JPanel();
                lblTotalSales = new javax.swing.JLabel();
                lblshowTotalSales = new javax.swing.JLabel();
                TodaySales = new javax.swing.JPanel();
                lblTodaySales = new javax.swing.JLabel();
                lblMonthlyProfit5 = new javax.swing.JLabel();
                jpanelchart = new javax.swing.JPanel();
                lbl_username = new javax.swing.JLabel();
                jPanel4 = new javax.swing.JPanel();
                jLabel3 = new javax.swing.JLabel();
                jScrollPane_lowStock = new javax.swing.JScrollPane();
                txt_lowStockArea = new javax.swing.JTextArea();

                menu1.setLabel("File");
                menuBar1.add(menu1);

                menu2.setLabel("Edit");
                menuBar1.add(menu2);

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setTitle("Admin Dashboard");
                setMinimumSize(new java.awt.Dimension(1000, 600));
                setSize(new java.awt.Dimension(1000, 600));

                jPanel1.setBackground(new java.awt.Color(243, 247, 244));
                jPanel1.setAlignmentX(0.0F);
                jPanel1.setAlignmentY(0.0F);
                jPanel1.setMaximumSize(new java.awt.Dimension(1920, 1080));
                jPanel1.setMinimumSize(new java.awt.Dimension(1000, 600));

                jPanel2.setBackground(new java.awt.Color(73, 128, 37));

                jLabel1.setFont(new java.awt.Font("Unispace", 1, 24)); // NOI18N
                jLabel1.setForeground(new java.awt.Color(243, 247, 244));
                jLabel1.setText("MANAGER PANEL");

                btnLogout.setBackground(new java.awt.Color(255, 102, 102));
                btnLogout.setFont(new java.awt.Font("Unispace", 1, 12)); // NOI18N
                btnLogout.setForeground(new java.awt.Color(5, 63, 22));
                btnLogout.setText("Logout ");
                btnLogout.setAlignmentX(0.5F);
                btnLogout.setBorder(null);
                btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnLogout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                btnLogout.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnLogoutActionPerformed(evt);
                        }
                });

                btnStockmanagement.setBackground(new java.awt.Color(147, 202, 55));
                btnStockmanagement.setFont(new java.awt.Font("Unispace", 1, 12)); // NOI18N
                btnStockmanagement.setForeground(new java.awt.Color(5, 63, 22));
                btnStockmanagement.setText("Stock Management");
                btnStockmanagement.setAlignmentX(0.5F);
                btnStockmanagement.setBorder(null);
                btnStockmanagement.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnStockmanagement.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                btnStockmanagement.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnStockmanagementActionPerformed(evt);
                        }
                });

                btnproductmanagement.setBackground(new java.awt.Color(147, 202, 55));
                btnproductmanagement.setFont(new java.awt.Font("Unispace", 1, 12)); // NOI18N
                btnproductmanagement.setForeground(new java.awt.Color(5, 63, 22));
                btnproductmanagement.setText("Product Management");
                btnproductmanagement.setAlignmentX(0.5F);
                btnproductmanagement.setBorder(null);
                btnproductmanagement.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnproductmanagement.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                btnproductmanagement.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnproductmanagementActionPerformed(evt);
                        }
                });

                btnReport.setBackground(new java.awt.Color(147, 202, 55));
                btnReport.setFont(new java.awt.Font("Unispace", 1, 12)); // NOI18N
                btnReport.setForeground(new java.awt.Color(5, 63, 22));
                btnReport.setText("Reports");
                btnReport.setAlignmentX(0.5F);
                btnReport.setBorder(null);
                btnReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnReport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                btnReport.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnReportActionPerformed(evt);
                        }
                });

                btnSuppliermanagement.setBackground(new java.awt.Color(147, 202, 55));
                btnSuppliermanagement.setFont(new java.awt.Font("Unispace", 1, 12)); // NOI18N
                btnSuppliermanagement.setForeground(new java.awt.Color(5, 63, 22));
                btnSuppliermanagement.setText("Supplier Management");
                btnSuppliermanagement.setAlignmentX(0.5F);
                btnSuppliermanagement.setBorder(null);
                btnSuppliermanagement.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnSuppliermanagement.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                btnSuppliermanagement.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnSuppliermanagementActionPerformed(evt);
                        }
                });

                GRNbtn1.setBackground(new java.awt.Color(147, 202, 55));
                GRNbtn1.setFont(new java.awt.Font("Unispace", 1, 12)); // NOI18N
                GRNbtn1.setForeground(new java.awt.Color(5, 63, 22));
                GRNbtn1.setText("GRN");
                GRNbtn1.setAlignmentX(0.5F);
                GRNbtn1.setBorder(null);
                GRNbtn1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                GRNbtn1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                GRNbtn1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                GRNbtn1ActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addGroup(jPanel2Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                false)
                                                                                .addComponent(btnStockmanagement,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(btnproductmanagement,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(btnLogout,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(btnReport,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                228, Short.MAX_VALUE)
                                                                                .addComponent(btnSuppliermanagement,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(GRNbtn1,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                228, Short.MAX_VALUE))
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                jPanel2Layout.createSequentialGroup()
                                                                                .addGap(0, 39, Short.MAX_VALUE)
                                                                                .addComponent(jLabel1)
                                                                                .addGap(35, 35, 35)));
                jPanel2Layout.setVerticalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addGap(28, 28, 28)
                                                                .addComponent(jLabel1)
                                                                .addGap(34, 34, 34)
                                                                .addComponent(btnproductmanagement,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                51,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(btnSuppliermanagement,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                51,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(btnReport,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                51,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(GRNbtn1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                51,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(btnStockmanagement,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                51,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                542, Short.MAX_VALUE)
                                                                .addComponent(btnLogout,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                51,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(21, 21, 21)));

                jPanel3.setBackground(new java.awt.Color(243, 247, 244));
                jPanel3.setForeground(new java.awt.Color(5, 63, 22));
                jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                Welcome.setFont(new java.awt.Font("Unispace", 1, 18)); // NOI18N
                Welcome.setText("Welcome , ");
                jPanel3.add(Welcome, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, 39));

                MonthlyProfit.setBackground(new java.awt.Color(204, 255, 204));

                lblMonthlyProfit1.setFont(new java.awt.Font("Unispace", 1, 18)); // NOI18N
                lblMonthlyProfit1.setText("Monthly Profit - ");

                lblMonthlyProfit.setFont(new java.awt.Font("Unispace", 1, 18)); // NOI18N
                lblMonthlyProfit.setText("Loarding...");

                javax.swing.GroupLayout MonthlyProfitLayout = new javax.swing.GroupLayout(MonthlyProfit);
                MonthlyProfit.setLayout(MonthlyProfitLayout);
                MonthlyProfitLayout.setHorizontalGroup(
                                MonthlyProfitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(MonthlyProfitLayout.createSequentialGroup()
                                                                .addGap(44, 44, 44)
                                                                .addComponent(lblMonthlyProfit1)
                                                                .addGap(50, 50, 50)
                                                                .addComponent(lblMonthlyProfit)
                                                                .addContainerGap(68, Short.MAX_VALUE)));
                MonthlyProfitLayout.setVerticalGroup(
                                MonthlyProfitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                MonthlyProfitLayout.createSequentialGroup()
                                                                                .addContainerGap(33, Short.MAX_VALUE)
                                                                                .addGroup(MonthlyProfitLayout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                .addComponent(lblMonthlyProfit1,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                39,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addComponent(lblMonthlyProfit,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                39,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGap(28, 28, 28)));

                jPanel3.add(MonthlyProfit, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 470, -1));

                TotalSales.setBackground(new java.awt.Color(204, 255, 204));

                lblTotalSales.setFont(new java.awt.Font("Unispace", 1, 18)); // NOI18N
                lblTotalSales.setText("Total Sales    -");

                lblshowTotalSales.setFont(new java.awt.Font("Unispace", 1, 18)); // NOI18N
                lblshowTotalSales.setText("Loarding...");

                javax.swing.GroupLayout TotalSalesLayout = new javax.swing.GroupLayout(TotalSales);
                TotalSales.setLayout(TotalSalesLayout);
                TotalSalesLayout.setHorizontalGroup(
                                TotalSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(TotalSalesLayout.createSequentialGroup()
                                                                .addGap(42, 42, 42)
                                                                .addComponent(lblTotalSales,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                187,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(51, 51, 51)
                                                                .addComponent(lblshowTotalSales)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                TotalSalesLayout.setVerticalGroup(
                                TotalSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(TotalSalesLayout.createSequentialGroup()
                                                                .addGap(31, 31, 31)
                                                                .addGroup(TotalSalesLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(lblTotalSales,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                39,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(lblshowTotalSales,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                39,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addContainerGap(30, Short.MAX_VALUE)));

                jPanel3.add(TotalSales, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 467, -1));

                TodaySales.setBackground(new java.awt.Color(204, 255, 204));

                lblTodaySales.setFont(new java.awt.Font("Unispace", 1, 18)); // NOI18N
                lblTodaySales.setText("Today Sales    - ");

                lblMonthlyProfit5.setFont(new java.awt.Font("Unispace", 1, 18)); // NOI18N
                lblMonthlyProfit5.setText("Loarding...");

                javax.swing.GroupLayout TodaySalesLayout = new javax.swing.GroupLayout(TodaySales);
                TodaySales.setLayout(TodaySalesLayout);
                TodaySalesLayout.setHorizontalGroup(
                                TodaySalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(TodaySalesLayout.createSequentialGroup()
                                                                .addGap(42, 42, 42)
                                                                .addComponent(lblTodaySales)
                                                                .addGap(49, 49, 49)
                                                                .addComponent(lblMonthlyProfit5)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                TodaySalesLayout.setVerticalGroup(
                                TodaySalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(TodaySalesLayout.createSequentialGroup()
                                                                .addGap(24, 24, 24)
                                                                .addGroup(TodaySalesLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(lblTodaySales,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                39,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(lblMonthlyProfit5,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                39,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addContainerGap(25, Short.MAX_VALUE)));

                jPanel3.add(TodaySales, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 467, -1));

                jpanelchart.setBorder(
                                javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));

                javax.swing.GroupLayout jpanelchartLayout = new javax.swing.GroupLayout(jpanelchart);
                jpanelchart.setLayout(jpanelchartLayout);
                jpanelchartLayout.setHorizontalGroup(
                                jpanelchartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 536, Short.MAX_VALUE));
                jpanelchartLayout.setVerticalGroup(
                                jpanelchartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 606, Short.MAX_VALUE));

                jPanel3.add(jpanelchart, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 50, 540, 610));

                lbl_username.setFont(new java.awt.Font("Unispace", 1, 18)); // NOI18N
                lbl_username.setText("Loarding..");
                jPanel3.add(lbl_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, -1, 39));

                jPanel4.setBackground(new java.awt.Color(255, 204, 204));

                jLabel3.setFont(new java.awt.Font("Unispace", 1, 14)); // NOI18N
                jLabel3.setText(" Low Stock Alert");

                txt_lowStockArea.setEditable(false);
                txt_lowStockArea.setColumns(20);
                txt_lowStockArea.setRows(5);
                jScrollPane_lowStock.setViewportView(txt_lowStockArea);

                javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
                jPanel4.setLayout(jPanel4Layout);
                jPanel4Layout.setHorizontalGroup(
                                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jLabel3)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(jScrollPane_lowStock,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                488, Short.MAX_VALUE)
                                                                .addContainerGap()));
                jPanel4Layout.setVerticalGroup(
                                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jLabel3)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jScrollPane_lowStock,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                572, Short.MAX_VALUE)
                                                                .addContainerGap()));

                jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 40, 500, 620));

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jPanel2,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, 0)
                                                                .addComponent(jPanel3,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                jPanel1Layout.setVerticalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

                getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

                setSize(new java.awt.Dimension(1935, 1017));
                setLocationRelativeTo(null);
        }// </editor-fold>//GEN-END:initComponents

        private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {
                new LoginFrame().setVisible(true);
                this.dispose();
        }

        private void btnStockmanagementActionPerformed(java.awt.event.ActionEvent evt) {
                new StockManagement().setVisible(true);
                this.dispose();
        }

        private void btnproductmanagementActionPerformed(java.awt.event.ActionEvent evt) {
                new ProductManagement().setVisible(true);
                this.dispose();
        }

        private void btnReportActionPerformed(java.awt.event.ActionEvent evt) {
                new SalesHistoryFrame().setVisible(true);
        }

        private void btnSuppliermanagementActionPerformed(java.awt.event.ActionEvent evt) {
                new SupplierManagement().setVisible(true);
                this.dispose();
        }

        private void GRNbtn1ActionPerformed(java.awt.event.ActionEvent evt) {
                new GRN().setVisible(true);
                this.dispose();
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
                        java.util.logging.Logger.getLogger(ManagerDashboard.class.getName()).log(
                                        java.util.logging.Level.SEVERE,
                                        null, ex);
                }

                java.awt.EventQueue.invokeLater(() -> new ManagerDashboard().setVisible(true));
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton GRNbtn1;
        private javax.swing.JPanel MonthlyProfit;
        private javax.swing.JPanel TodaySales;
        private javax.swing.JPanel TotalSales;
        private javax.swing.JLabel Welcome;
        private javax.swing.JButton btnLogout;
        private javax.swing.JButton btnReport;
        private javax.swing.JButton btnStockmanagement;
        private javax.swing.JButton btnSuppliermanagement;
        private javax.swing.JButton btnproductmanagement;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JPanel jPanel4;
        private javax.swing.JScrollPane jScrollPane_lowStock;
        private javax.swing.JPanel jpanelchart;
        private javax.swing.JLabel lblMonthlyProfit;
        private javax.swing.JLabel lblMonthlyProfit1;
        private javax.swing.JLabel lblMonthlyProfit5;
        private javax.swing.JLabel lblTodaySales;
        private javax.swing.JLabel lblTotalSales;
        private javax.swing.JLabel lbl_username;
        private javax.swing.JLabel lblshowTotalSales;
        private java.awt.Menu menu1;
        private java.awt.Menu menu2;
        private java.awt.MenuBar menuBar1;
        private javax.swing.JTextArea txt_lowStockArea;
        // End of variables declaration//GEN-END:variables
}
