package com.poultryflow.pro.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Expense {
    private String id;
    private String date;
    private String category;
    private String description;
    private double quantity;
    private double totalCost;

    public Expense() {}

    public Expense(String id, String date, String category, String description, double quantity, double totalCost) {
        this.id = id;
        this.date = date;
        this.category = category;
        this.description = description;
        this.quantity = quantity;
        this.totalCost = totalCost;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
}
