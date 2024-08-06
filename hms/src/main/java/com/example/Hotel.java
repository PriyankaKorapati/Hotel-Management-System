package com.example;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private static final String DB_URL = "jdbc:postgresql://localhost:5433/hotel";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Priyanka11@.";
    private SimpleDateFormat dateFormat;
    
    public Hotel() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }
    
    public double getStandardRoomPrice() {
        // Assuming method for fetching Standard Room Price from the database or otherwise.
        return 100.0;
    }

    public double getDeluxeRoomPrice() {
        // Assuming method for fetching Deluxe Room Price from the database or otherwise.
        return 200.0;
    }

    public void setStandardRoomPrice(double price) {
        // Implement database update logic for changing the standard room price
    }

    public void setDeluxeRoomPrice(double price) {
        // Implement database update logic for changing the deluxe room price
    }

    public boolean createRoom(int roomNumber, String roomType, String specialFeatures) {
        String query = "INSERT INTO rooms (room_number, room_type, special_features, price, occupied) VALUES (?, ?, ?, ?, false)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, roomNumber);
            pstmt.setString(2, roomType);
            pstmt.setString(3, specialFeatures);
            pstmt.setDouble(4, roomType.equals("Standard") ? getStandardRoomPrice() : getDeluxeRoomPrice());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkInCustomer(String name, int roomNumber, Date checkInDate, Date checkOutDate) {
        String insertGuestQuery = "INSERT INTO guests (name, room_number, check_in_date, check_out_date) VALUES (?, ?, ?, ?)";
        String updateRoomQuery = "UPDATE rooms SET occupied = true WHERE room_number = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement guestStmt = conn.prepareStatement(insertGuestQuery);
             PreparedStatement roomStmt = conn.prepareStatement(updateRoomQuery)) {

            // Insert guest details
            guestStmt.setString(1, name);
            guestStmt.setInt(2, roomNumber);
            guestStmt.setDate(3, checkInDate);
            guestStmt.setDate(4, checkOutDate);
            guestStmt.executeUpdate();

            // Update room to occupied
            roomStmt.setInt(1, roomNumber);
            roomStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkOutCustomer(int roomNumber) {
        String deleteGuestQuery = "DELETE FROM guests WHERE room_number = ?";
        String updateRoomQuery = "UPDATE rooms SET occupied = false WHERE room_number = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement guestStmt = conn.prepareStatement(deleteGuestQuery);
             PreparedStatement roomStmt = conn.prepareStatement(updateRoomQuery)) {

            // Delete guest details
            guestStmt.setInt(1, roomNumber);
            guestStmt.executeUpdate();

            // Update room to unoccupied
            roomStmt.setInt(1, roomNumber);
            roomStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Customer> getAllCustomers() {
        String query = "SELECT name, room_number, check_in_date, check_out_date FROM guests";
        List<Customer> customers = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("name");
                int roomNumber = rs.getInt("room_number");
                Date checkInDate = rs.getDate("check_in_date");
                Date checkOutDate = rs.getDate("check_out_date");
                customers.add(new Customer(name, roomNumber, checkInDate, checkOutDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    public List<Room> getAllRooms() {
        String query = "SELECT room_number, room_type, special_features, price, occupied FROM rooms";
        List<Room> rooms = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int roomNumber = rs.getInt("room_number");
                String roomType = rs.getString("room_type");
                String specialFeatures = rs.getString("special_features");
                double price = rs.getDouble("price");
                boolean occupied = rs.getBoolean("occupied");
                rooms.add(new Room(roomNumber, roomType, specialFeatures, price, occupied));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    public boolean updateCustomerDetails(int oldRoomNumber, String newName, int newRoomNumber, Date newCheckInDate, Date newCheckOutDate) {
        String updateCustomerQuery = "UPDATE guests SET name = ?, room_number = ?, check_in_date = ?, check_out_date = ? WHERE room_number = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(updateCustomerQuery)) {

            pstmt.setString(1, newName);
            pstmt.setInt(2, newRoomNumber);
            pstmt.setDate(3, newCheckInDate);
            pstmt.setDate(4, newCheckOutDate);
            pstmt.setInt(5, oldRoomNumber);

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String generateOccupancyReport() {
        String query = "SELECT room_number, occupied FROM rooms";
        StringBuilder report = new StringBuilder("Occupancy Report:\n");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int roomNumber = rs.getInt("room_number");
                boolean occupied = rs.getBoolean("occupied");
                report.append("Room ").append(roomNumber).append(": ").append(occupied ? "Occupied" : "Unoccupied").append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return report.toString();
    }

    public String generateRevenueReport() {
        // Assuming a revenue generation logic involving bookings/costs
        String query = "SELECT room_type, COUNT(*) AS count FROM guests AS g JOIN rooms AS r ON g.room_number = r.room_number GROUP BY room_type";
        StringBuilder report = new StringBuilder("Revenue Report:\n");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String roomType = rs.getString("room_type");
                int count = rs.getInt("count");
                double pricePerRoom = roomType.equals("Standard") ? getStandardRoomPrice() : getDeluxeRoomPrice();
                double totalRevenue = count * pricePerRoom;
                report.append("Room Type ").append(roomType).append(": ").append(" Total Revenue: $").append(totalRevenue).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return report.toString();
    }

    public boolean isRoomValid(int roomNumber) {
        String query = "SELECT 1 FROM rooms WHERE room_number = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, roomNumber);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}