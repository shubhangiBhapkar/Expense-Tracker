package com.acc;

import com.acc.exception.InvalidChoiceException;
import com.acc.service.AuthenticationService;
import com.acc.service.FinanceService;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		AuthenticationService authService = new AuthenticationService();
		FinanceService financeService = new FinanceService();

		System.out.println("Welcome to Expense Tracker!");
		System.out.println("1. Login");
		System.out.println("2. Register");
		System.out.print("Enter your choice: ");
		int choice = sc.nextInt();
		sc.nextLine();

		int userId = -1;

		if (choice == 1) {
			// Login Process
			System.out.print("Enter email: ");
			String email = sc.nextLine();
			System.out.print("Enter password: ");
			String password = sc.nextLine();

			// Authenticate user and get userId
			userId = authService.authenticateUser(email, password);

			if (userId == -1) {
				System.out.println("Login failed. Exiting...");
				return;
			}
			System.out.println("Login successful!");
		} else if (choice == 2) {
			// Registration Process
			System.out.print("Enter name: ");
			String name = sc.nextLine();
			System.out.print("Enter email: ");
			String email = sc.nextLine();
			System.out.print("Enter password: ");
			String password = sc.nextLine();

			// Register the user
			if (authService.registerUser(name, email, password)) {
				System.out.println("Registration successful! Please login.");
				return; // After successful registration, the user is expected to log in.
			} else {
				System.out.println("Registration failed. Exiting...");
				return;
			}
			
		} else {
			throw new InvalidChoiceException("Selected option is not available :( ");
		
		}

		// After successful login, user can perform operations
		System.out.println("\nWelcome, User " + userId + "!");
		while (true) {
			System.out.println("\n1. Add Income");
			System.out.println("2. Add Expense");
			System.out.println("3. View Summary");
			System.out.println("4. Exit");
			System.out.print("Enter your choice: ");
			int option = sc.nextInt();

			switch (option) {
			case 1:
				financeService.addIncome(userId);
				break;
			case 2:
				financeService.addExpense(userId);
				break;
			case 3:
				financeService.viewSummary(userId);
				break;
			case 4:
				System.out.println("Goodbye!");
				return;
			default:
				throw new InvalidChoiceException("Selected option is not available :( ");

			}
		}
	}
}
