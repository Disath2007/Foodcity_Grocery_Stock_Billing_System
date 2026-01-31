package controller;

import database.DatabaseConnection;
import model.Supplier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * CONTROLLER LAYER - Supplier Controller
 * 
 * This class handles all business logic and database operations for suppliers.
 * It acts as an intermediary between the View (GUI) and the Model (Supplier).
 * 
 * Responsibilities:
 * - CRUD operations for suppliers
 * - Data validation
 * - Business rule enforcement
 */
public class SupplierController {

    /**
     * Retrieves all suppliers from the database with product counts
     * 
     * @return List of all suppliers
     */
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT s.supplier_id, s.supplier_name, s.company_name, s.phone, " +
                "COUNT(sp.product_id) as product_count " +
                "FROM supplier s LEFT JOIN supplier_product sp ON s.supplier_id = sp.supplier_id " +
                "GROUP BY s.supplier_id, s.supplier_name, s.company_name, s.phone " +
                "ORDER BY s.supplier_name";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Supplier supplier = new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("company_name"),
                        rs.getString("phone"),
                        rs.getInt("product_count"));
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all suppliers: " + e.getMessage());
        }
        return suppliers;
    }

    /**
     * Searches for suppliers by name or company
     * 
     * @param searchTerm The search term to match
     * @return List of matching suppliers
     */
    public List<Supplier> searchSuppliers(String searchTerm) {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT s.supplier_id, s.supplier_name, s.company_name, s.phone, " +
                "COUNT(sp.product_id) as product_count " +
                "FROM supplier s LEFT JOIN supplier_product sp ON s.supplier_id = sp.supplier_id " +
                "WHERE s.supplier_name LIKE ? OR s.company_name LIKE ? " +
                "GROUP BY s.supplier_id, s.supplier_name, s.company_name, s.phone " +
                "ORDER BY s.supplier_name";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + searchTerm + "%");
            pstmt.setString(2, "%" + searchTerm + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Supplier supplier = new Supplier(
                            rs.getInt("supplier_id"),
                            rs.getString("supplier_name"),
                            rs.getString("company_name"),
                            rs.getString("phone"),
                            rs.getInt("product_count"));
                    suppliers.add(supplier);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching suppliers: " + e.getMessage());
        }
        return suppliers;
    }

    /**
     * Adds a new supplier to the database
     * 
     * @param supplierName Name of the supplier
     * @param companyName  Company name
     * @param phone        Phone number
     * @return String message indicating success or error
     */
    public String addSupplier(String supplierName, String companyName, String phone) {
        // Validate inputs
        if (supplierName == null || supplierName.trim().isEmpty()) {
            return "Error: Supplier name cannot be empty!";
        }
        if (companyName == null || companyName.trim().isEmpty()) {
            return "Error: Company name cannot be empty!";
        }

        String sql = "INSERT INTO supplier (supplier_name, company_name, phone) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, supplierName.trim());
            pstmt.setString(2, companyName.trim());
            pstmt.setString(3, phone != null ? phone.trim() : "");
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Success: Supplier added successfully!";
            } else {
                return "Error: Failed to add supplier!";
            }
        } catch (SQLException e) {
            System.err.println("Error adding supplier: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Updates an existing supplier
     * 
     * @param supplierId   ID of the supplier to update
     * @param supplierName New name
     * @param companyName  New company name
     * @param phone        New phone number
     * @return String message indicating success or error
     */
    public String updateSupplier(int supplierId, String supplierName, String companyName, String phone) {
        // Validate inputs
        if (supplierName == null || supplierName.trim().isEmpty()) {
            return "Error: Supplier name cannot be empty!";
        }
        if (companyName == null || companyName.trim().isEmpty()) {
            return "Error: Company name cannot be empty!";
        }

        String sql = "UPDATE supplier SET supplier_name = ?, company_name = ?, phone = ? WHERE supplier_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, supplierName.trim());
            pstmt.setString(2, companyName.trim());
            pstmt.setString(3, phone != null ? phone.trim() : "");
            pstmt.setInt(4, supplierId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Success: Supplier updated successfully!";
            } else {
                return "Error: Supplier not found!";
            }
        } catch (SQLException e) {
            System.err.println("Error updating supplier: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Deletes a supplier from the database
     * 
     * @param supplierId ID of the supplier to delete
     * @return String message indicating success or error
     */
    public String deleteSupplier(int supplierId) {
        // First delete supplier-product relationships
        String deleteSPSql = "DELETE FROM supplier_product WHERE supplier_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(deleteSPSql)) {
            pstmt.setInt(1, supplierId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting supplier products: " + e.getMessage());
            // Continue to try deleting the supplier anyway
        }

        // Then delete the supplier
        String sql = "DELETE FROM supplier WHERE supplier_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, supplierId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Success: Supplier deleted successfully!";
            } else {
                return "Error: Supplier not found!";
            }
        } catch (SQLException e) {
            System.err.println("Error deleting supplier: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
}
