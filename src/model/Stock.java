package model;

/**
 * MODEL LAYER - Stock Entity
 * 
 * This class represents stock/inventory information in the system.
 * It is a pure data class (POJO) with no database or UI logic.
 * All database operations are handled by StockController.
 */
public class Stock {
    private int stockId;
    private int productId;
    private String productName;
    private String categoryName;
    private int quantity;
    private double price;
    private String lastUpdated;

    public Stock() {
    }

    /**
     * Constructor for creating new stock entries
     * 
     * @param productId Product ID
     * @param quantity  Quantity in stock
     */
    public Stock(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    /**
     * Full constructor with all fields including product details
     * 
     * @param stockId      Stock ID
     * @param productId    Product ID
     * @param productName  Product name for display
     * @param categoryName Category name for display
     * @param quantity     Quantity in stock
     */
    public Stock(int stockId, int productId, String productName, String categoryName, int quantity, double price) {
        this.stockId = stockId;
        this.productId = productId;
        this.productName = productName;
        this.categoryName = categoryName;
        this.quantity = quantity;
        this.price = price;
    }

    /**
     * Constructor with timestamp
     * 
     * @param stockId      Stock ID
     * @param productId    Product ID
     * @param productName  Product name for display
     * @param categoryName Category name for display
     * @param quantity     Quantity in stock
     * @param lastUpdated  Last update timestamp
     */
    public Stock(int stockId, int productId, String productName, String categoryName, int quantity,
            double price, String lastUpdated) {
        this.stockId = stockId;
        this.productId = productId;
        this.productName = productName;
        this.categoryName = categoryName;
        this.quantity = quantity;
        this.price = price;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
