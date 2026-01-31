package model;

/**
 * MODEL LAYER - Category Entity
 * 
 * This class represents a product category in the system.
 * It is a pure data class (POJO) with no database or UI logic.
 * All database operations are handled by CategoryController.
 */
public class Category {
    private int categoryId;
    private String categoryName;
    private int productCount;

    /**
     * Default constructor
     */
    public Category() {
    }

    /**
     * Constructor with name only (for creating new categories)
     * 
     * @param categoryName Name of the category
     */
    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Constructor with id and name (for updates)
     * 
     * @param categoryId   ID of the category
     * @param categoryName Name of the category
     */
    public Category(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    /**
     * Full constructor with all fields
     * 
     * @param categoryId   ID of the category
     * @param categoryName Name of the category
     * @param productCount Number of products in this category
     */
    public Category(int categoryId, String categoryName, int productCount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.productCount = productCount;
    }

    // Getters and Setters

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

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    /**
     * Returns the category name for display in combo boxes
     */
    @Override
    public String toString() {
        return categoryName;
    }
}
