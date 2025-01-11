package com.acc.models;

public class Income {
    private int id;
    private int userId;
    private String source;
    private double amount;

    // Constructor
    public Income(int id, int userId, String source, double amount) {
        this.id = id;
        this.userId = userId;
        this.source = source;
        this.amount = amount;
    }

    // Getters and Setters
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Income{" + "id=" + id + ", userId=" + userId + ", source='" + source + '\'' + ", amount=" + amount + '}';
    }
}
