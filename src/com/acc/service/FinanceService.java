package com.acc.service;

import com.acc.service.FinanceManageble;
import com.acc.models.Expense;
import com.acc.models.Income;
import com.acc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class FinanceService implements FinanceManageble {
    private final Scanner scanner = new Scanner(System.in);
    private final Object lock = new Object(); // Lock for synchronization

    @Override
    public void addIncome(int userId) {
        System.out.print("Enter income source: ");
        String source = scanner.nextLine();
        System.out.print("Enter income amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
     
        
        synchronized (lock) { // Synchronize income addition
        	String query = "INSERT INTO Income (user_id, source, amount) VALUES (?, ?, ?)";
            String updateAmountquery = "UPDATE Users SET amount = amount + ? WHERE id = ?";
             
            try (Connection conn = DBConnection.getDBConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query);
                 PreparedStatement updateStmt = conn.prepareStatement(updateAmountquery)) {

                // Add income
                pstmt.setInt(1, userId);
                pstmt.setString(2, source);
                pstmt.setDouble(3, amount);
                pstmt.executeUpdate();

                // Update total amount
                updateStmt.setDouble(1, amount);
                updateStmt.setInt(2, userId);
                updateStmt.executeUpdate();

                System.out.println("Income added successfully!");
                
            } catch (SQLException e) {
                System.out.println("Error adding income: " + e.getMessage());
            }
           
        }
    }

    @Override
    public void addExpense(int userId) {
        System.out.print("Enter expense category: ");
        String category = scanner.nextLine();
        System.out.print("Enter expense amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        synchronized (lock) { // Synchronize expense addition
            String query = "INSERT INTO Expenses (user_id, category, amount) VALUES (?, ?, ?)";
            String checkAmountquery = "SELECT amount FROM Users WHERE id = ?";
            String updateAmountquery = "UPDATE Users SET amount = amount - ? WHERE id = ?";

            try (Connection conn = DBConnection.getDBConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query);
                 PreparedStatement checkStmt = conn.prepareStatement(checkAmountquery);
                 PreparedStatement updateStmt = conn.prepareStatement(updateAmountquery)) {

                // Check current balance
                checkStmt.setInt(1, userId);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    double currentAmount = rs.getDouble("amount");
                    if (currentAmount >= amount) {
                        // Add expense
                        pstmt.setInt(1, userId);
                        pstmt.setString(2, category);
                        pstmt.setDouble(3, amount);
                        pstmt.executeUpdate();

                        // Update total amount
                        updateStmt.setDouble(1, amount);
                        updateStmt.setInt(2, userId);
                        updateStmt.executeUpdate();

                        System.out.println("Expense added successfully!");
                       
                    } else {
                        System.out.println("Insufficient balance! Expense cannot be added.");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error adding expense: " + e.getMessage());
            }
        }
    }

    @Override
    public void viewSummary(int userId) {
        List<Income> incomes = new ArrayList<>();
        List<Expense> expenses = new ArrayList<>();

        String incomeSql = "SELECT * FROM Income WHERE user_id = ?";
        String expenseSql = "SELECT * FROM Expenses WHERE user_id = ?";
        String checkAmountSql = "SELECT amount FROM Users WHERE id = ?";

        synchronized (lock) { // Synchronize viewing summary
            try (Connection conn = DBConnection.getDBConnection();
                 PreparedStatement incomeStmt = conn.prepareStatement(incomeSql);
                 PreparedStatement expenseStmt = conn.prepareStatement(expenseSql);
                 PreparedStatement checkAmountStmt = conn.prepareStatement(checkAmountSql)) {

                // Fetch incomes
                incomeStmt.setInt(1, userId);
                ResultSet incomeRs = incomeStmt.executeQuery();

                while (incomeRs.next()) {
                    incomes.add(new Income(
                           incomeRs.getInt("id"),
                            incomeRs.getInt("user_id"),
                            incomeRs.getString("source"),
                            incomeRs.getDouble("amount")
                    ));
                }

                // Fetch expenses
                expenseStmt.setInt(1, userId);
                ResultSet expenseRs = expenseStmt.executeQuery();

                while (expenseRs.next()) {
                    expenses.add(new Expense(
                            expenseRs.getInt("id"),
                            expenseRs.getInt("user_id"),
                            expenseRs.getString("category"),
                            expenseRs.getDouble("amount")
                    ));
                }

                // Fetch current balance
                checkAmountStmt.setInt(1, userId);
                ResultSet balanceRs = checkAmountStmt.executeQuery();
                double netSavings = 0;
                if (balanceRs.next()) {
                    netSavings = balanceRs.getDouble("amount");
                }

                System.out.println("\n--- Financial Summary ---");
                System.out.println("Incomes:");
                
                incomes.forEach(System.out::println);

                System.out.println("Expenses:");
                expenses.forEach(System.out::println);

                double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
                double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();

                System.out.println("Total Income: " + totalIncome);
                System.out.println("Total Expense: " + totalExpense);
                System.out.println("Net Savings: " + netSavings);
            } catch (SQLException e) {
                System.out.println("Error viewing summary: " + e.getMessage());
            }
        }
    }
}
