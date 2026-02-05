-- =========================================
-- Foodcity Grocery Stock & Billing System
-- Database Schema (Updated for Buying Price & GRN Total)
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
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
    price DECIMAL(10, 2) NOT NULL COMMENT 'Selling Price',
    buying_price DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT 'Buying Price',
    FOREIGN KEY (category_id) REFERENCES category(category_id) 
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- =========================================
-- Table: stock
-- Description: Store product stock quantities
-- =========================================
CREATE TABLE stock (
    stock_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL UNIQUE,
    quantity INT NOT NULL DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(product_id) 
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- =========================================
-- Table: supplier
-- Description: Store supplier information
-- =========================================
CREATE TABLE supplier (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    supplier_name VARCHAR(100) NOT NULL,
    company_name VARCHAR(150) NOT NULL,
    phone VARCHAR(15) NOT NULL
) ENGINE=InnoDB;

-- =========================================
-- Table: supplier_product
-- Description: Link suppliers with products they supply
-- =========================================
CREATE TABLE supplier_product (
    sp_id INT AUTO_INCREMENT PRIMARY KEY,
    supplier_id INT NOT NULL,
    product_id INT NOT NULL,
    FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id) 
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id) 
        ON UPDATE CASCADE ON DELETE CASCADE,
    UNIQUE KEY unique_supplier_product (supplier_id, product_id)
) ENGINE=InnoDB;

-- =========================================
-- Table: grn
-- Description: Goods Received Note for stock receiving
-- =========================================
CREATE TABLE grn (
    grn_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    supplier_id INT NOT NULL,
    ordered_quantity INT NOT NULL,
    delivered_quantity INT NOT NULL,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(product_id) 
        ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id) 
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- =========================================
-- Table: sales
-- Description: Transaction summaries
-- =========================================
CREATE TABLE sales (
    sale_id INT AUTO_INCREMENT PRIMARY KEY,
    cashier_name VARCHAR(100) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    discount DECIMAL(10, 2) NOT NULL,
    grand_total DECIMAL(10, 2) NOT NULL,
    cash_received DECIMAL(10, 2) NOT NULL,
    balance DECIMAL(10, 2) NOT NULL,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =========================================
-- Table: sales_items
-- Description: Individual items for each sale
-- =========================================
CREATE TABLE sales_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    sale_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (sale_id) REFERENCES sales(sale_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id)
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

-- Sample Products (Including dummy buying price approx 70-80% of selling price)
INSERT INTO product (product_name, category_id, price, buying_price) VALUES
('Fresh Milk 1L', 1, 350.00, 280.00),
('Cheddar Cheese 200g', 1, 550.00, 440.00),
('Butter 250g', 1, 480.00, 380.00),
('Yogurt Cup 150g', 1, 120.00, 95.00),
('Coca Cola 1.5L', 2, 350.00, 280.00),
('Sprite 500ml', 2, 150.00, 120.00),
('Orange Juice 1L', 2, 420.00, 330.00),
('Mineral Water 1L', 2, 80.00, 40.00),
('White Bread Loaf', 3, 180.00, 140.00),
('Croissant', 3, 150.00, 100.00),
('Apple 1kg', 4, 650.00, 500.00),
('Banana 1kg', 4, 280.00, 200.00),
('Orange 1kg', 4, 450.00, 350.00),
('Carrot 500g', 5, 180.00, 130.00),
('Tomato 500g', 5, 220.00, 160.00),
('Onion 1kg', 5, 320.00, 250.00),
('Chicken Breast 500g', 6, 850.00, 680.00),
('Ground Beef 500g', 6, 1200.00, 950.00),
('Salmon Fillet 250g', 7, 1500.00, 1200.00),
('Shrimp 500g', 7, 1800.00, 1400.00),
('Frozen Pizza', 8, 750.00, 500.00),
('Ice Cream 1L', 8, 680.00, 540.00),
('Canned Beans 400g', 9, 180.00, 140.00),
('Canned Corn 400g', 9, 150.00, 110.00),
('Potato Chips 150g', 10, 280.00, 210.00),
('Chocolate Bar 100g', 10, 220.00, 170.00),
('Ketchup 500ml', 11, 350.00, 280.00),
('Mayonnaise 250g', 11, 420.00, 330.00),
('Black Pepper 50g', 12, 180.00, 130.00),
('Chili Powder 100g', 12, 150.00, 110.00),
('Rice 5kg', 13, 1200.00, 950.00),
('Spaghetti 500g', 13, 280.00, 200.00),
('Shampoo 250ml', 14, 480.00, 350.00),
('Toothpaste 100g', 14, 220.00, 170.00),
('Dish Soap 500ml', 15, 320.00, 250.00),
('Laundry Detergent 1kg', 15, 650.00, 520.00);

INSERT INTO supplier (supplier_name, company_name, phone) VALUES
('Nimal Perera', 'Nimal Traders', '0771234567'),
('Kamal Fernando', 'Kamal & Sons', '0712345678'),
('Sunil Silva', 'Silva Distributors', '0759876543'),
('Ruwan Jayasinghe', 'Jayasinghe Supplies', '0763456789'),
('Ajith Kumara', 'Ajith Wholesale', '0704567890'),
('Saman Wickramasinghe', 'Wickrama Enterprises', '0723344556'),
('Pradeep Gunaratne', 'Gunaratne Trading Co.', '0789988776'),
('Tharindu Weerasinghe', 'Weera Suppliers', '0741122334');

-- Sample Stock Data (Dummy Quantities for all products)
INSERT INTO stock (product_id, quantity) VALUES
(1, 120),   -- Fresh Milk 1L
(2, 85),    -- Cheddar Cheese 200g
(3, 95),    -- Butter 250g
(4, 200),   -- Yogurt Cup 150g
(5, 150),   -- Coca Cola 1.5L
(6, 180),   -- Sprite 500ml
(7, 75),    -- Orange Juice 1L
(8, 250),   -- Mineral Water 1L
(9, 140),   -- White Bread Loaf
(10, 60),   -- Croissant
(11, 90),   -- Apple 1kg
(12, 110),  -- Banana 1kg
(13, 85),   -- Orange 1kg
(14, 130),  -- Carrot 500g
(15, 145),  -- Tomato 500g
(16, 160),  -- Onion 1kg
(17, 55),   -- Chicken Breast 500g
(18, 65),   -- Ground Beef 500g
(19, 35),   -- Salmon Fillet 250g
(20, 40),   -- Shrimp 500g
(21, 70),   -- Frozen Pizza
(22, 80),   -- Ice Cream 1L
(23, 190),  -- Canned Beans 400g
(24, 200),  -- Canned Corn 400g
(25, 175),  -- Potato Chips 150g
(26, 220),  -- Chocolate Bar 100g
(27, 95),   -- Ketchup 500ml
(28, 85),   -- Mayonnaise 250g
(29, 110),  -- Black Pepper 50g
(30, 125),  -- Chili Powder 100g
(31, 45),   -- Rice 5kg
(32, 150),  -- Spaghetti 500g
(33, 100),  -- Shampoo 250ml
(34, 210),  -- Toothpaste 100g
(35, 120),  -- Dish Soap 500ml
(36, 75);  -- Laundry Detergent 1kg

-- Sample Supplier-Product Data (Linking Products to Suppliers)
INSERT INTO supplier_product (supplier_id, product_id) VALUES
(1, 1),  -- Nimal supplies Fresh Milk
(1, 2),  -- Nimal supplies Cheese
(1, 3),  -- Nimal supplies Butter
(1, 4),  -- Nimal supplies Yogurt
(2, 5),  -- Kamal supplies Coca Cola
(2, 6),  -- Kamal supplies Sprite
(2, 7),  -- Kamal supplies Orange Juice
(2, 8),  -- Kamal supplies Mineral Water
(3, 9),  -- Sunil supplies Bread
(3, 10), -- Sunil supplies Croissant
(3, 11), -- Sunil supplies Apple
(3, 12), -- Sunil supplies Banana
(3, 15), -- Sunil supplies Tomato
(4, 21), -- Ruwan supplies Frozen Pizza
(4, 22), -- Ruwan supplies Ice Cream
(5, 31), -- Ajith supplies Rice
(5, 32), -- Ajith supplies Spaghetti
(5, 23), -- Ajith supplies Canned Beans
(6, 33), -- Saman supplies Shampoo
(6, 34), -- Saman supplies Toothpaste
(6, 35), -- Saman supplies Dish Soap
(6, 36), -- Saman supplies Laundry Detergent
(7, 17), -- Pradeep supplies Chicken
(7, 18), -- Pradeep supplies Beef
(8, 19), -- Tharindu supplies Salmon
(8, 20); -- Tharindu supplies Shrimp

-- Sample GRN Data
INSERT INTO grn (product_id, supplier_id, ordered_quantity, delivered_quantity, date_created) VALUES
(1, 1, 50, 50, '2023-10-01 08:30:00'),   -- Fresh Milk from Nimal
(5, 2, 100, 100, '2023-10-02 09:45:00'),  -- Coca Cola from Kamal
(11, 3, 30, 30, '2023-10-03 07:15:00'),   -- Apples from Sunil
(21, 4, 40, 40, '2023-10-05 14:20:00'),   -- Frozen Pizza from Ruwan
(31, 5, 25, 25, '2023-10-08 11:00:00'),   -- Rice from Ajith
(2, 1, 20, 20, '2023-10-10 10:30:00'),    -- Cheese from Nimal
(15, 3, 60, 50, '2023-10-12 13:45:00'),   -- Tomatoes (Partial delivery)
(33, 6, 50, 50, '2023-10-15 15:00:00'),   -- Shampoo from Saman
(36, 6, 40, 40, CURRENT_TIMESTAMP - INTERVAL 3 DAY), -- Detergent
(17, 7, 20, 20, CURRENT_TIMESTAMP - INTERVAL 1 DAY); -- Chicken

-- =========================================
-- End of Database Schema
-- =========================================
