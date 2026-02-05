package controller;

import database.DatabaseConnection;
import model.Stock;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockController {

    /**
     * Retrieves all stock items from the database with product and category names
     * 
     * @return List of all stock items
     */
    public List<Stock> getAllStock() {
        List<Stock> stockList = new ArrayList<>();
        String sql = "SELECT s.stock_id, s.product_id, p.product_name, c.category_name, s.quantity, p.price, s.last_updated "
                +
                "FROM stock s " +
                "INNER JOIN product p ON s.product_id = p.product_id " +
                "INNER JOIN category c ON p.category_id = c.category_id " +
                "ORDER BY p.product_id";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Stock stock = new Stock(
                        rs.getInt("stock_id"),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("category_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("last_updated"));
                stockList.add(stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockList;
    }

    /**
     * Searches for stock items by product name
     * 
     * @param searchTerm The search term to match against product names
     * @return List of matching stock items
     */
    public List<Stock> searchStock(String searchTerm) {
        List<Stock> stockList = new ArrayList<>();
        String sql = "SELECT s.stock_id, s.product_id, p.product_name, c.category_name, s.quantity, p.price, s.last_updated "
                +
                "FROM stock s " +
                "INNER JOIN product p ON s.product_id = p.product_id " +
                "INNER JOIN category c ON p.category_id = c.category_id " +
                "WHERE p.product_name LIKE ? " +
                "ORDER BY p.product_name";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Stock stock = new Stock(
                        rs.getInt("stock_id"),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("category_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("last_updated"));
                stockList.add(stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockList;
    }

    /**
     * Updates the quantity of a stock item
     * 
     * @param productId ID of the product
     * @param quantity  New quantity value
     * @return String message indicating success or error
     */
    public String updateStockQuantity(int productId, String quantityStr) {
        // Validate inputs
        if (quantityStr == null || quantityStr.trim().isEmpty()) {
            return "Please enter a quantity";
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr.trim());
            if (quantity < 0) {
                return "Quantity cannot be negative";
            }
        } catch (NumberFormatException e) {
            return "Invalid quantity format";
        }

        String sql = "UPDATE stock SET quantity = ? WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                return "SUCCESS";
            } else {
                return "No stock record found for this product";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error: " + e.getMessage();
        }
    }

    /**
     * Gets the current stock quantity for a specific product
     * 
     * @param productId Product ID
     * @return Stock quantity, or -1 if not found
     */
    public int getStockQuantity(int productId) {
        String sql = "SELECT quantity FROM stock WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Stock> getLowStockItems(int threshold) {
        List<Stock> lowStockList = new ArrayList<>();
        String sql = "SELECT s.stock_id, s.product_id, p.product_name, " +
                "c.category_name, s.quantity, p.price, s.last_updated " +
                "FROM stock s " +
                "INNER JOIN product p ON s.product_id = p.product_id " +
                "INNER JOIN category c ON p.category_id = c.category_id " +
                "WHERE s.quantity < ? " +
                "ORDER BY s.quantity ASC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, threshold); // Set the threshold value
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Stock stock = new Stock(
                        rs.getInt("stock_id"),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("category_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("last_updated"));
                lowStockList.add(stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lowStockList;
    }

}
