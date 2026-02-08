package controller;

import database.DatabaseConnection;
import model.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * CONTROLLER LAYER - Category Controller
 * 
 * This class handles all business logic and database operations for categories.
 * It acts as an intermediary between the View (GUI) and the Model (Category).
 * 
 * Responsibilities:
 * - CRUD operations for categories
 * - Data validation
 * - Business rule enforcement
 * 
 * MVC Pattern:
 * - Model: Category.java (data structure)
 * - View: CategoryManagement.java (GUI)
 * - Controller: This class (business logic)
 */
public class CategoryController {

    /**
     * Retrieves all categories from the database
     * 
     * @return List of all categories with product counts
     */
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        // SQL query to fetch categories and count associated products using a LEFT JOIN
        String sql = "SELECT c.category_id, c.category_name, COUNT(p.product_id) as product_count " +
                "FROM category c LEFT JOIN product p ON c.category_id = p.category_id " +
                "GROUP BY c.category_id, c.category_name ORDER BY c.category_id";

        // Try-with-resources: connects to database and prepares the select statement
        try (Connection conn = DatabaseConnection.getConnection(); // Get connection
                PreparedStatement pstmt = conn.prepareStatement(sql); // Prepare statement
                ResultSet rs = pstmt.executeQuery()) { // Execute selection

            // Loop through each record found in the category table
            while (rs.next()) {
                // Initialize Category model with data from the current row
                Category category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getInt("product_count"));
                // Add the category object to our return list
                categories.add(category);
            }
        } catch (SQLException e) {
            // Print database error logs
            System.err.println("Error getting all categories: " + e.getMessage());
        }
        return categories;
    }

    /**
     * Searches for categories by name
     * 
     * @param searchTerm The search term to match against category names
     * @return List of matching categories
     */
    public List<Category> searchCategories(String searchTerm) {
        List<Category> categories = new ArrayList<>();
        // Search query using LIKE for partial name matching
        String sql = "SELECT c.category_id, c.category_name, COUNT(p.product_id) as product_count " +
                "FROM category c LEFT JOIN product p ON c.category_id = p.category_id " +
                "WHERE c.category_name LIKE ? " +
                "GROUP BY c.category_id, c.category_name ORDER BY c.category_name";

        try (Connection conn = DatabaseConnection.getConnection(); // Database link
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare SQL

            // Set the search parameter with wildcards (%) for 'contains' search
            pstmt.setString(1, "%" + searchTerm + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                // Collect matching categories into the list
                while (rs.next()) {
                    Category category = new Category(
                            rs.getInt("category_id"),
                            rs.getString("category_name"),
                            rs.getInt("product_count"));
                    categories.add(category);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching categories: " + e.getMessage());
        }
        return categories;
    }

    /**
     * Adds a new category to the database using the Category model
     * 
     * @param category The Category object containing data
     * @return String message indicating success or error
     */
    public String addCategory(Category category) {
        // 1. Validation: Ensure the category name isn't empty
        if (category.getCategoryName() == null || category.getCategoryName().trim().isEmpty()) {
            return "Error: Category name cannot be empty!";
        }

        // 2. Business Rule: Prevent duplicate category names
        if (isCategoryNameExists(category.getCategoryName(), -1)) {
            return "Error: Category name already exists!";
        }

        // 3. Database Operation: Insert new category name
        String sql = "INSERT INTO category (category_name) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection(); // Get DB access
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // Create insert statement

            // Bind the name from the Category model
            pstmt.setString(1, category.getCategoryName().trim());

            // Execute the operation
            int rowsAffected = pstmt.executeUpdate();

            // Check if insertion was successful
            if (rowsAffected > 0) {
                return "Success: Category added successfully!";
            } else {
                return "Error: Failed to add category!";
            }
        } catch (SQLException e) {
            // Log and return database errors
            System.err.println("Error adding category: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Updates an existing category using the Category model
     * 
     * @param category The Category object containing updated data
     * @return String message indicating success or error
     */
    public String updateCategory(Category category) {
        // Validation: Name must be present
        if (category.getCategoryName() == null || category.getCategoryName().trim().isEmpty()) {
            return "Error: Category name cannot be empty!";
        }

        // Rule: Ensure name isn't already used by another category ID
        if (isCategoryNameExists(category.getCategoryName(), category.getCategoryId())) {
            return "Error: Category name already exists!";
        }

        // Update existing record by ID
        String sql = "UPDATE category SET category_name = ? WHERE category_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); // Connect
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare update

            // Bind name and ID
            pstmt.setString(1, category.getCategoryName().trim());
            pstmt.setInt(2, category.getCategoryId());

            // Execute update
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Success: Category updated successfully!";
            } else {
                return "Error: Category not found!";
            }
        } catch (SQLException e) {
            // Error handling
            System.err.println("Error updating category: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Deletes a category from the database
     * 
     * @param categoryId The ID of the category to delete
     * @return String message indicating success or error
     */
    public String deleteCategory(int categoryId) {
        // Security Check: Cannot delete if products are linked to this category
        if (hasProducts(categoryId)) {
            return "Error: Cannot delete category with existing products!";
        }

        // SQL removal query
        String sql = "DELETE FROM category WHERE category_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); // Initialize connection
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare delete

            // Set the ID parameter
            pstmt.setInt(1, categoryId);

            // Execute deletion
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Success: Category deleted successfully!";
            } else {
                return "Error: Category not found!";
            }
        } catch (SQLException e) {
            System.err.println("Error deleting category: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Checks if a category name already exists
     * 
     * @param categoryName The name to check
     * @param excludeId    Category ID to exclude from check (for updates), -1 for
     *                     new categories
     * @return true if the name exists, false otherwise
     */
    public boolean isCategoryNameExists(String categoryName, int excludeId) {
        // Query to count categories with the same name but different ID
        String sql = "SELECT COUNT(*) FROM category WHERE category_name = ? AND category_id != ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, categoryName.trim());
            pstmt.setInt(2, excludeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                // If count > 0, the name is taken
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking category name: " + e.getMessage());
        }
        return false;
    }

    /**
     * Checks if a category has any products
     * 
     * @param categoryId The category ID to check
     * @return true if the category has products, false otherwise
     */
    public boolean hasProducts(int categoryId) {
        // Check if any product records reference this category ID
        String sql = "SELECT COUNT(*) FROM product WHERE category_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, categoryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                // If count > 0, products exist for this category
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking category products: " + e.getMessage());
        }
        return false;
    }
}
