package com.example;

import java.sql.*;

public class UserDAO {
    private static final String DB_URL = "jdbc:postgresql://localhost:5433/hotel";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Priyanka11@.";

    public boolean registerUser(String username, String password) throws SQLException {
        // Check if the username already exists
        String checkUserQuery = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(checkUserQuery)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return false;
            }
        }

        // Register the new user
        String registerUserQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(registerUserQuery)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean loginUser(String username, String password) throws SQLException {
        String query = "SELECT 1 FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }
}