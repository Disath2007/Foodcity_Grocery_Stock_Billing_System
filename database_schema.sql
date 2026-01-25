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
-- Table: category
-- Description: Product categories
-- =========================================
CREATE TABLE category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- =========================================
-- Table: product
-- Description: Store product/item information
-- =========================================
CREATE TABLE product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(150) NOT NULL,
    category_id INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category(category_id) 
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- =========================================
-- Insert Sample Data
-- =========================================

-- Default User Accounts
INSERT INTO users (username, password, full_name, phone, role) VALUES
('admin', 'admin123', 'System Administrator', '0771234567', 'Admin'),
('manager1', 'manager123', 'John Manager', '0777654321', 'Manager'),
('cashier1', 'cashier123', 'Mary Cashier', '0712345678', 'Cashier');

-- Sample Categories
INSERT INTO category (category_name) VALUES 
('Dairy Products'),
('Beverages'),
('Bakery'),
('Fruits'),
('Vegetables'),
('Meat & Poultry'),
('Seafood'),
('Frozen Foods'),
('Canned Goods'),
('Snacks'),
('Condiments'),
('Spices'),
('Grains & Pasta'),
('Personal Care'),
('Household Items');

-- Sample Products
INSERT INTO product (product_name, category_id, price) VALUES
('Fresh Milk 1L', 1, 350.00),
('Cheddar Cheese 200g', 1, 550.00),
('Butter 250g', 1, 480.00),
('Yogurt Cup 150g', 1, 120.00),
('Coca Cola 1.5L', 2, 350.00),
('Sprite 500ml', 2, 150.00),
('Orange Juice 1L', 2, 420.00),
('Mineral Water 1L', 2, 80.00),
('White Bread Loaf', 3, 180.00),
('Croissant', 3, 150.00),
('Apple 1kg', 4, 650.00),
('Banana 1kg', 4, 280.00),
('Orange 1kg', 4, 450.00),
('Carrot 500g', 5, 180.00),
('Tomato 500g', 5, 220.00),
('Onion 1kg', 5, 320.00),
('Chicken Breast 500g', 6, 850.00),
('Ground Beef 500g', 6, 1200.00),
('Salmon Fillet 250g', 7, 1500.00),
('Shrimp 500g', 7, 1800.00),
('Frozen Pizza', 8, 750.00),
('Ice Cream 1L', 8, 680.00),
('Canned Beans 400g', 9, 180.00),
('Canned Corn 400g', 9, 150.00),
('Potato Chips 150g', 10, 280.00),
('Chocolate Bar 100g', 10, 220.00),
('Ketchup 500ml', 11, 350.00),
('Mayonnaise 250g', 11, 420.00),
('Black Pepper 50g', 12, 180.00),
('Chili Powder 100g', 12, 150.00),
('Rice 5kg', 13, 1200.00),
('Spaghetti 500g', 13, 280.00),
('Shampoo 250ml', 14, 480.00),
('Toothpaste 100g', 14, 220.00),
('Dish Soap 500ml', 15, 320.00),
('Laundry Detergent 1kg', 15, 650.00);

-- =========================================
-- End of Database Schema
-- =========================================
