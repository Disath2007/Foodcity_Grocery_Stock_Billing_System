package controller;

import database.DatabaseConnection;
import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * CONTROLLER LAYER - User Controller
 * 
 * This class handles all business logic and database operations for users.
 * It acts as an intermediary between the View (GUI) and the Model (User).
 * 
 * Responsibilities:
 * - User authentication (login)
 * - CRUD operations for users
 * - Data validation
 * - Business rule enforcement
 */
public class UserController {

    /**
     * Authenticates a user with username and password
     * 
     * @param username The username
     * @param password The password
     * @return User object if successful, null if authentication fails
     */
    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.isEmpty()) {
            return null;
        }

        String sql = "SELECT user_id, username, password, full_name, phone, role, created_at " +
                "FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username.trim());
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("role"),
                            rs.getTimestamp("created_at"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all users from the database
     * 
     * @return List of all users
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password, full_name, phone, role, created_at " +
                "FROM users ORDER BY full_name";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("role"),
                        rs.getTimestamp("created_at"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
        }
        return users;
    }

    /**
     * Gets a user by ID
     * 
     * @param userId The user ID
     * @return User object if found, null otherwise
     */
    public User getUserById(int userId) {
        String sql = "SELECT user_id, username, password, full_name, phone, role, created_at " +
                "FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("role"),
                            rs.getTimestamp("created_at"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Searches for users by name or username
     * 
     * @param searchTerm The search term
     * @return List of matching users
     */
    public List<User> searchUsers(String searchTerm) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password, full_name, phone, role, created_at " +
                "FROM users WHERE full_name LIKE ? OR username LIKE ? ORDER BY full_name";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + searchTerm + "%");
            pstmt.setString(2, "%" + searchTerm + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("role"),
                            rs.getTimestamp("created_at"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching users: " + e.getMessage());
        }
        return users;
    }

    /**
     * Adds a new user to the database using the User model
     * 
     * @param user The User object containing user data
     * @return String message indicating success or error
     */
    public String addUser(User user) {
        // Validate inputs using getters
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            return "Error: Full name cannot be empty!";
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return "Error: Password cannot be empty!";
        }
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            return "Error: Please select a role!";
        }

        // Generate username from full name (lowercase, no spaces)
        String username = user.getFullName().trim().toLowerCase().replace(" ", "");

        // Check if username already exists
        if (isUsernameExists(username, -1)) {
            // Add a number suffix if exists
            int suffix = 1;
            while (isUsernameExists(username + suffix, -1)) {
                suffix++;
            }
            username = username + suffix;
        }

        // Set the generated username back to the model
        user.setUsername(username);

        String sql = "INSERT INTO users (username, password, full_name, phone, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFullName().trim());
            pstmt.setString(4, user.getPhone() != null ? user.getPhone().trim() : "");
            pstmt.setString(5, user.getRole().trim());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                return "Success: User added successfully! Username: " + username;
            } else {
                return "Error: Failed to add user!";
            }
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Updates an existing user using the User model
     * 
     * @param user The User object containing updated data (must include userId)
     * @return String message indicating success or error
     */
    public String updateUser(User user) {
        // Validate inputs using getters
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            return "Error: Full name cannot be empty!";
        }
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            return "Error: Please select a role!";
        }

        String sql;
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            sql = "UPDATE users SET password = ?, full_name = ?, phone = ?, role = ? WHERE user_id = ?";
        } else {
            sql = "UPDATE users SET full_name = ?, phone = ?, role = ? WHERE user_id = ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                pstmt.setString(1, user.getPassword());
                pstmt.setString(2, user.getFullName().trim());
                pstmt.setString(3, user.getPhone() != null ? user.getPhone().trim() : "");
                pstmt.setString(4, user.getRole().trim());
                pstmt.setInt(5, user.getUserId());
            } else {
                pstmt.setString(1, user.getFullName().trim());
                pstmt.setString(2, user.getPhone() != null ? user.getPhone().trim() : "");
                pstmt.setString(3, user.getRole().trim());
                pstmt.setInt(4, user.getUserId());
            }
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Success: User updated successfully!";
            } else {
                return "Error: User not found!";
            }
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Deletes a user from the database
     * 
     * @param userId User ID to delete
     * @return String message indicating success or error
     */
    public String deleteUser(int userId) {
        // Prevent deletion of last admin
        User user = getUserById(userId);
        if (user != null && "Admin".equals(user.getRole())) {
            int adminCount = countUsersByRole("Admin");
            if (adminCount <= 1) {
                return "Error: Cannot delete the last admin user!";
            }
        }

        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Success: User deleted successfully!";
            } else {
                return "Error: User not found!";
            }
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Checks if a username already exists
     * 
     * @param username  The username to check
     * @param excludeId User ID to exclude from check (for updates), -1 for new
     *                  users
     * @return true if the username exists, false otherwise
     */
    public boolean isUsernameExists(String username, int excludeId) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND user_id != ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username.trim());
            pstmt.setInt(2, excludeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
        }
        return false;
    }

    /**
     * Counts users by role
     * 
     * @param role The role to count
     * @return Number of users with the specified role
     */
    private int countUsersByRole(String role) {
        String sql = "SELECT COUNT(*) FROM users WHERE role = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, role);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting users by role: " + e.getMessage());
        }
        return 0;
    }
}
