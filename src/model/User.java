package model;

import java.sql.Timestamp;

/**
 * MODEL LAYER - User Entity
 * 
 * This class represents a user in the system.
 * It is a pure data class (POJO) with no database or UI logic.
 * All database operations are handled by UserController.
 */
public class User {
    private int userId;
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private String role;
    private Timestamp createdAt;

    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * Constructor for creating new users
     * 
     * @param username Username
     * @param password Password
     * @param fullName Full name
     * @param phone    Phone number
     * @param role     User role (Admin/Manager/Staff)
     */
    public User(String username, String password, String fullName, String phone, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
    }

    /**
     * Full constructor with all fields
     * 
     * @param userId    User ID
     * @param username  Username
     * @param password  Password
     * @param fullName  Full name
     * @param phone     Phone number
     * @param role      User role
     * @param createdAt Creation timestamp
     */
    public User(int userId, String username, String password, String fullName,
            String phone, String role, Timestamp createdAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getters and Setters

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
