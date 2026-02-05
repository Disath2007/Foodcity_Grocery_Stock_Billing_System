package controller;

import database.DatabaseConnection;
import model.Sale;
import model.SaleItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesController {

    public String saveSale(Sale sale, List<SaleItem> items) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Use transaction

            // 1. Insert Sale Summary
            String sqlSale = "INSERT INTO sales (cashier_name, subtotal, discount, grand_total, cash_received, balance) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmtSale = conn.prepareStatement(sqlSale, Statement.RETURN_GENERATED_KEYS);
            pstmtSale.setString(1, sale.getCashierName());
            pstmtSale.setDouble(2, sale.getSubtotal());
            pstmtSale.setDouble(3, sale.getDiscount());
            pstmtSale.setDouble(4, sale.getGrandTotal());
            pstmtSale.setDouble(5, sale.getCashReceived());
            pstmtSale.setDouble(6, sale.getBalance());
            pstmtSale.executeUpdate();

            ResultSet rs = pstmtSale.getGeneratedKeys();
            int saleId = -1;
            if (rs.next()) {
                saleId = rs.getInt(1);
            }

            if (saleId == -1) {
                conn.rollback();
                return "ERROR: Failed to retrieve sale ID";
            }

            // 2. Insert Sale Items and Update Stock
            String sqlItem = "INSERT INTO sales_items (sale_id, product_id, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?)";
            String sqlUpdateStock = "UPDATE stock SET quantity = quantity - ? WHERE product_id = ?";

            PreparedStatement pstmtItem = conn.prepareStatement(sqlItem);
            PreparedStatement pstmtUpdateStock = conn.prepareStatement(sqlUpdateStock);

            for (SaleItem item : items) {
                // Insert item
                pstmtItem.setInt(1, saleId);
                pstmtItem.setInt(2, item.getProductId());
                pstmtItem.setInt(3, item.getQuantity());
                pstmtItem.setDouble(4, item.getUnitPrice());
                pstmtItem.setDouble(5, item.getTotalPrice());
                pstmtItem.executeUpdate();

                // Update stock
                pstmtUpdateStock.setInt(1, item.getQuantity());
                pstmtUpdateStock.setInt(2, item.getProductId());
                pstmtUpdateStock.executeUpdate();
            }

            conn.commit();
            return "SUCCESS";

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Sale> getAllSales() {
        List<Sale> salesList = new ArrayList<>();
        String sql = "SELECT * FROM sales ORDER BY sale_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Sale sale = new Sale(
                        rs.getInt("sale_id"),
                        rs.getString("cashier_name"),
                        rs.getDouble("subtotal"),
                        rs.getDouble("discount"),
                        rs.getDouble("grand_total"),
                        rs.getDouble("cash_received"),
                        rs.getDouble("balance"),
                        rs.getTimestamp("sale_date"));
                salesList.add(sale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salesList;
    }

    public List<SaleItem> getItemsBySaleId(int saleId) {
        List<SaleItem> itemList = new ArrayList<>();
        String sql = "SELECT * FROM sales_items WHERE sale_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, saleId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SaleItem item = new SaleItem(
                        rs.getInt("item_id"),
                        rs.getInt("sale_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price"),
                        rs.getDouble("total_price"));
                itemList.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemList;
    }
}
