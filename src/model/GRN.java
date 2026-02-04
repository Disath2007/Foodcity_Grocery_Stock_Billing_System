package model;

/**
 * MODEL LAYER - GRN (Goods Received Note) Entity
 * 
 * This class represents a GRN record in the system.
 * It is a pure data class (POJO) with no database or UI logic.
 * All database operations are handled by GRNController.
 */
public class GRN {
    private int grnId;
    private int productId;
    private String productName;
    private double buyingPrice;
    private int supplierId;
    private String supplierName;
    private int orderedQuantity;
    private int deliveredQuantity;
    private String dateCreated;

    /**
     * Default constructor
     */
    public GRN() {
    }

    /**
     * Constructor for creating new GRN
     */
    public GRN(int productId, int supplierId, int orderedQuantity, int deliveredQuantity) {
        this.productId = productId;
        this.supplierId = supplierId;
        this.orderedQuantity = orderedQuantity;
        this.deliveredQuantity = deliveredQuantity;
    }

    /**
     * Full constructor with all fields
     */
    public GRN(int grnId, int productId, String productName, double buyingPrice,
            int supplierId, String supplierName, int orderedQuantity,
            int deliveredQuantity, String dateCreated) {
        this.grnId = grnId;
        this.productId = productId;
        this.productName = productName;
        this.buyingPrice = buyingPrice;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.orderedQuantity = orderedQuantity;
        this.deliveredQuantity = deliveredQuantity;
        this.dateCreated = dateCreated;
    }

    // Getters and Setters

    public int getGrnId() {
        return grnId;
    }

    public void setGrnId(int grnId) {
        this.grnId = grnId;
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

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    // Alias for getBuyingPrice to maintain some compatibility if needed,
    // but prefer using getBuyingPrice
    public double getPrice() {
        return buyingPrice;
    }

    public void setPrice(double price) {
        this.buyingPrice = price;
    }

    /**
     * Calculates total price (Buying Price * Delivered Quantity)
     */
    public double getTotalPrice() {
        return buyingPrice * deliveredQuantity;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public int getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(int orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public int getDeliveredQuantity() {
        return deliveredQuantity;
    }

    public void setDeliveredQuantity(int deliveredQuantity) {
        this.deliveredQuantity = deliveredQuantity;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "GRN{" +
                "grnId=" + grnId +
                ", productName='" + productName + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", orderedQty=" + orderedQuantity +
                ", deliveredQty=" + deliveredQuantity +
                '}';
    }
}
