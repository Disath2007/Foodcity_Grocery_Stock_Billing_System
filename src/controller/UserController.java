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
        // Check if username or password is null or empty
        // If invalid input, return null (login fails)
        if (username == null || username.trim().isEmpty() ||
                password == null || password.isEmpty()) {
            return null;
        }

        // SQL query to select user details from the database
        // It checks if the username and password match a record
        String sql = "SELECT user_id, username, password, full_name, phone, role, created_at " +
                "FROM users WHERE username = ? AND password = ?";

        // Try-with-resources: automatically closes connection and statement
        try (Connection conn = DatabaseConnection.getConnection(); // Get database connection
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare SQL statement

            // Set the first parameter (?) in the SQL query as username
            pstmt.setString(1, username.trim());

            // Set the second parameter (?) in the SQL query as password
            pstmt.setString(2, password);

            // Execute the query and store the result in ResultSet
            try (ResultSet rs = pstmt.executeQuery()) {

                // If a matching user is found in the database
                if (rs.next()) {

                    // Create and return a new User object using database values
                    return new User(
                            rs.getInt("user_id"), // Get user ID
                            rs.getString("username"), // Get username
                            rs.getString("password"), // Get password
                            rs.getString("full_name"), // Get full name
                            rs.getString("phone"), // Get phone number
                            rs.getString("role"), // Get user role
                            rs.getTimestamp("created_at") // Get account creation time
                    );
                }
            }

            // Catch any SQL errors and print an error message
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
        }

        // If no user is found or an error occurs, return null
        return null;
    }

    /**
     * Retrieves all users from the database
     * 
     * @return List of all users
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        // SQL query to fetch all user records ordered by full name
        String sql = "SELECT user_id, username, password, full_name, phone, role, created_at " +
                "FROM users ORDER BY full_name";

        // Try-with-resources to ensure connection and statement are closed
        try (Connection conn = DatabaseConnection.getConnection(); // Get connection
                PreparedStatement pstmt = conn.prepareStatement(sql); // Prepare statement
                ResultSet rs = pstmt.executeQuery()) { // Execute query

            // Iterate through each record in the result set
            while (rs.next()) {
                // Create User object from current result row
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("role"),
                        rs.getTimestamp("created_at"));
                // Add user to the list
                users.add(user);
            }
        } catch (SQLException e) {
            // Log database errors
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
        // SQL query to find a specific user by their ID
        String sql = "SELECT user_id, username, password, full_name, phone, role, created_at " +
                "FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); // Initialize connection
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare SQL

            // Map the method parameter to the SQL query
            pstmt.setInt(1, userId);

            // Execute the selection query
            try (ResultSet rs = pstmt.executeQuery()) {
                // If a record is found, build the User model
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
            // Error handling for database failures
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
        // SQL query using LIKE operators for flexible searching by name or username
        String sql = "SELECT user_id, username, password, full_name, phone, role, created_at " +
                "FROM users WHERE full_name LIKE ? OR username LIKE ? ORDER BY full_name";

        try (Connection conn = DatabaseConnection.getConnection(); // Connect to DB
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare statement

            // Set search terms with wildcards for partial matching
            pstmt.setString(1, "%" + searchTerm + "%");
            pstmt.setString(2, "%" + searchTerm + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                // Collect all matching records into a list
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
            // Log search errors
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
        // 1. Logic Validation: Check essential fields using Model getters
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            return "Error: Full name cannot be empty!";
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return "Error: Password cannot be empty!";
        }
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            return "Error: Please select a role!";
        }

        // 2. Business Logic: Generate username from full name (lowercase, no spaces)
        String username = user.getFullName().trim().toLowerCase().replace(" ", "");

        // 3. Duplicate Handling: Check if username already exists and append suffix if
        // needed
        if (isUsernameExists(username, -1)) {
            int suffix = 1;
            while (isUsernameExists(username + suffix, -1)) {
                suffix++;
            }
            username = username + suffix;
        }

        // Set the final generated username back into the User model
        user.setUsername(username);

        // SQL query to insert a new user record
        String sql = "INSERT INTO users (username, password, full_name, phone, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); // Get connection
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare insert statement

            // Map User model fields to query parameters
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFullName().trim());
            pstmt.setString(4, user.getPhone() != null ? user.getPhone().trim() : "");
            pstmt.setString(5, user.getRole().trim());

            // Execute the update query
            int rowsAffected = pstmt.executeUpdate();

            // Return success or failure feedback
            if (rowsAffected > 0) {
                return "Success: User added successfully! Username: " + username;
            } else {
                return "Error: Failed to add user!";
            }
        } catch (SQLException e) {
            // Handle and return database specific errors
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
        // Validate core data using model getters
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            return "Error: Full name cannot be empty!";
        }
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            return "Error: Please select a role!";
        }

        // Determine if password needs updating (conditional SQL)
        String sql;
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            sql = "UPDATE users SET password = ?, full_name = ?, phone = ?, role = ? WHERE user_id = ?";
        } else {
            sql = "UPDATE users SET full_name = ?, phone = ?, role = ? WHERE user_id = ?";
        }

        try (Connection conn = DatabaseConnection.getConnection(); // Connect to DB
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare update statement

            // Dynamically set parameters based on whether password is being changed
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

            // Perform the update
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Success: User updated successfully!";
            } else {
                return "Error: User not found!";
            }
        } catch (SQLException e) {
            // Log and report update errors
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
        // Business Rule: Prevent deletion of the last admin user
        User user = getUserById(userId);
        if (user != null && "Admin".equals(user.getRole())) {
            int adminCount = countUsersByRole("Admin");
            if (adminCount <= 1) {
                return "Error: Cannot delete the last admin user!";
            }
        }

        // SQL query to remove a user record
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); // Database connection
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // Statement creation

            // Set ID parameter
            pstmt.setInt(1, userId);

            // Execute delete
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Success: User deleted successfully!";
            } else {
                return "Error: User not found!";
            }
        } catch (SQLException e) {
            // Error handling
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
        // SQL query to count matching usernames while excluding the current user's ID
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND user_id != ?";
        try (Connection conn = DatabaseConnection.getConnection(); // Connect
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare

            // Bind values
            pstmt.setString(1, username.trim());
            pstmt.setInt(2, excludeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                // If count > 0, the username is taken
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
        // Aggregate query to count users by role
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
