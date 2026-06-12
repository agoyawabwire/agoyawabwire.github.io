package com.poultryflow.pro.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class EggSale {
    private String id;
    private String date;
    private String saleType; // e.g., Trays or Single Eggs
    private int quantity;
    private double unitPrice;
    private double totalAmount;
    private String customer;
    private String paymentStatus;

    public EggSale() {}

    public EggSale(String id, String date, String saleType, int quantity, double unitPrice, double totalAmount, String customer, String paymentStatus) {
        this.id = id;
        this.date = date;
        this.saleType = saleType;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        this.customer = customer;
        this.paymentStatus = paymentStatus;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getSaleType() { return saleType; }
    public void setSaleType(String saleType) { this.saleType = saleType; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getCustomer() { return customer; }
    public void setCustomer(String customer) { this.customer = customer; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}
