package com.acc.service;

public interface FinanceManageble {

    void addIncome(int userId);
    void addExpense(int userId);
    void viewSummary(int userId);
}
