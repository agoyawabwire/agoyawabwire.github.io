package com.poultryflow.pro.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ChickenSale {
    private String id;
    private String date;
    private String chickenType;
    private int quantity;
    private double unitPrice;
    private double weight;
    private double totalAmount;
    private String customer;
    private String paymentStatus;

    public ChickenSale() {}

    public ChickenSale(String id, String date, String chickenType, int quantity, double unitPrice, double weight, double totalAmount, String customer, String paymentStatus) {
        this.id = id;
        this.date = date;
        this.chickenType = chickenType;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.weight = weight;
        this.totalAmount = totalAmount;
        this.customer = customer;
        this.paymentStatus = paymentStatus;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getChickenType() { return chickenType; }
    public void setChickenType(String chickenType) { this.chickenType = chickenType; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getCustomer() { return customer; }
    public void setCustomer(String customer) { this.customer = customer; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}
