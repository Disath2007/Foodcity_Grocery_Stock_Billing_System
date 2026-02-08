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
        // SQL query to fetch stock details joined with product and category tables
        String sql = "SELECT s.stock_id, s.product_id, p.product_name, c.category_name, s.quantity, p.price, s.last_updated "
                + "FROM stock s " +
                "INNER JOIN product p ON s.product_id = p.product_id " +
                "INNER JOIN category c ON p.category_id = c.category_id " +
                "ORDER BY p.product_id";

        // Try-with-resources: automatically handles connection and statement closing
        try (Connection conn = DatabaseConnection.getConnection(); // Get DB connection
                PreparedStatement pstmt = conn.prepareStatement(sql); // Prepare selection statement
                ResultSet rs = pstmt.executeQuery()) { // Execute and retrieve results

            // Iterate through each stock record returned
            while (rs.next()) {
                // Map database values to a new Stock model object
                Stock stock = new Stock(
                        rs.getInt("stock_id"),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("category_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("last_updated"));
                // Add the object to our list
                stockList.add(stock);
            }
        } catch (SQLException e) {
            // Print stack trace for debugging database issues
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
        // Search query using LIKE with JOINs to filter stock by product name
        String sql = "SELECT s.stock_id, s.product_id, p.product_name, c.category_name, s.quantity, p.price, s.last_updated "
                + "FROM stock s " +
                "INNER JOIN product p ON s.product_id = p.product_id " +
                "INNER JOIN category c ON p.category_id = c.category_id " +
                "WHERE p.product_name LIKE ? " +
                "ORDER BY p.product_name";

        try (Connection conn = DatabaseConnection.getConnection(); // Connect to DB
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare statement

            // Bind the search term with wildcards for matching
            pstmt.setString(1, "%" + searchTerm + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                // Populate the list with search results
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockList;
    }

    /**
     * Updates the quantity of a stock item using the Stock model
     * 
     * @param stock The Stock object containing updated data
     * @return String message indicating success or error
     */
    public String updateStockQuantity(Stock stock) {
        // 1. Validation Check: Ensure the quantity is a real value
        if (stock.getQuantity() < 0) {
            return "Quantity cannot be negative";
        }

        // 2. Persistence: Update the quantity field in the stock table
        String sql = "UPDATE stock SET quantity = ? WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); // Get connection
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare update statement

            // Bind parameters from the Stock model
            pstmt.setInt(1, stock.getQuantity());
            pstmt.setInt(2, stock.getProductId());

            // Execute the update
            int rowsAffected = pstmt.executeUpdate();

            // Return success code or error message
            if (rowsAffected > 0) {
                return "SUCCESS";
            } else {
                return "No stock record found for this product";
            }
        } catch (SQLException e) {
            // Final catch for database failures
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
        // Simple selection query to fetch a single numeric value
        String sql = "SELECT quantity FROM stock WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); // Connection initialization
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // Statement creation

            // Bind the target product ID
            pstmt.setInt(1, productId);

            try (ResultSet rs = pstmt.executeQuery()) {
                // If the record exists, return the quantity
                if (rs.next()) {
                    return rs.getInt("quantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return -1 to signal error or missing record
        return -1;
    }

    /**
     * Retrieves items with quantity below a specific threshold
     *
     * @param threshold The quantity limit for alert
     * @return List of low stock items
     */
    public List<Stock> getLowStockItems(int threshold) {
        List<Stock> lowStockList = new ArrayList<>();
        // Query to find stock records that are running low
        String sql = "SELECT s.stock_id, s.product_id, p.product_name, " +
                "c.category_name, s.quantity, p.price, s.last_updated " +
                "FROM stock s " +
                "INNER JOIN product p ON s.product_id = p.product_id " +
                "INNER JOIN category c ON p.category_id = c.category_id " +
                "WHERE s.quantity < ? " +
                "ORDER BY s.quantity ASC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Bind the threshold value to the query
            pstmt.setInt(1, threshold);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Process the results into models
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lowStockList;
    }

}
