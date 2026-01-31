package model;

/**
 * MODEL LAYER - Product Entity
 * 
 * This class represents a product in the system.
 * It is a pure data class (POJO) with no database or UI logic.
 * All database operations are handled by ProductController.
 */
public class Product {
    private int productId;
    private String productName;
    private int categoryId;
    private String categoryName;
    private double price;

    /**
     * Default constructor
     */
    public Product() {
    }

    /**
     * Constructor for creating new products
     * 
     * @param productName Name of the product
     * @param categoryId  Category ID
     * @param price       Price of the product
     */
    public Product(String productName, int categoryId, double price) {
        this.productName = productName;
        this.categoryId = categoryId;
        this.price = price;
    }

    /**
     * Constructor with id (for updates)
     * 
     * @param productId   Product ID
     * @param productName Name of the product
     * @param categoryId  Category ID
     * @param price       Price of the product
     */
    public Product(int productId, String productName, int categoryId, double price) {
        this.productId = productId;
        this.productName = productName;
        this.categoryId = categoryId;
        this.price = price;
    }

    /**
     * Full constructor with all fields including category name
     * 
     * @param productId    Product ID
     * @param productName  Name of the product
     * @param categoryId   Category ID
     * @param categoryName Category name for display
     * @param price        Price of the product
     */
    public Product(int productId, String productName, int categoryId, String categoryName, double price) {
        this.productId = productId;
        this.productName = productName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.price = price;
    }

    // Getters and Setters

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return productName;
    }
}
