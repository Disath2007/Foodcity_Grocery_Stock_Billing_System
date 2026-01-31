# MVC Architecture Guide for Foodcity Grocery Stock & Billing System

## 📚 Table of Contents
1. [What is MVC?](#what-is-mvc)
2. [Project Structure](#project-structure)
3. [Layer Responsibilities](#layer-responsibilities)
4. [How to Use This MVC Setup](#how-to-use)
5. [Step-by-Step Guide to Convert Views](#conversion-guide)
6. [Example Code Comparisons](#examples)
7. [Best Practices](#best-practices)

---

## 🏗️ What is MVC? <a name="what-is-mvc"></a>

MVC (Model-View-Controller) is a design pattern that separates an application into three interconnected components:

```
┌─────────────────────────────────────────────────────────────────┐
│                           USER                                   │
│                    (Interacts with GUI)                          │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                       VIEW (GUI Layer)                           │
│  • JFrame, JPanel, JButton, JTable, JTextField                  │
│  • Displays data to user                                         │
│  • Captures user input                                           │
│  • Shows dialogs (JOptionPane)                                   │
│  • Talks to CONTROLLER only                                      │
│                                                                   │
│  Files: GUI/Manager/*.java, GUI/Admin/*.java                    │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    CONTROLLER (Logic Layer)                      │
│  • Receives actions from View                                    │
│  • Validates user input                                          │
│  • Calls appropriate DAO methods                                 │
│  • Returns results/messages to View                              │
│                                                                   │
│  Files: controller/*.java                                        │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      MODEL (Data Layer)                          │
│                                                                   │
│  ┌─────────────────────┐    ┌─────────────────────────────────┐ │
│  │   Entity Classes    │    │    DAO (Data Access Objects)    │ │
│  │                     │    │                                 │ │
│  │  • Product.java     │    │  • ProductDAO.java              │ │
│  │  • Category.java    │    │  • CategoryDAO.java             │ │
│  │  • Supplier.java    │    │  • SupplierDAO.java             │ │
│  │  • User.java        │    │  • UserDAO.java                 │ │
│  │                     │    │                                 │ │
│  │  (Data containers)  │    │  (SQL queries)                  │ │
│  └─────────────────────┘    └─────────────────────────────────┘ │
│                                                                   │
│  Files: model/*.java, dao/*.java                                 │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                         DATABASE                                 │
│                     (MySQL - foodcity_db)                        │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📂 Project Structure <a name="project-structure"></a>

```
src/
├── model/                          # Entity classes (data containers)
│   ├── Product.java                # Represents 'product' table
│   ├── Category.java               # Represents 'category' table
│   ├── Supplier.java               # Represents 'supplier' table
│   ├── User.java                   # Represents 'users' table
│   └── SupplierProduct.java        # Represents 'supplier_product' table
│
├── dao/                            # Data Access Objects (database operations)
│   ├── ProductDAO.java             # CRUD for products
│   ├── CategoryDAO.java            # CRUD for categories
│   ├── SupplierDAO.java            # CRUD for suppliers
│   ├── UserDAO.java                # CRUD for users + login
│   └── SupplierProductDAO.java     # CRUD for supplier-product links
│
├── controller/                     # Controllers (business logic)
│   ├── ProductController.java      # Product operations + validation
│   ├── CategoryController.java     # Category operations + validation
│   ├── SupplierController.java     # Supplier operations + validation
│   └── UserController.java         # User operations + login logic
│
├── view/                           # Example MVC View
│   └── ProductManagementMVC.java   # Reference example
│
├── database/                       # Database connection
│   └── DatabaseConnection.java     # (Already exists)
│
├── GUI/                            # Your existing Views (to be modified)
│   ├── Manager/
│   │   ├── ProductManagement.java
│   │   ├── CategoryManagement.java
│   │   ├── SupplierManagement.java
│   │   └── ...
│   ├── Admin/
│   │   ├── UserManagement.java
│   │   └── AdminDashboard.java
│   ├── LoginFrame.java
│   └── SplashScreen.java
│
└── IMG/                            # Images
```

---

## 📋 Layer Responsibilities <a name="layer-responsibilities"></a>

### MODEL (model/*.java)
| ✅ Should Do | ❌ Should NOT Do |
|-------------|-----------------|
| Define data fields (matching DB columns) | Contain SQL queries |
| Provide getters/setters | Reference GUI components |
| Include constructors | Show dialogs |
| Override toString() | Contain business logic |

### DAO (dao/*.java)
| ✅ Should Do | ❌ Should NOT Do |
|-------------|-----------------|
| Execute SQL queries | Show JOptionPane dialogs |
| Handle Connection, PreparedStatement, ResultSet | Validate user input |
| Throw SQLException | Reference any GUI component |
| Return Model objects or Lists | Handle UI errors |

### CONTROLLER (controller/*.java)
| ✅ Should Do | ❌ Should NOT Do |
|-------------|-----------------|
| Validate user input | Execute SQL directly |
| Call DAO methods | Show JOptionPane dialogs |
| Handle exceptions from DAO | Reference GUI components |
| Return result messages | Access JFrame, JPanel, etc. |
| Coordinate between View and Model | |

### VIEW (GUI/*.java)
| ✅ Should Do | ❌ Should NOT Do |
|-------------|-----------------|
| Display data in tables, text fields | Execute SQL queries |
| Capture user input | Contain business logic |
| Show dialogs (JOptionPane) | Create Connection objects |
| Call Controller methods | Directly create Model objects |
| Handle GUI events (button clicks) | Validate complex business rules |

---

## 🚀 How to Use This MVC Setup <a name="how-to-use"></a>

### Step 1: Add Controller to Your View

In your existing JFrame (e.g., `ProductManagement.java`), add:

```java
// Add this import at the top
import controller.ProductController;
import model.Product;
import model.Category;
import java.util.List;

// Add this field in your class
private ProductController controller;

// Initialize in constructor (after initComponents)
public ProductManagement() {
    initComponents();
    controller = new ProductController();  // <-- ADD THIS
    loadCategories();
    loadProductData();
}
```

### Step 2: Replace Database Code with Controller Calls

**Loading Data (BEFORE):**
```java
private void loadProductData() {
    String sql = "SELECT p.product_id, p.product_name...";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            model.addRow(new Object[] { 
                rs.getInt("product_id"),
                rs.getString("product_name"),
                ...
            });
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}
```

**Loading Data (AFTER - using Controller):**
```java
private void loadProductData() {
    DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
    tableModel.setRowCount(0);
    
    // One line replaces all the SQL code!
    List<Product> products = controller.getAllProducts();
    
    for (Product p : products) {
        tableModel.addRow(new Object[] {
            p.getProductId(),
            p.getProductName(),
            p.getCategoryName(),
            String.format("%.2f", p.getPrice())
        });
    }
}
```

### Step 3: Simplify CRUD Operations

**Adding Product (BEFORE):**
```java
private void addProduct() {
    String productName = txt_itemname.getText().trim();
    String priceText = txt_itemprice.getText().trim();
    
    // Validation
    if (productName.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter name!");
        return;
    }
    // ... more validation ...
    
    // Database
    String sql = "INSERT INTO product...";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, productName);
        // ... 
        pstmt.executeUpdate();
        JOptionPane.showMessageDialog(this, "Success!");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}
```

**Adding Product (AFTER - using Controller):**
```java
private void addProduct() {
    Category selectedCategory = null;
    if (cmb_Category.getSelectedIndex() >= 0) {
        String catName = cmb_Category.getSelectedItem().toString();
        int catId = categoryMap.get(catName);
        selectedCategory = new Category(catId, catName);
    }
    
    // Controller handles validation and database!
    String result = controller.addProduct(
        txt_itemname.getText(),
        selectedCategory,
        txt_itemprice.getText()
    );
    
    if (result.startsWith("Success")) {
        JOptionPane.showMessageDialog(this, result, "Success", JOptionPane.INFORMATION_MESSAGE);
        clearFields();
        loadProductData();
    } else {
        JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
```

---

## 📝 Example Code Comparisons <a name="examples"></a>

### Login Example

**BEFORE (in LoginFrame.java):**
```java
private void btnloginActionPerformed(ActionEvent evt) {
    String username = txtUsername.getText();
    String password = new String(txtPassword.getPassword());
    
    String sql = "SELECT * FROM users WHERE username=? AND password=?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            String role = rs.getString("role");
            if (role.equals("Admin")) {
                new AdminDashboard().setVisible(true);
            } else if (role.equals("Manager")) {
                new ManagerDashboard().setVisible(true);
            }
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database error!");
    }
}
```

**AFTER (using UserController):**
```java
private UserController controller = new UserController();

private void btnloginActionPerformed(ActionEvent evt) {
    String username = txtUsername.getText();
    String password = new String(txtPassword.getPassword());
    
    User user = controller.login(username, password);
    
    if (user != null) {
        switch (user.getRole()) {
            case "Admin":
                new AdminDashboard().setVisible(true);
                break;
            case "Manager":
                new ManagerDashboard().setVisible(true);
                break;
            case "Cashier":
                // new CashierDashboard().setVisible(true);
                break;
        }
        this.dispose();
    } else {
        JOptionPane.showMessageDialog(this, "Invalid username or password!");
    }
}
```

---

## ✅ Best Practices <a name="best-practices"></a>

### 1. Never Mix Layers
```java
// ❌ BAD - DAO showing dialog
public boolean addProduct(Product p) throws SQLException {
    // ...
    JOptionPane.showMessageDialog(null, "Added!");  // NO!
}

// ✅ GOOD - DAO throws exception, Controller handles it
public boolean addProduct(Product p) throws SQLException {
    // Just execute SQL and return result
    return pstmt.executeUpdate() > 0;
}
```

### 2. Controller Returns Messages, View Shows Them
```java
// Controller returns:
return "Error: Please enter a product name!";
return "Success: Product added!";

// View displays:
String result = controller.addProduct(...);
if (result.startsWith("Success")) {
    JOptionPane.showMessageDialog(this, result, "Success", JOptionPane.INFORMATION_MESSAGE);
}
```

### 3. Keep Views Thin
Views should only:
- Get data from form fields
- Pass data to Controller
- Display results from Controller

### 4. Use Model Objects for Data Transfer
```java
// ❌ BAD - Passing multiple parameters
controller.addProduct(name, categoryId, price, description, quantity);

// ✅ GOOD - Passing a Product object
Product product = new Product(name, categoryId, price);
controller.addProduct(product);
```

### 5. Handle Exceptions in Controller
```java
// Controller
try {
    boolean success = productDAO.addProduct(product);
    return success ? "Success!" : "Error: Failed to add.";
} catch (SQLException e) {
    return "Error: " + e.getMessage();  // Controller catches, returns message
}
```

---

## 🔄 Quick Reference: Data Flow

```
User clicks "Add" button
        │
        ▼
   VIEW: btn_saveActionPerformed()
        │
        ├── Gets data from text fields
        ├── Calls controller.addProduct(name, category, price)
        │
        ▼
   CONTROLLER: addProduct()
        │
        ├── Validates all inputs
        ├── Creates Product object
        ├── Calls productDAO.addProduct(product)
        │
        ▼
   DAO: addProduct()
        │
        ├── Executes INSERT SQL
        ├── Returns true/false
        │
        ▼
   CONTROLLER: 
        │
        ├── Returns "Success" or "Error" message
        │
        ▼
   VIEW:
        │
        ├── Shows JOptionPane with result
        ├── Refreshes table if successful
        │
        ▼
   User sees result!
```

---

## 📞 Need Help?

If you have questions about implementing MVC in your project:
1. Check the example file: `view/ProductManagementMVC.java`
2. Look at the detailed comments in `controller/ProductController.java`
3. Review the DAO examples in `dao/ProductDAO.java`

---

*Created for Foodcity Grocery Stock & Billing System*
*Author: Disath Damsutha*
