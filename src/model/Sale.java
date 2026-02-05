package model;

import java.sql.Timestamp;

public class Sale {
    private int saleId;
    private String cashierName;
    private double subtotal;
    private double discount;
    private double grandTotal;
    private double cashReceived;
    private double balance;
    private Timestamp saleDate;

    public Sale() {
    }

    public Sale(int saleId, String cashierName, double subtotal, double discount, double grandTotal,
            double cashReceived, double balance, Timestamp saleDate) {
        this.saleId = saleId;
        this.cashierName = cashierName;
        this.subtotal = subtotal;
        this.discount = discount;
        this.grandTotal = grandTotal;
        this.cashReceived = cashReceived;
        this.balance = balance;
        this.saleDate = saleDate;
    }

    // Getters and Setters
    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public double getCashReceived() {
        return cashReceived;
    }

    public void setCashReceived(double cashReceived) {
        this.cashReceived = cashReceived;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Timestamp getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Timestamp saleDate) {
        this.saleDate = saleDate;
    }
}
