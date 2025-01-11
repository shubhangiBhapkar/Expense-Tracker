package com.acc.service;

import com.acc.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationService {
    // Method to authenticate user and return user ID
    public int authenticateUser(String email, String password) {
        String query = "SELECT id FROM Users WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id"); // Return user ID if login is successful
            } else {
                System.out.println("Invalid email or password.");
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("Authentication error: " + e.getMessage());
        }
       
        return -1; // Return -1 if authentication fails
    }

    //  Method to register a new user
    public boolean registerUser(String name, String email, String password) {
        String query = "INSERT INTO Users (name, email, password) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);

            pstmt.executeUpdate();
            System.out.println("Registration successful!");
            
            conn.close();
            
            return true;
        } catch (SQLException e) {
            System.out.println("Registration error: " + e.getMessage());
        }
       
        return false;
    }
}
