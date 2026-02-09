/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI.Admin;

// Add these imports at the TOP
import GUI.LoginFrame;
import GUI.SalesHistoryFrame;

import controller.SalesController;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.*;
import javax.swing.*;
import model.Stock;
import controller.StockController;

/**
 *
 * @author Disath Damsutha
 */
public class AdminDashboard extends javax.swing.JFrame {

        private static final java.util.logging.Logger logger = java.util.logging.Logger
                        .getLogger(AdminDashboard.class.getName());
        private SalesController salesController;
        private StockController stockController;
        private ChartPanel chartPanel;
        private javax.swing.Timer refreshTimer;
        private static final int REFRESH_INTERVAL = 10000;

        /**
         * Creates new form AdminDashboard
         */
        public AdminDashboard() {
                salesController = new SalesController();
                stockController = new StockController();
                initComponents();
                setLocationRelativeTo(null);
                createChart();
                loadDashboardStats();
                loadLowStockAlerts();
                startAutoRefresh();
                this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

        }

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
         * Fetches items with low stock and displays alerts in the text area.
         */
        private void loadLowStockAlerts() {
                int threshold = 50;

                try {
                        java.util.List<Stock> lowStockList = stockController.getLowStockItems(threshold);
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

        // ======== ADD THIS METHOD RIGHT HERE ========
        // Dummy Chart
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

                        // Create chart
                        JFreeChart chart = ChartFactory.createBarChart(
                                        "Monthly Sales Overview", // Chart title
                                        "Month", // X-axis label
                                        "Sales ($)", // Y-axis label
                                        dataset, // Data
                                        PlotOrientation.VERTICAL, // Orientation
                                        true, // Include legend
                                        true, // Tooltips
                                        false // URLs
                        );

                        // Customize chart appearance
                        chart.setBackgroundPaint(Color.WHITE);
                        CategoryPlot plot = (CategoryPlot) chart.getPlot();
                        plot.setBackgroundPaint(new Color(243, 247, 244)); // Match your background
                        plot.setRangeGridlinePaint(Color.GRAY);

                        // Create chart panel
                        chartPanel = new ChartPanel(chart);
                        chartPanel.setPreferredSize(new Dimension(380, 370));
                        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                        // IMPORTANT: Change to jpanelchart (your panel name)
                        jpanelchart.removeAll(); // Clear existing content
                        jpanelchart.setLayout(new BorderLayout());
                        jpanelchart.add(chartPanel, BorderLayout.CENTER);
                        jpanelchart.revalidate();
                        jpanelchart.repaint();

                } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error creating chart: " + e.getMessage());
                }
        }
        // ======== END OF ADDED METHOD ========

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")

        // <editor-fold defaultstate="collapsed" desc="Generated Code">

        // <editor-fold defaultstate="collapsed" desc="Generated
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
                btnUserManagement = new javax.swing.JButton();
                btnLogout = new javax.swing.JButton();
                btnReport = new javax.swing.JButton();
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
                jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                jPanel2.setBackground(new java.awt.Color(73, 128, 37));

                jLabel1.setFont(new java.awt.Font("Unispace", 1, 24)); // NOI18N
                jLabel1.setForeground(new java.awt.Color(243, 247, 244));
                jLabel1.setText("ADMIN PANEL");

                btnUserManagement.setBackground(new java.awt.Color(147, 202, 55));
                btnUserManagement.setFont(new java.awt.Font("Unispace", 1, 12)); // NOI18N
                btnUserManagement.setText(" User Management ");
                btnUserManagement.setAlignmentX(0.5F);
                btnUserManagement.setBorder(null);
                btnUserManagement.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnUserManagement.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                btnUserManagement.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnUserManagementActionPerformed(evt);
                        }
                });

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

                btnReport.setBackground(new java.awt.Color(147, 202, 55));
                btnReport.setFont(new java.awt.Font("Unispace", 1, 12)); // NOI18N
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

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addGroup(jPanel2Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel2Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                .addComponent(btnLogout,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                227,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGroup(jPanel2Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                .addGroup(jPanel2Layout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addContainerGap()
                                                                                                                                .addComponent(btnReport,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                227,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                jPanel2Layout.createSequentialGroup()
                                                                                                                                                .addGap(23, 23, 23)
                                                                                                                                                .addComponent(btnUserManagement,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                227,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                                                .addGroup(jPanel2Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(50, 50, 50)
                                                                                                .addComponent(jLabel1)))
                                                                .addContainerGap(18, Short.MAX_VALUE)));
                jPanel2Layout.setVerticalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addGap(36, 36, 36)
                                                                .addComponent(jLabel1)
                                                                .addGap(41, 41, 41)
                                                                .addComponent(btnUserManagement,
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
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(btnLogout,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                51,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(21, 21, 21)));

                jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 1010));

                jPanel3.setBackground(new java.awt.Color(243, 247, 244));
                jPanel3.setForeground(new java.awt.Color(5, 63, 22));
                jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                Welcome.setFont(new java.awt.Font("Unispace", 1, 18)); // NOI18N
                Welcome.setText("Welcome , ");
                jPanel3.add(Welcome, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 24, -1, 39));

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
                                                                .addContainerGap(58, Short.MAX_VALUE)));
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

                jPanel3.add(MonthlyProfit, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 69, 460, -1));

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
                                                                .addContainerGap(59, Short.MAX_VALUE)));
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

                jPanel3.add(TotalSales, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 187, 460, -1));

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
                                                                .addContainerGap(61, Short.MAX_VALUE)));
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

                jPanel3.add(TodaySales, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 305, 460, -1));

                jpanelchart.setBorder(new javax.swing.border.MatteBorder(null));

                javax.swing.GroupLayout jpanelchartLayout = new javax.swing.GroupLayout(jpanelchart);
                jpanelchart.setLayout(jpanelchartLayout);
                jpanelchartLayout.setHorizontalGroup(
                                jpanelchartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 518, Short.MAX_VALUE));
                jpanelchartLayout.setVerticalGroup(
                                jpanelchartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 608, Short.MAX_VALUE));

                jPanel3.add(jpanelchart, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 50, 520, 610));

                lbl_username.setFont(new java.awt.Font("Unispace", 1, 18)); // NOI18N
                lbl_username.setText("Loarding..");
                jPanel3.add(lbl_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(143, 24, -1, 39));

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

                jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 40, 500, 620));

                jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(268, 0, 1651, 1008));

                getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

                setSize(new java.awt.Dimension(1935, 1017));
                setLocationRelativeTo(null);
        }// </editor-fold>//GEN-END:initComponents

        private void btnUserManagementActionPerformed(java.awt.event.ActionEvent evt) {

                new UserManagement().setVisible(true);
                this.dispose();

        }

        private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {

                new LoginFrame().setVisible(true);
                this.dispose();

        }

        private void btnReportActionPerformed(java.awt.event.ActionEvent evt) {
                new SalesHistoryFrame().setVisible(true);
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
                java.awt.EventQueue.invokeLater(() -> new AdminDashboard().setVisible(true));
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JPanel MonthlyProfit;
        private javax.swing.JPanel TodaySales;
        private javax.swing.JPanel TotalSales;
        private javax.swing.JLabel Welcome;
        private javax.swing.JButton btnLogout;
        private javax.swing.JButton btnReport;
        private javax.swing.JButton btnUserManagement;
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
