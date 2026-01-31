package model;

/**
 * MODEL LAYER - Supplier Entity
 * 
 * This class represents a supplier in the system.
 * It is a pure data class (POJO) with no database or UI logic.
 * All database operations are handled by SupplierController.
 */
public class Supplier {
    private int supplierId;
    private String supplierName;
    private String companyName;
    private String phone;
    private int productCount;

    /**
     * Default constructor
     */
    public Supplier() {
    }

    /**
     * Constructor for creating new suppliers
     * 
     * @param supplierName Name of the supplier
     * @param companyName  Company name
     * @param phone        Phone number
     */
    public Supplier(String supplierName, String companyName, String phone) {
        this.supplierName = supplierName;
        this.companyName = companyName;
        this.phone = phone;
    }

    /**
     * Constructor with ID (for updates)
     * 
     * @param supplierId   Supplier ID
     * @param supplierName Name of the supplier
     * @param companyName  Company name
     * @param phone        Phone number
     */
    public Supplier(int supplierId, String supplierName, String companyName, String phone) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.companyName = companyName;
        this.phone = phone;
    }

    /**
     * Full constructor with product count
     * 
     * @param supplierId   Supplier ID
     * @param supplierName Name of the supplier
     * @param companyName  Company name
     * @param phone        Phone number
     * @param productCount Number of products from this supplier
     */
    public Supplier(int supplierId, String supplierName, String companyName, String phone, int productCount) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.companyName = companyName;
        this.phone = phone;
        this.productCount = productCount;
    }

    // Getters and Setters

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    @Override
    public String toString() {
        return supplierName + " (" + companyName + ")";
    }
}
