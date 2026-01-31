package model;

/**
 * MODEL LAYER - SupplierProduct Entity
 * 
 * This class represents the relationship between suppliers and products.
 * It maps to the supplier_product junction table in the database.
 * It is a pure data class (POJO) with no database or UI logic.
 */
public class SupplierProduct {
    private int spId;
    private int supplierId;
    private int productId;
    private double buyingPrice;
    private String supplierName;
    private String productName;

    /**
     * Default constructor
     */
    public SupplierProduct() {
    }

    /**
     * Constructor for creating new supplier-product relationships
     * 
     * @param supplierId  Supplier ID
     * @param productId   Product ID
     * @param buyingPrice Buying price from supplier
     */
    public SupplierProduct(int supplierId, int productId, double buyingPrice) {
        this.supplierId = supplierId;
        this.productId = productId;
        this.buyingPrice = buyingPrice;
    }

    /**
     * Constructor with ID (for updates)
     * 
     * @param spId        Supplier-Product relationship ID
     * @param supplierId  Supplier ID
     * @param productId   Product ID
     * @param buyingPrice Buying price from supplier
     */
    public SupplierProduct(int spId, int supplierId, int productId, double buyingPrice) {
        this.spId = spId;
        this.supplierId = supplierId;
        this.productId = productId;
        this.buyingPrice = buyingPrice;
    }

    /**
     * Full constructor with names for display
     * 
     * @param spId         Supplier-Product relationship ID
     * @param supplierId   Supplier ID
     * @param productId    Product ID
     * @param buyingPrice  Buying price from supplier
     * @param supplierName Supplier name for display
     * @param productName  Product name for display
     */
    public SupplierProduct(int spId, int supplierId, int productId, double buyingPrice,
            String supplierName, String productName) {
        this.spId = spId;
        this.supplierId = supplierId;
        this.productId = productId;
        this.buyingPrice = buyingPrice;
        this.supplierName = supplierName;
        this.productName = productName;
    }

    // Getters and Setters

    public int getSpId() {
        return spId;
    }

    public void setSpId(int spId) {
        this.spId = spId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return productName + " from " + supplierName;
    }
}
