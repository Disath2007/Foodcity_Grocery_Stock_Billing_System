package controller;

import database.DatabaseConnection;
import model.Product;
import model.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * CONTROLLER LAYER - Product Controller
 * 
 * This class handles all business logic and database operations for products.
 * It acts as an intermediary between the View (GUI) and the Model (Product).
 * 
 * Responsibilities:
 * - CRUD operations for products
 * - Data validation
 * - Business rule enforcement
 */
public class ProductController {

    /**
     * Retrieves all products from the database with category names
     * 
     * @return List of all products
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.product_id, p.product_name, p.category_id, c.category_name, p.price, p.buying_price " +
                "FROM product p JOIN category c ON p.category_id = c.category_id " +
                "ORDER BY p.product_name";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getDouble("price"),
                        rs.getDouble("buying_price"));
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all products: " + e.getMessage());
        }
        return products;
    }

    /**
     * Searches for products by name
     * 
     * @param searchTerm The search term to match against product names
     * @return List of matching products
     */
    public List<Product> searchProducts(String searchTerm) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.product_id, p.product_name, p.category_id, c.category_name, p.price, p.buying_price " +
                "FROM product p JOIN category c ON p.category_id = c.category_id " +
                "WHERE p.product_name LIKE ? " +
                "ORDER BY p.product_name";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + searchTerm + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getInt("category_id"),
                            rs.getString("category_name"),
                            rs.getDouble("price"),
                            rs.getDouble("buying_price"));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching products: " + e.getMessage());
        }
        return products;
    }

    /**
     * Gets all categories for the product form dropdown
     * 
     * @return List of all categories
     */
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT category_id, category_name FROM category ORDER BY category_name";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("category_name"));
                categories.add(category);
            }
        } catch (SQLException e) {
            System.err.println("Error getting categories: " + e.getMessage());
        }
        return categories;
    }

    /**
     * Adds a new product to the database using the Product model
     * 
     * @param product The Product object containing data
     * @return String message indicating success or error
     */
    public String addProduct(Product product) {
        // Validate inputs
        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            return "Error: Product name cannot be empty!";
        }
        if (product.getCategoryId() <= 0) {
            return "Error: Please select a category!";
        }
        if (product.getPrice() < 0 || product.getBuyingPrice() < 0) {
            return "Error: Prices cannot be negative!";
        }

        // Check if product name already exists
        if (isProductNameExists(product.getProductName(), -1)) {
            return "Error: Product name already exists!";
        }

        String sql = "INSERT INTO product (product_name, category_id, price, buying_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getProductName().trim());
            pstmt.setInt(2, product.getCategoryId());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setDouble(4, product.getBuyingPrice());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Success: Product added successfully!";
            } else {
                return "Error: Failed to add product!";
            }
        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Updates an existing product using the Product model
     * 
     * @param product The Product object containing updated data
     * @return String message indicating success or error
     */
    public String updateProduct(Product product) {
        // Validate inputs
        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            return "Error: Product name cannot be empty!";
        }
        if (product.getCategoryId() <= 0) {
            return "Error: Please select a category!";
        }
        if (product.getPrice() < 0 || product.getBuyingPrice() < 0) {
            return "Error: Prices cannot be negative!";
        }

        // Check if product name already exists (excluding current product)
        if (isProductNameExists(product.getProductName(), product.getProductId())) {
            return "Error: Product name already exists!";
        }

        String sql = "UPDATE product SET product_name = ?, category_id = ?, price = ?, buying_price = ? WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getProductName().trim());
            pstmt.setInt(2, product.getCategoryId());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setDouble(4, product.getBuyingPrice());
            pstmt.setInt(5, product.getProductId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Success: Product updated successfully!";
            } else {
                return "Error: Product not found!";
            }
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Deletes a product from the database
     * 
     * @param productId ID of the product to delete
     * @return String message indicating success or error
     */
    public String deleteProduct(int productId) {
        String sql = "DELETE FROM product WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Success: Product deleted successfully!";
            } else {
                return "Error: Product not found!";
            }
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Checks if a product name already exists
     * 
     * @param productName The name to check
     * @param excludeId   Product ID to exclude from check (for updates), -1 for new
     *                    products
     * @return true if the name exists, false otherwise
     */
    public boolean isProductNameExists(String productName, int excludeId) {
        String sql = "SELECT COUNT(*) FROM product WHERE product_name = ? AND product_id != ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productName.trim());
            pstmt.setInt(2, excludeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking product name: " + e.getMessage());
        }
        return false;
    }
}
