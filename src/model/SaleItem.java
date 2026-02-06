package model;

public class SaleItem {
    private int itemId;
    private int saleId;
    private int productId;

    private String productName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;

    public SaleItem() {
    }

    public SaleItem(int itemId, int saleId, int productId, String productName, int quantity, double unitPrice,
            double totalPrice) {
        this.itemId = itemId;
        this.saleId = saleId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    // Constructor for backward compatibility if needed, or update callers
    public SaleItem(int itemId, int saleId, int productId, int quantity, double unitPrice, double totalPrice) {
        this(itemId, saleId, productId, "", quantity, unitPrice, totalPrice);
    }

    // Getters and Setters
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
