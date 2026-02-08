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
        String sql = "SELECT g.grn_id, g.product_id, p.product_name, p.buying_price, " +
                "g.supplier_id, s.supplier_name, g.ordered_quantity, " +
                "g.delivered_quantity, g.date_created " +
                "FROM grn g " +
                "INNER JOIN product p ON g.product_id = p.product_id " +
                "INNER JOIN supplier s ON g.supplier_id = s.supplier_id " +
                "ORDER BY g.grn_id";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

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
        } catch (SQLException e) {
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
        String sql = "SELECT g.grn_id, g.product_id, p.product_name, p.buying_price, " +
                "g.supplier_id, s.supplier_name, g.ordered_quantity, " +
                "g.delivered_quantity, g.date_created " +
                "FROM grn g " +
                "INNER JOIN product p ON g.product_id = p.product_id " +
                "INNER JOIN supplier s ON g.supplier_id = s.supplier_id " +
                "WHERE DATE(g.date_created) = ? " +
                "ORDER BY g.date_created DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dateStr);
            ResultSet rs = pstmt.executeQuery();

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
        // Validate inputs
        if (grn.getProductId() <= 0) {
            return "Error: Please select a product!";
        }
        if (grn.getSupplierId() <= 0) {
            return "Error: Please select a supplier!";
        }
        if (grn.getOrderedQuantity() <= 0 || grn.getDeliveredQuantity() < 0) {
            return "Error: Invalid quantity values!";
        }

        String insertSql = "INSERT INTO grn (product_id, supplier_id, ordered_quantity, delivered_quantity) VALUES (?, ?, ?, ?)";
        // Use UPSERT to handle both new stock entries and updates to existing stock
        String updateStockSql = "INSERT INTO stock (product_id, quantity) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE quantity = quantity + ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Insert GRN record
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setInt(1, grn.getProductId());
                pstmt.setInt(2, grn.getSupplierId());
                pstmt.setInt(3, grn.getOrderedQuantity());
                pstmt.setInt(4, grn.getDeliveredQuantity());
                pstmt.executeUpdate();
            }

            // Update or Insert stock with delivered quantity
            try (PreparedStatement pstmt = conn.prepareStatement(updateStockSql)) {
                pstmt.setInt(1, grn.getProductId());
                pstmt.setInt(2, grn.getDeliveredQuantity()); // Value for INSERT (initial quantity)
                pstmt.setInt(3, grn.getDeliveredQuantity()); // Value for UPDATE (increment)
                pstmt.executeUpdate();
            }

            conn.commit();
            return "Success: GRN added and stock updated!";

        } catch (SQLException e) {
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
        if (grn.getGrnId() <= 0) {
            return "Error: Please select a GRN to update!";
        }
        if (grn.getOrderedQuantity() <= 0 || grn.getDeliveredQuantity() < 0) {
            return "Error: Invalid quantity values!";
        }

        // Get old delivered quantity to adjust stock
        int oldDeliveredQty = 0;
        int oldProductId = 0;
        String selectSql = "SELECT delivered_quantity, product_id FROM grn WHERE grn_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
            pstmt.setInt(1, grn.getGrnId());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                oldDeliveredQty = rs.getInt("delivered_quantity");
                oldProductId = rs.getInt("product_id");
            }
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }

        String updateSql = "UPDATE grn SET product_id = ?, supplier_id = ?, ordered_quantity = ?, delivered_quantity = ? WHERE grn_id = ?";
        String updateStockSql = "UPDATE stock SET quantity = quantity + ? WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Update GRN record
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setInt(1, grn.getProductId());
                pstmt.setInt(2, grn.getSupplierId());
                pstmt.setInt(3, grn.getOrderedQuantity());
                pstmt.setInt(4, grn.getDeliveredQuantity());
                pstmt.setInt(5, grn.getGrnId());
                pstmt.executeUpdate();
            }

            // Adjust stock: subtract old, add new
            if (oldProductId == grn.getProductId()) {
                int stockDiff = grn.getDeliveredQuantity() - oldDeliveredQty;
                try (PreparedStatement pstmt = conn.prepareStatement(updateStockSql)) {
                    pstmt.setInt(1, stockDiff);
                    pstmt.setInt(2, grn.getProductId());
                    pstmt.executeUpdate();
                }
            } else {
                // Different product - subtract from old, add to new
                try (PreparedStatement pstmt = conn.prepareStatement(updateStockSql)) {
                    pstmt.setInt(1, -oldDeliveredQty);
                    pstmt.setInt(2, oldProductId);
                    pstmt.executeUpdate();
                }
                try (PreparedStatement pstmt = conn.prepareStatement(updateStockSql)) {
                    pstmt.setInt(1, grn.getDeliveredQuantity());
                    pstmt.setInt(2, grn.getProductId());
                    pstmt.executeUpdate();
                }
            }

            conn.commit();
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
        if (grnId <= 0) {
            return "Error: Please select a GRN to delete!";
        }

        // Get delivered quantity to subtract from stock
        int deliveredQty = 0;
        int productId = 0;
        String selectSql = "SELECT delivered_quantity, product_id FROM grn WHERE grn_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
            pstmt.setInt(1, grnId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                deliveredQty = rs.getInt("delivered_quantity");
                productId = rs.getInt("product_id");
            }
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }

        String deleteSql = "DELETE FROM grn WHERE grn_id = ?";
        String updateStockSql = "UPDATE stock SET quantity = quantity - ? WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Delete GRN
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                pstmt.setInt(1, grnId);
                pstmt.executeUpdate();
            }

            // Subtract from stock
            try (PreparedStatement pstmt = conn.prepareStatement(updateStockSql)) {
                pstmt.setInt(1, deliveredQty);
                pstmt.setInt(2, productId);
                pstmt.executeUpdate();
            }

            conn.commit();
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
        String sql = "SELECT product_id, product_name, price, buying_price FROM product WHERE product_name LIKE ? ORDER BY product_name";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setProductName(rs.getString("product_name"));
                p.setPrice(rs.getDouble("price"));
                p.setBuyingPrice(rs.getDouble("buying_price"));
                products.add(p);
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
        String sql = "SELECT supplier_id, supplier_name, company_name FROM supplier ORDER BY supplier_name";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
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
        String sql = "SELECT s.supplier_id, s.supplier_name, s.company_name " +
                "FROM supplier s " +
                "INNER JOIN supplier_product sp ON s.supplier_id = sp.supplier_id " +
                "WHERE sp.product_id = ? " +
                "ORDER BY s.supplier_name";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Supplier s = new Supplier();
                s.setSupplierId(rs.getInt("supplier_id"));
                s.setSupplierName(rs.getString("supplier_name"));
                s.setCompanyName(rs.getString("company_name"));
                suppliers.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Error getting suppliers for product: " + e.getMessage());
        }
        return suppliers;
    }
}
