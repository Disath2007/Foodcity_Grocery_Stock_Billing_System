package controller;

import database.DatabaseConnection;
import model.GRN;
import model.Supplier;
import model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * CONTROLLER LAYER - GRN Controller
 * 
 * This class handles all business logic and database operations for GRN.
 * Provides CRUD operations and date filtering functionality.
 */
public class GRNController {

    /**
     * Get all GRN records
     */
    public List<GRN> getAllGRN() {
        List<GRN> grnList = new ArrayList<>();
        // Complex SQL query to join GRN with Product and Supplier tables for meaningful
        // data
        String sql = "SELECT g.grn_id, g.product_id, p.product_name, p.buying_price, " +
                "g.supplier_id, s.supplier_name, g.ordered_quantity, " +
                "g.delivered_quantity, g.date_created " +
                "FROM grn g " +
                "INNER JOIN product p ON g.product_id = p.product_id " +
                "INNER JOIN supplier s ON g.supplier_id = s.supplier_id " +
                "ORDER BY g.grn_id";

        // Try-with-resources: automatically handles connection and result set cleanup
        try (Connection conn = DatabaseConnection.getConnection(); // Connect to database
                PreparedStatement pstmt = conn.prepareStatement(sql); // Prepare selection statement
                ResultSet rs = pstmt.executeQuery()) { // Execute and fetch results

            // Map each database row to a new GRN model object
            while (rs.next()) {
                GRN grn = new GRN(
                        rs.getInt("grn_id"),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("buying_price"),
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getInt("ordered_quantity"),
                        rs.getInt("delivered_quantity"),
                        rs.getString("date_created"));
                // Add the populated object to our return list
                grnList.add(grn);
            }
        } catch (SQLException e) {
            // Log database-level errors
            System.err.println("Error getting all GRN: " + e.getMessage());
        }
        return grnList;
    }

    /**
     * Filter GRN records by date
     * 
     * @param dateStr Date in format "dd-MMMM-yyyy" or "yyyy-MM-dd"
     */
    public List<GRN> getGRNByDate(String dateStr) {
        List<GRN> grnList = new ArrayList<>();
        // Filtering query using the DATE() function to match specific creation dates
        String sql = "SELECT g.grn_id, g.product_id, p.product_name, p.buying_price, " +
                "g.supplier_id, s.supplier_name, g.ordered_quantity, " +
                "g.delivered_quantity, g.date_created " +
                "FROM grn g " +
                "INNER JOIN product p ON g.product_id = p.product_id " +
                "INNER JOIN supplier s ON g.supplier_id = s.supplier_id " +
                "WHERE DATE(g.date_created) = ? " +
                "ORDER BY g.date_created DESC";

        try (Connection conn = DatabaseConnection.getConnection(); // Database link
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare SQL

            // Set the target date as the query parameter
            pstmt.setString(1, dateStr);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Collect filtered records into the list
                while (rs.next()) {
                    GRN grn = new GRN(
                            rs.getInt("grn_id"),
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getDouble("buying_price"),
                            rs.getInt("supplier_id"),
                            rs.getString("supplier_name"),
                            rs.getInt("ordered_quantity"),
                            rs.getInt("delivered_quantity"),
                            rs.getString("date_created"));
                    grnList.add(grn);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error filtering GRN by date: " + e.getMessage());
        }
        return grnList;
    }

    /**
     * Add a new GRN record and update stock using the GRN model
     * 
     * @param grn The GRN object containing data
     * @return String message indicating success or error
     */
    public String addGRN(GRN grn) {
        // 1. Validation Logic: Ensure all foreign keys and quantities are valid
        if (grn.getProductId() <= 0) {
            return "Error: Please select a product!";
        }
        if (grn.getSupplierId() <= 0) {
            return "Error: Please select a supplier!";
        }
        if (grn.getOrderedQuantity() <= 0 || grn.getDeliveredQuantity() < 0) {
            return "Error: Invalid quantity values!";
        }

        // 2. Dual Operations: We need to insert a GRN AND update corresponding stock
        String insertSql = "INSERT INTO grn (product_id, supplier_id, ordered_quantity, delivered_quantity) VALUES (?, ?, ?, ?)";
        // Use an "UPSERT" strategy for stock (INSERT if not exists, otherwise UPDATE
        // existing)
        String updateStockSql = "INSERT INTO stock (product_id, quantity) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE quantity = quantity + ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Enable Transaction Management
            conn.setAutoCommit(false);

            // Operation A: Insert the GRN Log
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setInt(1, grn.getProductId());
                pstmt.setInt(2, grn.getSupplierId());
                pstmt.setInt(3, grn.getOrderedQuantity());
                pstmt.setInt(4, grn.getDeliveredQuantity());
                pstmt.executeUpdate();
            }

            // Operation B: Update the physical stock levels
            try (PreparedStatement pstmt = conn.prepareStatement(updateStockSql)) {
                pstmt.setInt(1, grn.getProductId());
                pstmt.setInt(2, grn.getDeliveredQuantity()); // Initial quantity for new products
                pstmt.setInt(3, grn.getDeliveredQuantity()); // Increment value for existing products
                pstmt.executeUpdate();
            }

            // Commit all changes if both operations were successful
            conn.commit();
            return "Success: GRN added and stock updated!";

        } catch (SQLException e) {
            // Rollback is usually automatic on error, but logging is crucial
            System.err.println("Error adding GRN: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Update an existing GRN record using the GRN model
     * 
     * @param grn The GRN object containing updated data
     * @return String message indicating success or error
     */
    public String updateGRN(GRN grn) {
        // Validation check
        if (grn.getGrnId() <= 0) {
            return "Error: Please select a GRN to update!";
        }
        if (grn.getOrderedQuantity() <= 0 || grn.getDeliveredQuantity() < 0) {
            return "Error: Invalid quantity values!";
        }

        // 1. Pre-update Phase: Retrieve current state to calculate stock adjustments
        int oldDeliveredQty = 0;
        int oldProductId = 0;
        String selectSql = "SELECT delivered_quantity, product_id FROM grn WHERE grn_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
            pstmt.setInt(1, grn.getGrnId());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    oldDeliveredQty = rs.getInt("delivered_quantity");
                    oldProductId = rs.getInt("product_id");
                }
            }
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }

        // 2. Transaction Phase: Update GRN and adjust stock levels
        String updateSql = "UPDATE grn SET product_id = ?, supplier_id = ?, ordered_quantity = ?, delivered_quantity = ? WHERE grn_id = ?";
        String updateStockSql = "UPDATE stock SET quantity = quantity + ? WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Begin transaction

            // Step A: Update the central GRN record
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setInt(1, grn.getProductId());
                pstmt.setInt(2, grn.getSupplierId());
                pstmt.setInt(3, grn.getOrderedQuantity());
                pstmt.setInt(4, grn.getDeliveredQuantity());
                pstmt.setInt(5, grn.getGrnId());
                pstmt.executeUpdate();
            }

            // Step B: Manage Stock balance
            if (oldProductId == grn.getProductId()) {
                // If same product, simply apply the difference (New - Old)
                int stockDiff = grn.getDeliveredQuantity() - oldDeliveredQty;
                try (PreparedStatement pstmt = conn.prepareStatement(updateStockSql)) {
                    pstmt.setInt(1, stockDiff);
                    pstmt.setInt(2, grn.getProductId());
                    pstmt.executeUpdate();
                }
            } else {
                // If product changed: Revert old product stock, Add new product stock
                try (PreparedStatement pstmt = conn.prepareStatement(updateStockSql)) {
                    pstmt.setInt(1, -oldDeliveredQty); // Subtract original amount from old product
                    pstmt.setInt(2, oldProductId);
                    pstmt.executeUpdate();
                }
                try (PreparedStatement pstmt = conn.prepareStatement(updateStockSql)) {
                    pstmt.setInt(1, grn.getDeliveredQuantity()); // Add new amount to new product
                    pstmt.setInt(2, grn.getProductId());
                    pstmt.executeUpdate();
                }
            }

            conn.commit(); // Finalize update
            return "Success: GRN updated!";

        } catch (SQLException e) {
            System.err.println("Error updating GRN: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Delete a GRN record
     */
    public String deleteGRN(int grnId) {
        // Check if ID is present
        if (grnId <= 0) {
            return "Error: Please select a GRN to delete!";
        }

        // 1. Snapshot Phase: Get quantity to revert from stock before record is lost
        int deliveredQty = 0;
        int productId = 0;
        String selectSql = "SELECT delivered_quantity, product_id FROM grn WHERE grn_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
            pstmt.setInt(1, grnId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    deliveredQty = rs.getInt("delivered_quantity");
                    productId = rs.getInt("product_id");
                }
            }
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }

        // 2. Deletion Phase: Remove logging record and revert stock increase
        String deleteSql = "DELETE FROM grn WHERE grn_id = ?";
        String updateStockSql = "UPDATE stock SET quantity = quantity - ? WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // Remove the GRN entry
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                pstmt.setInt(1, grnId);
                pstmt.executeUpdate();
            }

            // Undo the stock addition that occurred when this GRN was created
            try (PreparedStatement pstmt = conn.prepareStatement(updateStockSql)) {
                pstmt.setInt(1, deliveredQty);
                pstmt.setInt(2, productId);
                pstmt.executeUpdate();
            }

            conn.commit(); // Done
            return "Success: GRN deleted!";

        } catch (SQLException e) {
            System.err.println("Error deleting GRN: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Search products by name
     */
    public List<Product> searchProducts(String searchTerm) {
        List<Product> products = new ArrayList<>();
        // Select query with LIKE for partial product name matching
        String sql = "SELECT product_id, product_name, price, buying_price FROM product WHERE product_name LIKE ? ORDER BY product_name";

        try (Connection conn = DatabaseConnection.getConnection(); // Connect to DB
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare statement

            // Bind the search term with wildcards
            pstmt.setString(1, "%" + searchTerm + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Populate a blank Product model manually from results
                    Product p = new Product();
                    p.setProductId(rs.getInt("product_id"));
                    p.setProductName(rs.getString("product_name"));
                    p.setPrice(rs.getDouble("price"));
                    p.setBuyingPrice(rs.getDouble("buying_price"));
                    // Collect matching products
                    products.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching products: " + e.getMessage());
        }
        return products;
    }

    /**
     * Get all suppliers for dropdown
     */
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        // Simple selection query to fill supplier dropdown menus
        String sql = "SELECT supplier_id, supplier_name, company_name FROM supplier ORDER BY supplier_name";

        try (Connection conn = DatabaseConnection.getConnection(); // Database connection
                PreparedStatement pstmt = conn.prepareStatement(sql); // Prepare SQL
                ResultSet rs = pstmt.executeQuery()) { // Execute selection

            while (rs.next()) {
                // Initialize a Supplier model for each record
                Supplier s = new Supplier();
                s.setSupplierId(rs.getInt("supplier_id"));
                s.setSupplierName(rs.getString("supplier_name"));
                s.setCompanyName(rs.getString("company_name"));
                suppliers.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Error getting suppliers: " + e.getMessage());
        }
        return suppliers;
    }

    /**
     * Get suppliers for a specific product
     */
    public List<Supplier> getSuppliersForProduct(int productId) {
        List<Supplier> suppliers = new ArrayList<>();
        // Query fetching suppliers authorized for a specific product via the join table
        String sql = "SELECT s.supplier_id, s.supplier_name, s.company_name " +
                "FROM supplier s " +
                "INNER JOIN supplier_product sp ON s.supplier_id = sp.supplier_id " +
                "WHERE sp.product_id = ? " +
                "ORDER BY s.supplier_name";

        try (Connection conn = DatabaseConnection.getConnection(); // Database link
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare SQL

            // Set the target product ID
            pstmt.setInt(1, productId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Collect authorized suppliers for return
                    Supplier s = new Supplier();
                    s.setSupplierId(rs.getInt("supplier_id"));
                    s.setSupplierName(rs.getString("supplier_name"));
                    s.setCompanyName(rs.getString("company_name"));
                    suppliers.add(s);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting suppliers for product: " + e.getMessage());
        }
        return suppliers;
    }
}
