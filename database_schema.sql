-- =========================================
-- Foodcity Grocery Stock & Billing System
-- Database Schema
-- =========================================

-- Create Database
DROP DATABASE IF EXISTS foodcity_db;
CREATE DATABASE foodcity_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE foodcity_db;

-- =========================================
-- Table: users
-- Description: Store user accounts (Admin, Manager, Cashier)
-- =========================================
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,

    phone VARCHAR(15),
    role ENUM('Admin', 'Manager', 'Cashier') NOT NULL,
   
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
) ENGINE=InnoDB;

-- =========================================
-- Insert Sample Data
-- =========================================

-- Default User Accounts
INSERT INTO users (username, password, full_name, phone, role) VALUES
('admin', 'admin123', 'System Administrator', '0771234567', 'Admin'),
('manager1', 'manager123', 'John Manager', '0777654321', 'Manager'),
('cashier1', 'cashier123', 'Mary Cashier', '0712345678', 'Cashier');

-- =========================================
-- End of Database Schema
-- =========================================
