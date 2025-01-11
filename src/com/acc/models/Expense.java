package com.acc.models;

public class Expense {
    private int id;
    private int userId;
    private String category;
    private double amount;

    // Constructor
    public Expense(int id, int userId, String category, double amount) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.amount = amount;
    }

    // Getters and Setters for expense class
    public int getId() {
       return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", userId=" + userId +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                '}';
    }
}
